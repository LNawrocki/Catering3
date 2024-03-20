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
<form:form method="post">
<div class="main-block">
    <div class="login-row">
        Zmieniasz stan konta dla: ${user.firstName} ${user.lastName}, </br>
        Bierzący stan konta wynosi: ${user.credit} zł
    </div>
</div>
    <div class="main-block">
        <div class="login-row" style="display: flex; flex-direction: row; width: 1000px; justify-content: space-around; align-self: center">
            <div>
                <label>
                    <button type="submit" name="moneyOperation" value="plus" style="box-shadow: 0px 0px 15px rgb(166,229,135) inset;"> >> Wpłać na konto <<
                    </button>
                </label>
            </div>
            <div>
                <input name="amount" type="number" min="0" step="0.01" pattern="^\d+(\.\d{2})?$" placeholder="0"> zł
                <input name="userId" hidden="hidden">
            </div>
            <div>
                <label>
                    <button type="submit" name="moneyOperation" value="minus" style="box-shadow: 0px 0px 15px rgb(229,62,62) inset;">
                        << Wypłać z konta >>
                    </button>
                </label>
            </div>
<%--            <div id="loadingIcon" style="display: none;">Ładowanie...</div>--%>
        </div>
    </div>
</form:form>
<jsp:include page="../fragments/footer.jsp"/>
<%--<script src="${pageContext.request.contextPath}/static/js/app1.js"></script>--%>
</body>
</html>
