package be.virtualsushi.wadisda.services.security.impl;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.annotations.PostInjection;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;

import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.services.security.AuthenticationManager;
import be.virtualsushi.wadisda.services.security.GoogleAccount;
import be.virtualsushi.wadisda.services.security.GoogleAuthenticationToken;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;

public class GoogleAuthenticationManagerImpl implements AuthenticationManager {

	@Inject
	private AuthorizationCodeFlow authorizationCodeFlow;

	@Inject
	private Response response;

	@Inject
	private LinkSource linkSource;

	private Logger logger;

	private String redirectUrl;

	public GoogleAuthenticationManagerImpl(Logger logger) {
		this.logger = logger;
	}

	@PostInjection
	public void init() {
		redirectUrl = linkSource.createPageRenderLink("oauth2callback", false).toAbsoluteURI();
	}

	@Override
	public void requestAuthorization() {
		try {
			response.sendRedirect(authorizationCodeFlow.newAuthorizationUrl().setRedirectUri(redirectUrl).build());

		} catch (IOException e) {
			logger.error("Unable to request google authorization.", e);
		}
	}

	@Override
	public void authorize(String userId) {
		try {
			TokenResponse token = authorizationCodeFlow.newTokenRequest(userId).setRedirectUri(redirectUrl).setGrantType("authorization_code").execute();
			Credential credential = authorizationCodeFlow.createAndStoreCredential(token, userId);
			SecurityUtils.getSubject().login(new GoogleAuthenticationToken(userId, credential));
		} catch (IOException e) {
			logger.error("Unable to get google API access token.", e);
		}
	}

	@Override
	public Credential getCurrentUserCredential() {
		try {
			return authorizationCodeFlow.loadCredential(((GoogleAccount) SecurityUtils.getSubject().getPrincipal()).getGoogleUserId());
		} catch (IOException e) {
			logger.error("Error getting access token.", e);
			return null;
		}
	}

	@Override
	public User getCurrentUser() {
		return ((GoogleAccount) SecurityUtils.getSubject().getPrincipal()).getUser();
	}

	@Override
	public void logout() {
		SecurityUtils.getSubject().logout();
	}

	@Override
	public void updateCurrentUserInfo(User user) {
		((GoogleAccount) SecurityUtils.getSubject().getPrincipal()).updateUser(user);
	}
}
