package be.virtualsushi.wadisda.services.security;

import org.apache.shiro.authc.AuthenticationToken;

import com.google.api.client.auth.oauth2.Credential;

public class GoogleAuthenticationToken implements AuthenticationToken {

	private static final long serialVersionUID = -6693427705102640134L;

	private Credential credential;
	private String userId;

	public GoogleAuthenticationToken(String userId, Credential credential) {
		this.userId = userId;
		this.credential = credential;
	}

	@Override
	public Object getPrincipal() {
		return userId;
	}

	@Override
	public Object getCredentials() {
		return credential;
	}

}
