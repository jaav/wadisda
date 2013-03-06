package be.virtualsushi.wadisda.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.SelectModelFactory;

import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.entities.valueobjects.CalendarInfo;
import be.virtualsushi.wadisda.entities.valueobjects.TasksListInfo;
import be.virtualsushi.wadisda.services.google.GoogleClientSource;
import be.virtualsushi.wadisda.services.repository.UserRepository;
import be.virtualsushi.wadisda.services.security.AuthenticationManager;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;

public class Account {

	@Inject
	private SelectModelFactory selectModelFactory;

	@Inject
	private GoogleClientSource googleClientSource;

	@Inject
	private AuthenticationManager authenticationManager;

	@Inject
	private UserRepository userRepository;

	@Property
	private User account;

	@OnEvent(value = EventConstants.ACTIVATE)
	public void onActivate() {
		account = authenticationManager.getCurrentUser();
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "accountForm")
	public Object onSubmit() {
		userRepository.save(account);
		authenticationManager.updateCurrentUserInfo(account);
		return Index.class;
	}

	public SelectModel getCalendarsModel() {
		Calendar client = googleClientSource.createCalendarClient(authenticationManager.getCurrentUserCredential());
		try {
			CalendarList list = client.calendarList().list().execute();
			List<CalendarInfo> calendars = new ArrayList<CalendarInfo>();
			for (CalendarListEntry entry : list.getItems()) {
				if ("owner".equals(entry.getAccessRole())) {
					CalendarInfo calendar = new CalendarInfo();
					calendar.setSummary(entry.getSummary());
					calendar.setId(entry.getId());
					calendars.add(calendar);
				}
			}
			return selectModelFactory.create(calendars, "summary");
		} catch (IOException e) {
			return selectModelFactory.create(Collections.EMPTY_LIST, "summary");
		}
	}

	public SelectModel getTodolistsModel() {
		Tasks client = googleClientSource.createTasksClient(authenticationManager.getCurrentUserCredential());
		try {
			TaskLists tasks = client.tasklists().list().execute();
			List<TasksListInfo> lists = new ArrayList<TasksListInfo>();
			for (TaskList list : tasks.getItems()) {
				TasksListInfo tasksList = new TasksListInfo();
				tasksList.setTitle(list.getTitle());
				tasksList.setId(list.getId());
				lists.add(tasksList);
			}
			return selectModelFactory.create(lists, "title");
		} catch (IOException e) {
			return selectModelFactory.create(Collections.EMPTY_LIST, "summary");
		}
	}

}