$(document).ready(function () {
    loadStations();

    refreshRoutesData(1, null);

    updateRoute();
    deleteRoute();
    addRoute();

    addListenerToHideErrorMessage();
});

function loadStations() {
    $.ajax({
        type: 'GET',
        url: '/api/stations?pageSize=500',
        contentType: 'application/json'
    })
        .then(pageableStations => pageableStations.content.forEach(
            station => buildSelectStation(station)
        ));
}

// TODO - replace with autocomplete datalist https://getbootstrap.com/docs/5.0/forms/form-control/#datalists
function buildSelectStation(station) {
    let option = `<option value="${station.id}">
                        ${station.city} (${station.name})</option>`;
    $('#departureStation').append(option);
    $('#arrivalStation').append(option);
}

function refreshRoutesData(pageNo, selectedRouteId) {
    $.ajax({
        type: 'GET',
        url: `/api/routes?pageNo=${pageNo}`,
        contentType: 'application/json'
    })
        .then(pageableRoutes => renderRoutes(pageableRoutes, selectedRouteId))
        .then(renderedRoutes => $('.routes-list')[0].innerHTML = renderedRoutes)

        // method for handling selection of route and page
        .then(() => {
            $('.btn-route').click(function (event) {

                let $departureStation = $('#departureStation');
                let $arrivalStation = $('#arrivalStation');
                let $departureTime = $('#departureTime');
                let $arrivalTime = $('#arrivalTime');
                let $trainName = $('#trainName');
                let $totalSeats = $('#totalSeats');
                let $pricePerSeat = $('#pricePerSeat');

                $departureStation.change();
                $arrivalStation.change();
                $departureTime.change();
                $arrivalTime.change();
                $trainName.change();
                $totalSeats.change();
                $pricePerSeat.change();

                let $selectedItem = $(event.currentTarget);
                $('#routeId')[0].value = $selectedItem.data('route-id');
                $departureStation[0].value = $selectedItem.data('route-departure-station-id');
                $arrivalStation[0].value = $selectedItem.data('route-arrival-station-id');
                $departureTime[0].value = $selectedItem.data('route-departure-time');
                $arrivalTime[0].value = $selectedItem.data('route-arrival-time');
                $trainName[0].value = $selectedItem.data('route-train-name');
                $totalSeats[0].value = $selectedItem.data('route-total-seats');
                $pricePerSeat[0].value = $selectedItem.data('route-price-per-seat');
            });

            $('.page-item').click(function (event) {
                event.preventDefault();
                $('.page-item').click(function () {
                    $(this).removeClass('active');
                });
                let $selectedItem = $(event.target);
                let pageNo = $selectedItem.data('page-no');
                refreshRoutesData(pageNo, null);
            })
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
            url: `/api/routes/${routeId}`,
            contentType: 'application/json'
        })
            .always(() => refreshRoutesData(1, +routeId))
            .fail(function (xhr, status, error) {
                let $error = $('.error-message');
                $error.removeClass('invisible');
                $error[0].innerText = xhr.responseText;
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
            url: '/api/routes',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(payload)
        })
            .then(response => refreshRoutesData(getTotalPages(), response.id))
            .fail(function (xhr, status, error) {
                let $error = $('.error-message');
                $error.removeClass('invisible');
                $error[0].innerText = xhr.responseText;
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
            url: `/api/routes/${routeId}`,
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(payload)
        })
            .always(function () {
                refreshRoutesData(getTotalPages(), +routeId);
            })
            .fail(function (xhr, status, error) {
                let $error = $('.error-message');
                $error.removeClass('invisible');
                $error[0].innerText = xhr.responseText;
            });
    });
}

function addListenerToHideErrorMessage() {
    let $departureStation = $("#departureStation");
    $departureStation.on('input', function () {
        $('.error-message').addClass('invisible');
    });
    $departureStation.on('change', function () {
        $('.error-message').addClass('invisible');
    });

    let $arrivalStation = $("#arrivalStation");
    $arrivalStation.on('input', function () {
        $('.error-message').addClass('invisible');
    });
    $arrivalStation.on('change', function () {
        $('.error-message').addClass('invisible');
    });

    let $departureTime = $("#departureTime");
    $departureTime.on('input', function () {
        $('.error-message').addClass('invisible');
    });
    $departureTime.on('change', function () {
        $('.error-message').addClass('invisible');
    });

    let $arrivalTime = $("#arrivalTime");
    $arrivalTime.on('input', function () {
        $('.error-message').addClass('invisible');
    });
    $arrivalTime.on('change', function () {
        $('.error-message').addClass('invisible');
    });

    let $trainName = $("#trainName");
    $trainName.on('input', function () {
        $('.error-message').addClass('invisible');
    });
    $trainName.on('change', function () {
        $('.error-message').addClass('invisible');
    });

    let $totalSeats = $("#totalSeats");
    $totalSeats.on('input', function () {
        $('.error-message').addClass('invisible');
    });
    $totalSeats.on('change', function () {
        $('.error-message').addClass('invisible');
    });

    let $pricePerSeat = $("#pricePerSeat");
    $pricePerSeat.on('input', function () {
        $('.error-message').addClass('invisible');
    });
    $pricePerSeat.on('change', function () {
        $('.error-message').addClass('invisible');
    });
}

function renderRoutes(pageableRoutes, selectedRouteId) {
    if (pageableRoutes.totalPages === 0) {
        return `<p class="fs-4 text-danger" style="text-align: center;">${i18n('noRoutes')}</p>`;
    }
    let routeList = pageableRoutes.content
        .map((route, index) => renderRouteLine(route, selectedRouteId, index))
        .reduce((a, b) => a + b, '');
    let pageBar = renderPageBar(pageableRoutes.currentPage, pageableRoutes.totalPages);
    return routeList + pageBar;
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
                            <div class="col-3">${formatDate(route.departureTime)}</div>
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
                                    <th>${formatDate(route.departureTime)}</th>
                                </tr>
                                <tr>
                                    <td>${i18n('arrivalDateTime')}</td>
                                    <th>${formatDate(route.arrivalTime)}</th>
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

function renderPageBar(currentPage, totalPages) {
    let pageBar = '<ul class="pagination justify-content-center" style="margin:20px 0">';
    pageBar += renderPageNumber(Math.max(currentPage - 1, 1), totalPages, '&laquo;', '');
    for (let i = 1; i <= totalPages; i++) {
        let activeClass = (i === currentPage) ? 'active' : '';
        pageBar += renderPageNumber(i, totalPages, i, activeClass);
    }
    pageBar += renderPageNumber(Math.min(currentPage + 1, totalPages), totalPages, '&raquo;', '');
    pageBar += '</ul>';
    return pageBar;
}

function renderPageNumber(pageNo, totalPages, text, activeClass) {
    return `<li class="page-item ${activeClass}">
                <a class="page-link" data-page-no="${pageNo}" data-page-total="${totalPages}">${text}</a>
            </li>`;
}

function getTotalPages() {
    let $pageElement = $('.page-item.active > .page-link')[0];
    return $($pageElement).data('page-total') || 1;
}

function formatDate(date) {
    return moment(date).format('DD.MM.YYYY HH:mm');
}
