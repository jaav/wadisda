package be.virtualsushi.wadisda.services.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import be.virtualsushi.wadisda.entities.GoogleApiCredential;
import be.virtualsushi.wadisda.services.repository.GoogleApiCredentialRepository;

public class GoogleApiCredentialRepositoryImpl extends AbstractJpaRepositoryImpl<GoogleApiCredential> implements GoogleApiCredentialRepository {

	public GoogleApiCredentialRepositoryImpl(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	public GoogleApiCredential findByGoogleUserId(String googleUserId) {
		Query query = getEntityManager().createQuery("from GoogleApiCredential where googleUserId=:googleUserId");
		query.setParameter("googleUserId", googleUserId);
		try {
			return (GoogleApiCredential) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void deleteByGoogleUserId(String googleUserId) {
		Query query = getEntityManager().createQuery("delete from GoogleApiCredential where googleUserId=:googleUserId");
		query.setParameter("googleUserId", googleUserId);
		query.executeUpdate();
	}

}
