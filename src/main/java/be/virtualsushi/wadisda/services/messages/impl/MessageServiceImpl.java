package be.virtualsushi.wadisda.services.messages.impl;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.slf4j.Logger;

import be.virtualsushi.wadisda.entities.Registration;
import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.entities.enums.MessageTypes;
import be.virtualsushi.wadisda.entities.valueobjects.Message;
import be.virtualsushi.wadisda.services.ExecutorService;
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

	@InjectService("emailMessageEndpoint")
	private MessageEndpoint emailMessageEndpoint;

	@InjectService("googleCalendarMessageEndpoint")
	private MessageEndpoint googleCalendarMessageEndpoint;

	@InjectService("googleTasksMessageEndpoint")
	private MessageEndpoint googleTasksMessageEndpoint;

	@Inject
	private ExecutorService executorService;

	public MessageServiceImpl(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void sendMessage(MessageTypes type, Message message, User creator, Registration registration) {
		executorService.execute(new SendMessageRunnable(type, message, registration));
	}

}