package be.virtualsushi.wadisda.services.security.impl;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Response;

import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.pages.Login;
import be.virtualsushi.wadisda.pages.NotActivated;
import be.virtualsushi.wadisda.pages.Oauth2Callback;
import be.virtualsushi.wadisda.services.repository.UserRepository;
import be.virtualsushi.wadisda.services.security.AuthenticationManager;

public class UserActiveRequestFilter implements ComponentRequestFilter {

	private static final String[] NOT_CLOSED_PAGES = new String[] { Login.class.getSimpleName(), NotActivated.class.getSimpleName(), Oauth2Callback.class.getSimpleName() };

	@Inject
	private AuthenticationManager authenticationManager;

	@Inject
	private UserRepository userRepository;

	@Inject
	private Response response;

	@Inject
	private LinkSource linkSource;

	@Override
	public void handleComponentEvent(ComponentEventRequestParameters parameters, ComponentRequestHandler handler) throws IOException {
		if (!notClosedPage(parameters.getActivePageName()) && !isCurrentUserActive()) {
			response.sendRedirect(linkSource.createPageRenderLink("notactivated", false));
		} else {
			handler.handleComponentEvent(parameters);
		}
	}

	@Override
	public void handlePageRender(PageRenderRequestParameters parameters, ComponentRequestHandler handler) throws IOException {
		if (!notClosedPage(parameters.getLogicalPageName()) && !isCurrentUserActive()) {
			response.sendRedirect(linkSource.createPageRenderLink("notactivated", false));
		} else {
			handler.handlePageRender(parameters);
		}
	}

	private boolean isCurrentUserActive() {
		User currentUser = authenticationManager.getCurrentUser();
		if (!currentUser.isActive()) {
			currentUser = userRepository.findOne(currentUser.getId());
			if (currentUser.isActive()) {
				authenticationManager.updateCurrentUserInfo(currentUser);
			}
			return currentUser.isActive();
		}
		return true;
	}

	private boolean notClosedPage(String pageName) {
		for (String notClosedPage : NOT_CLOSED_PAGES) {
			if (notClosedPage.equalsIgnoreCase(pageName)) {
				return true;
			}
		}
		return false;
	}
}
