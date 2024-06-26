<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>--%>
<html lang="pl">
<head>
    <meta charset="UTF-8"/>
    <jsp:include page="../fragments/title.jsp"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/css/style_admin_user_add_info_exist.css">
</head>

<body>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/menu-admin.jsp"/>


<div class="main-block">
    <div class="main-block">
        <div class="day-block">
            <div class="day" style="color: red; font-size: x-large; font-weight: bold">
                ${msg}
            </div>
        </div>
    </div>
</div>
<div class="main-block">
    <div class="login-row">
        <div class="day">
            <a href="/admin/addUser">Ok</a>
        </div>
    </div>
</div>
<jsp:include page="../fragments/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/app1.js"></script>
</body>
</html>
