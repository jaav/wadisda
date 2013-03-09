package be.virtualsushi.wadisda.services.tasks.impl;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import be.virtualsushi.wadisda.entities.enums.TaskTypes;
import be.virtualsushi.wadisda.services.google.GoogleApiClientSource;
import be.virtualsushi.wadisda.services.security.AuthenticationManager;
import be.virtualsushi.wadisda.services.tasks.TaskEndpoint;
import be.virtualsushi.wadisda.services.tasks.TaskEndpointFactory;

public class TaskEndpointFactoryImpl implements TaskEndpointFactory {

	@Inject
	private GoogleApiClientSource googleApiClientSource;

	@Inject
	private AuthenticationManager authenticationManager;

	private Map<TaskTypes, TaskEndpoint> endpointsPool = new HashMap<TaskTypes, TaskEndpoint>();

	@Override
	public TaskEndpoint getEndpoint(TaskTypes type) {
		if (endpointsPool.containsKey(type)) {
			return endpointsPool.get(type);
		}
		return createEndpoint(type);
	}

	private TaskEndpoint createEndpoint(TaskTypes type) {
		TaskEndpoint result = null;
		switch (type) {
		case CALENDAR_EVENT:
			result = new GoogleCalendarTaskEndpointImpl(googleApiClientSource, authenticationManager);
			break;
		case SEND_EMAIL:
			result = new EmailTaskEndpointImpl();
			break;
		case TODO_TASK:
			result = new GoogleTasksEndpointImpl(googleApiClientSource);
			break;
		}
		if (result != null) {
			endpointsPool.put(type, result);
		}
		return result;
	}

}
