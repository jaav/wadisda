package be.virtualsushi.wadisda.services.tasks.impl;

import javax.inject.Inject;

import be.virtualsushi.wadisda.entities.enums.TaskTypes;
import be.virtualsushi.wadisda.services.tasks.TaskEndpoint;
import be.virtualsushi.wadisda.services.tasks.TaskEndpointFactory;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

public class TaskEndpointFactoryImpl implements TaskEndpointFactory {

	@Inject
	private HttpTransport httpTransport;

	@Inject
	private JsonFactory jsonFactory;

	@Override
	public TaskEndpoint getEndpoint(TaskTypes type) {
		switch (type) {
		case CALENDAR_EVENT:
			return new GoogleCalendarTaskEndpointImpl(httpTransport, jsonFactory);
		case SEND_EMAIL:
			return new EmailTaskEndpointImpl();
		case TODO_TASK:
			return new GoogleTasksEndpointImpl(httpTransport, jsonFactory);
		}
		return null;
	}

}
