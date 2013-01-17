package be.virtualsushi.wadisda.entities.valueobjects;

import java.lang.annotation.Annotation;

import org.apache.tapestry5.PropertyConduit;

import be.virtualsushi.wadisda.entities.Registration;

public class RegistrationTimePropertyConduit implements PropertyConduit {

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return null;
	}

	@Override
	public Object get(Object instance) {
		TimeValue time = ((Registration) instance).getTime();
		return time.getHours() + ":" + time.getMinutes();
	}

	@Override
	public void set(Object instance, Object value) {
	}

	@Override
	public Class<String> getPropertyType() {
		return String.class;
	}

}
