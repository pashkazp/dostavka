// *********************************************************************************************
//
// Sheet Reqs
//
//
// *********************************************************************************************
function onAddCardSheetReqsButtonClick() {

}

function formatsheetreqs(d) {
	varStr = detailSheetReqsFormatStr;

	var sRegExInput = new RegExp('{d.id}', "g");
	varStr = varStr.replace(sRegExInput, d.id);
	var sRegExInput = new RegExp('{d.author}', "g");
	varStr = varStr.replace(sRegExInput, d.author);
	var sRegExInput = new RegExp('{d.fromAddr}', "g");
	varStr = varStr.replace(sRegExInput, d.fromAddr);
	var sRegExInput = new RegExp('{d.fromFacility}', "g");
	varStr = varStr.replace(sRegExInput, d.fromFacility);
	var sRegExInput = new RegExp('{d.toAddr}', "g");
	varStr = varStr.replace(sRegExInput, d.toAddr);
	var sRegExInput = new RegExp('{d.toFacility}', "g");
	varStr = varStr.replace(sRegExInput, d.toFacility);

	return varStr;
}

var cardSheetReqsPagesUrl = calculateSheetReqsUrl(0);

$(document).ready(function() {
	$.fn.dataTable.ext.errMode = 'none';

	sheetreqs_table = $('#sheetreqs-table').DataTable({
		dom: "<'row'<'col-sm-12 col-md-4'B>>" +
			"<'row'<'col-sm-12'tr>>" +
			"<'row'<'col-sm-12 col-md-4'i><'col-sm-12 col-md-3'l><'col-sm-12 col-md-5'p>>",
		"processing": true,
		"serverSide": true,
		scrollY: '30vh',
		scrollCollapse: true,
		paging: true,
		select: true,

		buttons: {
			dom: {
				button: {
					className: 'btn-outline-primary'
				}
			},
			buttons: [
				{
					"name": 'printSheetReqs',
					"extend": 'print',
					"text": '<span class="fas fa-print"></span>',
					"className": 'btn btn-xs'
				},
				{
					"name": 'toPdfSheetReqs',
					"extend": 'pdf',
					"text": '<span class="far fa-file-pdf"></span>',
					"className": 'btn btn-xs'
				},
				{
					"name": 'addSheetReqs',
					"text": '<span class="fas fa-plus"></span>',
					"enabled": canAddSheetReqs,
					"className": 'btn btn-xs',
					"action": function(e, dt, node, config) {
						onAddCardSheetReqsButtonClick();
					}
				},
				{
					"name": 'refreshSheetReqs',
					"text": '<span class="fas fa-sync-alt"></span>',
					"className": 'btn btn-xs',
					"action": function(e, dt, node, config) {
						//dt.clear().draw();
						dt.ajax.reload(null, false);
					}
				}
			]
		},

		"lengthMenu": [
			[25, 50, 100, -1], [25, 50, 100, "All"]
		],

		"pagingType": "numbers",

		"ajax": {
			"url": cardSheetReqsPagesUrl,
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
				"className": "details-control sheetreqs-table",
				orderable: false,
				data: null,
				defaultContent: '',
				width: '3em'
			},
			{
				"data": null,
				"bSortable": false,
				"width": "50px",
				"defaultContent": buttonsCardSheetReqsBtnContent,
				"targets": -1,
				"visible": canEditCardSheetReqs,
				responsivePriority: 2
			},
			//			{ "data": "id", "width": "3em" },
			{ "data": "creationDate", "width": "11em" },
			{ "data": "description", "width": "70%" }
		],
		"order": [[2, 'asc']]
	});

	$('#sheetreqs-table tbody').on('click', 'tr td.sheetreqs-table', function(e) {
		var tr = $(this).closest('tr');
		var row = sheetreqs_table.row(tr);
		e.stopPropagation();

		if (row.child.isShown()) {
			tr.removeClass('shown');
			row.child.hide();
		}
		else {
			tr.addClass('shown');
			row.child(formatsheetreqs(row.data())).show();
		}
	});

	sheetreqs_table
		.on('error.dt', function(e, settings, techNote, message) {
			console.log('An error has been reported by sheetreqs_table DataTables: ', message);
		})
		.on('init.dt', function(e, dt, type, indexes) {
			sheetreqs_table.buttons([0, 1, 2, 3]).disable();
		});

});
