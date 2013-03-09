package be.virtualsushi.wadisda.services.tasks.impl;

import java.io.IOException;

import be.virtualsushi.wadisda.entities.Task;
import be.virtualsushi.wadisda.entities.enums.TaskStatuses;
import be.virtualsushi.wadisda.services.google.GoogleApiClientSource;
import be.virtualsushi.wadisda.services.tasks.TaskEndpoint;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.Tasks;

public class GoogleTasksEndpointImpl implements TaskEndpoint {

	private final GoogleApiClientSource googleApiClientSource;

	public GoogleTasksEndpointImpl(GoogleApiClientSource googleApiClientSource) {
		this.googleApiClientSource = googleApiClientSource;
	}

	@Override
	public void send(Task task, Credential credential) throws IOException {
		Tasks client = googleApiClientSource.createTasksClient(credential);
		com.google.api.services.tasks.model.Task googleTask = new com.google.api.services.tasks.model.Task();
		googleTask.setTitle(task.getName());
		googleTask.setDue(new DateTime(task.getDueDate()));
		googleTask.setStatus("needsAction");
		client.tasks().insert("@default", googleTask).execute();
		task.setStatus(TaskStatuses.CREATED);
	}

}