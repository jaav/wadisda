package be.virtualsushi.wadisda.services.messages.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

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
		private Registration registration;

		public SendMessageRunnable(MessageTypes type, Message message, Registration registration) {
			this.type = type;
			this.message = message;
			this.registration = registration;
		}

		@Override
		public void run() {
			try {
				switch (type) {
				case EMAIL:
					emailMessageEndpoint.send(message, registration);
					break;
				case EVENT:
					googleCalendarMessageEndpoint.send(message, registration);
					break;
				case TASK:
					googleTasksMessageEndpoint.send(message, registration);
					break;
				}
			} catch (Exception e) {
				logger.error("Error sending message: ", e);
			}
		}

	}

	private final Logger logger;

	@Autowired
	private MessageEndpoint emailMessageEndpoint;

	@Autowired
	private MessageEndpoint googleCalendarMessageEndpoint;

	@Autowired
	private MessageEndpoint googleTasksMessageEndpoint;

	public MessageServiceImpl(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void sendMessage(MessageTypes type, Message message, User creator, Registration registration) {
	}

}