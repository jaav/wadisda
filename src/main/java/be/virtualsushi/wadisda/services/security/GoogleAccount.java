package be.virtualsushi.wadisda.services.security;

import be.virtualsushi.wadisda.entities.User;

public class GoogleAccount {

	private User user;
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

	public void updateUser(User user) {
		this.user = user;
	}

}
