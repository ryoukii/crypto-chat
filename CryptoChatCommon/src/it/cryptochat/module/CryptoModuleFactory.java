package it.cryptochat.module;

public class CryptoModuleFactory {
	
	public enum ModuleType { NO_CRYPTO, SSL, RSA, DH_3DES };
	
	public static CryptoModule getCryptoModule(ModuleType type) {
		switch(type) {
		
			case NO_CRYPTO:
				return new NoCryptoModule();
				
			case SSL:
				return new SSLCryptoModule();
				
			case RSA:
				return new RSACryptoModule();
			
			case DH_3DES:
				return new DH_3DESCryptoModule();
				
			default:
				return null;
				
		}
		
	}
}
