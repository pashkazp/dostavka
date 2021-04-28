/**
 * 
 */
var draftSheetDescrFiled = $('#editDraftSheetForm #descrEdit');
var draftSheetIdField = $('#editDraftSheetLabelId');
var draftSheetCreatedField = $('#editDraftSheetLabelCreated');
var draftSheetAuthorField = $('#editDraftSheetLabelAuthor');
var editDraftSheetModalFrame = $('#editDraftSheetModal');
var editDraftSheetForm = $('#editDraftSheetForm');
var addNewRouteSheetButton = $('#editDraftSheetForm #addNewRouteSheetButton');
var updateRouteSheetButton = $('#editDraftSheetForm #updateRouteSheetButton');
var draftSheetEditFormTitle = $('#editDraftSheetForm #editDraftSheetDiv');
var draftSheetAddFormTitle = $('#editDraftSheetForm #addDraftSheetDiv');

// чистка полей и чекбоксов редактора
function cleanDraftSheetFormFields() {
	$(this)
		.find("#editDraftSheetForm input,textarea,select")
		.not('[type=checkbox]')
		.val('')
		.end();
	$(this)
		.find("#editDraftSheetForm input[type=checkbox], input[type=radio]")
		.prop("checked", false)
		.end();
		draftSheetDescrFiled.val('');
};

// чистка сообщений об ошибках
function cleanDraftSheetFormErrors() {
	editDraftSheetForm.find('.alert-danger').remove();
	editDraftSheetForm.find('.text-danger').remove();
//	$('#editDraftSheetForm .alert-danger').remove();
//	$('#editDraftSheetForm .text-danger').attr("hidden", true);
};

// Очистка формы редактора
// чистка полей и чекбоксов
// чистка сообщений об ошибках
// сокрытие кнопок и служебных полей редактора
function cleanDraftSheetForm() {
	cleanDraftSheetFormFields();
	cleanDraftSheetFormErrors();
	addNewRouteSheetButton.hide();
	updateRouteSheetButton.hide();
	draftSheetEditFormTitle.hide();
	draftSheetAddFormTitle.hide();
};

// нажатие кнопки добавления черновика маршрутного листа 
// очистка формы
// включение нужных кнопок и служебных полей
// показ формы редактора
function onAddCardDraftSheetButtonClick() {
	console.log("press add draft sheet");
	//e.preventDefault();
	cleanDraftSheetForm();
	addNewRouteSheetButton.show();
	draftSheetAddFormTitle.show();
	editDraftSheetModalFrame.modal('show');
};



// EditButton в таблице значений черновиков маршрутных листов
// Чистка полей и сообщений об ошибках
// Включение кнопок и полей в режим редактирования
// Получение данных текущей строки
// Запрос данных из базы по ID
// при успехе - разнос данных по полям
// при сбое - показ ошибки
$(document).ready(function() {
	$('#draftsheet-table tbody').on('click', 'a[data-name="editButton"]', function(e) {
		event.preventDefault();

		cleanDraftSheetForm();
		updateRouteSheetButton.show();
		draftSheetEditFormTitle.show();

		var tr = $(this).closest("tr");
		var data = draftsheet_table.row(tr).data();
		$.get({
			url: GetDrafSheetUrl + "/" + data.id,
			dataType: "json",
			contentType: "application/json",
			//timeout: 3000,
			async: true
		})
			.done(function(draftRouteSheet) {
				console.log(draftRouteSheet);
				draftSheetIdField.text('# ' + draftRouteSheet.id);
				draftSheetIdField.attr('data-value', draftRouteSheet.id);
				draftSheetCreatedField.text('- ' + draftRouteSheet.creationDate);
				draftSheetCreatedField.attr('data-value', draftRouteSheet.creationDate);
				draftSheetAuthorField.text('- ' + draftRouteSheet.author);
				draftSheetAuthorField.attr('data-value', draftRouteSheet.author);
				draftSheetDescrFiled.val(draftRouteSheet.description);

				editDraftSheetModalFrame.modal('show');
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

// SAVE updateRouteSheetButton на форме редактора
// очиста сообщений об ошибках
// получение данных из полей редактора формы
// отправка данных в базу данных
// при успехе обновления - сокрытие формы редактора и рефреш таблицы
// при неуспехе - показ ошибок
// при сбое - показ ошибок
$(document).ready(function() {
	updateRouteSheetButton.click(function(e) {

		//Prevent default submission of form
		e.preventDefault();

		//Remove previous errors
		cleanDraftSheetFormErrors();

		var formData = {
			id: draftSheetIdField.attr('data-value'),
			description: draftSheetDescrFiled.val(),
		}

		console.log(formData);
		$.ajax({
			url: PutDrafSheetUrl + "/" + formData.id,
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
				editDraftSheetModalFrame.modal('hide');
				draftsheet_table.ajax.reload();
			})
			.fail(function(xhr, textStatus, errorThrown) {
				console.log(xhr);
				console.log(textStatus);
				$.each(xhr.responseJSON.subInfos, function(key, value) {
					if (!value.field) { value.field = 'editForm' }
					editDraftSheetForm.find('.text-danger[data-name=' + value.field + ']').append('<p class="alert alert-danger w-100 mb-0">' + value.message + '</p>');
				});
				editDraftSheetForm.find('.text-danger').attr("hidden", false);
				if (textStatus == "timeout") {
					alert("Got timeout");
				}
			});
	});
});

