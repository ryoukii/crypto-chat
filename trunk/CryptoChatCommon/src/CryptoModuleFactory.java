
public class CryptoModuleFactory {
	
	public enum ModuleType { NO_CRIPTO, SSL , RSA };
	
	public static CryptoModule getCriptoModule(ModuleType type) {
		switch(type) {
		
			case NO_CRIPTO:
				return new NoCryptoModule();
				
			case SSL:
				return new SSLCryptoModule();
				
			case RSA:
				return new RSACryptoModule();
				
			default:
				return null;
				
		}
		
	}
}
