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
        Regulamin
    </div>
</div>

<form:form method="post" modelAttribute="rule">
    <div class="main-block">
        <div class="login-row">
            <label>ID:
                <form:input type="number" path="ruleId"/>
            </label>
        </div>
        <div class="login-row">
            <label for="ruleContent">Nazwa:
                <form:textarea path="ruleContent" rows="4" cols="120"/>
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
            <c:forEach items="${rules}" var="rule1">
                <tr>
                    <td>
                        <div class="day">
                            <div>
                                <c:out value="${rule1.ruleId}"/>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="day">
                            <div>
                                <c:out value="${rule1.ruleContent}"/>
                            </div>
                        </div>
                    </td>
                    <td>
                        <form action="/admin/deleteRule" method="post">
                            <button id="loadingButton" name="deleteRuleId" value="${rule1.ruleId}"
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
