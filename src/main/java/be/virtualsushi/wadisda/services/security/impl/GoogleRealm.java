package be.virtualsushi.wadisda.services.security.impl;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.slf4j.Logger;
import org.tynamo.security.federatedaccounts.FederatedAccount.FederatedAccountType;
import org.tynamo.security.federatedaccounts.services.FederatedAccountService;

import be.virtualsushi.wadisda.services.security.GoogleAuthenticationToken;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

public class GoogleRealm extends AuthenticatingRealm {

	@Inject
	private AuthorizationCodeFlow authorizationCodeFlow;

	@Inject
	private HttpTransport httpTransport;

	@Inject
	private JsonFactory jsonFactory;

	@Inject
	private FederatedAccountService federatedAccountService;

	private Logger logger;

	public GoogleRealm(Logger logger) {
		super();
		this.logger = logger;
		setAuthenticationTokenClass(GoogleAuthenticationToken.class);
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		Credential credential = (Credential) token.getCredentials();
		Oauth2 oauth2 = new Oauth2(httpTransport, jsonFactory, credential);
		try {
			Userinfo userInfo = oauth2.userinfo().get().execute();
			return federatedAccountService.federate(FederatedAccountType.google.name(), token.getPrincipal(), token, userInfo);
		} catch (IOException e) {
			logger.error("Unable to get user info.", e);
		}
		return null;
	}
}
