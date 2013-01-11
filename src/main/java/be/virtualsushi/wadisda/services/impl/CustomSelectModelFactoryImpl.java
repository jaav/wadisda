package be.virtualsushi.wadisda.services.impl;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.ValueEncoderSource;

public class CustomSelectModelFactoryImpl implements SelectModelFactory {

	@Inject
	private PropertyAccess propertyAccess;

	@Inject
	private ValueEncoderSource valueEncoderSource;

	@Inject
	private Messages messages;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SelectModel create(final List<?> objects, final String labelProperty) {
		final List<OptionModel> options = CollectionFactory.newList();

		if (CollectionUtils.isNotEmpty(objects)) {
			Object object = objects.get(0);
			options.add(new OptionModelImpl(messages.get(object.getClass().getSimpleName().toLowerCase()), null) {
				@Override
				public boolean isDisabled() {

					return true;

				}
			});
		}

		for (final Object object : objects) {
			final ClassPropertyAdapter classPropertyAdapter = this.propertyAccess.getAdapter(object);

			final PropertyAdapter propertyAdapter = classPropertyAdapter.getPropertyAdapter(labelProperty);

			final ValueEncoder encoder = this.valueEncoderSource.getValueEncoder(propertyAdapter.getType());

			final Object label = propertyAdapter.get(object);

			options.add(new OptionModelImpl(encoder.toClient(label), object));

		}

		return new SelectModelImpl(null, options);
	}

}
