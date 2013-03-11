package be.virtualsushi.wadisda.services.messages;

import be.virtualsushi.wadisda.entities.Registration;
import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.entities.enums.MessageTypes;
import be.virtualsushi.wadisda.entities.valueobjects.Message;

public interface MessageService {

	void sendMessage(MessageTypes type, Message message, User creator, Registration registration);

}
