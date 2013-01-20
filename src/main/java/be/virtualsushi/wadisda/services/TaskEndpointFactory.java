package be.virtualsushi.wadisda.services;

import be.virtualsushi.wadisda.entities.enums.TaskTypes;

public interface TaskEndpointFactory {

	TaskEndpoint getEndpoint(TaskTypes type);

}
