/**
 * 
 */
// чистка полей и чекбоксов редактора
function cleanFacilityFormFields() {
	$(this)
		.find("#editFacilityForm input,textarea,select")
		.not('[type=checkbox]')
		.val('')
		.end();
	$(this)
		.find("#editFacilityForm input[type=checkbox], input[type=radio]")
		.prop("checked", false)
		.end();
};

// чистка сообщений об ошибках
function cleanFacilityFormErrors() {
	$('#editFacilityForm .alert-danger').remove();
	$('#editFacilityForm .text-danger').attr("hidden", true);
};

// Очистка формы редактора
// чистка полей и чекбоксов
// чистка сообщений об ошибках
// сокрытие кнопок и служебных полей редактора
function cleanFacilityForm() {
	cleanFacilityFormFields();
	cleanFacilityFormErrors();
	$('#editFacilityForm #addNewFacilityButton').hide();
	$('#editFacilityForm #updateFacilityButton').hide();
	$('#editFacilityForm #editFacilityNum').hide();
	$('#editFacilityForm #addFacilityNum').hide();
	$('#editFacilityForm #fEF-header').hide();
	$('#editFacilityForm #fEF-block').hide();
};

// нажатие кнопки добавления записи
// очистка формы
// включение нужных кнопок и служебных полей
// показ формы редактора
function onAddNewFacilityButtonModalClick() {
	console.log("press add");
	//e.preventDefault();
	cleanFacilityForm();
	$('#editFacilityForm #addNewFacilityButton').show();
	$('#editFacilityForm #addFacilityNum').show();
	$('#editFacilityForm #fEF-header').show();
	$('#editFacilityForm #fEF-block').show();
	$('#editFacilityModal').modal('show');
};

// ADD addNewFacilityButton на форме редактора
// Чистка сообщений об ошибках
// Получение значений полей формы редактора
// Отправка в базу
// при успехе добавления - скрытие формы редактора и рефреш таблицы
// при неуспехе - показ ошибок
// при сбое - показ ошибок
$(document).ready(function() {

	$('#addNewFacilityButton').click(function(e) {

		//Prevent default submission of form
		e.preventDefault();

		//Remove previous errors
		cleanFacilityFormErrors();

		var formData = {
			name: $('#editFacilityForm #nameEdit').val(),
			facilityAddress: {
				addressesAlias: $('#editFacilityForm #fEF-addressesAliasEdit').val(),
				address: $('#editFacilityForm #fEF-addressEdit').val(),
				lat: $('#editFacilityForm #fEF-latitudeEdit').val(),
				lng: $('#editFacilityForm #fEF-longitudeEdit').val(),
				defaultAddress: $('#editFacilityForm #fEF-defaultAddressEdit').prop('checked'),
			}
		}

		console.log(formData);
		$.post({
			url: PostNewFacilityUrl + "/",
			data: JSON.stringify(formData),
			dataType: "json",
			contentType: "application/json",
			//timeout: 3000,
			async: true
		})
			.done(function(res) {
				console.log(res);
				$('#editFacilityModal').modal('hide');
				$('#facilities-table').DataTable().ajax.reload(null, false);
				if (res.validated) {
					//take your successful action here; you may want to redirect to another page
				} else {
				}
			})
			.fail(function(xhr, textStatus, errorThrown) {
				console.log(xhr.status);
				console.log(xhr);
				$.each(xhr.responseJSON.subInfos, function(key, value) {
					if (!value.field) { value.field = 'editForm' }
					$('#editFacilityForm .text-danger[data-name=' + value.field + ']').append('<p class="alert alert-danger w-100 mb-0">' + value.message + '</p>');
				});
				$('#editFacilityForm .text-danger').attr("hidden", false);
				switch (xhr.status) {
					case 403:
						$('#editFacilityForm .text-danger[data-name=' + 'editForm' + ']')
							.append('<p class="alert alert-danger w-100 mb-0">'
								+ 'Not enough rights to perform action'
								+ '</p>');
						break;
					case 400:
						break;
					default:
						alert(xhr.responseText);
						console.log(xhr.responseText);
				}
				if (textStatus == "timeout") {
					alert("Got timeout");
				}
			})
	});
});

// SAVE updateFacilityButton на форме редактора
// очиста сообщений об ошибках
// получение данных из полей редактора формы
// отправка данных в базу данных
// при успехе обновления - сокрытие формы редактора и рефреш таблицы
// при неуспехе - показ ошибок
// при сбое - показ ошибок
$(document).ready(function() {
	$('#updateFacilityButton').click(function(e) {

		//Prevent default submission of form
		e.preventDefault();

		//Remove previous errors
		cleanFacilityFormErrors();

		var formData = {
			id: $('#editFacilityLabelId').attr('data-value'),
			name: $('#editFacilityForm #nameEdit').val(),
		}

		console.log(formData);
		$.ajax({
			url: PutFacilityUrl + "/" + formData.id,
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
				$('#editFacilityModal').modal('hide');
				$('#facilities-table').DataTable().ajax.reload(null, false);
			})
			.fail(function(xhr, textStatus, errorThrown) {
				console.log(xhr);
				console.log(textStatus);
				$.each(xhr.responseJSON.subInfos, function(key, value) {
					if (!value.field) { value.field = 'editForm' }
					$('#editFacilityForm .text-danger[data-name=' + value.field + ']').append('<p class="alert alert-danger w-100 mb-0">' + value.message + '</p>');
				});
				$('#editFacilityForm .text-danger').attr("hidden", false);
				if (textStatus == "timeout") {
					alert("Got timeout");
				}
			});
	});
});

// EditButton в таблице значений организаций
// Чистка полей и сообщений об ошибках
// Включение кнопок и полей в режим редактирования
// Получение данных текущей строки
// Запрос данных из базы по ID
// при успехе - разнос данных по полям
// при сбое - показ ошибки
$(document).ready(function() {
	$('#facilities-table tbody').on('click', 'a[data-name="editButton"]', function(e) {
		event.preventDefault();

		cleanFacilityForm();
		$('#editFacilityForm #updateFacilityButton').show();
		$('#editFacilityForm #editFacilityNum').show();

		var tr = $(this).closest("tr");
		var data = $("#facilities-table").DataTable().row(tr).data();
		$.get({
			url: GetFacilityUrl + "/" + data.id,
			dataType: "json",
			contentType: "application/json",
			//timeout: 3000,
			async: true
		})
			.done(function(facility) {
				console.log(facility);
				$('#editFacilityLabelId').text('# ' + facility.id);
				$('#editFacilityLabelId').attr('data-value', facility.id);
				$('#nameEdit').val(facility.name);

				$('#editFacilityModal').modal('show');
			})
			.fail(function(xhr, textStatus, errorThrown) {
				console.log(xhr);
				console.log(textStatus);
				console.log(errorThrown);
				if (textStatus == "timeout") {
					alert("Got timeout");
				}
			});
	})
});

// При скрытии формы редактора
// Очистка данных в полях и чекбоксах редактора. 
// Удаление сообщение об ошибках
$('#editFacilityModal').on('hidden.bs.modal', function(e) {
	$(this)
		.find("#editFacilityForm input,textarea,select")
		.not('[type=checkbox]')
		.val('')
		.end();
	$(this)
		.find("#editFacilityForm input[type=checkbox], input[type=radio]")
		.prop("checked", false)
		.end();
	$('#editFacilityForm .alert-danger').remove();
	$('#editFacilityForm .text-danger').attr("hidden", true);
})		
