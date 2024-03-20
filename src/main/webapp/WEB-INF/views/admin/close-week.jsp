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
    <div class="login-row">
        <p style="color: red; font-weight: bold">
            Zamykasz tydzień. Upewnij się, że pobrałeś plik z danymi.
        </p>
    </div>
</div>


<div class="main-block">
    <div class="login-row">
        Lista użytkowników | Lista dań | Nowe i bierzące zamówienia | Rozliczenie nowych zamówień
    </div>
    <div class="login-row">
        <form action="/admin/getDataBeforeCloseWeekInXl" method="post">
            <button>Pobierz plik Excel przed zamknięciem tygodnia</button>
        </form>
    </div>
    <div class="login-row">
        <form action="/admin/financial/">
            <button id="loadingButton" style="color: limegreen; font-weight: bold; font-size: large">Wróc do zakładki Rozliczenia
            </button>
        </form>
    </div>
    <div class="login-row">
        <form action="/admin/financial/closeWeek" method="post">
            <button id="loadingButton" name="confirm" value="true" style="color: red; font-weight: bold ; font-size: medium">Zamknij tydzień i rozlicz zamówienia</button>
            <div id="loadingIcon" style="display: none;">Ładowanie...</div>
        </form>
    </div>
</div>

<jsp:include page="../fragments/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/app1.js"></script>
</body>
</html>
