package be.virtualsushi.wadisda.services;

import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Marker;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.tynamo.security.Security;
import org.tynamo.security.SecuritySymbols;
import org.tynamo.security.federatedaccounts.services.FederatedAccountService;
import org.tynamo.security.rollingtokens.RollingTokenSymbols;
import org.tynamo.security.services.SecurityFilterChainFactory;
import org.tynamo.security.services.impl.SecurityFilterChain;

import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.services.security.AuthenticationManager;
import be.virtualsushi.wadisda.services.security.GoogleAccount;
import be.virtualsushi.wadisda.services.security.impl.GoogleAuthenticationManagerImpl;
import be.virtualsushi.wadisda.services.security.impl.GoogleFederatedAccountServiceImpl;
import be.virtualsushi.wadisda.services.security.impl.GoogleRealm;
import be.virtualsushi.wadisda.services.security.impl.RepositoryCredentialStoreImpl;
import be.virtualsushi.wadisda.services.security.impl.UserActiveRequestFilter;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.oauth2.Oauth2Scopes;
import com.google.api.services.tasks.TasksScopes;
import com.google.common.collect.ImmutableList;

public class SecurityModule {

	public static void bind(ServiceBinder binder) {
		binder.bind(FederatedAccountService.class, GoogleFederatedAccountServiceImpl.class);
		binder.bind(AuthenticationManager.class, GoogleAuthenticationManagerImpl.class);
		binder.bind(AuthenticatingRealm.class, GoogleRealm.class).withId("GoogleRealm");
		binder.bind(CredentialStore.class, RepositoryCredentialStoreImpl.class).withId("CredentialStore");
	}

	@ApplicationDefaults
	public static void contributeApplicationDefaults(MappedConfiguration<String, Object> configuration) {
		configuration.add(SecuritySymbols.LOGIN_URL, "/login");
		configuration.add(RollingTokenSymbols.CONFIGURED_PRINCIPALTYPE, GoogleAccount.class.getName());
	}

	@Contribute(HttpServletRequestFilter.class)
	@Marker(Security.class)
	public static void setupSecurity(Configuration<SecurityFilterChain> configuration, SecurityFilterChainFactory factory) {
		configuration.add(factory.createChain("/assets/**").add(factory.anon()).build());
		configuration.add(factory.createChain("/login*/**").add(factory.anon()).build());
		configuration.add(factory.createChain("/oauth2callback").add(factory.anon()).build());
		configuration.add(factory.createChain("/**").add(factory.authc()).build());
	}

	@Contribute(WebSecurityManager.class)
	public static void contributeWebSecurityManager(Configuration<Realm> configuration, @InjectService("GoogleRealm") AuthenticatingRealm googleRealm, @InjectService("RollingTokenRealm") AuthenticatingRealm rollingTokenRealm) {
		configuration.add(googleRealm);
		configuration.add(rollingTokenRealm);
	}

	public AuthorizationCodeFlow buildAuthorizationCodeFlow(HttpTransport httpTransport, JsonFactory jsonFactory, @InjectService("CredentialStore") CredentialStore credentialStore, @Symbol("google.client.id") String clientId,
			@Symbol("google.client.secret") String clientSecret) {
		return new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientId, clientSecret, ImmutableList.of(Oauth2Scopes.USERINFO_EMAIL, Oauth2Scopes.USERINFO_PROFILE, CalendarScopes.CALENDAR, TasksScopes.TASKS))
				.setCredentialStore(credentialStore).build();
	}

	@Contribute(FederatedAccountService.class)
	public static void contributeFederatedAccountService(MappedConfiguration<String, Object> configuration) {
		configuration.add("rollingtokens", User.class);
		configuration.add("rollingtokens" + FederatedAccountService.IDPROPERTY, "id");
	}

	@Contribute(ComponentRequestHandler.class)
	public void contributeComponentRequestHandler(OrderedConfiguration<ComponentRequestFilter> configuration) {
		configuration.addInstance("PageProtectionFilter", UserActiveRequestFilter.class);
	}

}
