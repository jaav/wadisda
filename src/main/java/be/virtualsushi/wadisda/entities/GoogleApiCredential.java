package be.virtualsushi.wadisda.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.google.api.client.auth.oauth2.Credential;

@Entity
@Table(name="google_api_credential")
public class GoogleApiCredential extends BaseEntity {

	private static final long serialVersionUID = -7092758512289360051L;

	@Column(name = "google_user_id")
	private String googleUserId;

	@Column(name = "access_token")
	private String accessToken;

	@Column(name = "refresh_token")
	private String refreshToken;

	@Column(name = "expiration_time_millis")
	private Long expirationTimeMillis;

	public String getGoogleUserId() {
		return googleUserId;
	}

	public void setGoogleUserId(String googleUserId) {
		this.googleUserId = googleUserId;
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

	public void load(Credential credential) {
		credential.setAccessToken(accessToken);
		credential.setRefreshToken(refreshToken);
		credential.setExpirationTimeMilliseconds(expirationTimeMillis);
	}

	public static GoogleApiCredential store(String userId, Credential credential) {
		GoogleApiCredential result = new GoogleApiCredential();
		result.setGoogleUserId(userId);
		result.setAccessToken(credential.getAccessToken());
		result.setRefreshToken(credential.getRefreshToken());
		result.setExpirationTimeMillis(credential.getExpirationTimeMilliseconds());
		return result;
	}

}
