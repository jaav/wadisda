(function($) {

	Tapestry.Initializer.initPage = function() {

		$('#modalZone').bind(Tapestry.ZONE_UPDATED_EVENT, function() {

			$('#dialog').modal();

		});

		$('#topZone').bind(Tapestry.ZONE_UPDATED_EVENT, function() {

			Tapestry.Initializer.initFormSelectors();
			$('#productDialog').modal('hide');
			$('#productQuestionDialog').modal('hide');
			$('#relationDialog').modal('hide');
			$('#refererDialog').modal('hide');
			$('#socialContextDialog').modal('hide');

		});

		$('#productZone').bind(Tapestry.ZONE_UPDATED_EVENT, function() {

			$('#productDialog').modal();

		});
		
		$('#productQuestionZone').bind(Tapestry.ZONE_UPDATED_EVENT, function() {
			
			$('#productQuestionDialog').modal();
			
		});
		
		$('#relationZone').bind(Tapestry.ZONE_UPDATED_EVENT, function() {

			$('#relationDialog').modal();

		});
		
		$('#refererZone').bind(Tapestry.ZONE_UPDATED_EVENT, function() {

			$('#refererDialog').modal();

		});
		
		$('#socialContextZone').bind(Tapestry.ZONE_UPDATED_EVENT, function() {

			$('#socialContextDialog').modal();

		});

	};

})(jQuery);