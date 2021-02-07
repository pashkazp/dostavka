/**
 * 
 */


var addNewFacilityId;

// чистка полей и чекбоксов редактора
function cleanFacilityAddrFormFields() {
	$(this)
		.find("#editFacilityAddrForm input,textarea,select")
		.not('[type=checkbox]')
		.val('')
		.end();
	$(this)
		.find("#editFacilityAddrForm input[type=checkbox], input[type=radio]")
		.prop("checked", false)
		.end();
};

// чистка сообщений об ошибках
function cleanFacilityAddrFormErrors() {
	$('#editFacilityAddrForm .alert-danger').remove();
	$('#editFacilityAddrForm .text-danger').attr("hidden", true);
};

// Очистка формы редактора
// чистка полей и чекбоксов
// чистка сообщений об ошибках
// сокрытие кнопок и служебных полей редактора
function cleanFacilityAddrForm() {
	cleanFacilityAddrFormFields();
	cleanFacilityAddrFormErrors();
	$('#editFacilityAddrForm #addNewFacilityAddrButton').hide();
	$('#editFacilityAddrForm #updateFacilityAddrButton').hide();
	$('#editFacilityAddrForm #editFacilityAddrNum').hide();
	$('#editFacilityAddrForm #addFacilityAddrNum').hide();
};
// нажатие кнопки добавления записи
// очистка формы
// включение нужных кнопок и служебных полей
// показ формы редактора
function onAddNewFacilityAddrButtonModalClick(Id) {
	console.log("press add");
	//e.preventDefault();
	cleanFacilityAddrForm();
	addNewFacilityId = Id;
	$('#editFacilityAddrForm #addNewFacilityAddrButton').show();
	$('#editFacilityAddrForm #addFacilityAddrNum').show();
	$('#editFacilityAddrModal').modal('show');
};

// SAVE updateFacilityAddrButton на форме редактора
// очиста сообщений об ошибках
// получение данных из полей редактора формы
// отправка данных в базу данных
// при успехе обновления - сокрытие формы редактора и рефреш таблицы
// при неуспехе - показ ошибок
// при сбое - показ ошибок
$(document).ready(function() {
	$('#updateFacilityAddrButton').click(function(e) {

		//Prevent default submission of form
		e.preventDefault();

		//Remove previous errors
		cleanFacilityAddrFormErrors();

		var formData = {
			//id: $('#editFacilityAddrLabelId').attr('data-value'),
			addressesAlias: $('#editFacilityAddrForm #fAE-addressesAliasEdit').val(),
			address: $('#editFacilityAddrForm #fAE-addressEdit').val(),
			lat: $('#editFacilityAddrForm #fAE-latitudeEdit').val(),
			lng: $('#editFacilityAddrForm #fAE-longitudeEdit').val(),
			defaultAddress: $('#editFacilityAddrForm #fAE-defaultAddressEdit').prop('checked'),
		}
		var addrId = $('#editFacilityAddrLabelId').attr('data-value');
		console.log(formData);
		$.ajax({
			url: facilitiesaddressurl + "/" + addrId,
			type: "PUT",
			data: JSON.stringify(formData),
			//data: JSON.stringify($('#addNewUserForm').serialize()),
			dataType: "json",
			contentType: "application/json",
			//timeout: 3000,
			async: true
		})
			.done(function(res) {
				console.log(res);
				$('#editFacilityAddrModal').modal('hide');
				//$('#facilities-table').DataTable().ajax.reload(null,false);
			})
			.fail(function(xhr, textStatus, errorThrown) {
				console.log(xhr);
				console.log(textStatus);
				console.log(xhr.responseJSON.subInfos);
				$.each(xhr.responseJSON.subInfos, function(key, value) {
					if (!value.field) { value.field = 'editForm' }
					$('#editFacilityAddrForm .text-danger[data-name=' + value.field + ']').append('<p class="alert alert-danger w-100 mb-0">' + value.message + '</p>');
				});
				$('#editFacilityAddrForm .text-danger').attr("hidden", false);
				if (textStatus == "timeout") {
					alert("Got timeout");
				}
			})
	});
});



