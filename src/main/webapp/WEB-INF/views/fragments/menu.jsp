<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="main-menu-block">
    <c:if test="${pageId == 20}">
    <div class="menu-links" style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
        </c:if>
        <c:if test="${pageId != 20}">
        <div class="menu-links">
            </c:if>
            <a href="${pageContext.request.contextPath}/rules">Regulamin</a>
        </div>
        <c:if test="${pageId == 0}">
        <div class="menu-links" style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
            </c:if>
            <c:if test="${pageId != 0}">
            <div class="menu-links">
                </c:if>
                <a href="${pageContext.request.contextPath}/">Nowe Menu</a>
            </div>
            <c:if test="${pageId == 1}">
            <div class="menu-links" style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
                </c:if>
                <c:if test="${pageId != 1}">
                <div class="menu-links">
                    </c:if>
                    <a href="${pageContext.request.contextPath}/login">Zaloguj</a>
                </div>
            </div>
