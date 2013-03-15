package be.virtualsushi.wadisda.services.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import be.virtualsushi.wadisda.services.repository.ListJpaRepository;

public class ListJpaRepositoryImpl implements ListJpaRepository {

	private final EntityManager entityManager;

	public ListJpaRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public <T> List<T> getValuesList(Class<T> entityClass) {
		return createBaseQuery(entityClass).getResultList();
	}

	protected EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public <T> List<T> getValuesList(Class<T> entityClass, int offset, int count) {
		TypedQuery<T> query = createBaseQuery(entityClass);
		query.setFirstResult(offset);
		query.setMaxResults(count);
		return query.getResultList();
	}

	protected <T> TypedQuery<T> createBaseQuery(Class<T> entityClass) {
		CriteriaQuery<T> query = entityManager.getCriteriaBuilder().createQuery(entityClass);
		Root<T> root = query.from(entityClass);
		query.select(root);
		return entityManager.createQuery(query);
	}
}
