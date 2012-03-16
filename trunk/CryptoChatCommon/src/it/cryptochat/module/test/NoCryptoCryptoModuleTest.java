package it.cryptochat.module.test;

import static org.junit.Assert.*;

import java.security.CryptoPrimitive;
import java.security.MessageDigest;

import it.cryptochat.common.Message;
import it.cryptochat.module.CryptoModule;
import it.cryptochat.module.CryptoModuleFactory;
import it.cryptochat.module.DH_DESCryptoModule;
import it.cryptochat.module.CryptoModuleFactory.ModuleType;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class NoCryptoCryptoModuleTest extends CryptoChatModuleTestUtils {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		setCryptoModule(CryptoModuleFactory.ModuleType.NO_CRYPTO);
		init();
		
	}

	@Test
	public void testClientSendServerRead() {
		
		Message messageToSend = new Message();
		Message messageReceived;
		
		messageToSend.setSender("Client1");
		messageToSend.setMessage("ciao sono il client");
		
		clientCryptoModule.send(clientOutput, messageToSend);
		messageReceived = serverCryptoModule.read(serverInput);
		
		assertEquals(messageToSend, messageReceived);
		
	}
	
	@Test
	public void testServerSendClientRead() {
		
		Message messageToSend = new Message();
		Message messageReceived;
		
		messageToSend.setSender("Server");
		messageToSend.setMessage("ciao sono il server");
		
		serverCryptoModule.send(serverOutput, messageToSend);
		messageReceived = clientCryptoModule.read(clientInput);
		
		assertEquals(messageToSend, messageReceived);
		
	}
	
	@Test
	public void testCSendSReadSSendCRead() {
		
		Message messageToSend = new Message();
		Message messageReceived;
		
		messageToSend.setSender("Client1");
		messageToSend.setMessage("ciao sono il client");
		
		clientCryptoModule.send(clientOutput, messageToSend);
		messageReceived = serverCryptoModule.read(serverInput);
		
		serverCryptoModule.send(serverOutput, messageReceived);
		messageReceived = clientCryptoModule.read(clientInput);
		
		assertEquals(messageToSend, messageReceived);
		
	}
	
	@Test
	public void testDoubleClientSend() {
		
		Message message1ToSend = new Message();
		Message message1Received;
		Message message2ToSend = new Message();
		Message message2Received;
		
		message1ToSend.setSender("Client1");
		message1ToSend.setMessage("ciao sono il client");
		
		message2ToSend.setSender("Client1");
		message2ToSend.setMessage("come va?");
		
		
		
		// Send message 1
		
		clientCryptoModule.send(clientOutput, message1ToSend);
		message1Received = serverCryptoModule.read(serverInput);
		logger.debug("Received message 1: " + message1Received);
		
		assertEquals(message1ToSend, message1Received);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		
		// Send message 2
		
		clientCryptoModule.send(clientOutput, message2ToSend);
		message2Received = serverCryptoModule.read(serverInput);
		logger.debug("Received message 2: " + message2Received);
		
		assertEquals(message2ToSend, message2Received);
		
	}

}
