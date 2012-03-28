package it.cryptochat.module;

import it.cryptochat.common.Message;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.PasswordAuthentication;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

import org.bouncycastle.util.encoders.Base64Encoder;


public class RSACertCryptoModule extends CryptoModule {

//	private static final String keyStorePath = "";
	private Cipher cipherSend;
	private Cipher cipherRead;

	@Override
	public void init(Socket socket) {

		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		try {
			
			PrivateKey myPrivateKey = loadMyPrivateKey();

			PublicKey peerPublicKey = loadPeerPublicKey();


			// generate ciphers

			cipherSend = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			cipherRead = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");

			cipherSend.init(Cipher.ENCRYPT_MODE, peerPublicKey);
			cipherRead.init(Cipher.DECRYPT_MODE, myPrivateKey);

		} catch (NoSuchAlgorithmException e) {
			logger.error("Problems in RSACertCryptoModule creation: NoSuchAlgorithmException: " + e);
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			logger.error("Problems in RSACertCryptoModule creation: NoSuchProviderException: " + e);
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			logger.error("Problems in RSACertCryptoModule creation: NoSuchPaddingException: " + e);
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			logger.error("Problems in RSACertCryptoModule creation: InvalidKeySpecException: " + e);
			e.printStackTrace();
		}
	}


	@Override
	public void send(ObjectOutputStream output, Message message) {

		SealedObject sealedObj;

		try {
			sealedObj = new SealedObject(message, cipherSend);

			logger.debug("Message to send: original: " + message + "\tencrypted: " + sealedObj);

			output.writeObject(sealedObj);

		} catch (IllegalBlockSizeException e) {
			logger.error("Problems in message sending: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Problems in message sending: " + e);
			e.printStackTrace();
		}

	}

	@Override
	public Message read(ObjectInputStream input) throws IOException {

		SealedObject sealedObj;
		Message plainMessage = null;

		try {

			sealedObj = (SealedObject) input.readObject();
			plainMessage = (Message) sealedObj.getObject(cipherRead);

			logger.debug("Message received: encrypted: " + sealedObj + "\tplain: " + plainMessage);

		} catch (IllegalBlockSizeException e) {
			logger.error("Problems in message reading: " + e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			logger.error("Problems in message reading: " + e);
			e.printStackTrace();
		} catch (BadPaddingException e) {
			logger.error("Problems in message reading: " + e);
			e.printStackTrace();
		}

		return plainMessage;
	}
	
	
	
	

	public PrivateKey loadMyPrivateKey() {
		
		KeyStore ks;
		String keyStorePath = System.getProperty("KeyStorePath");
		
		try {
			ks = KeyStore.getInstance(KeyStore.getDefaultType());

			// get user password and file input stream

			char[] password = "123456".toCharArray();

			FileInputStream fis = null;
			try {
				fis = new FileInputStream(keyStorePath);
				ks.load(fis, password);
			} finally {
				if (fis != null) {
					fis.close();
				}
			}


			// retrieve my private key

			PrivateKey myPrivateKey = (PrivateKey) ks.getKey("serverprvkey", password);			
			int lengthByte = myPrivateKey.getEncoded().length;
			int lengthBit = lengthByte * 8;
			logger.debug("My private key format: " + myPrivateKey.getFormat());
			logger.debug("My private key length: " + lengthByte + " byte (" + lengthBit + " bit)");
			
			return myPrivateKey;
			
		} catch (KeyStoreException e) {
			logger.error("Problems in RSACertCryptoModule creation: KeyStoreException: " + e);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			logger.error("Problems in RSACertCryptoModule creation: FileNotFoundException: " + e);
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			logger.error("Problems in RSACertCryptoModule creation: NoSuchAlgorithmException: " + e);
			e.printStackTrace();
		} catch (CertificateException e) {
			logger.error("Problems in RSACertCryptoModule creation: CertificateException: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Problems in RSACertCryptoModule creation: IOException: " + e);
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			logger.error("Problems in RSACertCryptoModule creation: UnrecoverableKeyException: " + e);
			e.printStackTrace();
		}
		
		return null;

	}
	
	
	
	public PublicKey loadPeerPublicKey() {
		// Retrieve peer certificate

		String filename = System.getProperty("PeerCertificatePath");
		InputStream is;
		
		try {
			
			is = new FileInputStream(filename);
			CertificateFactory fact = CertificateFactory.getInstance("X.509", "BC");			
			X509Certificate retrievedCert = (X509Certificate)fact.generateCertificate(is);
			is.close();
			
			PublicKey peerPublicKey = retrievedCert.getPublicKey();
			int lengthByte = peerPublicKey.getEncoded().length;
			int lengthBit = lengthByte * 8;
			logger.debug("Peer public key format: " + peerPublicKey.getFormat());
			logger.debug("Peer public key length: " + lengthByte + " byte (" + lengthBit + " bit)");
			
			return peerPublicKey;
			
		} catch (FileNotFoundException e) {
			logger.error("Problems in RSACertCryptoModule creation: FileNotFoundException: " + e);
			e.printStackTrace();
		} catch (CertificateException e) {
			logger.error("Problems in RSACertCryptoModule creation: CertificateException: " + e);
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			logger.error("Problems in RSACertCryptoModule creation: NoSuchProviderException: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Problems in RSACertCryptoModule creation: IOException: " + e);
			e.printStackTrace();
		}
		
		return null;
	}

}
