<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="pl">
<head>
    <meta charset="UTF-8"/>
    <jsp:include page="fragments/title.jsp"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style_department-edit.css">
</head>

<body>
<jsp:include page="fragments/header.jsp"/>
<%--<jsp:include page="fragments/menu-admin.jsp"/>--%>

<div class="main-block">
    <div class="login-row">
        Zlicz literki
    </div>
</div>

<form method="post" action="/zlicz">
    <div class="main-block">
        <div class="login-row">
            <label>Tutaj wklej
                <input name="zdanie" size="100"> (wymagane)
            </label>
        </div>
        <div class="login-row">
            <button id="loadingButton">Wyślij</button>
            <div id="loadingIcon" style="display: none;">Ładowanie...</div>

        </div>
    </div>
</form>
<jsp:include page="fragments/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/app1.js"></script>
</body>
</html>
