<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>--%>
<html lang="pl">
<head>
    <meta charset="UTF-8"/>
    <jsp:include page="../fragments/title.jsp"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style_admin_config.css">
</head>

<body>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/menu-admin.jsp"/>
<form:form method="get" action="/admin/newOrder/list">
<div class="main-block">
    <div class="login-row">
        Potwierdzenie rozliczenia / cofnięcia rozliczenia obiadu dla: ${user.firstName} ${user.lastName}, </br>
        Stan konta po operacji wynosi: ${user.credit} zł
    </div>
</div>
    <div class="main-block">
        <c:if test="${msg != ''}">
        <div class="login-row" style=" color: red; font-weight: bold; display: flex; flex-direction: row; width: 1000px; justify-content: space-around; align-self: center">
                ${msg}
        </div>
        </c:if>
        <div class="login-row" style="display: flex; flex-direction: row; width: 1000px; justify-content: space-around; align-self: center">
            <div>
                <label>
                    <button id="loadingButton" style="box-shadow: 0px 0px 15px rgb(166,229,135) inset;"> Wróć do listy zamówień </button>
                </label>
            </div>
            <div id="loadingIcon" style="display: none;">Ładowanie...</div>
        </div>
    </div>
</form:form>
<jsp:include page="../fragments/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/app1.js"></script>
</body>
</html>
