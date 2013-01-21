package be.virtualsushi.wadisda.services.tasks.impl;

import java.io.IOException;

import be.virtualsushi.wadisda.entities.Task;
import be.virtualsushi.wadisda.entities.enums.TaskStatuses;
import be.virtualsushi.wadisda.services.tasks.TaskEndpoint;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

public class GoogleCalendarTaskEndpointImpl implements TaskEndpoint {

	private final static String OWNER_ACCESS_ROLE = "owner";

	private final HttpTransport httpTransport;

	private final JsonFactory jsonFactory;

	public GoogleCalendarTaskEndpointImpl(HttpTransport httpTransport, JsonFactory jsonFactory) {
		this.httpTransport = httpTransport;
		this.jsonFactory = jsonFactory;
	}

	@Override
	public void send(Task task, Credential credential) throws IOException {
		Calendar client = new Calendar.Builder(httpTransport, jsonFactory, credential).build();
		CalendarList calendarList = client.calendarList().list().execute();
		for (CalendarListEntry calendar : calendarList.getItems()) {
			if (OWNER_ACCESS_ROLE.equals(calendar.getAccessRole())) {
				Event event = new Event();
				event.setStart(new EventDateTime().setDateTime(new DateTime(task.getRegistration().getEpoch())));
				event.setEnd(new EventDateTime().setDateTime(new DateTime(task.getRegistration().getEpoch().getTime() + task.getRegistration().getDuration())));
				event.setSummary(task.getName());
				client.events().insert(calendar.getId(), event).execute();
			}
		}
		task.setStatus(TaskStatuses.CREATED);
	}
}
