package be.virtualsushi.wadisda.services.security;

import java.io.Serializable;

import be.virtualsushi.wadisda.entities.User;

public class GoogleAccount implements Serializable {

	private static final long serialVersionUID = 7739345912771841482L;

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
