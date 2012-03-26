package it.cryptochat.module;

import it.cryptochat.common.Message;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.PasswordAuthentication;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
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


public class RSACryptoModule extends CryptoModule {

	private Cipher cipherSend;
	private Cipher cipherRead;
	private RSAPublicKey myPublicKey;
	private RSAPrivateKey myPrivateKey;
	
	@Override
	public void init(Socket socket) {
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		try {

//			KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
//			RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger("12345678", 16), new BigInteger("11", 16));
//			RSAPrivateKeySpec privKeySpec = new RSAPrivateKeySpec(new BigInteger("12345678", 16), new BigInteger("12345678", 16));
//
//			myPublicKey = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);
//			myPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(privKeySpec);
			
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA", "BC");
			keyPairGen.initialize(1024);	// 1024 bits: modulus size of RSA algorithm (n). Keysize supported for RSA key pair generation: 1024, 2048 
			KeyPair keyPair = keyPairGen.generateKeyPair();
			myPublicKey = (RSAPublicKey) keyPair.getPublic();
			myPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
			
			logger.debug("My public key length: " + myPublicKey.getEncoded().length + " byte");
			logger.debug("My private key length: " + myPrivateKey.getEncoded().length + " byte");
			
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			DataInputStream input = new DataInputStream(socket.getInputStream());
			
			// Send the public key length in bytes to the other peer
			byte[] myPublicKeyBytes = myPublicKey.getEncoded();
			output.writeInt(myPublicKeyBytes.length);

			// Send the public key bytes to the other peer
			logger.debug("Writing my public key: length = " + myPublicKeyBytes.length + " byte");
			output.write(myPublicKeyBytes);

			// Retrieve the public key length in bytes of the other peer
			int keyLength = input.readInt();
			byte[] peerPublicKeyBytes = new byte[keyLength];

			// Retrieve the public key bytes of the other peer
			logger.debug("Reading peer public key: length = " + keyLength + " byte");
			input.readFully(peerPublicKeyBytes);

			// Convert the public key bytes into a PublicKey object
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(peerPublicKeyBytes);
			KeyFactory keyFact = KeyFactory.getInstance("RSA", "BC");
			PublicKey peerPublicKey = keyFact.generatePublic(x509KeySpec);
						
//			KeyAgreement ka = KeyAgreement.getInstance("RSA", "BC");
//			ka.init(myPrivateKey);
//			ka.doPhase(peerPublicKey, true);
//			
//			SecretKey secretKey = ka.generateSecret("DESede");
//			logger.debug("Shared key length: " + secretKey.getEncoded().length + " byte");
//			
//			cipherSend = Cipher.getInstance("DESede", "BC");
//			cipherRead = Cipher.getInstance("DESede", "BC");
//			
//			cipherSend.init(Cipher.ENCRYPT_MODE, secretKey);
//			cipherRead.init(Cipher.DECRYPT_MODE, secretKey);
			
			cipherSend = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			cipherRead = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			
			cipherSend.init(Cipher.ENCRYPT_MODE, peerPublicKey);
			cipherRead.init(Cipher.DECRYPT_MODE, myPrivateKey);
			
		} catch (NoSuchAlgorithmException e) {
			logger.error("Problems in RSACryptoModule creation: NoSuchAlgorithmException: " + e);
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			logger.error("Problems in RSACryptoModule creation: NoSuchProviderException: " + e);
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			logger.error("Problems in RSACryptoModule creation: NoSuchPaddingException: " + e);
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			logger.error("Problems in RSACryptoModule creation: InvalidKeySpecException: " + e);
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			logger.error("Problems in RSACryptoModule creation: InvalidKeySpecException: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Problems in RSACryptoModule creation: IOException: " + e);
			e.printStackTrace();
		}
	}

//	@Override
//	public void send(DataOutputStream output, String message) {
//		
//		byte[] cipherText = null;
//		String cipherString = null;
//		
//		try {
//			
//			cipher.init(Cipher.ENCRYPT_MODE, pubKey);		
//			cipherText = cipher.doFinal(message.getBytes());
//			cipherString = new String(cipherText);
//			
//			logger.debug("cipher: " + cipherString);
//			
//			output.writeUTF(cipherString);
//			
//		} catch (IllegalBlockSizeException e) {
//			logger.error("Problems in message sending: " + e);
//			e.printStackTrace();
//		} catch (BadPaddingException e) {
//			logger.error("Problems in message sending: " + e);
//			e.printStackTrace();
//		} catch (InvalidKeyException e) {
//			logger.error("Problems in message sending: " + e);
//			e.printStackTrace();
//		} catch (IOException e) {
//			logger.error("Problems in message sending: " + e);
//			e.printStackTrace();
//		}
//		
//	}
//
//	@Override
//	public String read(DataInputStream input) {
//
//		String plainString = null;
//		
//		try {
//			
//			cipher.init(Cipher.DECRYPT_MODE, privKey);			
//			byte[] plainText = cipher.doFinal(input.readUTF().getBytes());
//			plainString = new String(plainText);
//			
//			logger.debug("plain: " + plainString);
//			
//		} catch (IllegalBlockSizeException e) {
//			logger.error("Problems in message reading: " + e);
//			e.printStackTrace();
//		} catch (BadPaddingException e) {
//			logger.error("Problems in message reading: " + e);
//			e.printStackTrace();
//		} catch (InvalidKeyException e) {
//			logger.error("Problems in message reading: " + e);
//			e.printStackTrace();
//		} catch (IOException e) {
//			logger.error("Problems in message reading: " + e);
//			e.printStackTrace();
//		}
//		
//		return plainString;
//	}
	
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

}
