package it.cryptochat.module;
import it.cryptochat.common.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;


public class RSACryptoModule extends CryptoModule {

	private Cipher cipherSend;
	private Cipher cipherRead;
	private RSAPublicKey pubKey;
	private RSAPrivateKey privKey;
	
	public RSACryptoModule() {
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		try {
//			cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
			cipherSend = Cipher.getInstance("RSA/None/PKCS1PADDING", "BC");
			cipherRead = Cipher.getInstance("RSA/None/PKCS1PADDING", "BC");
			
			cipherSend.init(Cipher.ENCRYPT_MODE, pubKey);
			cipherRead.init(Cipher.DECRYPT_MODE, privKey);

			KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
			RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger("12345678", 16), new BigInteger("11", 16));
			RSAPrivateKeySpec privKeySpec = new RSAPrivateKeySpec(new BigInteger("12345678", 16), new BigInteger("12345678", 16));

			pubKey = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);
			privKey = (RSAPrivateKey) keyFactory.generatePrivate(privKeySpec);
			
		} catch (NoSuchAlgorithmException e) {
			logger.error("Problems in RSACriptoModule creation: NoSuchAlgorithmException: " + e);
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			logger.error("Problems in RSACriptoModule creation: NoSuchProviderException: " + e);
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			logger.error("Problems in RSACriptoModule creation: NoSuchPaddingException: " + e);
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			logger.error("Problems in RSACriptoModule creation: InvalidKeySpecException: " + e);
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			logger.error("Problems in RSACriptoModule creation: InvalidKeySpecException: " + e);
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
	public Message read(ObjectInputStream input) {

		SealedObject sealedObj;
		Message plainMessage = null;
		
		try {
			
			sealedObj = (SealedObject) input.readObject();
			plainMessage = (Message) sealedObj.getObject(cipherRead);
			
			logger.debug("Message received: encrypted: " + sealedObj + "\tplain: " + plainMessage);
			
		} catch (IllegalBlockSizeException e) {
			logger.error("Problems in message reading: " + e);
			e.printStackTrace();
		} catch (IOException e) {
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
