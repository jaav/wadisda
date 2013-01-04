package be.virtualsushi.wadisda.components;

import javax.inject.Inject;

import be.virtualsushi.wadisda.services.security.GoogleAuthenticationManager;

public class GoogleSigninButton {

	@Inject
	private GoogleAuthenticationManager googleAuthenticationManager;

	public void onAction() {
		googleAuthenticationManager.requestAuthorization();
	}

}