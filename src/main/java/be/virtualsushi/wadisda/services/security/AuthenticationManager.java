package be.virtualsushi.wadisda.services.security;

import be.virtualsushi.wadisda.entities.User;

import com.google.api.client.auth.oauth2.Credential;

public interface AuthenticationManager {

	void requestAuthorization();

	void authorize(String userId);

	Credential getCurrentUserCredential();

	User getCurrentUser();

	void updateCurrentUserInfo(User user);

	void logout();

	boolean isAuthenticated();

}
