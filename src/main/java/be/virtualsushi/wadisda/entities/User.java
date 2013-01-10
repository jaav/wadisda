package be.virtualsushi.wadisda.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User extends IdNameEntity {

	private static final long serialVersionUID = -3133182804040655061L;

	@Column(name = "email")
	private String email;

	@Column(name = "avatar_url")
	private String avatarUrl;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

}
