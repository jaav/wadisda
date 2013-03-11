package be.virtualsushi.wadisda.pages;

import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

import be.virtualsushi.wadisda.entities.Registration;
import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.entities.enums.MessageTypes;
import be.virtualsushi.wadisda.entities.valueobjects.Message;
import be.virtualsushi.wadisda.pages.registrations.Overview;
import be.virtualsushi.wadisda.services.messages.MessageService;
import be.virtualsushi.wadisda.services.repository.UserRepository;
import be.virtualsushi.wadisda.services.security.AuthenticationManager;

public class SendMessage {

	private static final int PAGE_SIZE = 15;

	@Inject
	private UserRepository userRepository;

	@Inject
	private MessageService messageService;

	@Inject
	private AuthenticationManager authenticationManager;

	@Property
	private User user;

	@Property
	private Message message;

	private MessageTypes messageType;

	private Registration registration;

	@SetupRender
	public void setupRender() {
		message = new Message();
	}

	@OnEvent("nextPage")
	public List<User> moreValues(int pageNumber) throws InterruptedException {
		int first = pageNumber * PAGE_SIZE;
		return userRepository.list(first, PAGE_SIZE);
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "messageForm")
	public Object onSubmitMessageSend() {
		messageService.sendMessage(messageType, message, authenticationManager.getCurrentUser(), registration);
		return Overview.class;
	}

	public MessageTypes getTaskType() {
		return messageType;
	}

	public void setTaskType(MessageTypes taskType) {
		this.messageType = taskType;
	}

	public Registration getRegistration() {
		return registration;
	}

	public void setRegistration(Registration registration) {
		this.registration = registration;
	}

	public boolean isUserAttended(User user) {
		return message.isUserAttended(user);
	}

}