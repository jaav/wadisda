package be.virtualsushi.wadisda.services.messages.impl;

import javax.mail.internet.InternetAddress;

import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.Messages;

import be.virtualsushi.wadisda.entities.Registration;
import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.entities.valueobjects.Message;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;

public class EmailMessageEndpointImpl extends AbstractMessageEndpointImpl {

	public EmailMessageEndpointImpl(Messages messages, LinkSource linkSource, AuthorizationCodeFlow authorizationCodeFlow) {
		super(messages, linkSource, authorizationCodeFlow);
	}

	@Override
	public void send(Message message, Registration registration, User creator) throws Exception {
		EmailSender emailSender = new EmailSender(getCredential(creator).getAccessToken());

		InternetAddress from = new InternetAddress(creator.getEmail());

		StringBuilder textBuilder = new StringBuilder(message.getDescription());
		textBuilder.append(generateRegistrationLink(registration));
		textBuilder.append(formatDueDate(message.getDueDate()));

		for (User recipient : message.getAttendees()) {
			emailSender.sendEmail(from, new InternetAddress(recipient.getEmail()), message.getTitle(), textBuilder.toString());
		}
	}
}
