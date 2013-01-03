package be.virtualsushi.wadisda.services;

import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.ValidationMode;
import javax.sql.DataSource;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.jpa.EntityManagerSource;
import org.apache.tapestry5.jpa.JpaTransactionAdvisor;
import org.apache.tapestry5.jpa.PersistenceUnitConfigurer;
import org.apache.tapestry5.jpa.TapestryPersistenceUnitInfo;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class PersistenceModule {

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

	@Match("*Repository")
	public static void adviseTransactionally(JpaTransactionAdvisor advisor, MethodAdviceReceiver receiver) {
		advisor.addTransactionCommitAdvice(receiver);
	}

}
