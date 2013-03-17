package be.virtualsushi.wadisda.pages;

import javax.inject.Inject;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;

import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.services.repository.UserRepository;
import be.virtualsushi.wadisda.services.security.AuthenticationManager;

public class NotActivated extends BaseWadisdaPage {

	@Inject
	private UserRepository userRepository;

	@Inject
	private AuthenticationManager authenticationManager;

	@OnEvent(value = EventConstants.ACTION, component = "refresh")
	public Object onRefresh() {
		User user = authenticationManager.getCurrentUser();
		user = userRepository.findOne(user.getId());
		if (user.isActive()) {
			authenticationManager.updateCurrentUserInfo(user);
			return Index.class;
		}
		return null;
	}

}