package be.virtualsushi.wadisda.services;

import java.io.IOException;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.ValidationMode;
import javax.sql.DataSource;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.Marker;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.FactoryDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.jpa.EntityManagerSource;
import org.apache.tapestry5.jpa.JpaTransactionAdvisor;
import org.apache.tapestry5.jpa.PersistenceUnitConfigurer;
import org.apache.tapestry5.jpa.TapestryPersistenceUnitInfo;
import org.apache.tapestry5.services.ClasspathAssetAliasManager;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;
import org.tynamo.security.Security;
import org.tynamo.security.services.SecurityFilterChainFactory;
import org.tynamo.security.services.impl.SecurityFilterChain;

import be.virtualsushi.wadisda.services.impl.ClasspathPropertiesFileSymbolProvider;
import be.virtualsushi.wadisda.services.repository.ListJpaRepository;
import be.virtualsushi.wadisda.services.repository.impl.ListJpaRepositoryImpl;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * This module is automatically included as part of the Tapestry IoC Registry,
 * it's a good place to configure and extend Tapestry, or to place your own
 * service definitions.
 */
public class AppModule {

	public static void bind(ServiceBinder binder) {
		binder.bind(ListJpaRepository.class, ListJpaRepositoryImpl.class);
	}

	@FactoryDefaults
	public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
		// The application version number is incorprated into URLs for some
		// assets. Web browsers will cache assets because of the far future
		// expires
		// header. If existing assets are changed, the version number should
		// also
		// change, to force the browser to download new versions. This overrides
		// Tapesty's default
		// (a random hexadecimal number), but may be further overriden by
		// DevelopmentModule or
		// QaModule.
		configuration.override(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
	}

	@ApplicationDefaults
	public static void contributeApplicationDefaults(MappedConfiguration<String, Object> configuration) {
		// Contributions to ApplicationDefaults will override any contributions
		// to
		// FactoryDefaults (with the same key). Here we're restricting the
		// supported
		// locales to just "en" (English). As you add localised message catalogs
		// and other assets,
		// you can extend this list of locales (it's a comma separated series of
		// locale names;
		// the first locale name is the default when there's no reasonable
		// match).
		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en");
	}

	@Contribute(SymbolSource.class)
	public static void contributeSymbolSource(OrderedConfiguration<SymbolProvider> configuration, Logger logger) {
		configuration.add("ClasspathPropertiesFileSymbolProvider", new ClasspathPropertiesFileSymbolProvider(logger), "after:SystemProperties", "before:ApplicationDefaults");
	}

	@Contribute(ClasspathAssetAliasManager.class)
	public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration) {
		configuration.add("bootstrap", "META-INF/resources/webjars/bootstrap/2.2.2");
	}

	/**
	 * This is a service definition, the service will be named "TimingFilter".
	 * The interface, RequestFilter, is used within the RequestHandler service
	 * pipeline, which is built from the RequestHandler service configuration.
	 * Tapestry IoC is responsible for passing in an appropriate Logger
	 * instance. Requests for static resources are handled at a higher level, so
	 * this filter will only be invoked for Tapestry related requests.
	 * <p/>
	 * <p/>
	 * Service builder methods are useful when the implementation is inline as
	 * an inner class (as here) or require some other kind of special
	 * initialization. In most cases, use the static bind() method instead.
	 * <p/>
	 * <p/>
	 * If this method was named "build", then the service id would be taken from
	 * the service interface and would be "RequestFilter". Since Tapestry
	 * already defines a service named "RequestFilter" we use an explicit
	 * service id that we can reference inside the contribution method.
	 */
	public RequestFilter buildTimingFilter(final Logger log) {
		return new RequestFilter() {
			public boolean service(Request request, Response response, RequestHandler handler) throws IOException {
				long startTime = System.currentTimeMillis();

				try {
					// The responsibility of a filter is to invoke the
					// corresponding method
					// in the handler. When you chain multiple filters together,
					// each filter
					// received a handler that is a bridge to the next filter.

					return handler.service(request, response);
				} finally {
					long elapsed = System.currentTimeMillis() - startTime;

					log.info(String.format("Request time: %d ms", elapsed));
				}
			}
		};
	}

	/**
	 * This is a contribution to the RequestHandler service configuration. This
	 * is how we extend Tapestry using the timing filter. A common use for this
	 * kind of filter is transaction management or security. The @Local
	 * annotation selects the desired service by type, but only from the same
	 * module. Without @Local, there would be an error due to the other
	 * service(s) that implement RequestFilter (defined in other modules).
	 */
	@Contribute(RequestHandler.class)
	public static void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration, @Local RequestFilter filter) {
		// Each contribution to an ordered configuration has a name, When
		// necessary, you may
		// set constraints to precisely control the invocation order of the
		// contributed filter
		// within the pipeline.

		configuration.add("Timing", filter);
	}

	@EagerLoad
	public DataSource buildDataSource(final Map<String, String> config, @Symbol("jdbc.url") final String jdbcUrl, @Symbol("jdbc.user") final String user, @Symbol("jdbc.password") final String password) throws NamingException {

		final MysqlDataSource dataSource = new MysqlDataSource();

		dataSource.setUrl(jdbcUrl);
		dataSource.setUser(user);
		dataSource.setPassword(password);

		final Context initialContext = new InitialContext();
		try {
			Context envContext = initialContext.createSubcontext("java:comp/env");
			Context jdbcContext = envContext.createSubcontext("jdbc");
			jdbcContext.bind("wadisda_ds", dataSource);
		} catch (final NamingException ne) {
			throw new RuntimeException(ne.getExplanation(), ne);
		}

		return dataSource;
	}

	@Contribute(EntityManagerSource.class)
	public static void configurePersistenceUnitInfos(MappedConfiguration<String, PersistenceUnitConfigurer> cfg) {

		PersistenceUnitConfigurer configurer = new PersistenceUnitConfigurer() {

			public void configure(TapestryPersistenceUnitInfo unitInfo) {

				unitInfo.nonJtaDataSource("jdbc/wadisda_ds");
				unitInfo.addProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
				unitInfo.addProperty("hibernate.hbm2ddl.auto", "update");
				unitInfo.validationMode(ValidationMode.AUTO);
			}
		};

		cfg.add("wadisda-unit", configurer);
	}

	@Match("*DAO")
	public static void adviseTransactionally(JpaTransactionAdvisor advisor, MethodAdviceReceiver receiver) {
		advisor.addTransactionCommitAdvice(receiver);
	}

	@Contribute(HttpServletRequestFilter.class)
	@Marker(Security.class)
	public static void setupSecurity(Configuration<SecurityFilterChain> configuration, SecurityFilterChainFactory factory) {

		configuration.add(factory.createChain("/**").add(factory.anon()).build());
		configuration.add(factory.createChain("/secured/**").add(factory.authc()).build());

	}

}
