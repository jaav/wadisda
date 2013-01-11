package be.virtualsushi.wadisda.components;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.FieldValidationSupport;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(stack = "core-datefield")
@Events(EventConstants.VALIDATE)
public class BootstrapDateField extends AbstractField {

	/**
	 * The value parameter of a DateField must be a {@link java.util.Date}.
	 */
	@Parameter(required = true, principal = true, autoconnect = true)
	private Date value;

	/**
	 * The format used to format <em>and parse</em> dates. This is typically
	 * specified as a string which is coerced to a DateFormat. You should be
	 * aware that using a date format with a two digit year is problematic: Java
	 * (not Tapestry) may get confused about the century.
	 */
	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	private DateFormat format;

	/**
	 * If true, then the text field will be hidden, and only the icon for the
	 * date picker will be visible. The default is false.
	 */
	@Parameter
	private boolean hideTextField;

	/**
	 * The object that will perform input validation (which occurs after
	 * translation). The translate binding prefix is generally used to provide
	 * this object in a declarative fashion.
	 */
	@Parameter(defaultPrefix = BindingConstants.VALIDATE)
	private FieldValidator<Object> validate;

	/**
	 * Used to override the component's message catalog.
	 * 
	 * @since 5.2.0.0
	 */
	@Parameter("componentResources.messages")
	private Messages messages;

	@Environmental
	private JavaScriptSupport support;

	@Environmental
	private ValidationTracker tracker;

	@Inject
	private ComponentResources resources;

	@Inject
	private Request request;

	@Inject
	private Locale locale;

	@Inject
	private ComponentDefaultProvider defaultProvider;

	@Inject
	private FieldValidationSupport fieldValidationSupport;

	private static final String RESULT = "result";

	private static final String ERROR = "error";
	private static final String INPUT_PARAMETER = "input";

	DateFormat defaultFormat() {
		DateFormat shortDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);

		if (shortDateFormat instanceof SimpleDateFormat) {
			SimpleDateFormat simpleDateFormat = (SimpleDateFormat) shortDateFormat;

			String pattern = simpleDateFormat.toPattern();

			String revised = pattern.replaceAll("([^y])yy$", "$1yyyy");

			return new SimpleDateFormat(revised);
		}

		return shortDateFormat;
	}

	/**
	 * Computes a default value for the "validate" parameter using
	 * {@link ComponentDefaultProvider}.
	 */
	final Binding defaultValidate() {
		return defaultProvider.defaultValidatorBinding("value", resources);
	}

	/**
	 * Ajax event handler, used when initiating the popup. The client sends the
	 * input value form the field to the server to parse it according to the
	 * server-side format. The response contains a "result" key of the formatted
	 * date in a format acceptable to the JavaScript Date() constructor.
	 * Alternately, an "error" key indicates the the input was not formatted
	 * correct.
	 */
	JSONObject onParse(@RequestParameter(INPUT_PARAMETER) String input) {
		JSONObject response = new JSONObject();

		try {
			Date date = format.parse(input);

			response.put(RESULT, date.getTime());
		} catch (ParseException ex) {
			response.put(ERROR, ex.getMessage());
		}

		return response;
	}

	/**
	 * Ajax event handler, used after the client-side popup completes. The
	 * client sends the date, formatted as milliseconds since the epoch, to the
	 * server, which reformats it according to the server side format and
	 * returns the result.
	 */
	JSONObject onFormat(@RequestParameter(INPUT_PARAMETER) String input) {
		JSONObject response = new JSONObject();

		try {
			long millis = Long.parseLong(input);

			Date date = new Date(millis);

			response.put(RESULT, format.format(date));
		} catch (NumberFormatException ex) {
			response.put(ERROR, ex.getMessage());
		}

		return response;
	}

	void beginRender(MarkupWriter writer) {
		String value = tracker.getInput(this);

		if (value == null)
			value = formatCurrentValue();

		String clientId = getClientId();
		String triggerId = clientId + "-trigger";

		writer.element("div", "class", "input-append");

		writer.element("input",

		"type", hideTextField ? "hidden" : "text",

		"name", getControlName(),

		"id", clientId,

		"value", value);

		writeDisabled(writer);

		putPropertyNameIntoBeanValidationContext("value");

		validate.render(writer);

		removePropertyNameFromBeanValidationContext();

		resources.renderInformalParameters(writer);

		decorateInsideField();

		writer.end();

		writer.end();

		JSONObject spec = new JSONObject();

		spec.put("field", clientId);
		spec.put("parseURL", resources.createEventLink("parse").toURI());
		spec.put("formatURL", resources.createEventLink("format").toURI());
		support.addInitializerCall("dateField", spec);
	}

	private void writeDisabled(MarkupWriter writer) {
		if (isDisabled())
			writer.attributes("disabled", "disabled");
	}

	private String formatCurrentValue() {
		if (value == null)
			return "";

		return format.format(value);
	}

	@Override
	protected void processSubmission(String controlName) {
		String value = request.getParameter(controlName);

		tracker.recordInput(this, value);

		Date parsedValue = null;

		try {
			if (InternalUtils.isNonBlank(value))
				parsedValue = format.parse(value);
		} catch (ParseException ex) {
			tracker.recordError(this, messages.format("date-value-not-parseable", value));
			return;
		}

		putPropertyNameIntoBeanValidationContext("value");
		try {
			fieldValidationSupport.validate(parsedValue, resources, validate);

			this.value = parsedValue;
		} catch (ValidationException ex) {
			tracker.recordError(this, ex.getMessage());
		}

		removePropertyNameFromBeanValidationContext();
	}

	void injectResources(ComponentResources resources) {
		this.resources = resources;
	}

	@Override
	public boolean isRequired() {
		return validate.isRequired();
	}

}
