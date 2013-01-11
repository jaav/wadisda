package be.virtualsushi.wadisda.components;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

import be.virtualsushi.wadisda.entities.User;

/**
 * Layout component for pages of application wadisda.
 */
@Import(stylesheet = { "classpath:/META-INF/resources/webjars/bootstrap/2.2.2/css/bootstrap.css", "classpath:/META-INF/resources/webjars/bootstrap/2.2.2/css/bootstrap-responsive.css" },
		library = { "classpath:/META-INF/resources/webjars/bootstrap/2.2.2/js/bootstrap.js" })
public class Layout {
	/**
	 * The page title, for the <title> element and the <h1>element.
	 */
	@Property
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String title;

	@Inject
	private ComponentResources resources;

	@Property
	@Inject
	@Symbol(SymbolConstants.APPLICATION_VERSION)
	private String appVersion;

	public User getCurrentUser() {
		return (User) SecurityUtils.getSubject().getPrincipal();
	}

	public String getMenuItemClass(String itemName) {
		return resources.getPageName().equalsIgnoreCase(itemName) ? "active" : "";
	}

	public String getMenuCategoryClass(List<String> categoryItems) {
		for (String item : categoryItems) {
			if (resources.getPageName().equalsIgnoreCase(item)) {
				return "in";
			}
		}
		return "";
	}

}
