package be.virtualsushi.wadisda.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import be.virtualsushi.wadisda.entities.BaseEntity;

public interface BaseEntityRepository<T extends BaseEntity> extends PagingAndSortingRepository<T, Long> {

}
