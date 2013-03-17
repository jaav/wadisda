package be.virtualsushi.wadisda.services.messages.impl;

import java.io.IOException;
import java.security.Security;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import be.virtualsushi.wadisda.services.gmail.OAuth2Provider;
import be.virtualsushi.wadisda.services.gmail.OAuth2SaslClientFactory;

public class EmailSender {

	private Session session;

	public EmailSender(String accessToken) {
		Security.addProvider(new OAuth2Provider());
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");
		props.put("mail.smtp.sasl.enable", "true");
		props.put("mail.smtp.sasl.mechanisms", "XOAUTH2");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, accessToken);
		session = Session.getInstance(props);
		session.setDebug(true);
	}

	public void sendEmail(InternetAddress from, InternetAddress to, String subject, String text) throws IOException {
		MimeMessage emailMessage = new MimeMessage(session);

		try {
			emailMessage.setFrom(from);
			emailMessage.setRecipient(Message.RecipientType.TO, to);
			emailMessage.setSubject(subject);
			emailMessage.setText(text);

			Transport.send(emailMessage);

		} catch (MessagingException e) {
			throw new IOException(e);
		}
	}

}
