package be.virtualsushi.wadisda.services.repository.impl;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.services.repository.UserRepository;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialStore;

public class UserRepositoryImpl extends AbstractJpaRepositoryImpl<User> implements UserRepository, CredentialStore {

	public UserRepositoryImpl(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	public User findByEmail(String email) {
		Query query = getEntityManager().createQuery("from User where email=:email");
		query.setParameter("email", email);
		try {
			return (User) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean load(String userId, Credential credential) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void store(String userId, Credential credential) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String userId, Credential credential) throws IOException {
		// TODO Auto-generated method stub
	}

}
