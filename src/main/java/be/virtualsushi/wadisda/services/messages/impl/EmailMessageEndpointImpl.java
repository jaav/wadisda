package be.virtualsushi.wadisda.services.messages.impl;

import javax.mail.internet.InternetAddress;

import be.virtualsushi.wadisda.entities.Registration;
import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.entities.valueobjects.Message;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;

public class EmailMessageEndpointImpl extends AbstractMessageEndpointImpl {

	public EmailMessageEndpointImpl(AuthorizationCodeFlow authorizationCodeFlow) {
		super(authorizationCodeFlow);
	}

	@Override
	public void send(Message message, Registration registration) throws Exception {
		EmailSender emailSender = new EmailSender(getCredential(registration.getUser()).getAccessToken());

		InternetAddress from = new InternetAddress(registration.getUser().getEmail());

		StringBuilder textBuilder = new StringBuilder(message.getDescription());
		textBuilder.append(message.getRegistrationLink());

		for (User recipient : message.getAttendees()) {
			emailSender.sendEmail(from, new InternetAddress(recipient.getEmail()), message.getTitle(), textBuilder.toString());
		}
	}
}
