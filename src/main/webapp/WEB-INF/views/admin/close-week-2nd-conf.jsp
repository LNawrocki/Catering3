<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="pl">
<head>
    <meta charset="UTF-8"/>
    <jsp:include page="../fragments/title.jsp"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style_user_auth.css">
</head>

<body>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/menu-admin.jsp"/>

<div class="main-block">
    <div class="login-row" style="color: #FF0000; display: flex; flex-direction: column; align-self: center">

        <span style="align-self: center; font-weight: bold">Zamykasz tydzień przy braku nowych zamówień.</span>
        <span style="align-self: center; font-weight: bold">Wygląda na to, że robisz to drugi raz z rzędu, co spowoduje wyzerowanie wszystkich bierzących zamówień</span>
        <span style="align-self: center; font-weight: bold">oraz list i wykazów dań wraz z rozliczeniami,</span>
        <span style="align-self: center; font-weight: bold">które prawdopodobnie będą potrzebne w rozliczeniach z dostawcą.</span>
        <span style="align-self: center; font-weight: bold; font-size: xx-large">Czy na pweno chcesz to zrobić?!</span>
    </div>
</div>


<div class="main-block">
    <div class="login-row">
        <form action="/admin/financial/">
            <button id="loadingButton" style="color: limegreen; font-weight: bold; font-size: xx-large">Anuluj i wróć do zakładki Rozliczenia
            </button>
        </form>
    </div>
    <div class="login-row">
        <form action="/admin/financial/closeWeek" method="post">
            <button id="loadingButton" name="confirm" value="true"
                    style="color: red; font-weight: bold ; font-size: medium">Zamknij ponownie tydzień i wyzeruj
                wszystkie listy i rozliczenia
            </button>
            <div id="loadingIcon" style="display: none;">Ładowanie...</div>
        </form>
    </div>
</div>

<jsp:include page="../fragments/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/app1.js"></script>
</body>
</html>
