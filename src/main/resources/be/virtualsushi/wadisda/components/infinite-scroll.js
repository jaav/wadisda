(function($) {

	Tapestry.Initializer.infiniteScroll = function(specs) {

		var scroller = jQuery("#" + specs.scroller);
		scroller.onScrollBeyond(function() {

			if (typeof (this.pageIndex) == "undefined") {
				this.pageIndex = 0;
			}

			if (typeof (this.disable) == "undefined") {
				this.disable = false;
			}

			if (this.pageIndex == -1 || this.disable) {
				return;
			}

			var activeZone = $("#" + specs.zoneId);

			this.disable = true;
			scroller.addClass("scrollExtend-loading");
			if (activeZone.length != 0) {
				this.disable = true;
				activeZone.tapestryZone('update', {
					url : specs.scrollURI.replace("pageScrollIndex",
							this.pageIndex + 1),
					callback : function() {
						if (activeZone.is(":empty")) {
							self.pageIndex = -1;
						}

						var html = activeZone.html();
						activeZone.empty();
						activeZone.before(html);
						self.disable = false;
						scroller.removeClass("scrollExtend-loading");
					}
				});

				this.pageIndex++;
			}

		}, specs.params);
	};

})(jQuery);