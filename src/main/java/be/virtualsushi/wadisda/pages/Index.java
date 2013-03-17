package be.virtualsushi.wadisda.pages;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.time.DateUtils;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.slf4j.Logger;

import be.virtualsushi.wadisda.services.security.AuthenticationManager;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;

/**
 * Start page of application wadisda.
 */
public class Index {

	private static final String EVENT_CONFIRMED_STATUS = "confirmed";

	@Inject
	private HttpTransport httpTransport;

	@Inject
	private JsonFactory jsonFactory;

	@Inject
	private AuthenticationManager authenticationManager;

	@Inject
	private Logger logger;

	@Inject
	private Messages messages;

	@Property
	private List<Event> events;

	@Property
	private Event event;

	@Property
	private List<Task> tasks;

	@Property
	private Task task;

	private SimpleDateFormat dateFormat;

	private SimpleDateFormat timeFormat;

	@OnEvent(value = EventConstants.PROGRESSIVE_DISPLAY, component = "calendarsList")
	public void onCalendarsListLoad() {

		Calendar client = new Calendar.Builder(httpTransport, jsonFactory, authenticationManager.getCurrentUserCredential()).build();
		try {
			Events eventsList = client.events().list(authenticationManager.getCurrentUser().getEmail()).execute();
			events = new ArrayList<Event>();
			for (Event event : eventsList.getItems()) {
				if (EVENT_CONFIRMED_STATUS.equals(event.getStatus())) {
					events.add(event);
				}
				if (events.size() >= 10) {
					break;
				}
			}
		} catch (IOException e) {
			logger.error("Failed to load events.", e);
		}

	}

	public String getEventTimeRange() {
		if (dateFormat == null) {
			dateFormat = new SimpleDateFormat(messages.get("date.pattern") + " " + messages.get("time.pattern"));
		}
		if (timeFormat == null) {
			timeFormat = new SimpleDateFormat(messages.get("time.pattern"));
		}
		Date startDate = new Date(event.getStart().getDateTime().getValue());
		Date endDate = new Date(event.getEnd().getDateTime().getValue());
		if (DateUtils.isSameDay(startDate, endDate)) {
			return dateFormat.format(startDate) + " - " + timeFormat.format(endDate);
		}
		return dateFormat.format(startDate) + " - " + dateFormat.format(endDate);
	}

	@OnEvent(value = EventConstants.PROGRESSIVE_DISPLAY, component = "tasksList")
	public void onTasksListLoad() {
		Tasks client = new Tasks.Builder(httpTransport, jsonFactory, authenticationManager.getCurrentUserCredential()).build();
		try {
			List<Task> tasksList = client.tasks().list("@default").execute().getItems();
			if (tasksList.size() > 10) {
				tasks = tasksList.subList(0, 9);
			} else {
				tasks = tasksList;
			}
		} catch (IOException e) {
			logger.error("Failed to load tasks.", e);
		}
	}

	public String getTaskDueDate() {
		if (task.getDue() == null) {
			return "";
		}
		return dateFormat.format(new Date(task.getDue().getValue()));
	}
}
