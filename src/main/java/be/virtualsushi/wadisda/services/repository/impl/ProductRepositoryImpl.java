package be.virtualsushi.wadisda.services.repository.impl;

import javax.persistence.EntityManager;

import be.virtualsushi.wadisda.entities.Product;
import be.virtualsushi.wadisda.services.repository.ProductRepository;

public class ProductRepositoryImpl extends AbstractJpaRepositoryImpl<Product> implements ProductRepository {

	public ProductRepositoryImpl(EntityManager entityManager) {
		super(entityManager);
	}

}
