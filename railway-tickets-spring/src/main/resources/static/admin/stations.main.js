$(document).ready(function () {
    refreshStationData(1, null);

    updateStation();
    deleteStation();
    addStation();

    addListenerToHideErrorMessage();
});

function refreshStationData(pageNo, selectedStationId) {
    $.ajax({
        type: 'GET',
        url: `/api/stations?pageNo=${pageNo}`,
        contentType: 'application/json'
    })
        .then(pageableStations => renderStations(pageableStations, selectedStationId))
        .then(renderedStations => $('.stations-list')[0].innerHTML = renderedStations)

        // method for handling selection of station and page
        .then(() => {
            $('.list-group-item-action').click(function (event) {
                event.preventDefault();
                let $stationCity = $('#stationCity');
                let $stationName = $('#stationName');
                $stationCity.change();
                $stationName.change();
                $('.list-group-item-action').each(function () {
                    $(this).removeClass('active');
                });
                let $selectedItem = $(event.target);
                $selectedItem.addClass('active');
                $('#stationId')[0].value = $selectedItem.data('station-id');
                $stationCity[0].value = $selectedItem.data('station-city');
                $stationName[0].value = $selectedItem.data('station-name');
            });
            $('.page-item').click(function (event) {
                event.preventDefault();
                $('.page-item').click(function () {
                    $(this).removeClass('active');
                });
                let $selectedItem = $(event.target);
                let pageNo = $selectedItem.data('page-no');

                refreshStationData(pageNo, null);
            })
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
            url: `/api/stations/${stationId}`,
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(payload)
        })
            .always(function () {
                refreshStationData(getTotalPages(), +stationId);
            })
            .fail(function (xhr, status, error) {
                let $error = $('.error-message');
                $error.removeClass('invisible');
                $error[0].innerText = xhr.responseText;
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
            url: `/api/stations/${stationId}`,
            contentType: 'application/json'
        })
            .always(() => refreshStationData(1, +stationId))
            .fail(function (xhr, status, error) {
                let $error = $('.error-message');
                $error.removeClass('invisible');
                $error[0].innerText = xhr.responseText;
            });
    });
}

function addListenerToHideErrorMessage() {
    let $stationCity = $("#stationCity");
    $stationCity.on('input', function () {
        $('.error-message').addClass('invisible');
    });
    $stationCity.on('change', function () {
        $('.error-message').addClass('invisible');
    });

    let $stationName = $("#stationName");
    $stationName.on('input', function () {
        $('.error-message').addClass('invisible');
    });
    $stationName.on('change', function () {
        $('.error-message').addClass('invisible');
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
            url: '/api/stations',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(payload)
        })
            .then(response => refreshStationData(getTotalPages(), response.id))
            .fail(function (xhr, status, error) {
                let $error = $('.error-message');
                $error.removeClass('invisible');
                $error[0].innerText = xhr.responseText;
            });
    });
}

function renderStations(pageableStations, selectedStationId) {
    if (pageableStations.totalPages === 0) {
        return `<p class="fs-4 text-danger" style="text-align: center;">${i18n('noStations')}</p>`;
    }
    let stationList = pageableStations.content
        .map(station => renderStationLine(station, selectedStationId))
        .reduce((a, b) => a + b, '');
    let pageBar = renderPageBar(pageableStations.currentPage, pageableStations.totalPages);
    return stationList + pageBar;
}

function renderStationLine(station, selectedStationId) {
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
