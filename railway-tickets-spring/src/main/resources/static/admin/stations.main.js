$(document).ready(function () {
    refreshStationData();

    updateStation();
    deleteStation();
    addStation();

    addListenerToHideErrorMessage();
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
                $('#stationCity').change();
                $('#stationName').change();
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
        if (!stationId) {
            return;
        }
        $.ajax({
            type: 'PUT',
            url: '/api/stations/' + stationId + '/edit',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(payload)
        })
            .always(function () {
                refreshStationData(+stationId);
            })
            .fail(function (xhr, status, error) {
                $('.text-danger').removeClass('invisible');
                $('.text-danger')[0].innerText = xhr.responseText;
            });
    });
}

function deleteStation() {
    $('.btn-delete').click(function (event) {
        event.preventDefault();
        let stationId = $('#stationId')[0].value;
        if (!stationId) {
            return;
        }
        $.ajax({
            type: 'DELETE',
            url: '/api/stations/' + stationId,
            contentType: 'application/json'
        })
            .always(() => refreshStationData(+stationId))
            .fail(function (xhr, status, error) {
                $('.text-danger').removeClass('invisible');
                $('.text-danger')[0].innerText = xhr.responseText;
            });
    });
}

function addListenerToHideErrorMessage() {
    $("#stationCity").on('input', function () {
        $('.text-danger').addClass('invisible');
    });

    $("#stationName").on('input', function () {
        $('.text-danger').addClass('invisible');
    });

    $("#stationCity").on('change', function () {
        $('.text-danger').addClass('invisible');
    });

    $("#stationName").on('change', function () {
        $('.text-danger').addClass('invisible');
    });
}

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
            .then(response => refreshStationData(response.id))
            .fail(function (xhr, status, error) {
                $('.text-danger').removeClass('invisible');
                $('.text-danger')[0].innerText = xhr.responseText;
            });
    });
}

function renderStations(stations, selectedStationId) {
    if (stations.length === 0) {
        return `<p class="fs-4 text-danger" style="text-align: center;">${i18n('noStations')}</p>`;
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
