package be.virtualsushi.wadisda.pages.registrations;

import java.io.IOException;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Response;

import be.virtualsushi.wadisda.entities.Registration;
import be.virtualsushi.wadisda.services.repository.RegistrationRepository;

public class View {

	@Inject
	private RegistrationRepository registrationRepository;

	@Inject
	private Response response;

	@Property
	private Registration registration;

	private Long registrationId;

	@OnEvent(value = EventConstants.ACTIVATE)
	public void onActivate(Long registrationId) {
		this.registrationId = registrationId;
	}

	@OnEvent(value = EventConstants.PASSIVATE)
	public Long onPassivate() {
		return registrationId;
	}

	@SetupRender
	public void stupRender() throws IOException {
		registration = registrationRepository.findOne(registrationId);
		if (registration == null) {
			response.sendRedirect("overview");
		}
	}

}