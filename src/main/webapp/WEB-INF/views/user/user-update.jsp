<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="pl">
<head>
    <meta charset="UTF-8"/>
    <jsp:include page="../fragments/title.jsp"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style_admin_update.css">
</head>

<body>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/menu-user.jsp"/>

<form:form action="/user/update" method="post" modelAttribute="user">
    <div class="main-block">
        <div class="login-row">
            <label for="userId">ID użytkownika:
                <form:input path="userId" readonly="true"/>
            </label>
        </div>
        <div class="login-row">
            <label for="firstName">Imię:
                <form:input path="firstName" readonly="true"/>
            </label>
        </div>
        <div class="login-row">
            <label for="lastName">Nazwisko:
                <form:input path="lastName" readonly="true"/>
            </label>
        </div>
        <div class="login-row">
            <label for="department">Dział:
                <input name="department" value="${user.department.id}" readonly="true"
                       hidden="hidden">${user.department.name}
            </label>
        </div>
        <div class="login-row">
            <label for="email">e-mail:
                <form:input path="email"/>
            </label>
        </div>
        <div class="login-row">
            <label for="username">Login:
                <form:input path="username" readonly="true"/>
            </label>
        </div>
        <div class="login-row" style="color: red; font-weight: bold">
            <label for="username">Przed wpisaniem hasła wyczyść całe pole z hasłem (usuń wszystkie kropki)
            </label>
        </div>
        <div class="login-row">
            <label for="password">Hasło:
                <form:input path="password" type="password" name="password" size="50"/> (wymagane)
                <form:errors path="password"/>
            </label>
        </div>
        <div class="login-row">
            <form:input path="role" value="${user.role}" hidden="hidden"/>
            <form:input path="active" value="${user.active}" hidden="hidden"/>
            <form:input path="credit" hidden="hidden"/>
            <form:button id="loadingButton">Zapisz zmiany</form:button>
            <div id="loadingIcon" style="display: none;">Ładowanie...</div>
        </div>
    </div>
</form:form>

<jsp:include page="../fragments/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/app1.js"></script>

</body>
</html>
