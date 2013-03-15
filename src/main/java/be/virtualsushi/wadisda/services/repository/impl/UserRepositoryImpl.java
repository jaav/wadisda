package be.virtualsushi.wadisda.services.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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

	@Override
	public List<User> listWithoutCurrent(User current, int offset, int count) {
		TypedQuery<User> query = getEntityManager().createQuery("from User where active=true and id!=:id", User.class);
		query.setParameter("id", current.getId());
		query.setFirstResult(offset);
		query.setMaxResults(count);
		return query.getResultList();
	}

}
