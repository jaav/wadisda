package be.virtualsushi.wadisda.services.messages;

import be.virtualsushi.wadisda.entities.Registration;
import be.virtualsushi.wadisda.entities.valueobjects.Message;

public interface MessageEndpoint {

	void send(Message message, Registration registration) throws Exception;

}
