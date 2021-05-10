$(document).ready(function () {
    loadStations();

    refreshRoutesData();

    updateRoute();
    deleteRoute();
    addRoute();

    addListenerToHideErrorMessage();
});

function loadStations() {
    $.ajax({
        type: 'GET',
        url: '/api/stations',
        contentType: 'application/json'
    })
        .then(stations => stations.forEach(
            station => buildSelectStation(station)
        ));
}

function buildSelectStation(station) {
    let option = `<option value="${station.id}">
                        ${station.city} (${station.name})</option>`;
    $('#departureStation').append(option);
    $('#arrivalStation').append(option);
}

function refreshRoutesData(selectedRouteId) {
    $.ajax({
        type: 'GET',
        url: '/api/routes',
        contentType: 'application/json'
    })
        .then(routes => renderRoutes(routes, selectedRouteId))
        .then(renderedRoutes => $('.routes-list')[0].innerHTML = renderedRoutes)

        // method for handling selection of route
        .then(() => {
            $('.btn-route').click(function (event) {

                $('#departureStation').change();
                $('#arrivalStation').change();
                $('#departureTime').change();
                $('#arrivalTime').change();
                $('#trainName').change();
                $('#totalSeats').change();
                $('#pricePerSeat').change();

                let $selectedItem = $(event.currentTarget);
                $('#routeId')[0].value = $selectedItem.data('route-id');
                $('#departureStation')[0].value = $selectedItem.data('route-departure-station-id');
                $('#arrivalStation')[0].value = $selectedItem.data('route-arrival-station-id');
                $('#departureTime')[0].value = $selectedItem.data('route-departure-time');
                $('#arrivalTime')[0].value = $selectedItem.data('route-arrival-time');
                $('#trainName')[0].value = $selectedItem.data('route-train-name');
                $('#totalSeats')[0].value = $selectedItem.data('route-total-seats');
                $('#pricePerSeat')[0].value = $selectedItem.data('route-price-per-seat');
            });
        });
}

function deleteRoute() {
    $('.btn-delete').click(function (event) {
        event.preventDefault();
        let routeId = $('#routeId')[0].value;
        if (!routeId) {
            return;
        }
        $.ajax({
            type: 'DELETE',
            url: '/api/routes/' + routeId,
            contentType: 'application/json'
        })
            .always(() => refreshRoutesData(+routeId))
        .fail(function (xhr, status, error) {
            $('.text-danger').removeClass('invisible');
            $('.text-danger')[0].innerText = xhr.responseText;
        });
    });
}

function addRoute() {
    $('.btn-add').click(function (event) {
        event.preventDefault();
        let departureStationId = $('#departureStation')[0].value;
        let arrivalStationId = $('#arrivalStation')[0].value;
        let departureTime = $('#departureTime')[0].value;
        let arrivalTime = $('#arrivalTime')[0].value;
        let trainName = $('#trainName')[0].value;
        let totalSeats = $('#totalSeats')[0].value;
        let pricePerSeat = $('#pricePerSeat')[0].value;

        let payload = {
            "departureStationId": departureStationId,
            "arrivalStationId": arrivalStationId,
            "departureTime": departureTime,
            "arrivalTime": arrivalTime,
            "trainName": trainName,
            "totalSeats": totalSeats,
            "pricePerSeat": pricePerSeat
        };

        $.ajax({
            type: 'POST',
            url: '/api/routes/new',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(payload)
        })
            .then(response => refreshRoutesData(response.id))
            .fail(function (xhr, status, error) {
                $('.text-danger').removeClass('invisible');
                $('.text-danger')[0].innerText = xhr.responseText;
            });
    });
}

function updateRoute() {
    $('.btn-save').click(function (event) {
        event.preventDefault();
        let routeId = $('#routeId')[0].value;
        let departureStationId = $('#departureStation')[0].value;
        let arrivalStationId = $('#arrivalStation')[0].value;
        let departureTime = $('#departureTime')[0].value;
        let arrivalTime = $('#arrivalTime')[0].value;
        let trainName = $('#trainName')[0].value;
        let totalSeats = $('#totalSeats')[0].value;
        let pricePerSeat = $('#pricePerSeat')[0].value;

        let payload = {
            "departureStationId": departureStationId,
            "arrivalStationId": arrivalStationId,
            "departureTime": departureTime,
            "arrivalTime": arrivalTime,
            "trainName": trainName,
            "totalSeats": totalSeats,
            "pricePerSeat": pricePerSeat
        };

        if (!routeId) {
            return;
        }

        $.ajax({
            type: 'PUT',
            url: '/api/routes/' + routeId + '/edit',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(payload)
        })
            .always(function () {
                refreshRoutesData(+routeId);
            })
            .fail(function (xhr, status, error) {
                $('.text-danger').removeClass('invisible');
                $('.text-danger')[0].innerText = xhr.responseText;
            });
    });
}

