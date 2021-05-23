<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>

<html xmlns="http://www.w3.org/1999/xhtml" lang="${sessionScope.locale}">
<head>

    <!-- Required bootstrap meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6"
          crossorigin="anonymous">

    <!-- JQuery -->
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery-i18n-properties@1.2.7/jquery.i18n.properties.min.js"></script>

    <title>
        <fmt:message key="main"/>
    </title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/style.css"/>

    <!--    Script for i18n in js files-->
    <script>
        //Get default language
        var LANG = '${sessionScope.locale}';
        //initialization i18n plug-in unit
        $.i18n.properties({
            path: '/i18n/',
            name: 'messages',
            language: LANG,
            mode: 'both',
            async: false
        });

        //initialization i18n function
        function i18n(msgKey) {
            return $.i18n.map[msgKey];
        }
    </script>

</head>
<body>
<!--todo - change links in navbar-->
<nav class="navbar navbar-expand-lg navbar-light" style="background: #e5e5e5">
    <div class="container-fluid">
        <!--        TODO - ADD IMG  -->
        <!--        <img src="/img/railway.png" alt="railway">-->
        <a class="navbar-brand" href="${pageContext.request.contextPath}/"><fmt:message key="railwayTickets"/></a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">

                <c:if test="${not empty sessionUser}">
                    <c:if test="${sessionUser.isAdmin()}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/admin/stations"><fmt:message
                                    key="stations"/></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/admin/routes"><fmt:message
                                    key="routes"/></a>
                        </li>
                    </c:if>
                </c:if>
            </ul>

            <c:if test="${not empty sessionUser}">
                <div class="me-3">
                    <span>${sessionUser.firstName}</span>
                    <span>${sessionUser.lastName}</span>
                </div>
            </c:if>

            <c:if test="${empty sessionUser}">
                <a href="${pageContext.request.contextPath}/login" class="me-3">
                    <button class="btn btn-outline-success" type="submit">
                        <fmt:message key="signin"/>
                    </button>
                </a>
                <a href="${pageContext.request.contextPath}/registration" class="me-3">
                    <button class="btn btn-success" type="submit">
                        <fmt:message key="signup"/>
                    </button>
                </a>
            </c:if>

            <c:if test="${not empty sessionUser}">
                <form action="${pageContext.request.contextPath}/logout" method="get">
                    <button class="btn btn-success" type="submit">
                        <fmt:message key="signout"/>
                    </button>
                </form>
            </c:if>
        </div>
    </div>

    <!--TODO - add image for the language-->
    <!--Start of the button block for changing languages-->
    <div class="btn-group">
        <button type="button" class="btn btn-danger dropdown-toggle"
                data-bs-toggle="dropdown" aria-expanded="false">
            <fmt:message key="language"/>
        </button>
        <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="?lang=en">
                <!--                <img src="static/img/english.png" width="30">-->
                <fmt:message key="en"/></a>
            </li>
            <li><a class="dropdown-item" href="?lang=ua">
                <!--                <img src="static/img/ukrainian.png" width="30">-->
                <fmt:message key="ua"/></a>
            </li>
        </ul>
    </div>
    <!--End of the button block for changing languages-->
</nav>


<!--Start of the search widget -->
<div class="signup-form">
    <form action="#" method="post" class="search-form">
        <h3><fmt:message key="where"/></h3>
        <br>

        <fmt:message key="departureCity" var="departureCity"/>
        <fmt:message key="arrivalCity" var="arrivalCity"/>

        <div class="form-group">
            <div class="input-group">
                <input type="text" class="form-control" name="departureCity" placeholder="${departureCity}"
                       required="required">
            </div>
        </div>
        <div class="form-group">
            <div class="input-group">
                <input type="text" class="form-control" name="arrivalCity" placeholder="${arrivalCity}"
                       required="required">
            </div>
        </div>
        <div class="form-group">
            <div class="input-group">
                <input type="date" class="form-control" name="departureDate" placeholder="Departure date"
                       required="required">
            </div>
        </div>
        <div class="form-group d-grid gap-2">
            <button type="submit" class="btn btn-primary btn-block btn-lg"><fmt:message key="search"/></button>
        </div>
    </form>
</div>
<!--End of the search widget -->

<!-- Div container which will be filled with home.main.js -->
<div class="search-results"></div>


<!-- Bootstrap JavaScript Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf"
        crossorigin="anonymous">
</script>

<script src="${pageContext.request.contextPath}/static/home.main.js"></script>

</body>
</html>