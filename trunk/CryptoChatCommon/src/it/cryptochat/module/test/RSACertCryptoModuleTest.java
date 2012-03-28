package it.cryptochat.module.test;

import java.security.Security;
import it.cryptochat.module.RSACertCryptoModule;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class RSACertCryptoModuleTest {
	
	private RSACertCryptoModule module = new RSACertCryptoModule();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		System.setProperty("KeyStorePath", "C:\\Users\\Stefano\\Desktop\\serverKeyStore.jks");
		System.setProperty("PeerCertificatePath", "C:\\Users\\Stefano\\Desktop\\server.crt");
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testLoadMyPrivateKey() {
		module.loadMyPrivateKey();
	}

	@Test
	public void testLoadPeerPublicKey() {
//		fail("Not yet implemented");
		module.loadPeerPublicKey();
	}

}
