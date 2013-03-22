package be.virtualsushi.wadisda.services.security.impl;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.slf4j.Logger;
import org.tynamo.security.federatedaccounts.FederatedAccount.FederatedAccountType;
import org.tynamo.security.federatedaccounts.services.FederatedAccountService;

import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.entities.enums.Roles;
import be.virtualsushi.wadisda.entities.valueobjects.CalendarInfo;
import be.virtualsushi.wadisda.entities.valueobjects.TasksListInfo;
import be.virtualsushi.wadisda.services.ExecutorService;
import be.virtualsushi.wadisda.services.GoogleApiRequestExecutor;
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

	private static final String DEFAULT_AVATAR_URL = "/layout/images/avatar.png";

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
				CalendarListEntry calendarEntry = googleApiRequestExecutor.execute(calendarClient.calendarList().get(email));
				Tasks tasksClient = googleApiClientSource.createTasksClient(credential);
				TaskList taskList = googleApiRequestExecutor.execute(tasksClient.tasklists().get("@default"));
				User user = userRepository.findByEmail(email);
				if (calendarEntry != null) {
					user.setCalendar(new CalendarInfo(calendarEntry.getId(), calendarEntry.getSummary()));
				}
				if (taskList != null) {
					user.setTasksList(new TasksListInfo(taskList.getId(), taskList.getTitle()));
				}
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

	@Inject
	private GoogleApiRequestExecutor googleApiRequestExecutor;

	private Logger logger;

	public GoogleFederatedAccountServiceImpl(Logger logger) {
		this.logger = logger;
	}

	@Override
	public AuthenticationInfo federate(String realmName, Object remotePrincipal, AuthenticationToken authenticationToken, Object remoteAccount) {
		User user = null;
		if (FederatedAccountType.google.name().equals(realmName)) {
			user = doLoginFederate(remotePrincipal, (Credential) authenticationToken.getCredentials(), (String) remoteAccount);
		} else {
			user = doRemmberMeFederate(remotePrincipal);
		}
		return new SimpleAccount(new GoogleAccount(user, user.getGoogleUserId()), authenticationToken.getCredentials(), realmName);
	}

	private User doRemmberMeFederate(Object remotePrincipal) {
		GoogleAccount account = (GoogleAccount) remotePrincipal;
		return userRepository.findByEmail(account.getUser().getEmail());
	}

	private User doLoginFederate(Object remotePrincipal, Credential credential, String remoteUserId) {
		Userinfo userInfo = (Userinfo) remotePrincipal;
		User user = userRepository.findByEmail(userInfo.getEmail());
		if (user == null) {
			user = new User();
			user.setEmail(userInfo.getEmail());
			user.setName(userInfo.getName());
			user.setAvatarUrl(StringUtils.isNotBlank(userInfo.getPicture()) ? userInfo.getPicture() : DEFAULT_AVATAR_URL);
			user.addRole(Roles.USER);
			user.setActive(false);
			user.setCalendar(new CalendarInfo(user.getEmail(), ""));
			user.setTasksList(new TasksListInfo("@default", ""));
			executorService.execute(new GetGoogleDefaultsRunnable(userInfo.getEmail(), credential));
		} else {
			if (StringUtils.isNotBlank(userInfo.getPicture()) && DEFAULT_AVATAR_URL.equals(user.getAvatarUrl())) {
				user.setAvatarUrl(userInfo.getPicture());
			}
		}
		user.setGoogleUserId(remoteUserId);
		userRepository.save(user);
		return user;
	}
}
