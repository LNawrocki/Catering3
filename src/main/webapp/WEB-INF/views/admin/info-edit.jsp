<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="pl">
<head>
    <meta charset="UTF-8"/>
    <jsp:include page="../fragments/title.jsp"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style_department-edit.css">
</head>

<body>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/menu-admin.jsp"/>

<div class="main-block">
    <div class="login-row">
        Informacje
    </div>
</div>


<form:form method="post" modelAttribute="info">
    <div class="main-block">
        <div class="login-row">
            <label>ID:
                <form:input type="number" path="infoId"/>
            </label>
        </div>
        <div class="login-row">
            <label for="infoContent">Treść:
                <form:textarea path="infoContent" rows="4" cols="120"/>
            </label>
        </div>
        <div class="login-row">
            <form:button id="loadingButton">Dodaj / Zastąp</form:button>
        </div>
        <div id="loadingIcon" style="display: none;">Ładowanie...</div>
    </div>
</form:form>

<div class="main-block">
    <div class="login-row">
        <table>
            <tr>
                <td>
                    <div class="day">
                        <div>
                            ID
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day">
                        <div>
                            Treść
                        </div>
                    </div>
                </td>
                <td>
                    Akcja
                </td>
            </tr>
            <c:forEach items="${informations}" var="information">
                <tr>
                    <td>
                        <div class="day">
                            <div>
                                <c:out value="${information.infoId}"/>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="day">
                            <div>
                                <c:out value="${information.infoContent}"/>
                            </div>
                        </div>
                    </td>
                    <td>
                        <form action="/admin/deleteInfo" method="post">
                            <button id="loadingButton" name="deleteInfoId" value="${information.infoId}"
                                    style="font-size: small; border-radius: 5px; border-width: 1px; color: red">Usuń
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>


<jsp:include page="../fragments/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/app1.js"></script>
</body>
</html>
