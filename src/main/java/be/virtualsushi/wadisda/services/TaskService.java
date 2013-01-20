package be.virtualsushi.wadisda.services;

import java.util.Date;

import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.entities.enums.TaskTypes;

public interface TaskService {

	void createTask(TaskTypes type, User creator, User asignee, String name, Date dueDate);

}
