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

<div class="main-block">
    <div class="login-row">
        Rozliczasz konto: ${user.firstName} ${user.lastName}  ( ID: ${user.userId} )</br>
        Bierzący stan konta wynosi: ${user.credit} zł
    </div>
</div>

<div id="loadingIcon" style="display: none;">Ładowanie...</div>

<form:form method="post">
    <div class="main-block">
        <div class="login-row"
             style="display: flex; flex-direction: row; width: 800px; justify-content: space-around; align-self: center">
            <div>
                <label> Do zapłaty: ${newOrderByUserId.toPay} zł
                    <input name="newOrderUserId" value="${user.userId}" hidden="hidden">
                </label>
            </div>
            <c:if test="${newOrderByUserId.isPaid == true}">
            <div>
                <label>
                    <button id="loadingButton" type="submit" name="settlementOperation" value="add" style="box-shadow: 0px 0px 15px rgb(229,62,62) inset;">
                        << cofnij rozliczenie >>
                    </button>
                </label>
            </div>
            </c:if>
            <c:if test="${user.credit >= newOrderByUserId.toPay && newOrderByUserId.isPaid == false}">
                <div>
                    <label>
                        <button id="loadingButton" type="submit"  name="settlementOperation" value="subtract" style="box-shadow: 0px 0px 15px rgb(166,229,135) inset;">
                            >> rozlicz <<
                        </button>
                    </label>
                </div>
            </c:if>
            <c:if test="${user.credit < newOrderByUserId.toPay && newOrderByUserId.isPaid == false}">
                <div style="color: #FF0000; font-weight: bold">
                    Niewystarczające środki na koncie
                </div>
            </c:if>
        </div>
        <div class="login-row">
            rozlicz - pobiera kwotę zamówienia z konta użytkownika i oznacza zamówienie jako połacone </br>
            cofnij rozliczenie - zwraca kwotę zamówienia na konto użytkownika i oznacza zamówienie jako nieopłacone
        </div>
    </div>
</form:form>
<jsp:include page="../fragments/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/app1.js"></script>
</body>
</html>
