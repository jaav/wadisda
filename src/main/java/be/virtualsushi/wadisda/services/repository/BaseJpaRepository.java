package be.virtualsushi.wadisda.services.repository;

import java.util.List;

import org.apache.tapestry5.jpa.annotations.CommitAfter;

import be.virtualsushi.wadisda.entities.BaseEntity;

public interface BaseJpaRepository<T extends BaseEntity> {

	List<T> findAll();

	@CommitAfter
	void save(T entity);

	@CommitAfter
	T merge(T entity);

	@CommitAfter
	void delete(T entity);

	T findOne(Long id);

	List<T> list(int offset, int count);

}
