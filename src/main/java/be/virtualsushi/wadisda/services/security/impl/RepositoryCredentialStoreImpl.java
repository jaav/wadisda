package be.virtualsushi.wadisda.services.security.impl;

import java.io.IOException;

import javax.inject.Inject;

import be.virtualsushi.wadisda.entities.GoogleApiCredential;
import be.virtualsushi.wadisda.services.repository.GoogleApiCredentialRepository;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialStore;

public class RepositoryCredentialStoreImpl implements CredentialStore {

	@Inject
	private GoogleApiCredentialRepository googleApiCredentialRepository;

	@Override
	public boolean load(String userId, Credential credential) throws IOException {
		GoogleApiCredential storedCredential = googleApiCredentialRepository.findByGoogleUserId(userId);
		if (storedCredential == null) {
			return false;
		}
		storedCredential.load(credential);
		return true;
	}

	@Override
	public void store(String userId, Credential credential) throws IOException {
		GoogleApiCredential storedCredential = googleApiCredentialRepository.findByGoogleUserId(userId);
		if (storedCredential == null) {
			storedCredential = GoogleApiCredential.store(userId, credential);
		}
		googleApiCredentialRepository.save(storedCredential);
	}

	@Override
	public void delete(String userId, Credential credential) throws IOException {
		googleApiCredentialRepository.deleteByGoogleUserId(userId);
	}

}
