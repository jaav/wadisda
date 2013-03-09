package be.virtualsushi.wadisda.components;

import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.corelib.components.Checkbox;
import org.got5.tapestry5.jquery.mixins.ZoneUpdater;

public class AjaxCheckBox extends Checkbox {

	@Mixin
	private ZoneUpdater zoneUpdater;

}
