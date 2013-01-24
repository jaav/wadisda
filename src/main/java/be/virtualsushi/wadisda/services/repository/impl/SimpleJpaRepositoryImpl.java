package be.virtualsushi.wadisda.services.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.tapestry5.jpa.annotations.CommitAfter;

import be.virtualsushi.wadisda.services.repository.SimpleJpaRepository;

public class SimpleJpaRepositoryImpl extends ListJpaRepositoryImpl implements SimpleJpaRepository {

	public SimpleJpaRepositoryImpl(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	@CommitAfter
	public void save(Object entity) {
		getEntityManager().persist(entity);
	}

	@Override
	@CommitAfter
	public void delete(Object entity) {
		getEntityManager().remove(entity);
	}

	@Override
	public <T> T findOne(Class<T> persistedClass, Long id) {
		return getEntityManager().find(persistedClass, id);
	}

	@Override
	public <T> List<T> findAll(Class<T> persistedClass) {
		return getValuesList(persistedClass);
	}

}
