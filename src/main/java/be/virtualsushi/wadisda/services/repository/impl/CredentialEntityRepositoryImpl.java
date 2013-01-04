package be.virtualsushi.wadisda.services.repository.impl;

import java.io.IOException;

import javax.persistence.EntityManager;

import be.virtualsushi.wadisda.entities.CredentialEntity;
import be.virtualsushi.wadisda.services.repository.CredentialEntityRepository;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialStore;

public class CredentialEntityRepositoryImpl extends AbstractJpaRepositoryImpl<CredentialEntity> implements CredentialEntityRepository, CredentialStore {

	public CredentialEntityRepositoryImpl(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	public CredentialEntity findByUserId(String userId) {
		return null;
	}

	@Override
	public boolean load(String userId, Credential credential) throws IOException {
		CredentialEntity entity = findByUserId(userId);
		if (entity != null) {
			entity.load(credential);
			return true;
		}
		return false;
	}

	@Override
	public void store(String userId, Credential credential) throws IOException {
		CredentialEntity entity = new CredentialEntity();
		entity.store(credential);
		getEntityManager().persist(entity);
	}

	@Override
	public void delete(String userId, Credential credential) throws IOException {
		CredentialEntity entity = findByUserId(userId);
		getEntityManager().remove(entity);
	}

}
