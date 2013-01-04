package be.virtualsushi.wadisda.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.google.api.client.auth.oauth2.Credential;

@Entity
@Table(name = "credential")
public class CredentialEntity extends BaseEntity {

	private static final long serialVersionUID = -122012465981259169L;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "access_token")
	private String accessToken;

	@Column(name = "refresh_token")
	private String refreshToken;

	@Column(name = "expiration_time_millis")
	private Long expirationTimeMillis;

	public void store(Credential credential) {
		setAccessToken(credential.getAccessToken());
		setRefreshToken(credential.getRefreshToken());
		setExpirationTimeMillis(credential.getExpirationTimeMilliseconds());
	}

	public void load(Credential credential) {
		credential.setAccessToken(getAccessToken());
		credential.setRefreshToken(getRefreshToken());
		credential.setExpirationTimeMilliseconds(getExpirationTimeMillis());
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Long getExpirationTimeMillis() {
		return expirationTimeMillis;
	}

	public void setExpirationTimeMillis(Long expirationTimeMillis) {
		this.expirationTimeMillis = expirationTimeMillis;
	}

}
