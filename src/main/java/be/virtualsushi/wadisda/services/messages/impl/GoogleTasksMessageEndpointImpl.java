package be.virtualsushi.wadisda.services.messages.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import be.virtualsushi.wadisda.entities.Registration;
import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.entities.valueobjects.Message;
import be.virtualsushi.wadisda.services.google.GoogleApiClientSource;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.Task.Links;

public class GoogleTasksMessageEndpointImpl extends AbstractMessageEndpointImpl {

	@Autowired
	private GoogleApiClientSource googleApiClientSource;

	public GoogleTasksMessageEndpointImpl(AuthorizationCodeFlow authorizationCodeFlow) {
		super(authorizationCodeFlow);
	}

	@Override
	public void send(Message message, Registration registration) throws IOException {
		Task googleTask = new Task();
		googleTask.setTitle(message.getTitle());
		googleTask.setDue(new DateTime(message.getDueDate()));
		List<Task.Links> links = new ArrayList<Task.Links>();
		Links link = new Links();
		link.setLink(message.getRegistrationLink());
		link.setDescription(message.getDescription());
		links.add(link);
		googleTask.setLinks(links);
		googleTask.setStatus("needsAction");
		googleTask.setNotes(message.getDescription());

		for (User user : message.getAttendees()) {
			Tasks client = googleApiClientSource.createTasksClient(getCredential(user));
			client.tasks().insert(user.getTasksList().getId(), googleTask).execute();
		}
	}
}