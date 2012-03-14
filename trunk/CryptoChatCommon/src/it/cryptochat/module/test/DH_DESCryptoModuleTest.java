package it.cryptochat.module.test;

import static org.junit.Assert.*;

import java.security.CryptoPrimitive;

import it.cryptochat.module.CryptoModule;
import it.cryptochat.module.CryptoModuleFactory;
import it.cryptochat.module.DH_DESCryptoModule;
import it.cryptochat.module.CryptoModuleFactory.ModuleType;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DH_DESCryptoModuleTest extends CryptoChatModuleTestUtils {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		setCryptoModule(CryptoModuleFactory.ModuleType.DH_DES);
		init();
		
	}

	@Test
	public void testClientSendServerRead() {
		
		String messageToSend = "ciao sono il client";
		String messageReceived;
		
		clientCryptoModule.send(clientOutput, messageToSend);
		messageReceived = serverCryptoModule.read(serverInput);
		
		assertEquals(messageToSend, messageReceived);
		
	}
	
	@Test
	public void testServerSendClientRead() {
		
		String messageToSend = "ciao sono il server";
		String messageReceived;
		
		serverCryptoModule.send(serverOutput, messageToSend);
		messageReceived = clientCryptoModule.read(clientInput);
		
		assertEquals(messageToSend, messageReceived);
		
	}
	
	@Test
	public void testCSendSReadSSendCRead() {
		
		String messageToSend = "ciao sono il client";
		String messageReceived;
		
		clientCryptoModule.send(clientOutput, messageToSend);
		messageReceived = serverCryptoModule.read(serverInput);
		
		serverCryptoModule.send(serverOutput, messageReceived);
		messageReceived = clientCryptoModule.read(clientInput);
		
		assertEquals(messageToSend, messageReceived);
		
	}

}
