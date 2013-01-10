package be.virtualsushi.wadisda.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class WadisdaHeader {

	@Property
	@Parameter(value = "container", defaultPrefix = BindingConstants.LITERAL)
	private String containerClass;

}