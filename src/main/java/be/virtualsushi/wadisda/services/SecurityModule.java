package be.virtualsushi.wadisda.services;

import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Marker;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.tynamo.security.Security;
import org.tynamo.security.SecuritySymbols;
import org.tynamo.security.federatedaccounts.services.FederatedAccountService;
import org.tynamo.security.services.SecurityFilterChainFactory;
import org.tynamo.security.services.impl.SecurityFilterChain;

import be.virtualsushi.wadisda.services.security.GoogleAuthenticationManager;
import be.virtualsushi.wadisda.services.security.impl.GoogleAuthenticationManagerImpl;
import be.virtualsushi.wadisda.services.security.impl.GoogleFederatedAccountServiceImpl;
import be.virtualsushi.wadisda.services.security.impl.GoogleRealm;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.MemoryCredentialStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.common.collect.ImmutableList;

public class SecurityModule {

	public static void bind(ServiceBinder binder) {
		binder.bind(FederatedAccountService.class, GoogleFederatedAccountServiceImpl.class);
		binder.bind(GoogleAuthenticationManager.class, GoogleAuthenticationManagerImpl.class);
		binder.bind(AuthenticatingRealm.class, GoogleRealm.class).withId("GoogleRealm");
	}

	@ApplicationDefaults
	public static void contributeApplicationDefaults(MappedConfiguration<String, Object> configuration) {
		configuration.add(SecuritySymbols.LOGIN_URL, "/login");
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
	public static void contributeWebSecurityManager(Configuration<Realm> configuration, @InjectService("GoogleRealm") AuthenticatingRealm googleRealm) {
		configuration.add(googleRealm);
	}

	public AuthorizationCodeFlow buildAuthorizationCodeFlow(HttpTransport httpTransport, JsonFactory jsonFactory, @Symbol("google.client.id") String clientId, @Symbol("google.client.secret") String clientSecret) {
		return new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientId, clientSecret, ImmutableList.of("https://www.googleapis.com/auth/userinfo.profile", "https://www.googleapis.com/auth/userinfo.email"))
				.setCredentialStore(new MemoryCredentialStore()).build();
	}

	public HttpTransport buildHttpTRansport() {
		return new ApacheHttpTransport();
	}

	public JsonFactory buildJsonFactory() {
		return new JacksonFactory();
	}

}
