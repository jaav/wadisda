package be.virtualsushi.wadisda.services.tasks;

import be.virtualsushi.wadisda.entities.enums.TaskTypes;

public interface TaskEndpointFactory {

	TaskEndpoint getEndpoint(TaskTypes type);

}
