package be.virtualsushi.wadisda;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import be.virtualsushi.wadisda.repositories.BaseEntityRepository;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.podio.APIFactory;
import com.podio.ResourceFactory;
import com.podio.app.AppAPI;
import com.podio.oauth.OAuthAppCredentials;
import com.podio.oauth.OAuthClientCredentials;

@Configuration
@ComponentScan(basePackageClasses = {})
@EnableJpaRepositories(basePackageClasses = { BaseEntityRepository.class })
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class WadisdaConfig {

	@Autowired
	private Environment environment;

	@Value("podio.app.id")
	private Integer podioAppId;

	@Bean
	public DataSource dataSource() {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUrl(environment.getProperty("jdbc.url"));
		dataSource.setUser(environment.getProperty("jdbc.user"));
		dataSource.setPassword(environment.getProperty("jdbc.password"));
		return dataSource;
	}

	/**
	 * Sets up a {@link LocalContainerEntityManagerFactoryBean} to use
	 * Hibernate. Activates picking up entities from the project's base package.
	 * 
	 * @return
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabase(Database.MYSQL);
		vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan(getClass().getPackage().getName() + ".entities");
		factory.setDataSource(dataSource());

		return factory;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {

		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return txManager;
	}

	@Bean
	public APIFactory podioApiFactory() {
		ResourceFactory resourceFactory = new ResourceFactory(new OAuthClientCredentials(environment.getProperty("podio.client.id"), environment.getProperty("podio.secret")), new OAuthAppCredentials(podioAppId,
				environment.getProperty("podio.app.token")));
		return new APIFactory(resourceFactory);
	}

	@Bean
	public AppAPI podioAppApi() {
		return podioApiFactory().getAPI(AppAPI.class);
	}

	@Bean
	public void registrationApp() {
		podioAppApi().getApp(podioAppId);
	}

}
