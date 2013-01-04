package be.virtualsushi.wadisda.services.security.impl;

import javax.inject.Inject;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.tynamo.security.federatedaccounts.services.FederatedAccountService;

import be.virtualsushi.wadisda.services.repository.UserRepository;

public class GoogleFederatedAccountServiceImpl implements FederatedAccountService {

	@Inject
	private UserRepository userRepository;

	@Override
	public AuthenticationInfo federate(String realmName, Object remotePrincipal, AuthenticationToken authenticationToken, Object remoteAccount) {

		System.out.println("called");
		return new SimpleAuthenticationInfo(authenticationToken.getPrincipal(), authenticationToken.getCredentials(), realmName);
	}

}
