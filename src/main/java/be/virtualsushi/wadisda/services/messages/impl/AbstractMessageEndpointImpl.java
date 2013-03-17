package be.virtualsushi.wadisda.services.messages.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.ioc.Messages;

import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.services.messages.MessageEndpoint;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;

public abstract class AbstractMessageEndpointImpl implements MessageEndpoint {

	private SimpleDateFormat dateFormat;
	private AuthorizationCodeFlow authorizationCodeFlow;
	private Messages messages;

	public AbstractMessageEndpointImpl(Messages messages, AuthorizationCodeFlow authorizationCodeFlow) {
		this.authorizationCodeFlow = authorizationCodeFlow;
		this.dateFormat = new SimpleDateFormat(messages.get("date.pattern"));
		this.messages = messages;
	}

	protected String formatDueDate(Date dueDate) {
		return dateFormat.format(dueDate);
	}

	protected Credential getCredential(User user) throws IOException {
		Credential credential = authorizationCodeFlow.loadCredential(user.getGoogleUserId());
		if (System.currentTimeMillis() > credential.getExpirationTimeMilliseconds()) {
			TokenResponse token = authorizationCodeFlow.newTokenRequest(user.getGoogleUserId()).setGrantType("authorization_code").execute();
			credential = authorizationCodeFlow.createAndStoreCredential(token, user.getGoogleUserId());
		}
		return credential;
	}

	protected String getMessage(String key) {
		return messages.get(key);
	}

}
