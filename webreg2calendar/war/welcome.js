$(document).ready(function(){
	$("#mainform").validate();
	
	var options = {
			target: '#status',
			beforeSubmit: submitRequest,
			success: submitResponse
	};


	$('#mainform').ajaxForm(options);
});

function submitRequest(formData, jqForm, options) {
	$('#submit_to_google').attr("disabled", "disabled");
	$('#submit_to_ical').attr("disabled", "disabled");
	$('#status').text("Working...");
	return true;
};

function submitResponse(responseText, statusText, xhr, $form) {
};

function instructions() {
	window.open("instructions.html");
}