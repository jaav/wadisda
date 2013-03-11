package be.virtualsushi.wadisda.services.messages.impl;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.tapestry5.ioc.annotations.InjectService;
import org.slf4j.Logger;

import be.virtualsushi.wadisda.entities.Registration;
import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.entities.enums.MessageTypes;
import be.virtualsushi.wadisda.entities.valueobjects.Message;
import be.virtualsushi.wadisda.services.messages.MessageEndpoint;
import be.virtualsushi.wadisda.services.messages.MessageService;

public class MessageServiceImpl implements MessageService {

	private class SendMessageRunnable implements Runnable {

		private final MessageTypes type;
		private final Message message;
		private final User user;
		private Registration registration;

		public SendMessageRunnable(MessageTypes type, Message message, User creator, Registration registration) {
			this.type = type;
			this.message = message;
			this.user = creator;
			this.registration = registration;
		}

		@Override
		public void run() {
			try {
				switch (type) {
				case EMAIL:
					emailMessageEndpoint.send(message, registration, user);
					break;
				case EVENT:
					googleCalendarMessageEndpoint.send(message, registration, user);
					break;
				case TASK:
					googleTasksMessageEndpoint.send(message, registration, user);
					break;
				}
			} catch (Exception e) {
				logger.error("Error sending message: ", e);
			}
		}

	}

	private final Logger logger;

	@InjectService("emailMessageEndpoint")
	private MessageEndpoint emailMessageEndpoint;

	@InjectService("googleCalendarMessageEndpoint")
	private MessageEndpoint googleCalendarMessageEndpoint;

	@InjectService("googleTasksMessageEndpoint")
	private MessageEndpoint googleTasksMessageEndpoint;

	private Executor executor;

	public MessageServiceImpl(Logger logger) {
		this.logger = logger;
		executor = Executors.newFixedThreadPool(10);
	}

	@Override
	public void sendMessage(MessageTypes type, Message message, User creator, Registration registration) {
		executor.execute(new SendMessageRunnable(type, message, creator, registration));
	}

}