$(document).ready(function () {

// method for handling button to buy a ticket
    $('.btn-buy').click(function (event) {
        event.preventDefault();
        let pathname = window.location.pathname;
        let routeId = pathname.substring(pathname.lastIndexOf('/') + 1);
        $.ajax({
            type: 'POST',
            url: '/api/tickets?routeId=' + routeId,
            contentType: 'application/json',
        })
            //todo - add confirmation page
            .then(response => alert(i18n('purchaseSuccess')))
            .fail(function (xhr, status, error) {
                $('.text-danger').removeClass('invisible');
                $('.text-danger')[0].innerText = xhr.responseText;
            });
    });
});