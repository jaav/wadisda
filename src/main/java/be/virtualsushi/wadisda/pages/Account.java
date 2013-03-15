package be.virtualsushi.wadisda.pages;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.SelectModelFactory;

import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.entities.valueobjects.CalendarInfo;
import be.virtualsushi.wadisda.entities.valueobjects.TasksListInfo;
import be.virtualsushi.wadisda.services.google.GoogleApiClientSource;
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
	private GoogleApiClientSource googleClientSource;

	@Inject
	private AuthenticationManager authenticationManager;

	@Inject
	private UserRepository userRepository;

	@Property
	private User account;

	private List<CalendarInfo> calendarInfos;

	private List<TasksListInfo> tasksListInfos;

	@OnEvent(value = EventConstants.ACTIVATE)
	public void onActivate() {
		account = authenticationManager.getCurrentUser();
		calendarInfos = getCalendarInfosList();
		tasksListInfos = getTasksListInfosList();
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "accountForm")
	public Object onSubmit() throws IllegalAccessException, InvocationTargetException {
		User attachedUser = userRepository.findOne(account.getId());
		BeanUtils.copyProperties(attachedUser, account);
		userRepository.save(attachedUser);
		authenticationManager.updateCurrentUserInfo(attachedUser);
		return Index.class;
	}

	private List<CalendarInfo> getCalendarInfosList() {
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
			return calendars;
		} catch (IOException e) {
			return new ArrayList<CalendarInfo>();
		}
	}

	private List<TasksListInfo> getTasksListInfosList() {
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
			return lists;
		} catch (IOException e) {
			return new ArrayList<TasksListInfo>();
		}
	}

	public SelectModel getCalendarsModel() {
		return selectModelFactory.create(calendarInfos, "summary");
	}

	public SelectModel getTodolistsModel() {
		return selectModelFactory.create(tasksListInfos, "title");
	}

	public ValueEncoder<CalendarInfo> getCalendarInfoEncoder() {
		return new ValueEncoder<CalendarInfo>() {

			@Override
			public String toClient(CalendarInfo value) {
				return value.getId();
			}

			@Override
			public CalendarInfo toValue(String clientValue) {
				for (CalendarInfo calendarInfo : calendarInfos) {
					if (calendarInfo.getId().equals(clientValue)) {
						return calendarInfo;
					}
				}
				return null;
			}
		};
	}

	public ValueEncoder<TasksListInfo> getTasksListInfoEncoder() {
		return new ValueEncoder<TasksListInfo>() {

			@Override
			public String toClient(TasksListInfo value) {
				return value.getId();
			}

			@Override
			public TasksListInfo toValue(String clientValue) {
				for (TasksListInfo tasksListInfo : tasksListInfos) {
					if (tasksListInfo.getId().equals(clientValue)) {
						return tasksListInfo;
					}
				}
				return null;
			}
		};
	}

}