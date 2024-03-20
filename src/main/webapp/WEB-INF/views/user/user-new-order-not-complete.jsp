<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>--%>
<html lang="pl">
<head>
    <meta charset="UTF-8"/>
    <jsp:include page="../fragments/title.jsp"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style_user_home_editmode.css">
</head>

<body>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/menu-user.jsp"/>

<div class="main-block">
    <div class="login-row">
        <div class="day">
            Witaj ${firstName} ${lastName} ( ID: ${user.userId} )
        </div>
    </div>
    <div class="main-block">
        <div class="day-block">
            <div class="day" style="color: red; font-size: larger; font-weight: bold">
                Możliwość składania zamówień właśnie została wyłączona.<br>
                Twoje zamówienie nie zostało złożone.<br>
                Możesz jeszcze spróbować bezpośrednio u osoby obsługującej zamówienia.
            </div>
        </div>
    </div>
</div>
<jsp:include page="../fragments/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/app1.js"></script>
</body>
</html>
