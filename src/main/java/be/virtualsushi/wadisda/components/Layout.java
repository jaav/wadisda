package be.virtualsushi.wadisda.components;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import be.virtualsushi.wadisda.entities.User;
import be.virtualsushi.wadisda.entities.enums.Roles;
import be.virtualsushi.wadisda.pages.Index;
import be.virtualsushi.wadisda.services.security.AuthenticationManager;

/**
 * Layout component for pages of application wadisda.
 */
@Import(stylesheet = { "classpath:/META-INF/resources/webjars/bootstrap/2.2.2/css/bootstrap.css", "classpath:/META-INF/resources/webjars/bootstrap/2.2.2/css/bootstrap-responsive.css", "context:/layout/layout.css" }, library = {
		"classpath:/META-INF/resources/webjars/bootstrap/2.2.2/js/bootstrap.js", "custombubble-validation.js", "layout.js" })
public class Layout {
	/**
	 * The page title, for the <title> element and the <h1>element.
	 */
	@Property
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String title;

	@Inject
	private ComponentResources resources;

	@Inject
	private AuthenticationManager authenticationManager;

	@Property
	@Inject
	@Symbol(SymbolConstants.APPLICATION_VERSION)
	private String appVersion;

	@Environmental
	private JavaScriptSupport javaScriptSupport;

	@OnEvent(value = EventConstants.ACTION, component = "logout")
	public Object onActionFromLogout() {
		authenticationManager.logout();
		return Index.class;
	}

	public User getCurrentUser() {
		return authenticationManager.getCurrentUser();
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

	@AfterRender
	public void onAfterRender() {
		javaScriptSupport.addInitializerCall("initFormSelectors", "");
		javaScriptSupport.addInitializerCall("initTooltips", "");
	}

	public boolean isAdmin() {
		return getCurrentUser().hasRole(Roles.ADMIN);
	}

}
