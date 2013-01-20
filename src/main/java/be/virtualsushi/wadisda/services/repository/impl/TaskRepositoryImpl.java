package be.virtualsushi.wadisda.services.repository.impl;

import javax.persistence.EntityManager;

import be.virtualsushi.wadisda.entities.Task;
import be.virtualsushi.wadisda.services.repository.TaskRepository;

public class TaskRepositoryImpl extends AbstractJpaRepositoryImpl<Task> implements TaskRepository {

	public TaskRepositoryImpl(EntityManager entityManager) {
		super(entityManager);
	}

}
