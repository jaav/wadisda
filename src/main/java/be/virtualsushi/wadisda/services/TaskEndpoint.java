package be.virtualsushi.wadisda.services;

import java.io.IOException;

import be.virtualsushi.wadisda.entities.Task;

public interface TaskEndpoint {

	void send(Task task, String oauthToken) throws IOException;

}
