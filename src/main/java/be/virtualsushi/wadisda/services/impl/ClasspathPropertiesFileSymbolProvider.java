package be.virtualsushi.wadisda.services.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.slf4j.Logger;

public class ClasspathPropertiesFileSymbolProvider implements SymbolProvider {

	private final Properties properties = new Properties();

	/**
	 * Instantiate a new PropertiesFileSymbolProvider using a given resource
	 * name
	 * 
	 * @param logger
	 *            the logger to log error messages to
	 * @param resourceName
	 *            the name of the resource to load
	 * @param classPath
	 *            whether to look on the classpath or filesystem
	 */
	public ClasspathPropertiesFileSymbolProvider(Logger logger) {
		try {
			InputStream in;

			in = this.getClass().getClassLoader().getResourceAsStream("application.properties");
			if (in == null) {
				in = ClassLoader.getSystemResourceAsStream("application.properties");
			}

			// ClassLoader.getSystemResourceAsStream() returns null if
			// the resource cannot be found on the classpath
			if (in == null)
				throw new FileNotFoundException();

			initialize(logger, in);

		} catch (FileNotFoundException e) {
			String msg = "Could not find 'application.properties'";

			logger.error(msg);

			throw new IllegalArgumentException(msg, e);
		}
	}

	private void initialize(Logger logger, InputStream in) {
		try {
			properties.load(in);
		} catch (IOException e) {
			String msg = "IOEception while loading properties: " + e.getMessage();

			logger.error(msg);

			throw new IllegalArgumentException(msg, e);
		}
	}

	@Override
	public String valueForSymbol(String symbolName) {
		return (String) properties.get(symbolName);
	}

}
