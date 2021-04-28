// *********************************************************************************************
//
// Draft Reqs
//
//
// *********************************************************************************************

function onAddCardDraftReqsButtonClick() {

}

function formatdraftreqs(d) {
	varStr = detailDraftReqsFormatStr;

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

$(document).ready(function() {

	$.fn.dataTable.ext.errMode = 'none';

	draftreqs_table = $('#draftreqs-table').DataTable({
		dom: "<'row'<'col-sm-12 col-md-4'B><'col-sm-12 col-md-8'f>>" +
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
					"name": 'printDraftReqs',
					"extend": 'print',
					"text": '<span class="fas fa-print"></span>',
					"className": 'btn btn-xs'
				},
				{
					"name": 'toPdfDraftReqs',
					"extend": 'pdf',
					"text": '<span class="far fa-file-pdf"></span>',
					"className": 'btn btn-xs'
				},
				{
					"name": 'addDraftReqs',
					"text": '<span class="fas fa-plus"></span>',
					"enabled": canAddDraftReqs,
					"className": 'btn btn-xs',
					"action": function(e, dt, node, config) {
						onAddCardDraftReqsButtonClick();
					}
				},
				{
					"name": 'refreshDraftReqs',
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
			"url": cardDraftReqsPagesUrl,
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
				"className": "details-control draftreqs-table",
				orderable: false,
				data: null,
				defaultContent: '',
				width: '3em'
			},
			{
				"data": null,
				"bSortable": false,
				"width": "50px",
				"defaultContent": buttonsCardDraftReqsBtnContent,
				"targets": -1,
				"visible": canEditCardDraftReqs,
				responsivePriority: 2
			},
			//			{ "data": "id", "width": "3em" },
			{ "data": "creationDate", "width": "11em" },
			{ "data": "description", "width": "70%" }
		],
		"order": [[2, 'asc']]
	});

	$('#draftreqs-table tbody').on('click', 'tr td.draftreqs-table', function(e) {
		var tr = $(this).closest('tr');
		var row = draftreqs_table.row(tr);
		e.stopPropagation();

		if (row.child.isShown()) {
			tr.removeClass('shown');
			row.child.hide();
		}
		else {
			tr.addClass('shown');
			row.child(formatdraftreqs(row.data())).show();
		}
	});


	draftreqs_table
		.on('error.dt', function(e, settings, techNote, message) {
			console.log('An error has been reported by draftreqs_table DataTables: ', message);
		});

});