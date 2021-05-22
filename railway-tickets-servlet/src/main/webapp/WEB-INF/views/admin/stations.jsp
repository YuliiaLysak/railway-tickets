<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<fmt:setLocale value="${param.lang}"/>
<fmt:setBundle basename="messages"/>

<html xmlns="http://www.w3.org/1999/xhtml" lang="${param.lang}">
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
        <fmt:message key="stations"/>
    </title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/style.css"/>

<%--    TODO - configure i18n for js in servlet--%>
<%--    <!--    Script for i18n in js files-->--%>
<%--    <script th:inline="javascript">--%>
<%--        //Get default language--%>
<%--        var LANG = [[${#locale.language}]];--%>
<%--        //initialization i18n plug-in unit--%>
<%--        $.i18n.properties({--%>
<%--            path: '/i18n/',--%>
<%--            name: 'messages',--%>
<%--            language: LANG,--%>
<%--            mode: 'both',--%>
<%--            async: false--%>
<%--        });--%>

<%--        //initialization i18n function--%>
<%--        function i18n(msgKey) {--%>
<%--            return $.i18n.map[msgKey];--%>
<%--        }--%>
<%--    </script>--%>

</head>
<body>

<!--Start of header element-->
<div class="container">
    <header class="d-flex flex-wrap justify-content-center py-3 mb-4 border-bottom">
        <a href="${pageContext.request.contextPath}/" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-dark text-decoration-none">
            <svg class="bi me-2" width="40" height="32">
                <use xlink:href="#bootstrap"></use>
            </svg>
            <span class="fs-4"><fmt:message key="railwayRoutes"/></span>
        </a>

        <ul class="nav nav-pills">
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/" class="nav-link active">
                    <fmt:message key="main"/>
                </a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/admin/stations" class="nav-link">
                    <fmt:message key="stations"/>
                </a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/admin/routes" class="nav-link">
                    <fmt:message key="routes"/>
                </a>
            </li>
            <!--TODO - add image for the language-->
            <!--Start of the button block for changing languages-->
            <li class="btn-group">
                <button type="button" class="btn btn-primary dropdown-toggle"
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
            </li>
            <!--End of the button block for changing languages-->
        </ul>
    </header>
</div>
<!--End of header element-->

<!--Start of list of stations-->
<div class="container px-4">
    <div class="row gx-5">
        <div class="col border bg-light">

            <!-- div container which will be filled with stations.home.main.js -->
            <div class="p-3 list-group text-center stations-list"></div>

        </div>

        <div class="col">
            <div class="p-4 border bg-light">
                <div class="row ms-1">
                    <fmt:message key="addStationData"/>
                </div>
                <br>
                <form class="row g-3 align-items-center">
                    <input type="hidden" id="stationId">

                    <div class="row">

                        <fmt:message key="city" var="city"/>
                        <fmt:message key="name" var="name"/>

                        <div class="col-5">
                            <label class="visually-hidden" for="stationCity">City</label>
                            <div class="input-group">
                                <input type="text" class="form-control" id="stationCity" placeholder="${city}">
                            </div>
                        </div>
                        <div class="col-5">
                            <label class="visually-hidden" for="stationName">Name</label>
                            <div class="input-group">
                                <input type="text" class="form-control" id="stationName" placeholder="${name}">
                            </div>
                        </div>
                    </div>

                    <div class="row pb-2 pt-1">
                        <span class="invisible text-danger">Error!</span>
                    </div>


                    <div class="row">
                        <div class="col-md-auto">
                            <button type="submit" class="btn btn-primary btn-save">
                                <fmt:message key="save"/>
                            </button>
                        </div>
                        <div class="col-md-auto">
                            <button type="submit" class="btn btn-danger btn-delete">
                                <fmt:message key="remove"/>
                            </button>
                        </div>
                        <div class="col-md-auto">
                            <button type="submit" class="btn btn-success btn-add">
                                <fmt:message key="add"/>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<!--End of list of stations-->

<!-- Bootstrap JavaScript Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf"
        crossorigin="anonymous">
</script>

<script src="${pageContext.request.contextPath}/static/admin/stations.main.js"></script>

</body>
</html>