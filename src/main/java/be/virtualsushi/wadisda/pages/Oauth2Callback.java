package be.virtualsushi.wadisda.pages;

import javax.inject.Inject;

import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.Request;

import be.virtualsushi.wadisda.services.security.AuthenticationManager;

public class Oauth2Callback {

	@Inject
	private AuthenticationManager googleAuthenticationManager;

	@Inject
	private Request request;

	@Inject
	private AlertManager alertManager;

	@Inject
	private Messages messages;

	public Object onActivate() {
		String code = request.getParameter("code");
		if (code == null) {
			alertManager.error(messages.get("no.oauth.code"));
			return null;
		}
		googleAuthenticationManager.authorize(code);
		return Index.class;
	}
}