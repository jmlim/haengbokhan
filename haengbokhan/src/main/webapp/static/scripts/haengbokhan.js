if (!window.haengbokhan) {
	window.haengbokhan = {};
}

haengbokhan.imageUploader = function() {
	$("#fileupload").fileupload(
			{
				url : '/haengbokhan/image/upload',
				dataType : 'json',
				type : 'POST',
				maxFileSize : 5000000,
				maxNumberOfFiles : 1,
				acceptFileTypes : /(\.|\/)(gif|jpe?g|png)$/i,
				done : function(e, data) {
					alert("업로드 완료");
				},
				stop : function(e) {
					console.log(e);
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
					alert(123);
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
