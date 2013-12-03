package be.virtualsushi.wadisda.services.messages.impl;

import java.io.IOException;

import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.services.messages.MessageEndpoint;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;

public abstract class AbstractMessageEndpointImpl implements MessageEndpoint {

	private AuthorizationCodeFlow authorizationCodeFlow;

	public AbstractMessageEndpointImpl(AuthorizationCodeFlow authorizationCodeFlow) {
		this.authorizationCodeFlow = authorizationCodeFlow;
	}

	protected Credential getCredential(User user) throws IOException {
		Credential credential = authorizationCodeFlow.loadCredential(user.getGoogleUserId());
		if (System.currentTimeMillis() > credential.getExpirationTimeMilliseconds()) {
			TokenResponse token = authorizationCodeFlow.newTokenRequest(user.getGoogleUserId()).setGrantType("authorization_code").execute();
			credential = authorizationCodeFlow.createAndStoreCredential(token, user.getGoogleUserId());
		}
		return credential;
	}

}
