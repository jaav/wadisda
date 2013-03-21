package be.virtualsushi.wadisda.services;

import java.io.IOException;

import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;

public interface GoogleApiRequestExecutor {

	public <T> T execute(AbstractGoogleClientRequest<T> request) throws IOException;

	public TokenResponse execute(TokenRequest request) throws IOException;

}
