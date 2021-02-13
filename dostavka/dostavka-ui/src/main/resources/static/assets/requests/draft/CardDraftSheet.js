/**
 * 
 */
function formatdraftsheet(d) {
	var sRegExInput = new RegExp('{d.id}', "g");
	detailDraftSheetFormatStr = detailDraftSheetFormatStr.replace(sRegExInput, d.id);
	var sRegExInput = new RegExp('{d.author}', "g");
	detailDraftSheetFormatStr = detailDraftSheetFormatStr.replace(sRegExInput, d.author);
	return detailDraftSheetFormatStr;
}

function onAddCardDraftSheetButtonClick() {

}

$(document).ready(function() {
	const FROM_PATTERN = 'YYYY-MM-DD HH:mm:ss.SSS';
	const TO_PATTERN = 'DD.MM.YYYY HH:mm';
	var draftsheet_table = $('#draftsheet-table').DataTable({
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
					"extend": 'print',
					"text": '<span class="fas fa-print"></span>',
					"className": 'btn btn-xs'
				},
				{
					"extend": 'pdf',
					"text": '<span class="far fa-file-pdf"></span>',
					"className": 'btn btn-xs'
				},
				{
					"text": '<span class="fas fa-plus"></span>',
					"enabled": canAddFacility,
					"className": 'btn btn-xs',
					"action": function(e, dt, node, config) {
						onAddCardDraftSheetButtonClick();
					}
				},
				{
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
				"defaultContent": editFacilityBtnContent,
				"targets": -1,
				"visible": canEditCardDraftSheet,
				responsivePriority: 2
			},
			{ "data": "creationDate", "width": "11rem" },
			{ "data": "description", "width": "70%" }
		],
		"order": [[2, 'asc']],
		"columnDefs": [
			{
				"targets": 2,
				"render": $.fn.dataTable.render.moment(FROM_PATTERN, TO_PATTERN)
			}
		]
	});

	// Array to track the ids of the details displayed rows
	var draftsheet_table_detailRows = [];

	$('#draftsheet-table tbody').on('click', 'tr td.details-control', function() {
		var tr = $(this).closest('tr');
		var row = draftsheet_table.row(tr);
		var idx = $.inArray(tr.attr('id'), draftsheet_table_detailRows);

		if (row.child.isShown()) {
			tr.removeClass('details');
			row.child.hide();

			// Remove from the 'open' array
			draftsheet_table_detailRows.splice(idx, 1);
		}
		else {
			tr.addClass('details');
			row.child(formatdraftsheet(row.data())).show();

			// Add to the 'open' array
			if (idx === -1) {
				draftsheet_table_detailRows.push(tr.attr('id'));
			}
		}
	});

	// On each draw, loop over the `detailRows` array and show any child rows
	draftsheet_table.on('draw', function() {
		$.each(draftsheet_table_detailRows, function(i, id) {
			$('#' + id + ' td.details-control').trigger('click');
		});
	});
});