package be.virtualsushi.wadisda.services.tasks.impl;

import java.util.Date;

import javax.inject.Inject;

import org.slf4j.Logger;

import be.virtualsushi.wadisda.entities.Registration;
import be.virtualsushi.wadisda.entities.Task;
import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.entities.enums.TaskStatuses;
import be.virtualsushi.wadisda.entities.enums.TaskTypes;
import be.virtualsushi.wadisda.services.repository.TaskRepository;
import be.virtualsushi.wadisda.services.security.AuthenticationManager;
import be.virtualsushi.wadisda.services.tasks.TaskEndpointFactory;
import be.virtualsushi.wadisda.services.tasks.TaskService;

public class TaskServiceImpl implements TaskService {

	@Inject
	private TaskRepository taskRepository;

	@Inject
	private TaskEndpointFactory taskEndpointFactory;

	@Inject
	private AuthenticationManager authenticationManager;

	private final Logger logger;

	public TaskServiceImpl(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void createTask(TaskTypes type, User creator, User asignee, String name, Date dueDate, Registration registration) {
		Task task = new Task();
		task.setTaskType(type);
		task.setCreator(creator);
		task.setAssignee(asignee);
		task.setName(name);
		task.setDueDate(dueDate);
		task.setCreationDate(new Date());
		task.setRegistration(registration);
		try {
			taskEndpointFactory.getEndpoint(type).send(task, authenticationManager.getCurrentUserCredential());
		} catch (Exception e) {
			logger.error("Error sending task #" + task.getId(), e);
			task.setStatus(TaskStatuses.CREATED);
		}
		taskRepository.save(task);
	}

}