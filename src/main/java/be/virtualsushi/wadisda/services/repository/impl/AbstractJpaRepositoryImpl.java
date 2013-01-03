package be.virtualsushi.wadisda.services.repository.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;

import be.virtualsushi.wadisda.entities.BaseEntity;
import be.virtualsushi.wadisda.services.repository.BaseJpaRepository;

public abstract class AbstractJpaRepositoryImpl<T extends BaseEntity> extends ListJpaRepositoryImpl implements BaseJpaRepository<T> {

	public AbstractJpaRepositoryImpl(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return getValuesList((Class<T>) ((ParameterizedType) AbstractJpaRepositoryImpl.this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}

}
