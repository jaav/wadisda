(function($) {

	Tapestry.Initializer.initFormSelectors = function() {
		$("select option").addClass("not-empty");
		$("select").on('change keyup', colorizeSelect).change();
	};

	Tapestry.Initializer.initTooltips = function() {
		$("[data-toggle=tooltip]").tooltip();
	};

	function colorizeSelect() {
		if ($(this).val() == "")
			$(this).addClass("empty");
		else
			$(this).removeClass("empty");
	}

	Tapestry.Initializer.initAgreement = function() {

		$('#agreementZone').bind(Tapestry.ZONE_UPDATED_EVENT, function() {
			$('#agreementDialog').modal('hide');
		});
		
		$('#agreementDialog').modal();
		
	};

})(jQuery);