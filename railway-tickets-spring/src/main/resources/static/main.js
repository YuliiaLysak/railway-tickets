$(document).ready(function () {

    $('.search-form').submit(function (event) {
        event.preventDefault();
        let departureCity = $('input[name="departureCity"]')[0].value;
        let arrivalCity = $('input[name="arrivalCity"]')[0].value;
        let departureDate = $('input[name="departureDate"]')[0].value;
        let payload = {
            "departureStation": {
                "city": departureCity
            },
            "arrivalStation": {
                "city": arrivalCity
            },
            "departureDate": departureDate
        };
        $.ajax({
            type : 'POST',
            url: '/api/routes/search',
            contentType: 'application/json',
            dataType : 'json',
            data : JSON.stringify(payload)
        })
            .then(response => renderSearchResult(response))
            .then(it => $('.search-results')[0].innerHTML = it)

            // method for handling button to buy a ticket
            .then(it => {
                $('.buy-btn').click(function (event) {
                    event.preventDefault();
                    let routeId = $(event.target).data('route-id');
                    $.ajax({
                        type : 'POST',
                        url: '/api/tickets?routeId=' + routeId,
                        contentType: 'application/json',
                    })
                        .then(response => alert('You have successfully bought the ticket'));
                });
            });
    });
});

function renderSearchResult(routes) {
    if (routes.length === 0) {
        return '<p class="fs-2 text-danger" style="text-align: center;">No available routes</p>';
    }
    return '<ul class="list-group">'
        + routes.map(route => renderSearchResultLine(route))
            .reduce((a, b) => a + b, '')
        + '</ul>';
}

function renderSearchResultLine(route) {
    return `<li class="list-group-item">
                <div class="container">
                     <div class="row">
                         <div class="col-sm">${route.trainName}</div>
                         <div class="col-sm">
                             ${route.departureStationName}
                             ${route.arrivalStationName}
                         </div>
                         <div class="col-sm">
                             ${route.departureDateTime}
                             ${route.arrivalDateTime}
                         </div>
                         <div class="col-sm">${route.duration}</div>
                         <div class="col-sm">
                             Available seats: ${route.availableSeats} / ${route.totalSeats}
                         </div>
                         <div class="col-sm">${route.pricePerSeat}</div>
                         <div class="col-sm">${renderBuyButton(route)}</div>
                     </div>
                 </div>
             </li>`;
}

function renderBuyButton(route) {
    return `<button type="button" 
                    class="buy-btn btn btn-info" 
                    data-route-id="${route.routeId}">Buy</button>`;
}