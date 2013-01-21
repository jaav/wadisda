package be.virtualsushi.wadisda.services.security;

import be.virtualsushi.wadisda.entities.User;

public class GoogleAccount {

	private final User user;
	private final String googleUserId;

	public GoogleAccount(User user, String googleUserId) {
		this.user = user;
		this.googleUserId = googleUserId;
	}

	public User getUser() {
		return user;
	}

	public String getGoogleUserId() {
		return googleUserId;
	}

}