function addListenerToHideErrorMessage() {
    $("#departureStation").on('input', function () {
        $('.text-danger').addClass('invisible');
    });

    $("#arrivalStation").on('input', function () {
        $('.text-danger').addClass('invisible');
    });

    $("#departureTime").on('input', function () {
        $('.text-danger').addClass('invisible');
    });

    $("#arrivalTime").on('input', function () {
        $('.text-danger').addClass('invisible');
    });

    $("#trainName").on('input', function () {
        $('.text-danger').addClass('invisible');
    });

    $("#totalSeats").on('input', function () {
        $('.text-danger').addClass('invisible');
    });

    $("#pricePerSeat").on('input', function () {
        $('.text-danger').addClass('invisible');
    });


    $("#departureStation").on('change', function () {
        $('.text-danger').addClass('invisible');
    });

    $("#arrivalStation").on('change', function () {
        $('.text-danger').addClass('invisible');
    });

    $("#departureTime").on('change', function () {
        $('.text-danger').addClass('invisible');
    });

    $("#arrivalTime").on('change', function () {
        $('.text-danger').addClass('invisible');
    });

    $("#trainName").on('change', function () {
        $('.text-danger').addClass('invisible');
    });

    $("#totalSeats").on('change', function () {
        $('.text-danger').addClass('invisible');
    });

    $("#pricePerSeat").on('change', function () {
        $('.text-danger').addClass('invisible');
    });
}

function renderRoutes(routes, selectedRouteId) {
    if (routes.length === 0) {
        return `<p class="fs-4 text-danger" style="text-align: center;">${i18n('noRoutes')}</p>`;
    }
    return routes.map((route, index) => renderRouteLine(route, selectedRouteId, index))
        .reduce((a, b) => a + b, '');
}

function renderRouteLine(route, selectedRouteId, index) {
    let headerClass = 'accordion-button collapsed';
    let bodyClass = 'accordion-collapse collapse';
    if (selectedRouteId === route.id) {
        headerClass = 'accordion-button';
        bodyClass = 'accordion-collapse collapse show';
    }

    let headingId = 'heading' + index;
    let collapseId = 'collapse' + index;

    return `<div class="accordion-item">
                    <h2 class="accordion-header" id="${headingId}">
                        <button class="${headerClass} btn-route" type="button"
                                data-bs-toggle="collapse" data-bs-target="#${collapseId}"
                                aria-expanded="true" aria-controls="${collapseId}"
                                data-route-id="${route.id}"
                                data-route-departure-station-id="${route.departureStation.id}"
                                data-route-arrival-station-id="${route.arrivalStation.id}"
                                data-route-departure-time="${route.departureTime}"
                                data-route-arrival-time="${route.arrivalTime}"
                                data-route-train-name="${route.trainName}"
                                data-route-total-seats="${route.totalSeats}"
                                data-route-price-per-seat="${route.pricePerSeat}">
                            <div class="col-8">
                                <div class="row ms-2">${route.departureStation.city} (${route.departureStation.name})</div>
                                <div class="row ms-2">${route.arrivalStation.city} (${route.arrivalStation.name})</div>
                            </div>
                            <div class="col-3">${route.departureTime}</div>
                        </button>
                    </h2>
                    <div id="${collapseId}" class="${bodyClass}"
                         aria-labelledby="${headingId}" data-bs-parent="#accordionExample">
                        <div class="accordion-body">
                            <table class="table">
                                <tbody>
                                <tr>
                                    <td>${i18n('departureStation')}</td>
                                    <th>${route.departureStation.city} (${route.departureStation.name})</th>
                                </tr>
                                <tr>
                                    <td>${i18n('arrivalStation')}</td>
                                    <th>${route.arrivalStation.city} (${route.arrivalStation.name})</th>
                                </tr>
                                <tr>
                                    <td>${i18n('departureDateTime')}</td>
                                    <th>${route.departureTime}</th>
                                </tr>
                                <tr>
                                    <td>${i18n('arrivalDateTime')}</td>
                                    <th>${route.arrivalTime}</th>
                                </tr>
                                <tr>
                                    <td>${i18n('trainName')}</td>
                                    <th>${route.trainName}</th>
                                </tr>
                                <tr>
                                    <td>${i18n('totalSeats')}</td>
                                    <th>${route.totalSeats}</th>
                                </tr>
                                <tr>
                                    <td>${i18n('pricePerSeat')}</td>
                                    <th>${route.pricePerSeat}</th>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>`;
}