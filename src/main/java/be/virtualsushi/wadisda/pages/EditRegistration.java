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
import org.apache.tapestry5.annotations.Import;
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
import be.virtualsushi.wadisda.services.repository.ProductRepository;
import be.virtualsushi.wadisda.services.repository.RegistrationRepository;
import be.virtualsushi.wadisda.services.repository.SimpleJpaRepository;
import be.virtualsushi.wadisda.services.security.AuthenticationManager;
import be.virtualsushi.wadisda.services.tasks.TaskService;

@Import(library = { "edit-registration.js" })
public class EditRegistration {

	@Inject
	private ListJpaRepository listJpaRepository;

	@Inject
	private SelectModelFactory selectModelFactory;

	@Inject
	private RegistrationRepository registrationRepository;

	@Inject
	private ProductRepository productRepository;

	@Inject
	private SimpleJpaRepository simpleJpaRepository;

	@Inject
	private Request request;

	@Inject
	private TaskService taskService;

	@Inject
	private AuthenticationManager authenticationManager;

	@Environmental
	private JavaScriptSupport javaScriptSupport;

	@Property
	private String registrationZipCode;

	@Property
	private String registrationLocation;

	@Property
	private Registration registration;

	@Property
	private Product product;

	@Property
	private ProductQuestion productQuestion;

	@Property
	private Relation relation;

	@Property
	private Referer referer;

	@Property
	private SocialContext socialContext;

	@InjectComponent
	private Zone modalZone, sendEmailZone, addEventZone, addTaskZone, topZone, productZone, productQuestionZone, relationZone, refererZone, socialContextZone;

	@OnEvent(value = EventConstants.ACTIVATE)
	public void onActivate(String context) {
		product = new Product();
		productQuestion = new ProductQuestion();
		relation = new Relation();
		referer = new Referer();
		socialContext = new SocialContext();
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
		return ajaxReturnWithGracefullFallback(sendEmailZone);
	}

	@OnEvent(value = EventConstants.ACTION, component = "addEvent")
	public Object onActionFromAddEvent() {
		createTask(TaskTypes.CALENDAR_EVENT);
		return ajaxReturnWithGracefullFallback(addEventZone);
	}

	@OnEvent(value = EventConstants.ACTION, component = "addTask")
	public Object onActionFromAddTask() {
		createTask(TaskTypes.TODO_TASK);
		return ajaxReturnWithGracefullFallback(addTaskZone);
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
		return ajaxReturnWithGracefullFallback(modalZone);
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "productForm")
	public Object onSuccessFromProductForm() {
		productRepository.save(product);
		return ajaxReturnWithGracefullFallback(topZone);
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "productQuestionForm")
	public Object onSuccessFromProductQuestionForm() {
		simpleJpaRepository.save(productQuestion);
		return ajaxReturnWithGracefullFallback(topZone);
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "relationForm")
	public Object onSuccessFromRelationForm() {
		simpleJpaRepository.save(relation);
		return ajaxReturnWithGracefullFallback(topZone);
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "refererForm")
	public Object onSuccessFromRefererForm() {
		simpleJpaRepository.save(referer);
		return ajaxReturnWithGracefullFallback(topZone);
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "socialContextForm")
	public Object onSuccessFromSocialContextForm() {
		simpleJpaRepository.save(socialContext);
		return ajaxReturnWithGracefullFallback(topZone);
	}

	@OnEvent(value = EventConstants.ACTION, component = "addProduct")
	public Object onActionFromAddProduct() {
		product = new Product();
		return ajaxReturnWithGracefullFallback(productZone);
	}

	@OnEvent(value = EventConstants.ACTION, component = "addProductQuestion")
	public Object onActionFromAddProductQuestion() {
		productQuestion = new ProductQuestion();
		return ajaxReturnWithGracefullFallback(productQuestionZone);
	}

	@OnEvent(value = EventConstants.ACTION, component = "addRelation")
	public Object onActionFromAddRelation() {
		relation = new Relation();
		return ajaxReturnWithGracefullFallback(relationZone);
	}

	@OnEvent(value = EventConstants.ACTION, component = "addReferer")
	public Object onActionFromAddReferer() {
		referer = new Referer();
		return ajaxReturnWithGracefullFallback(refererZone);
	}

	@OnEvent(value = EventConstants.ACTION, component = "addSocialContext")
	public Object onActionFromAddSocialContext() {
		socialContext = new SocialContext();
		return ajaxReturnWithGracefullFallback(socialContextZone);
	}

	@AfterRender
	public void onAfterRender() {
		javaScriptSupport.addInitializerCall("initPage", "");
	}

	private Object ajaxReturnWithGracefullFallback(Object ajaxObject) {
		if (request.isXHR()) {
			return ajaxObject;
		}
		return null;
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