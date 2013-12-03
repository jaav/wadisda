package be.virtualsushi.wadisda.services.messages.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import be.virtualsushi.wadisda.entities.Registration;
import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.entities.valueobjects.Message;
import be.virtualsushi.wadisda.services.google.GoogleApiClientSource;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;

public class GoogleCalendarMessageEndpointImpl extends AbstractMessageEndpointImpl {

	@Autowired
	private GoogleApiClientSource googleClientSource;

	public GoogleCalendarMessageEndpointImpl(AuthorizationCodeFlow authorizationCodeFlow) {
		super(authorizationCodeFlow);
	}

	@Override
	public void send(Message message, Registration registration) throws IOException {
		Calendar client = googleClientSource.createCalendarClient(getCredential(registration.getUser()));
		Event event = new Event();
		event.setStart(new EventDateTime().setDateTime(new DateTime(registration.getEpoch())));
		event.setEnd(new EventDateTime().setDateTime(new DateTime(registration.getEpoch().getTime() + registration.getDuration())));
		event.setSummary(message.getTitle());
		event.setDescription(message.getDescription() + message.getRegistrationLink() + message.getDueDate());
		List<EventAttendee> attendees = new ArrayList<EventAttendee>();
		for (User user : message.getAttendees()) {
			EventAttendee attendee = new EventAttendee();
			attendee.setEmail(user.getEmail());
			attendee.setDisplayName(user.getName());
			attendees.add(attendee);
		}
		event.setAttendees(attendees);
		client.events().insert(registration.getUser().getCalendar().getId(), event).execute();
	}
}
