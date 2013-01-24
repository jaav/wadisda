package be.virtualsushi.wadisda.services.tasks.impl;

import java.io.IOException;

import be.virtualsushi.wadisda.entities.Task;
import be.virtualsushi.wadisda.entities.enums.TaskStatuses;
import be.virtualsushi.wadisda.services.security.AuthenticationManager;
import be.virtualsushi.wadisda.services.tasks.TaskEndpoint;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

public class GoogleCalendarTaskEndpointImpl implements TaskEndpoint {

	private final HttpTransport httpTransport;

	private final JsonFactory jsonFactory;

	private final AuthenticationManager authenticationManager;

	public GoogleCalendarTaskEndpointImpl(HttpTransport httpTransport, JsonFactory jsonFactory, AuthenticationManager authenticationManager) {
		this.httpTransport = httpTransport;
		this.jsonFactory = jsonFactory;
		this.authenticationManager = authenticationManager;
	}

	@Override
	public void send(Task task, Credential credential) throws IOException {
		Calendar client = new Calendar.Builder(httpTransport, jsonFactory, credential).build();
		Event event = new Event();
		event.setStart(new EventDateTime().setDateTime(new DateTime(task.getRegistration().getEpoch())));
		event.setEnd(new EventDateTime().setDateTime(new DateTime(task.getRegistration().getEpoch().getTime() + task.getRegistration().getDuration())));
		event.setSummary(task.getName());
		client.events().insert(authenticationManager.getCurrentUser().getEmail(), event).execute();
		task.setStatus(TaskStatuses.CREATED);
	}
}
