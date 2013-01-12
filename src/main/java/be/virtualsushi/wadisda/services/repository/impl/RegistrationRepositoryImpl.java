package be.virtualsushi.wadisda.services.repository.impl;

import javax.persistence.EntityManager;

import be.virtualsushi.wadisda.entities.Registration;
import be.virtualsushi.wadisda.services.repository.RegistrationRepository;

public class RegistrationRepositoryImpl extends AbstractJpaRepositoryImpl<Registration> implements RegistrationRepository {

	public RegistrationRepositoryImpl(EntityManager entityManager) {
		super(entityManager);
	}

}
