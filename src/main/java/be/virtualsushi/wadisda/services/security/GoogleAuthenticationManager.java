package be.virtualsushi.wadisda.services.security;

public interface GoogleAuthenticationManager {

	void requestAuthorization();

	void authenticate(String userId);

}
