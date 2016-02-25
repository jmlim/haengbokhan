if (!window.haengbokhan) {
	window.haengbokhan = {};
}

haengbokhan.imageUploader = function(groupId) {
	$("#fileupload").fileupload(
			{
				url : '/haengbokhan/image-article/upload?groupId='+groupId,
				dataType : 'json',
				type : 'POST',
				maxFileSize : 5000000,
				maxNumberOfFiles : 20,
				acceptFileTypes : /(\.|\/)(g@RequestParam(value = "groupId") String groupId@RequestParam(value = "groupId") String groupIdif|jpe?g|png)$/i,
				progressall: function(e, data) {
					var progress = parseInt(data.loaded / data.total
							* 100, 10);
					$(".progress .progress-bar").css("width",
							progress + "%").text(progress + "%");
				},
				done : function(e, data) {
					//alert("업로드 완료");
				},
				stop : function(e) {
					var that = $(this).data('blueimp-fileupload')
							|| $(this).data('fileupload'), deferred = that
							._addFinishedDeferreds();
					$.when.apply($, that._getFinishedDeferreds()).done(
							function() {
								that._trigger('stopped', e);
							});
					that._transition($(this).find('.fileupload-progress'))
							.done(function() {
								deferred.resolve();
							});
				},
				destroy : function(e, data) {
					var that = $(this).data('blueimp-fileupload')
							|| $(this).data('fileupload');
					if (data.url) {
						$.ajax(data);
					}
					that._adjustMaxNumberOfFiles(1);
					that._transition($(this).find('.fileupload-progress'))
							.done(
									function() {
										$(this).find('.progress').attr(
												'aria-valuenow', '0').find(
												'.bar').css('width', '0%');
										$(this).find('.progress-extended')
												.html('&nbsp;');
									});
					that._transition(data.context).done(function() {
						$(this).remove();
						that._trigger('destroyed', e, data);
					});
				}
			});
};
