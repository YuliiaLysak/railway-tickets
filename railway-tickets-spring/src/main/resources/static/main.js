$(document).ready(function () {
    $('.search-form').submit(function (event) {
        event.preventDefault();
        var data = $('.search-form').serialize();
        let departureCity = $('input[name="departureCity"]')[0].value;
        let arrivalCity = $('input[name="arrivalCity"]')[0].value;
        let departureDate = $('input[name="departureDate"]')[0].value;
        console.log(data);
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
                             ${route.totalSeats}
                             ${route.availableSeats}
                         </div>
                         <div class="col-sm">${route.pricePerSeat}</div>
                         <div class="col-sm">
                             <button type="button" class="btn btn-info">Buy</button>
                         </div>
                     </div>
                 </div>
             </li>`;
}