$(document).ready(function () {
    refreshStationData();

    updateStation();
    deleteStation();
    addStation();
});

function refreshStationData(selectedStationId) {
    $.ajax({
        type: 'GET',
        url: '/api/stations',
        contentType: 'application/json'
    })
        .then(stations => renderStations(stations, selectedStationId))
        .then(renderedStations => $('.stations-list')[0].innerHTML = renderedStations)

        // method for handling selection of station
        .then(() => {
            $('.list-group-item-action').click(function (event) {
                event.preventDefault();
                $('.list-group-item-action').each(function () {
                    $(this).removeClass('active');
                });
                let $selectedItem = $(event.target);
                $selectedItem.addClass('active');
                $('#stationId')[0].value = $selectedItem.data('station-id');
                $('#stationCity')[0].value = $selectedItem.data('station-city');
                $('#stationName')[0].value = $selectedItem.data('station-name');
            });
        });
}

function updateStation() {
    $('.btn-save').click(function (event) {
        event.preventDefault();
        let stationId = $('#stationId')[0].value;
        let stationCity = $('#stationCity')[0].value;
        let stationName = $('#stationName')[0].value;
        let payload = {
            "city": stationCity,
            "name": stationName
        };
        $.ajax({
            type: 'PUT',
            url: '/api/stations/' + stationId + '/edit',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(payload)
        })
            .always(function () {
                refreshStationData(+stationId);
            });
    });
}

// TODO - add alert (exception handling Spring boot) that station from the active route cannot be deleted
function deleteStation() {
    $('.btn-delete').click(function (event) {
        event.preventDefault();
        let stationId = $('#stationId')[0].value;
        $.ajax({
            type: 'DELETE',
            url: '/api/stations/' + stationId,
            contentType: 'application/json'
        })
            .always(() => refreshStationData(+stationId));
    });
}

// TODO - add alert (exception handling Spring boot) that station with the same name and city already exists
function addStation() {
    $('.btn-add').click(function (event) {
        event.preventDefault();
        let stationCity = $('#stationCity')[0].value;
        let stationName = $('#stationName')[0].value;
        let payload = {
            "city": stationCity,
            "name": stationName
        };
        $.ajax({
            type: 'POST',
            url: '/api/stations/new',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(payload)
        })
            .then(response =>  refreshStationData(response.id));
    });
}

function renderStations(stations, selectedStationId) {
    if (stations.length === 0) {
        return '<p class="fs-2 text-danger" style="text-align: center;">No available stations</p>';
    }
    return stations.map(station => renderStationLine(station, selectedStationId))
        .reduce((a, b) => a + b, '');
}

function renderStationLine(station, selectedStationId) {
    console.log(`#renderStationLine(${station.id}, ${selectedStationId})`);
    let classes = 'list-group-item list-group-item-action';
    if (selectedStationId === station.id) {
        classes += ' active';
    }
    return `<a href="#" class="${classes}"
                        data-station-id="${station.id}"
                        data-station-city="${station.city}"
                        data-station-name="${station.name}">
                ${station.city} (${station.name})
            </a>`;
}
