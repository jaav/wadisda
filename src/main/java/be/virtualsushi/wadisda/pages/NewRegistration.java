package be.virtualsushi.wadisda.pages;

import javax.inject.Inject;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.services.SelectModelFactory;

import be.virtualsushi.wadisda.entities.Product;
import be.virtualsushi.wadisda.entities.ProductQuestion;
import be.virtualsushi.wadisda.entities.Referer;
import be.virtualsushi.wadisda.entities.Registration;
import be.virtualsushi.wadisda.entities.Relation;
import be.virtualsushi.wadisda.entities.SocialContext;
import be.virtualsushi.wadisda.services.repository.ListJpaRepository;
import be.virtualsushi.wadisda.valueobjects.TimeValue;

public class NewRegistration {

	@Inject
	private ListJpaRepository listJpaRepository;

	@InjectService("customSelectModelFactory")
	private SelectModelFactory selectModelFactory;

	@Property
	private Relation registrationRelation;

	@Property
	private Product registrationProduct;

	@Property
	private Referer registrationReferer;

	@Property
	private SocialContext registrationSocialContext;

	@Property
	private ProductQuestion registrationProductQuestion;

	@Property
	private String registrationZipCode;

	@Property
	private String registrationLocation;

	public TimeValue getEpochTime() {
		if (getRegistration().getEpoch() != null) {
			return TimeValue.of(getRegistration().getEpoch());
		} else {
			return null;
		}
	}

	public Registration getRegistration() {
		Registration registration = new Registration();
		return registration;
	}

	public SelectModel getSelectModel(String className) throws ClassNotFoundException {
		return selectModelFactory.create(listJpaRepository.getValuesList(Class.forName("be.virtualsushi.wadisda.entities." + className)), "name");
	}

	public SelectModel getSelectModel(String className, String propertyName) throws ClassNotFoundException {
		return selectModelFactory.create(listJpaRepository.getValuesList(Class.forName("be.virtualsushi.wadisda.entities." + className)), propertyName);
	}

}