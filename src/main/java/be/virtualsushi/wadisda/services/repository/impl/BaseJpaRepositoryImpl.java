package be.virtualsushi.wadisda.services.repository.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;

import be.virtualsushi.wadisda.entities.BaseEntity;
import be.virtualsushi.wadisda.services.repository.BaseJpaRepository;

public abstract class BaseJpaRepositoryImpl<T extends BaseEntity> extends ListJpaRepositoryImpl implements BaseJpaRepository<T> {

	public BaseJpaRepositoryImpl(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return getValuesList((Class<T>) ((ParameterizedType) BaseJpaRepositoryImpl.this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}

}
