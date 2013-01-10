package be.virtualsushi.wadisda.services.security.impl;

import javax.inject.Inject;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.tynamo.security.federatedaccounts.services.FederatedAccountService;

import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.services.repository.UserRepository;

import com.google.api.services.oauth2.model.Userinfo;

public class GoogleFederatedAccountServiceImpl implements FederatedAccountService {

	@Inject
	private UserRepository userRepository;

	@Override
	public AuthenticationInfo federate(String realmName, Object remotePrincipal, AuthenticationToken authenticationToken, Object remoteAccount) {
		Userinfo userInfo = (Userinfo) remoteAccount;
		User user = userRepository.findByEmail(userInfo.getEmail());
		if (user == null) {
			user = new User();
			user.setEmail(userInfo.getEmail());
			user.setName(userInfo.getName());
			user.setAvatarUrl(userInfo.getPicture());
			userRepository.save(user);
		}
		return new SimpleAuthenticationInfo(user, authenticationToken.getCredentials(), realmName);
	}

}