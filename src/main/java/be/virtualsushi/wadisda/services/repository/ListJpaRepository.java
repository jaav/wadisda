package be.virtualsushi.wadisda.services.repository;

import java.util.List;

public interface ListJpaRepository {

	<T> List<T> getValuesList(Class<T> entityClass);

	<T> List<T> getValuesList(Class<T> entityClass, int offset, int count);

}
