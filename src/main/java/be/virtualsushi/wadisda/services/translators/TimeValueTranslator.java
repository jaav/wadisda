package be.virtualsushi.wadisda.services.translators;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.internal.translator.AbstractTranslator;
import org.apache.tapestry5.services.FormSupport;

import be.virtualsushi.wadisda.entities.valueobjects.TimeValue;

public class TimeValueTranslator extends AbstractTranslator<TimeValue> {

	private final static String VALUE_PATTERN = "\\d\\d:\\d\\d";

	public TimeValueTranslator() {
		super("timevalue", TimeValue.class, "");
	}

	@Override
	public String toClient(TimeValue value) {
		if (value != null) {
			return (value.getHours() < 10 ? "0" + value.getHours() : value.getHours()) + ":" + (value.getMinutes() < 10 ? "0" + value.getMinutes() : value.getMinutes());
		}
		return null;
	}

	@Override
	public TimeValue parseClient(Field field, String clientValue, String message) throws ValidationException {
		if (clientValue == null) {
			return null;
		}
		if (!clientValue.matches(VALUE_PATTERN)) {
			throw new ValidationException("Value must match hh:mm pattern.");
		}
		String[] parts = clientValue.split(":");
		return new TimeValue(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
	}

	@Override
	public void render(Field field, String message, MarkupWriter writer, FormSupport formSupport) {

	}

}
