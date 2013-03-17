package be.virtualsushi.wadisda.services.security.impl;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.slf4j.Logger;
import org.tynamo.security.federatedaccounts.services.FederatedAccountService;

import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.entities.enums.Roles;
import be.virtualsushi.wadisda.entities.valueobjects.CalendarInfo;
import be.virtualsushi.wadisda.entities.valueobjects.TasksListInfo;
import be.virtualsushi.wadisda.services.ExecutorService;
import be.virtualsushi.wadisda.services.google.GoogleApiClientSource;
import be.virtualsushi.wadisda.services.repository.UserRepository;
import be.virtualsushi.wadisda.services.security.GoogleAccount;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.oauth2.model.Userinfo;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.TaskList;

public class GoogleFederatedAccountServiceImpl implements FederatedAccountService {

	private class GetGoogleDefaultsRunnable implements Runnable {

		private String email;
		private Credential credential;

		public GetGoogleDefaultsRunnable(String email, Credential credential) {
			this.email = email;
			this.credential = credential;
		}

		@Override
		public void run() {
			try {
				Calendar calendarClient = googleApiClientSource.createCalendarClient(credential);
				CalendarListEntry calendarEntry = calendarClient.calendarList().get(email).execute();
				CalendarInfo calendarInfo = new CalendarInfo();
				if (calendarEntry != null) {
					calendarInfo.setId(calendarEntry.getId());
					calendarInfo.setSummary(calendarEntry.getSummary());
				}
				Tasks tasksClient = googleApiClientSource.createTasksClient(credential);
				TaskList taskList = tasksClient.tasklists().get("@default").execute();
				TasksListInfo tasksListInfo = new TasksListInfo();
				if (taskList != null) {
					tasksListInfo.setId(taskList.getId());
					tasksListInfo.setTitle(taskList.getTitle());
				}
				User user = userRepository.findByEmail(email);
				user.setCalendar(calendarInfo);
				user.setTasksList(tasksListInfo);
				userRepository.save(user);
			} catch (IOException e) {
				logger.error("Error getting Google services defaults (Calendar and Tasks list) for user: " + email, e);
			}
		}
	}

	@Inject
	private UserRepository userRepository;

	@Inject
	private ExecutorService executorService;

	@Inject
	private GoogleApiClientSource googleApiClientSource;

	private Logger logger;

	public GoogleFederatedAccountServiceImpl(Logger logger) {
		this.logger = logger;
	}

	@Override
	public AuthenticationInfo federate(String realmName, Object remotePrincipal, AuthenticationToken authenticationToken, Object remoteAccount) {
		Userinfo userInfo = (Userinfo) remoteAccount;
		User user = userRepository.findByEmail(userInfo.getEmail());
		if (user == null) {
			user = new User();
			user.setEmail(userInfo.getEmail());
			user.setName(userInfo.getName());
			if (StringUtils.isNotBlank(userInfo.getPicture())) {
				user.setAvatarUrl(userInfo.getPicture());
			} else {
				user.setAvatarUrl("/layout/images/avatar.png");
			}
			user.addRole(Roles.USER);
			user.setActive(false);
			executorService.execute(new GetGoogleDefaultsRunnable(userInfo.getEmail(), (Credential) authenticationToken.getCredentials()));
		}
		user.setGoogleUserId((String) remotePrincipal);
		userRepository.save(user);
		return new SimpleAccount(new GoogleAccount(user, (String) remotePrincipal), authenticationToken.getCredentials(), realmName);
	}
}
