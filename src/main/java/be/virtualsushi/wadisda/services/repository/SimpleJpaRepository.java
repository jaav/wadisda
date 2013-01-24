package be.virtualsushi.wadisda.services.repository;

import java.util.List;

import org.apache.tapestry5.jpa.annotations.CommitAfter;

public interface SimpleJpaRepository {

	@CommitAfter
	public void save(Object entity);

	@CommitAfter
	public void delete(Object entity);

	public <T> T findOne(Class<T> persistedClass, Long id);
	
	public <T> List<T> findAll(Class<T> persistedClass);

}
