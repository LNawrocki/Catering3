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
<jsp:include page="../fragments/menu-admin.jsp"/>


<form:form method="post" modelAttribute="user">
    <div class="main-block">
        <div class="login-row">
            <label for="userId">ID użytkownika:
                <form:input path="userId" readonly="true"/>
            </label>
        </div>
        <div class="login-row">
            <label for="firstName">Imię:
                <form:input path="firstName"/>
            </label>
        </div>
        <div class="login-row">
            <label for="lastName">Nazwisko:
                <form:input path="lastName"/>
            </label>
        </div>
        <div class="login-row">
            <label for="department">Dział:
                <form:select path="department" items="${departments}" itemLabel="name" itemValue="id"/>
            </label>
        </div>
        <div class="login-row">
            <label for="username">Login:
                <form:input path="username"/>
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
            <label for="email">e-mail:
                <form:input path="email"/>
            </label>
        </div>
        <div class="login-row">
            <label>Admin:
                <form:radiobutton path="role" value="ROLE_ADMIN"/>TAK
                <form:radiobutton path="role" value="ROLE_USER" checked="true"/>NIE
            </label>
        </div>
        <div class="login-row">
            <label>
                <form:radiobutton path="active" value="true"/>Użytkownik aktywny</br>
                <form:radiobutton path="active" value="false"/>Użytkownik nieaktywny
            </label>
        </div>
        <div class="login-row">
            <form:input path="credit" hidden="hidden" />
            <form:button id="loadingButton">Zapisz</form:button>
            <div id="loadingIcon" style="display: none;">Ładowanie...</div>
        </div>
    </div>
</form:form>

<jsp:include page="../fragments/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/app1.js"></script>
</body>
</html>
