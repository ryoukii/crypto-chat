package it.cryptochat.module;

import it.cryptochat.common.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.SignedObject;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class IntegrityCryptoModule extends CryptoModule {
	
	private RSAPublicKey myPublicKey;
	private RSAPrivateKey myPrivateKey;
	private PublicKey peerPublicKey;

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
			peerPublicKey = keyFact.generatePublic(x509KeySpec);
						
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
			
			
		} catch (NoSuchAlgorithmException e) {
			logger.error("Problems in IntegrityCryptoModule creation: NoSuchAlgorithmException: " + e);
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			logger.error("Problems in IntegrityCryptoModule creation: NoSuchProviderException: " + e);
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			logger.error("Problems in IntegrityCryptoModule creation: InvalidKeySpecException: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Problems in IntegrityCryptoModule creation: IOException: " + e);
			e.printStackTrace();
		}
	}
	
	@Override
	public void send(ObjectOutputStream output, Message message) {
		
		try {
			
			Signature signingEngine = Signature.getInstance("SHA256withRSA", "BC");
			SignedObject signedObj = new SignedObject(message, myPrivateKey, signingEngine);
			
			logger.debug("Signature: " + new String(signedObj.getSignature()));
			
			output.writeObject(signedObj);
			
		} catch (InvalidKeyException e) {
			logger.error("Problems in IntegrityCryptoModule sending: InvalidKeyException: " + e);
			e.printStackTrace();
		} catch (SignatureException e) {
			logger.error("Problems in IntegrityCryptoModule sending: SignatureException: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Problems in IntegrityCryptoModule sending: IOException: " + e);
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			logger.error("Problems in IntegrityCryptoModule sending: NoSuchAlgorithmException: " + e);
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			logger.error("Problems in IntegrityCryptoModule sending: NoSuchProviderException: " + e);
			e.printStackTrace();
		}
	}

	@Override
	public Message read(ObjectInputStream input) throws IOException {
		
		try {
			
			SignedObject signedObj = (SignedObject) input.readObject();
			Signature verificationEngine = Signature.getInstance("SHA256withRSA", "BC");
			boolean res = signedObj.verify(peerPublicKey, verificationEngine);
			
			logger.debug("Signature received: " + new String(signedObj.getSignature()));
			logger.debug("Signaure verifying result: " + res);
			
			if(res)
				return (Message) signedObj.getObject();
			else
				logger.error("Corrupted message recevied");
			
		} catch (ClassNotFoundException e) {
			logger.error("Problems in IntegrityCryptoModule reading: ClassNotFoundException: " + e);
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			 logger.error("Problems in IntegrityCryptoModule reading: NoSuchAlgorithmException: " + e);
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			logger.error("Problems in IntegrityCryptoModule reading: NoSuchProviderException: " + e);
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			logger.error("Problems in IntegrityCryptoModule reading: InvalidKeyException: " + e);
			e.printStackTrace();
		} catch (SignatureException e) {
			logger.error("Problems in IntegrityCryptoModule reading: SignatureException: " + e);
			e.printStackTrace();
		}
		
		return null;
	}

}
