(function($) {

	$.ui.tapestryFieldEventManager.prototype.showValidationMessage = function(
			message) {

		var field = this.element;
		var form = field.closest('form');

		this.options.validationError = true;
		form.formEventManager("setValidationError", true);

		field.addClass("t-error");
		this.getLabel() && this.getLabel().addClass("t-error");

		var id = field.attr('id') + "\\:errorpopup";

		if ($("#" + id).size() == 0) {
			// if the errorpopup isn't on the
			// page yet, we create it

			var fieldPosition = $(field).offset();

			var bubblePosition = "absolute";
			if ($(form).parent().attr('class').indexOf('modal') == 0) {
				bubblePosition = "fixed";
			}

			field
					.after("<div id='"
							+ field.attr('id')
							+ ":errorpopup' class='label label-important' style='position:"
							+ bubblePosition + " !important;left:"
							+ fieldPosition.left + "px;" + "top:"
							+ (fieldPosition.top + $(field).height() + 11)
							+ "px;'/>");
		}
		
		Tapestry.ErrorPopup.show($("#" + id), "<span>" + message + "</span>");

	};

})(jQuery);