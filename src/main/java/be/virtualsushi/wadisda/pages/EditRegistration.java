package be.virtualsushi.wadisda.pages;

import java.util.Date;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import be.virtualsushi.wadisda.entities.Location;
import be.virtualsushi.wadisda.entities.Product;
import be.virtualsushi.wadisda.entities.ProductQuestion;
import be.virtualsushi.wadisda.entities.Referer;
import be.virtualsushi.wadisda.entities.Registration;
import be.virtualsushi.wadisda.entities.Relation;
import be.virtualsushi.wadisda.entities.SocialContext;
import be.virtualsushi.wadisda.entities.enums.TaskTypes;
import be.virtualsushi.wadisda.entities.valueobjects.TimeValue;
import be.virtualsushi.wadisda.services.repository.ListJpaRepository;
import be.virtualsushi.wadisda.services.repository.RegistrationRepository;
import be.virtualsushi.wadisda.services.security.AuthenticationManager;
import be.virtualsushi.wadisda.services.tasks.TaskService;

public class EditRegistration {

	@Inject
	private ListJpaRepository listJpaRepository;

	@Inject
	private SelectModelFactory selectModelFactory;

	@Inject
	private RegistrationRepository registrationRepository;

	@Inject
	private Request request;

	@Inject
	private TaskService taskService;

	@Inject
	private AuthenticationManager authenticationManager;

	@Property
	private String registrationZipCode;

	@Property
	private String registrationLocation;

	@Property
	private Registration registration;

	@Environmental
	private JavaScriptSupport javaScriptSupport;

	@InjectComponent
	private Zone modalZone, sendEmailZone, addEventZone, addTaskZone;

	@OnEvent(value = EventConstants.ACTIVATE)
	public void onActivate(String context) {
		try {
			registration = registrationRepository.findOne(Long.parseLong(context));
		} catch (Exception e) {
			registration = new Registration();
		}
		if (registration.getLocation() != null) {
			registrationLocation = registration.getLocation().getName();
			registrationZipCode = registration.getLocation().getZipCode();
		}
	}

	@OnEvent(value = EventConstants.PASSIVATE)
	public String onPassivate() {
		return registration.isNew() ? "new" : String.valueOf(registration.getId());
	}

	@OnEvent(value = EventConstants.ACTION, component = "sendEmail")
	public Object onActionFromSendEmail() {
		createTask(TaskTypes.SEND_EMAIL);
		if (request.isXHR()) {
			return sendEmailZone;
		}
		return null;
	}

	@OnEvent(value = EventConstants.ACTION, component = "addEvent")
	public Object onActionFromAddEvent() {
		createTask(TaskTypes.CALENDAR_EVENT);
		if (request.isXHR()) {
			return addEventZone;
		}
		return null;
	}

	@OnEvent(value = EventConstants.ACTION, component = "addTask")
	public Object onActionFromAddTask() {
		createTask(TaskTypes.TODO_TASK);
		if (request.isXHR()) {
			return addTaskZone;
		}
		return null;
	}

	private void createTask(TaskTypes type) {
		taskService.createTask(type, registration.getUser(), registration.getUser(), registration.getQuestion(), DateUtils.addDays(new Date(), 7), registration);
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "registrationForm")
	public Object onSuccess() {

		if (registration.getLocation() == null) {
			if (StringUtils.isNotBlank(registrationLocation) || StringUtils.isNotBlank(registrationZipCode)) {
				Location location = new Location();
				location.setName(registrationLocation);
				location.setZipCode(registrationZipCode);
				registration.setLocation(location);
			}
		} else {
			registration.getLocation().setName(registrationLocation);
			registration.getLocation().setZipCode(registrationZipCode);
		}
		registration.setUser(authenticationManager.getCurrentUser());
		registrationRepository.save(registration);
		if (request.isXHR()) {
			return modalZone;
		} else {
			return OverviewRegistrations.class;
		}
	}

	@AfterRender
	public void afterRender() {
		javaScriptSupport.addScript("$('#%s').bind(Tapestry.ZONE_UPDATED_EVENT, function(){$('#dialog').modal();});", modalZone.getClientId());
	}

	public TimeValue getEpochTime() {
		if (registration.getEpoch() != null) {
			return TimeValue.of(registration.getEpoch());
		} else {
			return null;
		}
	}

	public void setEpochTime(TimeValue value) {
		if (registration.getEpoch() != null && value != null) {
			DateUtils.setHours(registration.getEpoch(), value.getHours());
			DateUtils.setMinutes(registration.getEpoch(), value.getMinutes());
		}
	}

	public SelectModel getSelectModel(String className) throws ClassNotFoundException {
		return getSelectModel(className, "name");
	}

	public SelectModel getSelectModel(String className, String propertyName) throws ClassNotFoundException {
		return selectModelFactory.create(listJpaRepository.getValuesList(Class.forName("be.virtualsushi.wadisda.entities." + className)), propertyName);
	}

	public Relation getRegistrationRelation() {
		if (CollectionUtils.isEmpty(registration.getRelations())) {
			return null;
		}
		return registration.getRelations().iterator().next();
	}

	public void setRegistrationRelation(Relation relation) {
		registration.addRelation(relation);
	}

	public Product getRegistrationProduct() {
		if (CollectionUtils.isEmpty(registration.getProducts())) {
			return null;
		}
		return registration.getProducts().iterator().next();
	}

	public void setRegistrationProduct(Product product) {
		registration.addProduct(product);
	}

	public ProductQuestion getRegistrationProductQuestion() {
		if (CollectionUtils.isEmpty(registration.getProductQuestions())) {
			return null;
		}
		return registration.getProductQuestions().iterator().next();
	}

	public void setRegistrationProductQuestion(ProductQuestion productQuestion) {
		registration.addProductgQuestion(productQuestion);
	}

	public SocialContext getRegistrationSocialContext() {
		if (CollectionUtils.isEmpty(registration.getSocialContexts())) {
			return null;
		}
		return registration.getSocialContexts().iterator().next();
	}

	public void setRegistrationSocialContext(SocialContext socialContext) {
		registration.addSocialContext(socialContext);
	}

	public Referer getRegistrationReferer() {
		if (CollectionUtils.isEmpty(registration.getReferers())) {
			return null;
		}
		return registration.getReferers().iterator().next();
	}

	public void setRegistrationReferer(Referer referer) {
		registration.addReferer(referer);
	}

}