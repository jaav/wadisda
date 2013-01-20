package be.virtualsushi.wadisda.services.impl;

import be.virtualsushi.wadisda.entities.enums.TaskTypes;
import be.virtualsushi.wadisda.services.TaskEndpoint;
import be.virtualsushi.wadisda.services.TaskEndpointFactory;

public class TaskEndpointFactoryImpl implements TaskEndpointFactory {

	@Override
	public TaskEndpoint getEndpoint(TaskTypes type) {
		switch (type) {
		case CALENDAR_EVENT:
			return null;
		case SEND_EMAIL:
			return new EmailTaskEndpointImpl();
		case TODO_TASK:
			return null;
		}
		return null;
	}

}
