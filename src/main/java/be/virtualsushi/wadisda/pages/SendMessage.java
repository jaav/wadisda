package be.virtualsushi.wadisda.pages;

import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.entities.enums.TaskTypes;
import be.virtualsushi.wadisda.services.repository.UserRepository;

public class SendMessage {

	private static final int PAGE_SIZE = 15;

	@Inject
	private UserRepository userRepository;

	@Property
	private User user;

	private Integer context;

	private TaskTypes taskType;

	@OnEvent(value = EventConstants.ACTIVATE)
	public void onActivate(Integer context) {
		this.context = context;
	}

	@OnEvent(value = EventConstants.PASSIVATE)
	public Integer onPassivate() {
		return context;
	}

	@SetupRender
	public void setupRender() {
		taskType = TaskTypes.values()[context];
	}

	@OnEvent("nextPage")
	public List<User> moreValues(int pageNumber) throws InterruptedException {
		int first = pageNumber * PAGE_SIZE;
		return userRepository.list(first, PAGE_SIZE);
	}

}