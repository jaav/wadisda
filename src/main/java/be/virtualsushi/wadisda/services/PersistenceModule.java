package be.virtualsushi.wadisda.services;

import java.lang.reflect.Field;
import java.util.Map;

import javax.naming.NamingException;
import javax.persistence.ValidationMode;
import javax.sql.DataSource;

import org.apache.tapestry5.internal.jpa.PersistenceUnitInfoImpl;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.jpa.EntityManagerSource;
import org.apache.tapestry5.jpa.JpaTransactionAdvisor;
import org.apache.tapestry5.jpa.PersistenceUnitConfigurer;
import org.apache.tapestry5.jpa.TapestryPersistenceUnitInfo;

import be.virtualsushi.wadisda.services.repository.ListJpaRepository;
import be.virtualsushi.wadisda.services.repository.ProductRepository;
import be.virtualsushi.wadisda.services.repository.RegistrationRepository;
import be.virtualsushi.wadisda.services.repository.SimpleJpaRepository;
import be.virtualsushi.wadisda.services.repository.TaskRepository;
import be.virtualsushi.wadisda.services.repository.UserRepository;
import be.virtualsushi.wadisda.services.repository.impl.ListJpaRepositoryImpl;
import be.virtualsushi.wadisda.services.repository.impl.ProductRepositoryImpl;
import be.virtualsushi.wadisda.services.repository.impl.RegistrationRepositoryImpl;
import be.virtualsushi.wadisda.services.repository.impl.SimpleJpaRepositoryImpl;
import be.virtualsushi.wadisda.services.repository.impl.TaskRepositoryImpl;
import be.virtualsushi.wadisda.services.repository.impl.UserRepositoryImpl;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class PersistenceModule {

	public static void bind(ServiceBinder binder) {
		binder.bind(ListJpaRepository.class, ListJpaRepositoryImpl.class);
		binder.bind(UserRepository.class, UserRepositoryImpl.class);
		binder.bind(RegistrationRepository.class, RegistrationRepositoryImpl.class);
		binder.bind(TaskRepository.class, TaskRepositoryImpl.class);
		binder.bind(ProductRepository.class, ProductRepositoryImpl.class);
		binder.bind(SimpleJpaRepository.class, SimpleJpaRepositoryImpl.class);
	}

	public DataSource buildDataSource(final Map<String, String> config, @Symbol("jdbc.url") final String jdbcUrl, @Symbol("jdbc.user") final String user, @Symbol("jdbc.password") final String password) throws NamingException {

		final MysqlDataSource dataSource = new MysqlDataSource();

		dataSource.setUrl(jdbcUrl);
		dataSource.setUser(user);
		dataSource.setPassword(password);

		return dataSource;
	}

	@Contribute(EntityManagerSource.class)
	public static void configurePersistenceUnitInfos(MappedConfiguration<String, PersistenceUnitConfigurer> cfg, final @InjectService("DataSource") DataSource dataSource) {

		PersistenceUnitConfigurer configurer = new PersistenceUnitConfigurer() {

			public void configure(TapestryPersistenceUnitInfo unitInfo) {

				try {
					Field dataSourceField = PersistenceUnitInfoImpl.class.getDeclaredField("nonJtaDataSource");
					dataSourceField.setAccessible(true);
					dataSourceField.set(unitInfo, dataSource);
				} catch (Exception e) {
					throw new RuntimeException("Unable to set data source", e);
				}

				unitInfo.addProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
				unitInfo.addProperty("hibernate.hbm2ddl.auto", "update");
				unitInfo.validationMode(ValidationMode.AUTO);
			}
		};

		cfg.add("wadisda-unit", configurer);
	}

	@Match("*Repository")
	public static void adviseTransactionally(JpaTransactionAdvisor advisor, MethodAdviceReceiver receiver) {
		advisor.addTransactionCommitAdvice(receiver);
	}

}
