package be.virtualsushi.wadisda.pages;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.SelectModelFactory;

import be.virtualsushi.wadisda.entities.Location;
import be.virtualsushi.wadisda.entities.Product;
import be.virtualsushi.wadisda.entities.ProductQuestion;
import be.virtualsushi.wadisda.entities.Referer;
import be.virtualsushi.wadisda.entities.Registration;
import be.virtualsushi.wadisda.entities.Relation;
import be.virtualsushi.wadisda.entities.SocialContext;
import be.virtualsushi.wadisda.services.repository.ListJpaRepository;
import be.virtualsushi.wadisda.services.repository.RegistrationRepository;
import be.virtualsushi.wadisda.valueobjects.TimeValue;

public class EditRegistration {

	@Inject
	private ListJpaRepository listJpaRepository;

	@Inject
	private SelectModelFactory selectModelFactory;

	@Inject
	private RegistrationRepository registrationRepository;

	@Property
	private String registrationZipCode;

	@Property
	private String registrationLocation;

	@Property
	private Registration registration;

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

	public String onPassivate() {
		return registration.isNew() ? "new" : String.valueOf(registration.getId());
	}

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
		registrationRepository.save(registration);
		return OverviewRegistrations.class;
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