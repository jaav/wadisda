package be.virtualsushi.wadisda.services.tasks;

import java.io.IOException;

import be.virtualsushi.wadisda.entities.Task;

import com.google.api.client.auth.oauth2.Credential;

public interface TaskEndpoint {

	void send(Task task, Credential credential) throws IOException;

}
