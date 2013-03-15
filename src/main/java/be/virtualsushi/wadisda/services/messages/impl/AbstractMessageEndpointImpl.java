package be.virtualsushi.wadisda.services.messages.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.ioc.Messages;

import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.services.messages.MessageEndpoint;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;

public abstract class AbstractMessageEndpointImpl implements MessageEndpoint {

	private SimpleDateFormat dateFormat;
	private AuthorizationCodeFlow authorizationCodeFlow;

	public AbstractMessageEndpointImpl(Messages messages, AuthorizationCodeFlow authorizationCodeFlow) {
		this.authorizationCodeFlow = authorizationCodeFlow;
		this.dateFormat = new SimpleDateFormat(messages.get("date.pattern"));
	}

	protected String formatDueDate(Date dueDate) {
		return dateFormat.format(dueDate);
	}

	protected Credential getCredential(User user) throws IOException {
		return authorizationCodeFlow.loadCredential(user.getGoogleUserId());
	}

}
