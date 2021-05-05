$(document).ready(function () {
    refreshRoutesData();

    // updateRoute();
    // deleteRoute();
    // addRoute();
});

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
                let $selectedItem = $(event.currentTarget);
                $('#routeId')[0].value = $selectedItem.data('route-id');
                $('#departureStationCity')[0].value = $selectedItem.data('route-departure-station-city');
                $('#departureStationName')[0].value = $selectedItem.data('route-departure-station-name');
                $('#arrivalStationCity')[0].value = $selectedItem.data('route-arrival-station-city');
                $('#arrivalStationName')[0].value = $selectedItem.data('route-arrival-station-name');
                $('#departureTime')[0].value = $selectedItem.data('route-departure-time');
                $('#arrivalTime')[0].value = $selectedItem.data('route-arrival-time');
                $('#trainName')[0].value = $selectedItem.data('route-train-name');
                $('#totalSeats')[0].value = $selectedItem.data('route-total-seats');
                $('#pricePerSeat')[0].value = $selectedItem.data('route-price-per-seat');
            });
        });
}

function renderRoutes(routes, selectedRouteId) {
    if (routes.length === 0) {
        return '<p class="fs-2 text-danger" style="text-align: center;">No available routes</p>';
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
                                data-route-departure-station-city="${route.departureStation.city}"
                                data-route-departure-station-name="${route.departureStation.name}"
                                data-route-arrival-station-city="${route.arrivalStation.city}"
                                data-route-arrival-station-name="${route.arrivalStation.name}"
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
                                    <td>Departure station</td>
                                    <th>${route.departureStation.city} (${route.departureStation.name})</th>
                                </tr>
                                <tr>
                                    <td>Arrival station</td>
                                    <th>${route.arrivalStation.city} (${route.arrivalStation.name})</th>
                                </tr>
                                <tr>
                                    <td>Departure time</td>
                                    <th>${route.departureTime}</th>
                                </tr>
                                <tr>
                                    <td>Arrival time</td>
                                    <th>${route.arrivalTime}</th>
                                </tr>
                                <tr>
                                    <td>Train name</td>
                                    <th>${route.trainName}</th>
                                </tr>
                                <tr>
                                    <td>Total seats</td>
                                    <th>${route.totalSeats}</th>
                                </tr>
                                <tr>
                                    <td>Price per seat</td>
                                    <th>${route.pricePerSeat}</th>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>`;
}