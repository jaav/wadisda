package be.virtualsushi.wadisda.services.tasks.impl;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import be.virtualsushi.wadisda.entities.enums.TaskTypes;
import be.virtualsushi.wadisda.services.security.AuthenticationManager;
import be.virtualsushi.wadisda.services.tasks.TaskEndpoint;
import be.virtualsushi.wadisda.services.tasks.TaskEndpointFactory;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

public class TaskEndpointFactoryImpl implements TaskEndpointFactory {

	@Inject
	private HttpTransport httpTransport;

	@Inject
	private JsonFactory jsonFactory;

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
			result = new GoogleCalendarTaskEndpointImpl(httpTransport, jsonFactory, authenticationManager);
			break;
		case SEND_EMAIL:
			result = new EmailTaskEndpointImpl();
			break;
		case TODO_TASK:
			result = new GoogleTasksEndpointImpl(httpTransport, jsonFactory);
			break;
		}
		if (result != null) {
			endpointsPool.put(type, result);
		}
		return result;
	}

}