// ADD addNewFacilityAddrButton на форме редактора
// Чистка сообщений об ошибках
// Получение значений полей формы редактора
// Отправка в базу
// при успехе добавления - скрытие формы редактора и рефреш таблицы
// при неуспехе - показ ошибок
// при сбое - показ ошибок
$(document).ready(function() {

	$('#addNewFacilityAddrButton').click(function(e) {

		//Prevent default submission of form
		e.preventDefault();

		//Remove previous errors
		cleanFacilityAddrFormErrors();
		var tableId = 'addr-table-' + addNewFacilityId;
		var
			facilityAddress = {
				addressesAlias: $('#editFacilityAddrForm #fAE-addressesAliasEdit').val(),
				address: $('#editFacilityAddrForm #fAE-addressEdit').val(),
				lat: $('#editFacilityAddrForm #fAE-latitudeEdit').val(),
				lng: $('#editFacilityAddrForm #fAE-longitudeEdit').val(),
				defaultAddress: $('#editFacilityAddrForm #fAE-defaultAddressEdit').prop('checked')

				//facilityId : addNewFacilityId
			}

		console.log(facilityAddress);
		$.post({
			url: facilitiesurl + "/" + addNewFacilityId + facilitiesaddrurl,
			data: JSON.stringify(facilityAddress),
			dataType: "json",
			contentType: "application/json",
			//timeout: 3000,
			async: true
		})
			.done(function(res) {
				console.log(res);
				$('#editFacilityAddrModal').modal('hide');
				//$('#facilities-table').DataTable().ajax.reload(null,false);
				$('#' + tableId).DataTable().ajax.reload(null, false);
			})
			.fail(function(xhr, textStatus, errorThrown) {
				console.log(xhr);
				console.log(textStatus);
				$.each(xhr.responseJSON.subInfos, function(key, value) {
					if (!value.field) { value.field = 'editForm' }
					$('#editFacilityAddrForm .text-danger[data-name=' + value.field + ']').append('<p class="alert alert-danger w-100 mb-0">' + value.message + '</p>');
				});
				$('#editFacilityAddrForm .text-danger').attr("hidden", false);
				if (textStatus == "timeout") {
					alert("Got timeout");
				}
			})
	});
});



// editAddrButton в таблице значений адресов
// Чистка полей и сообщений об ошибках
// Включение кнопок и полей в режим редактирования
// Получение данных текущей строки
// Запрос данных из базы по ID
// при успехе - разнос данных по полям
// при сбое - показ ошибки
$(document).ready(function() {
	$('#facilities-table tbody').on('click', 'a[data-name="editAddrButton"]', function(e) {
		event.preventDefault();

		cleanFacilityAddrForm();
		$('#editFacilityAddrForm #updateFacilityAddrButton').show();
		$('#editFacilityAddrForm #editFacilityAddrNum').show();
		$('#editFacilityAddrForm #addFacilityAddrNum').hide();
		$('#editFacilityAddrForm #addNewFacilityAddrButton').hide();

		var tableId = $(this).closest('table').attr('id');
		var facilityAddress = $("#" + tableId).DataTable().row($(this).closest('tr')).data();
		console.log(facilityAddress);
		$.get({
			url: facilitiesaddressurl + "/" + facilityAddress.id,
			dataType: "json",
			contentType: "application/json",
			//timeout: 3000,
			async: true
		})
			.done(function(facilityAddr) {
				console.log(facilityAddr);
				$('#editFacilityAddrLabelId').text('# ' + facilityAddr.id);
				$('#editFacilityAddrLabelId').attr('data-value', facilityAddr.id);
				$('#fAE-addressesAliasEdit').val(facilityAddr.addressesAlias);
				$('#fAE-addressEdit').val(facilityAddr.address);
				$('#fAE-latitudeEdit').val(facilityAddr.lat);
				$('#fAE-longitudeEdit').val(facilityAddr.lng);
				$("#fAE-defaultAddressEdit").prop("checked", facilityAddr.defaultAddress);

				$('#editFacilityAddrModal').modal('show');
			})
			.fail(function(xhr, textStatus, errorThrown) {
				console.log(xhr);
				console.log(textStatus);
				if (textStatus == "timeout") {
					alert("Got timeout");
				}
			});
	})
});

// При скрытии формы редактора
// Очистка данных в полях и чекбоксах редактора. 
// Удаление сообщение об ошибках
$('#editFacilityAddrModal').on('hidden.bs.modal', function(e) {
	$(this)
		.find("#editFacilityAddrForm input,textarea,select")
		.not('[type=checkbox]')
		.val('')
		.end();
	$(this)
		.find("#editFacilityAddrForm input[type=checkbox], input[type=radio]")
		.prop("checked", false)
		.end();
	$('#editFacilityAddrForm .alert-danger').remove();
	$('#editFacilityAddrForm .text-danger').attr("hidden", true);
})		
