package be.virtualsushi.wadisda.services.security.impl;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;

import be.virtualsushi.wadisda.services.security.GoogleAccessToken;
import be.virtualsushi.wadisda.services.security.GoogleAuthenticationManager;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;

public class GoogleAuthenticationManagerImpl implements GoogleAuthenticationManager {

	@Inject
	private AuthorizationCodeFlow authorizationCodeFlow;

	@Inject
	private Response response;

	private Logger logger;

	@Inject
	private LinkSource linkSource;

	public GoogleAuthenticationManagerImpl(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void requestAuthorization() {
		try {
			response.sendRedirect(authorizationCodeFlow.newAuthorizationUrl().setRedirectUri(linkSource.createPageRenderLink("oauth2callback", false).toAbsoluteURI()).build());
		} catch (IOException e) {
			logger.error("Unable to request google authorization.", e);
		}
	}

	@Override
	public void authenticate(String userId) {
		try {
			TokenResponse token = authorizationCodeFlow.newTokenRequest(userId).execute();
			Credential credential = authorizationCodeFlow.createAndStoreCredential(token, userId);
			SecurityUtils.getSubject().login(new GoogleAccessToken(userId, credential));
		} catch (IOException e) {
			logger.error("Unable to get google API access token.", e);
		}
	}
}
