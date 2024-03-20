<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<header class="header">
    <div class="name">
        <img src="${pageContext.request.contextPath}/static/logo.jpg" alt="Logo" height="100px">
    </div>

    <div class="info-contact info-adress">
        <h1>Adres: ${companyData.get('companyAddress')}</h1>
        <h1>Telefon:</h1>
        <h1>E-mail:</h1>
    </div>

<%--    <div class="info-contact info-adress">--%>
<%--        <h1>info line 1</h1>--%>
<%--        <h1>info line 2</h1>--%>
<%--        <h1>info line 3</h1>--%>
<%--    </div>--%>

</header>



