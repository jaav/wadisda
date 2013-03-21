package be.virtualsushi.wadisda.services.impl;

import java.io.IOException;

import be.virtualsushi.wadisda.services.GoogleApiRequestExecutor;

import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;

public class GoogleApiRequestExecutorImpl implements GoogleApiRequestExecutor {

	private static final int TRIES_COUNT = 5;

	@Override
	public <T> T execute(AbstractGoogleClientRequest<T> request) throws IOException {
		IOException exception = null;
		for (int i = 0; i < TRIES_COUNT; i++) {
			try {
				return request.execute();
			} catch (IOException e) {
				exception = e;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {

			}
		}
		throw exception;
	}

	@Override
	public TokenResponse execute(TokenRequest request) throws IOException {
		IOException exception = null;
		for (int i = 0; i < TRIES_COUNT; i++) {
			try {
				return request.execute();
			} catch (IOException e) {
				exception = e;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {

			}
		}
		throw exception;
	}
}
