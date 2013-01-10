package be.virtualsushi.wadisda.services.repository;

import java.util.List;

import org.apache.tapestry5.jpa.annotations.CommitAfter;

import be.virtualsushi.wadisda.entities.BaseEntity;

public interface BaseJpaRepository<T extends BaseEntity> {

	List<T> findAll();

	@CommitAfter
	void save(T entity);

	@CommitAfter
	void delete(T entity);

	void findOne(Long id);

}
