package it.cryptochat.module;

public class CryptoModuleFactory {
	
	public enum ModuleType { NO_CRYPTO, SSL , RSA, DH_DES };
	
	public static CryptoModule getCriptoModule(ModuleType type) {
		switch(type) {
		
			case NO_CRYPTO:
				return new NoCryptoModule();
				
			case SSL:
				return new SSLCryptoModule();
				
			case RSA:
				return new RSACryptoModule();
			
			case DH_DES:
				return new DH_DESCryptoModule();
				
			default:
				return null;
				
		}
		
	}
}
