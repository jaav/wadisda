package be.virtualsushi.wadisda.services.tasks.impl;

import java.io.IOException;
import java.security.Provider;
import java.security.Security;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import be.virtualsushi.wadisda.entities.Task;
import be.virtualsushi.wadisda.entities.enums.TaskStatuses;
import be.virtualsushi.wadisda.services.gmail.OAuth2SaslClientFactory;
import be.virtualsushi.wadisda.services.tasks.TaskEndpoint;

import com.google.api.client.auth.oauth2.Credential;

public class EmailTaskEndpointImpl implements TaskEndpoint {

	public static final class OAuth2Provider extends Provider {
		private static final long serialVersionUID = 1L;

		public OAuth2Provider() {
			super("Google OAuth2 Provider", 1.0, "Provides the XOAUTH2 SASL Mechanism");
			put("SaslClientFactory.XOAUTH2", "com.google.code.samples.oauth2.OAuth2SaslClientFactory");
		}
	}

	public EmailTaskEndpointImpl() {
		Security.addProvider(new OAuth2Provider());
	}

	@Override
	public void send(Task task, Credential credential) throws IOException {
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");
		props.put("mail.smtp.sasl.enable", "true");
		props.put("mail.smtp.sasl.mechanisms", "XOAUTH2");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, credential.getAccessToken());
		Session session = Session.getInstance(props);
		session.setDebug(true);

		MimeMessage message = new MimeMessage(session);

		try {
			message.setFrom(new InternetAddress(task.getCreator().getEmail()));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(task.getAssignee().getEmail()));
			message.setSubject("New registration on Wadisda.");
			message.setText(task.getName());

			Transport.send(message);

		} catch (MessagingException e) {
			throw new IOException(e);
		}

		task.setStatus(TaskStatuses.FINISHED);
	}

}
