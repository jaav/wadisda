package be.virtualsushi.wadisda.services.google.impl;

import org.springframework.beans.factory.annotation.Autowired;

import be.virtualsushi.wadisda.services.google.GoogleApiClientSource;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.tasks.Tasks;

public class GoogleApiClientSourceImpl implements GoogleApiClientSource {

	@Autowired
	private HttpTransport httpTransport;

	@Autowired
	private JsonFactory jsonFactory;

	@Override
	public Calendar createCalendarClient(Credential credential) {
		return new Calendar.Builder(httpTransport, jsonFactory, credential).build();
	}

	@Override
	public Tasks createTasksClient(Credential credential) {
		return new Tasks.Builder(httpTransport, jsonFactory, credential).build();
	}

}
