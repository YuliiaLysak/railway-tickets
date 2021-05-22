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
            .then(it => $('.search-results')[0].innerHTML = it);
    });
});

function renderSearchResult(routes) {
    if (routes.length === 0) {
        return `<p class="fs-2 text-danger" style="text-align: center;">No available routes</p>`;
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
                             <div class="row">${route.departureStationName}</div>
                             <div class="row">${route.arrivalStationName}</div>
                         </div>
                         <div class="col-sm">
                            <div class="row">${route.departureDateTime}</div>
                            <div class="row">${route.arrivalDateTime}</div>
                         </div>
                         <div class="col-sm">${route.duration}</div>
                         <div class="col-sm">
                            <div class="row">Available seats</div>
                            <div class="row">${route.availableSeats} / ${route.totalSeats}</div>
                         </div>
                         <div class="col-sm">${route.pricePerSeat}</div>
                         <div class="col-sm-auto">
                            <a href="/routes/${route.routeId}">
                                <button class="btn btn-success">Details</button>
                            </a>
                         </div>
                     </div>
                 </div>
             </li>`;
}