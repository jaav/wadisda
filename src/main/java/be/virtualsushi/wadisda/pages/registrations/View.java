package be.virtualsushi.wadisda.pages.registrations;

import java.io.IOException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Response;

import be.virtualsushi.wadisda.entities.Registration;
import be.virtualsushi.wadisda.services.DateFormatService;
import be.virtualsushi.wadisda.services.repository.RegistrationRepository;
import be.virtualsushi.wadisda.services.security.AuthenticationManager;

public class View extends RegistrationsPage {

	@Inject
	private RegistrationRepository registrationRepository;

	@Inject
	private Response response;

	@Inject
	private DateFormatService dateFormatService;

	@Inject
	private AuthenticationManager authenticationManager;

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

	public String getDate() {
		return dateFormatService.formatDate(registration.getEpoch());
	}

	public String getTime() {
		return dateFormatService.formatTime(registration.getEpoch());
	}

	public String getRegistrationRelation() {
		if (CollectionUtils.isNotEmpty(registration.getRelations())) {
			return registration.getRelations().iterator().next().getName();
		}
		return null;
	}

	public String getRegistrationProduct() {
		if (CollectionUtils.isNotEmpty(registration.getProducts())) {
			return registration.getProducts().iterator().next().getName();
		}
		return null;
	}

	public String getRegistrationReferer() {
		if (CollectionUtils.isNotEmpty(registration.getReferers())) {
			return registration.getReferers().iterator().next().getName();
		}
		return null;
	}

	public String getRegistrationProductQuestion() {
		if (CollectionUtils.isNotEmpty(registration.getProductQuestions())) {
			return registration.getProductQuestions().iterator().next().getQuestion();
		}
		return null;
	}

	public String getRegistrationSocialContext() {
		if (CollectionUtils.isNotEmpty(registration.getSocialContexts())) {
			return registration.getSocialContexts().iterator().next().getName();
		}
		return null;
	}

	public boolean isCreator() {
		return registration.getUser() == null || registration.getUser().equals(authenticationManager.getCurrentUser());
	}
}