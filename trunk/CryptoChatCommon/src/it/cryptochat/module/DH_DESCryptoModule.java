package it.cryptochat.module;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

public class DH_DESCryptoModule extends CryptoModule {

	private Socket socket = null;
//	private BigInteger p = new BigInteger("1234567890", 16);
//	private BigInteger g = new BigInteger("4285630184", 16);
	private BigInteger p = new BigInteger(	"6956784810473563351980376157766276930111" +
											"8825568739457881793647826895545602658086" +
											"5047498169698612690761411899173633625068" +
											"3275474827529802605444040482993559");
//	private BigInteger g = BigInteger.valueOf(2);
	private BigInteger g = new BigInteger(	"4164779446886066922311416213697649900773" +
											"2020685183031841477595460754880122608598" +
											"6393957046846105107425244808414910327936" +
											"7141094520918980414214128835369764");
	private int l = 5;
	
	private Cipher cipherSend;
	private Cipher cipherRead;
	private SecretKey secretKey;

	
	
	
	public SecretKey getSecretKey() {
		return secretKey;
	}

	@Override
	public void init(Socket socket) {

		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		try {
			// Use the values to generate a key pair
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH", "BC");
			DHParameterSpec dhSpec = new DHParameterSpec(p, g, l);
			keyGen.initialize(dhSpec);
//			keyGen.initialize(512);
			KeyPair myKeyPair = keyGen.generateKeyPair();
//			DHParameterSpec dhSpec = ((DHPublicKey)myKeyPair.getPublic()).getParams();
//			logger.debug("p = " + dhSpec.getP());
//			logger.debug("g = " + dhSpec.getG());
//			logger.debug("l = " + dhSpec.getL());

			// Get the generated public and private keys
			PrivateKey myPrivateKey = myKeyPair.getPrivate();
			PublicKey myPublicKey = myKeyPair.getPublic();

			// Retrieve communication streams
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());

			// Send the public key bytes to the other peer...
			byte[] myPublicKeyBytes = myPublicKey.getEncoded();
			output.writeInt(myPublicKeyBytes.length);
			output.write(myPublicKeyBytes);
			logger.debug("Writing publicKey: length = " + myPublicKeyBytes.length);

			// Retrieve the public key bytes of the other peer
			int keyLength = input.readInt();
			byte[] peerPublicKeyBytes = new byte[keyLength];
			logger.debug("Reading peerPublicKeyBytes: length = " + keyLength);
			input.readFully(peerPublicKeyBytes);
			
			// Convert the public key bytes into a PublicKey object
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(peerPublicKeyBytes);
			KeyFactory keyFact = KeyFactory.getInstance("DH", "BC");
			PublicKey peerPublicKey = keyFact.generatePublic(x509KeySpec);

			// Prepare to generate the secret key with the private key and public key of the other peer
			KeyAgreement ka = KeyAgreement.getInstance("DH", "BC");
			ka.init(myPrivateKey);
			ka.doPhase(peerPublicKey, true);

			// Generate shared secret for algorithm
			// see Listing All Available Symmetric Key Generators
			
//			byte[] secretKey = ka.generateSecret();
//			logger.debug("Shared key generated: length = " + secretKey.length);

			secretKey = ka.generateSecret("DES");
			logger.debug("Shared key length: " + secretKey.getEncoded().length);

			cipherSend = Cipher.getInstance("DES", "BC");
			cipherSend.init(Cipher.ENCRYPT_MODE, secretKey);
			cipherRead = Cipher.getInstance("DES", "BC");
			cipherRead.init(Cipher.DECRYPT_MODE, secretKey);
			
			// Use the secret key to encrypt/decrypt data;
			// see Encrypting a String with DES

		} catch (java.security.InvalidKeyException e) {
			logger.error("Problems during DH key generation: " + e);
			e.printStackTrace();
		} catch (java.security.spec.InvalidKeySpecException e) {
			logger.error("Problems during DH key generation: " + e);
			e.printStackTrace();
		} catch (java.security.InvalidAlgorithmParameterException e) {
			logger.error("Problems during DH key generation: " + e);
			e.printStackTrace();
		} catch (java.security.NoSuchAlgorithmException e) {
			logger.error("Problems during DH key generation: " + e);
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			logger.error("Problems during DH key generation: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Problems during DH key generation: " + e);
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			logger.error("Problems during DH key generation: " + e);
			e.printStackTrace();
		}
	}


	@Override
	public void send(DataOutputStream output, String message) {
		
		byte[] cipherText = null;
		String cipherString = null;
		
		try {
			
//			cipher.init(Cipher.ENCRYPT_MODE, secretKey);		
			cipherText = cipherSend.doFinal(message.getBytes());
			cipherString = new String(cipherText);
			
			logger.debug("Message to send: original: " + message + "\tencrypted: " + cipherString);
			
			output.writeUTF(cipherString);
			
		} catch (IllegalBlockSizeException e) {
			logger.error("Problems in message sending: " + e);
			e.printStackTrace();
		} catch (BadPaddingException e) {
			logger.error("Problems in message sending: " + e);
			e.printStackTrace();
//		} catch (InvalidKeyException e) {
//			logger.error("Problems in message sending: " + e);
//			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Problems in message sending: " + e);
			e.printStackTrace();
		}
		
	}

	@Override
	public String read(DataInputStream input) {

		String plainString = null;
		
		try {
			
//			cipher.init(Cipher.DECRYPT_MODE, secretKey);			
			String encrypted = input.readUTF();
			byte[] plainText = cipherRead.doFinal(encrypted.getBytes());
			plainString = new String(plainText);
			
			logger.debug("Message received: encrypted: " + encrypted + "\tplain: " + plainString);
			
		} catch (IllegalBlockSizeException e) {
			logger.error("Problems in message reading: " + e);
			e.printStackTrace();
		} catch (BadPaddingException e) {
			logger.error("Problems in message reading: " + e);
			e.printStackTrace();
//		} catch (InvalidKeyException e) {
//			logger.error("Problems in message reading: " + e);
//			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Problems in message reading: " + e);
			e.printStackTrace();
		}
		
		return plainString;
	}
	
}
