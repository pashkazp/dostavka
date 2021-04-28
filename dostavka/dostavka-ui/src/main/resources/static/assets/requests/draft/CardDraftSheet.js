// *********************************************************************************************
//
// Draft Sheets
//
//
// *********************************************************************************************

function formatdraftsheet(d) {
	varStr = detailDraftSheetFormatStr;

	var sRegExInput = new RegExp('{d.id}', "g");
	varStr = varStr.replace(sRegExInput, d.id);
	var sRegExInput = new RegExp('{d.author}', "g");
	varStr = varStr.replace(sRegExInput, d.author);

	return varStr;
}

function calculateSheetReqsUrl(id) {
	varStr = cardSheetReqsPagesTemplateUrl;

	var sRegExInput = new RegExp('{id}', "g");
	varStr = varStr.replace(sRegExInput, id);
	return varStr;
}

function draftsheetTableRowSelected() {
	reloadSheetReqsTable();
	sheetreqs_table.buttons([0, 1, 3]).enable();
	if (canAddSheetReqs) {
		sheetreqs_table.buttons([2]).enable();
	}  
	draftsheet_table.buttons([0, 1]).enable();
}
function draftsheetTableRowDeselected() {
	reloadSheetReqsTable();
	sheetreqs_table.buttons([0, 1, 2, 3]).disable();
	draftsheet_table.buttons([0, 1]).disable();
}

function reloadSheetReqsTable() {
	var id = getDraftSheetSelectedId();
	var url = calculateSheetReqsUrl(id);
	sheetreqs_table.ajax.url(url);
	sheetreqs_table.ajax.reload(null, true);
}


function getDraftSheetSelectedId() {
	var data = draftsheet_table.row({ selected: true }).data();
	if (!data) {
		return 0;
	}
	return data.id;
}

$(document).ready(function() {

	$.fn.dataTable.ext.errMode = 'none';


	draftsheet_table = $('#draftsheet-table').DataTable({
		dom: "<'row'<'col-sm-12 col-md-4'B><'col-sm-12 col-md-8'f>>" +
			"<'row'<'col-sm-12'tr>>" +
			"<'row'<'col-sm-12 col-md-4'i><'col-sm-12 col-md-3'l><'col-sm-12 col-md-5'p>>",
		"processing": true,
		"serverSide": true,
		scrollY: '30vh',
		scrollCollapse: true,
		paging: true,
		select: {
			style: 'single'
		},
		buttons: {
			dom: {
				button: {
					className: 'btn-outline-primary'
				}
			},
			buttons: [
				{
					"name": 'printDraftSheet',
					"extend": 'print',
					"text": '<span class="fas fa-print"></span>',
					"className": 'btn btn-xs'
				},
				{
					"name": 'toPdfDraftSheet',
					"extend": 'pdf',
					"text": '<span class="far fa-file-pdf"></span>',
					"className": 'btn btn-xs'
				},
				{
					"name": 'addDraftSheet',
					"text": '<span class="fas fa-plus"></span>',
					"enabled": canAddDraftSheet,
					"className": 'btn btn-xs',
					"action": function(e, dt, node, config) {
						onAddCardDraftSheetButtonClick();
					}
				},
				{
					"name": 'refreshDraftSheet',
					"text": '<span class="fas fa-sync-alt"></span>',
					"className": 'btn btn-xs',
					"action": function(e, dt, node, config) {
						dt.ajax.reload(null, true);
						dt.rows().deselect();
					}
				}
			]
		},

		"lengthMenu": [
			[25, 50, 100, -1], [25, 50, 100, "All"]
		],

		"pagingType": "numbers",

		"ajax": {
			"url": cardDraftSheetPagesUrl,
			"type": "POST",
			"dataType": "json",
			"contentType": "application/json",
			"data": function(d) {
				return JSON.stringify(d);
			}
		},

		"language": {
			"url": localecallstr
		},


		"columns": [
			{
				"className": "details-control draftsheet-table",
				orderable: false,
				data: null,
				defaultContent: '',
				width: '3em'
			},
			{
				"data": null,
				"bSortable": false,
				"width": "5em",
				"defaultContent": buttonsCardDraftSheetBtnContent,
				"targets": -1,
				"visible": canEditCardDraftSheet,
				responsivePriority: 2
			},
			{ "data": "creationDate", "width": "11em" },
			{ "data": "description", "width": "70%" }
		],
		"order": [[2, 'asc']]
	});

	$('#draftsheet-table tbody').on('click', 'tr td.draftsheet-table', function(e) {
		var tr = $(this).closest('tr');
		var row = draftsheet_table.row(tr);
		e.stopPropagation();

		if (row.child.isShown()) {
			tr.removeClass('shown');
			row.child.hide();
		}
		else {
			tr.addClass('shown');
			row.child(formatdraftsheet(row.data())).show();
		}
	});


	draftsheet_table
		.on('error.dt', function(e, settings, techNote, message) {
			console.log('An error has been reported by draftsheet_table DataTables: ', message);
		})
		.on('init.dt', function(e, dt, type, indexes) {
			draftsheet_table.buttons([0, 1]).disable();
		})
		.on('select', function(e, dt, type, indexes) {
			//var rowData = draftsheet_table.rows(indexes).data().toArray();
			draftsheetTableRowSelected();
		})
		.on('deselect', function(e, dt, type, indexes) {
			//var rowData = draftsheet_table.rows(indexes).data().toArray();
			draftsheetTableRowDeselected();
		});

});

