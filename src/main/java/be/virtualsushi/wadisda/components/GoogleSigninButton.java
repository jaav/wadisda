package be.virtualsushi.wadisda.components;

import javax.inject.Inject;

import be.virtualsushi.wadisda.services.security.AuthenticationManager;

public class GoogleSigninButton {

	@Inject
	private AuthenticationManager googleAuthenticationManager;

	public void onAction() {
		googleAuthenticationManager.requestAuthorization();
	}

}