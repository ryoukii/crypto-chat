package it.cryptochat.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import it.cryptochat.client.CryptoChatClient;
import it.cryptochat.module.CryptoModuleFactory;

public class CryptoChatClientCommonTest {
	
	protected static final int SERVER_PORT = 54321;
	protected static final String SERVER_ADDR = "127.0.0.1"; 
	protected static final CryptoModuleFactory.ModuleType MODULE_TYPE = CryptoModuleFactory.ModuleType.NO_CRYPTO;
	
	protected CryptoChatClient client;
	
	@BeforeClass
	protected void SetUpClient() {
		CryptoChatClient client = new CryptoChatClient(SERVER_PORT, SERVER_ADDR, MODULE_TYPE);
	}
	
	@AfterClass
	protected void tearDownClient() {
		client.close();
	}
}
