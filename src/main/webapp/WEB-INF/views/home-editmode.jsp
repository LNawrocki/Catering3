<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="pl">
<head>
    <meta charset="UTF-8"/>
    <jsp:include page="fragments/title.jsp"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style_home.css">
</head>

<body>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/menu.jsp"/>

<c:if test="${infos.size() > 0}">
    <div class="main-block">
        <div style="text-align: center; color: #000000; font-weight: bold">Ogłoszenia
        </div>
        <div class="day-block">
            <div class="day" style="width: 800px; word-wrap: break-word">
                <ol>
                    <c:forEach var="info" items="${infos}">
                        <li style="list-style-type: block">${info.infoContent}</li>
                    </c:forEach>
                </ol>
            </div>
        </div>
    </div>
</c:if>

<div class="main-block">
    <div class="main-block">
        <div class="day-block">
            <div class="day" style="color: red; font-size: larger; font-weight: bold">
                Nowa lista dań jest w trakcie przygotowywania... Proszę spróbować później.
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/app1.js"></script>
</body>
</html>
