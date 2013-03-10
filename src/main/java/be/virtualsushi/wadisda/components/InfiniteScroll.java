package be.virtualsushi.wadisda.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(library = { "jquery.scrollExtend.min.js", "infinite-scroll.js" })
public class InfiniteScroll implements ClientElement {

	@Component(publishParameters = "encoder, formState, element, index, empty")
	private Loop<?> loop;

	@Parameter
	@Property
	private Object row;

	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	private String assignedClientId;

	@Inject
	private JavaScriptSupport javaScriptSupport;

	@Inject
	private ComponentResources resources;

	@Inject
	private Block nextPageBlock;

	private int index;

	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	private String zone;

	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	private String scroller;

	@Parameter
	private JSONObject params;

	@Inject
	private Request request;

	@BeginRender
	void assignClientId() {
		assignedClientId = javaScriptSupport.allocateClientId(clientId);
	}

	@AfterRender
	void addJavaScript() {
		JSONObject specs = new JSONObject().put("scroller", scroller).put("scrollURI", getScrollURI()).put("zoneId", zone).put("params", params);

		javaScriptSupport.addInitializerCall("infiniteScroll", specs);
	}

	@OnEvent("scroll")
	Object scroll(int index) {
		this.index = index;
		return nextPageBlock;
	}

	public List<?> getNextPage() {
		CaptureResultCallback<List<Object>> resultCallback = new CaptureResultCallback<List<Object>>();
		resources.triggerEvent("nextPage", new Object[] { index }, resultCallback);

		List<?> result = resultCallback.getResult();
		result = (result == null ? new ArrayList<Object>() : result);

		return result;
	}

	@Override
	public String getClientId() {
		return assignedClientId;
	}

	public String getScrollURI() {
		return resources.createEventLink("scroll", "pageScrollIndex").toAbsoluteURI();
	}

}