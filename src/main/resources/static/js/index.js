$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

	let title = $('#recipient-name').val();
	let content = $('#message-text').val();
	$.post(
		context_path + "/discuss/add",
		{
			title: title,
			content: content
		},
		function(data) {
			data = $.parseJSON(data);
			$("#hintBody").text(data.msg);

			$("#hintModal").modal("show");
			setTimeout(function(){
				$("#hintModal").modal("hide");
				if (data.code == 0) {
					window.location.reload();
				}
			}, 2000);
		}
	);


}