package be.virtualsushi.wadisda.services.encoders;

import javax.inject.Inject;

import org.apache.tapestry5.ValueEncoder;

import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.services.repository.UserRepository;

public class UserEncoder implements ValueEncoder<User> {

	@Inject
	private UserRepository userRepository;

	@Override
	public String toClient(User value) {
		return String.valueOf(value.getId());
	}

	@Override
	public User toValue(String clientValue) {
		return userRepository.findOne(Long.parseLong(clientValue));
	}

}
