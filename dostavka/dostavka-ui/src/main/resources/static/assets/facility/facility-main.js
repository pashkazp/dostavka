/**
 * 
 */

var childEditors = {};  // Globally track created chid editors
// Generate table ID
function getTableId(level, uniqueData) {
	// level = child level.
	// uniqueData - unique data value in table.
	return level + '-' + uniqueData.replace(' ', '-'); // Replace sapces with dashes
}

// Return table with id generated from row's name field
function format(rowData, tableId) {
	// rowData - data for the table.
	// tableId - unique table ID for child table.
	// This function just builds the table tag.
	return '<table id="' + tableId + '" class="table table-striped table-hover table-sm table-bordered" style="padding-left:2em;">' +
		'</table>';
}

$(document).ready(function() {

	$.fn.dataTable.ext.errMode = 'none';


	facilitiesTable = $('#facilities-table').DataTable({
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
						onAddNewFacilityButtonModalClick();
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
			"url": facilitiespagesurl,
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
				className: 'details-control main-table',
				orderable: false,
				data: null,
				defaultContent: '',
				width: '3rem'
			},
			{
				"data": null,
				"bSortable": false,
				"width": "5rem",
				"defaultContent": editFacilityBtnContent,
				"targets": -1,
				"visible": canEditFacility,
				responsivePriority: 2
			},
			{ "data": "id", "width": "9rem" },
			{ "data": "name", "width": "100%" }
		],

		"order": [
			[2, 'asc']
		]

	});


	facilitiesTable
		.on('error.dt', function(e, settings, techNote, message) {
			console.log('An error has been reported by draftreqs_table DataTables: ', message);
		});

	// Add event listener for for main talbe to open and close first level child details
	$('#facilities-table tbody').on('click', 'td.main-table', function() {
		var tr = $(this).closest('tr');
		var row = facilitiesTable.row(tr);
		var rowData = row.data();


		if (row.child.isShown()) {
			// This row is already open - close it
			row.child.hide();
			tr.removeClass('shown');

			// Destroy the Child Datatable
			$('#' + 'addr-table-' + rowData.id).DataTable().destroy();
		}
		else {

			var tableId = 'addr-table-' + rowData.id; //getTableId("1", rowData.id);
			row.child(format(tableId, tableId)).show();

			var facilityAdrrTable = $('#' + tableId).DataTable({
				dom: "Bt",
				scrollY: '200px',
				destroy: true,
				scrollCollapse: true,
				select: true,
				"paging": false,
				"ajax": function(data, callback, settings) {
					$.ajax({
						"url": facilitiesurl + "/" + rowData.id + facilitiesaddrurl,
						"type": "GET",
						"dataType": "json",
						"contentType": "application/json",
						//timeout: 3000,
						"async": true,
					})
						.then(function(facilityAddresses) {
							//		                var data = JSON.parse(dt);
							var display = [];
							//						console.log(facilityAddresses);
							$.each(facilityAddresses, function(index, value) {
								//						  console.log(value);
								display.push(value);
							});
							callback({ data: display });

						});
				},
				columns: [
					{ data: "id", title: AddrTableColTitleId, "width": "9rem", "orderable": "true" },
					{
						data: null,
						title: AddrTableTitleActions,
						"bSortable": false,
						"width": "5rem",
						"defaultContent": editFacilityAddrBtnContent,
						"targets": -1,
						"visible": canEditFacilityAddr,
						responsivePriority: 2
					},
					{ data: "addressesAlias", title: AddrTableColTitleAlias, "width": "30%", "orderable": "true" },
					{ data: "address", title: AddrTableColTitleAddr, "width": "30%", "orderable": "true" },
					{ data: "defaultAddress", title: AddrTableColTitleDefAddr, "render": true_false_show_check_func, "width": "10%", "orderable": "true" },
					{ data: "lat", title: AddrTableColTitleLat, "width": "15%", "orderable": "true" },
					{ data: "lng", title: AddrTableColTitleLng, "width": "15%", "orderable": "true" }
				],
				"aaSorting": [[3, 'desc'], [0, 'asc']],
				buttons: {
					dom: {
						button: {
							className: 'btn-outline-primary'
						}
					},
					buttons: [
						{
							"text": '<span class="fas fa-plus"></span>',
							"enabled": canAddFacilityAddr,
							"className": 'btn btn-xs',
							"action": function(e, dt, node, config) {
								onAddNewFacilityAddrButtonModalClick(rowData.id);
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
				}
			});
			
			facilityAdrrTable
				.on('error.dt', function(e, settings, techNote, message) {
					console.log('An error has been reported by facilityAdrrTable DataTables: ', message);
				});

			tr.addClass('shown');
		}
	});
});
