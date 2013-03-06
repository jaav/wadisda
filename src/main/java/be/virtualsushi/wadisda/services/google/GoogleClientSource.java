package be.virtualsushi.wadisda.services.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.tasks.Tasks;

public interface GoogleClientSource {

	public Calendar createCalendarClient(Credential credential);

	public Tasks createTasksClient(Credential credential);

}
