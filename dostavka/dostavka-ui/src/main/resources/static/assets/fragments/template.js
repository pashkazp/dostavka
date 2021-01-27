/**
 * 
 */


function sendLocaleSettings(val1) {
	var radVal = $("input[type=radio][name=userLocale]:checked").val();
	var localeData = {};
	localeData["name"] = val1;
	localeData["value"] = radVal;
	$.ajax({
		type: "post",
		contentType: "application/json",
		url: LocaleSettingsUrl + "?lang=" + radVal,
		data: JSON.stringify(localeData),
		dataType: 'JSON',
		success: function(localeData) {
			$("#localedescripription").html(localedescripriptionmsg);
		},
		error: function() {
		}
	});
};
