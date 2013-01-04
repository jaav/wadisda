package be.virtualsushi.wadisda.services.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.services.repository.UserRepository;

public class UserRepositoryImpl extends AbstractJpaRepositoryImpl<User> implements UserRepository {

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

}
