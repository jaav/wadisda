package be.virtualsushi.wadisda.pages.registrations;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.BeanModelSource;

import be.virtualsushi.wadisda.entities.Registration;
import be.virtualsushi.wadisda.entities.valueobjects.RegistrationTimePropertyConduit;
import be.virtualsushi.wadisda.services.repository.RegistrationRepository;

public class Overview extends RegistrationsPage {

	@Inject
	private RegistrationRepository registrationRepository;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private Messages messages;

	@Property
	private Registration registration;

	public List<Registration> getRegistrations() {
		return registrationRepository.findAll();
	}

	public BeanModel<Registration> getRegistrationModel() {
		BeanModel<Registration> model = beanModelSource.createDisplayModel(Registration.class, messages);
		model.add("time", new RegistrationTimePropertyConduit()).sortable(false);
		model.get("id").label("#").sortable(false);
		model.get("epoch").sortable(false).label(messages.get("date"));
		model.get("question").sortable(false);
		return model;
	}
}