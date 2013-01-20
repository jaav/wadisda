package be.virtualsushi.wadisda.services.impl;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;

import org.slf4j.Logger;

import be.virtualsushi.wadisda.entities.Task;
import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.entities.enums.TaskStatuses;
import be.virtualsushi.wadisda.entities.enums.TaskTypes;
import be.virtualsushi.wadisda.services.TaskEndpointFactory;
import be.virtualsushi.wadisda.services.TaskService;
import be.virtualsushi.wadisda.services.repository.TaskRepository;

public class TaskServiceImpl implements TaskService {

	@Inject
	private TaskRepository taskRepository;

	@Inject
	private TaskEndpointFactory taskEndpointFactory;

	private final Logger logger;

	public TaskServiceImpl(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void createTask(TaskTypes type, User creator, User asignee, String name, Date dueDate) {
		Task task = new Task();
		task.setTaskType(type);
		task.setCreator(creator);
		task.setAssignee(asignee);
		task.setName(name);
		task.setDueDate(dueDate);
		task.setCreationDate(new Date());
		switch (type) {
		case CALENDAR_EVENT:
		case TODO_TASK:
			task.setStatus(TaskStatuses.CREATED);
			break;
		case SEND_EMAIL:
			task.setStatus(TaskStatuses.FINISHED);
			break;
		}
		taskRepository.save(task);
		try {
			taskEndpointFactory.getEndpoint(type).send(task, "");
		} catch (IOException e) {
			logger.error("Error sending task #" + task.getId(), e);
		}
	}

}
