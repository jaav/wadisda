package be.virtualsushi.wadisda.services.impl;

import org.apache.tapestry5.BaseValidationDecorator;
import org.apache.tapestry5.CSSClassConstants;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.services.Environment;

public class CustomValidationDecorator extends BaseValidationDecorator {

	private final Environment environment;

	private final MarkupWriter markupWriter;

	public CustomValidationDecorator(Environment environment, MarkupWriter markupWriter) {
		this.environment = environment;
		this.markupWriter = markupWriter;
	}

	@Override
	public void insideField(Field field) {
		if (inError(field))
			addErrorClassToCurrentElement();
	}

	@Override
	public void insideLabel(Field field, Element element) {
		if (field == null)
			return;

		if (inError(field))
			element.addClassName(CSSClassConstants.ERROR);
	}

	@Override
	public void afterField(Field field) {

	}

	private boolean inError(Field field) {
		ValidationTracker tracker = environment.peekRequired(ValidationTracker.class);

		return tracker.inError(field);
	}

	private void addErrorClassToCurrentElement() {
		markupWriter.getElement().addClassName(CSSClassConstants.ERROR);
	}

}
