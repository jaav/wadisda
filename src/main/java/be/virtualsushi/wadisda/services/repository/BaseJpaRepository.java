package be.virtualsushi.wadisda.services.repository;

import java.util.List;

import be.virtualsushi.wadisda.entities.BaseEntity;

public interface BaseJpaRepository<T extends BaseEntity> {

	List<T> findAll();

}
