package be.virtualsushi.wadisda.pages;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.BeanModelSource;

import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.services.repository.UserRepository;

public class Admin extends BaseWadisdaPage {

	@Inject
	private UserRepository userRepository;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private Messages messages;

	@Property
	private List<User> users;

	@Property
	private User user;

	@OnEvent(value = EventConstants.ACTIVATE)
	public void onActivate() {
		users = userRepository.findAll();
	}

	@OnEvent(value = "change", component = "activecheckbox")
	public void onActiveCheckBoxChanged(long userId) {
		User changedUser = userRepository.findOne(userId);
		changedUser.setActive(!changedUser.isActive());
		userRepository.save(changedUser);
	}

	public BeanModel<User> getUserModel() {
		BeanModel<User> model = beanModelSource.createDisplayModel(User.class, messages);
		model.get("name").sortable(false);
		model.get("email").sortable(false);
		model.get("active").sortable(false);
		return model;
	}

}