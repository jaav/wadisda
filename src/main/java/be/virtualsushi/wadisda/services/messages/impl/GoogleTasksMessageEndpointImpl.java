package be.virtualsushi.wadisda.services.messages.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.Messages;

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

	@Inject
	private GoogleApiClientSource googleApiClientSource;

	public GoogleTasksMessageEndpointImpl(Messages messages, LinkSource linkSource, AuthorizationCodeFlow authorizationCodeFlow) {
		super(messages, linkSource, authorizationCodeFlow);
	}

	@Override
	public void send(Message message, Registration registration, User creator) throws IOException {
		Task googleTask = new Task();
		googleTask.setTitle(message.getTitle());
		googleTask.setDue(new DateTime(message.getDueDate()));
		List<Task.Links> links = new ArrayList<Task.Links>();
		Links link = new Links();
		link.setLink(generateRegistrationLink(registration));
		link.setDescription(message.getDescription());
		links.add(link);
		googleTask.setLinks(links);
		googleTask.setStatus("needsAction");

		for (User user : message.getAttendees()) {
			Tasks client = googleApiClientSource.createTasksClient(getCredential(user));
			client.tasks().insert(user.getTasksList().getId(), googleTask).execute();
		}
	}
}