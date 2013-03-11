package be.virtualsushi.wadisda.services.gmail;

import java.security.Provider;
import java.security.Security;

public class OAuth2Provider extends Provider {

	private static final long serialVersionUID = 1L;

	public OAuth2Provider() {
		super("Google OAuth2 Provider", 1.0, "Provides the XOAUTH2 SASL Mechanism");
		put("SaslClientFactory.XOAUTH2", "com.google.code.samples.oauth2.OAuth2SaslClientFactory");
	}

	static {
		Security.addProvider(new OAuth2Provider());
	}

}
