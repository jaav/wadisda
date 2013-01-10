package be.virtualsushi.wadisda.services.repository.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;

import be.virtualsushi.wadisda.entities.BaseEntity;
import be.virtualsushi.wadisda.services.repository.BaseJpaRepository;

public abstract class AbstractJpaRepositoryImpl<T extends BaseEntity> extends ListJpaRepositoryImpl implements BaseJpaRepository<T> {

	private Class<T> persistedClass;

	@SuppressWarnings("unchecked")
	public AbstractJpaRepositoryImpl(EntityManager entityManager) {
		super(entityManager);
		persistedClass = (Class<T>) ((ParameterizedType) AbstractJpaRepositoryImpl.this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Override
	public List<T> findAll() {
		return getValuesList(persistedClass);
	}

	@Override
	public void save(T entity) {
		getEntityManager().persist(entity);
	}

	@Override
	public void delete(T entity) {
		getEntityManager().detach(entity);
	}

	@Override
	public void findOne(Long id) {
		getEntityManager().find(persistedClass, id);
	}

}
