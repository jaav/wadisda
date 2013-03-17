package be.virtualsushi.wadisda.services.gmail;

import java.security.Provider;

public class OAuth2Provider extends Provider {

	private static final long serialVersionUID = 1L;

	public OAuth2Provider() {
		super("Google OAuth2 Provider", 2.0, "Provides the XOAUTH2 SASL Mechanism");
		put("SaslClientFactory.XOAUTH2", "be.virtualsushi.wadisda.services.gmail.OAuth2SaslClientFactory");
	}

}
