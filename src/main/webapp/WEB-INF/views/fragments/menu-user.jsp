<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="main-menu-block">
    <c:if test="${pageId == 2}">
    <div class="menu-links" style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
        </c:if>
        <c:if test="${pageId != 2}">
        <div class="menu-links">
            </c:if>
                <a href="${pageContext.request.contextPath}/user/home">Nowe Menu</a>
            </div>
            <c:if test="${pageId == 3}">
            <div class="menu-links" style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
                </c:if>
                <c:if test="${pageId != 3}">
                <div class="menu-links">
                    </c:if>
                <a href="${pageContext.request.contextPath}/user/newOrder">Zamów obiad</a>
            </div>
                    <c:if test="${pageId == 4}">
                    <div class="menu-links" style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
                        </c:if>
                        <c:if test="${pageId != 4}">
                        <div class="menu-links">
                            </c:if>
                <a href="${pageContext.request.contextPath}/user/actualOrder">Sprawdź obiad</a>
            </div>
                            <c:if test="${pageId == 5}">
                            <div class="menu-links" style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
                                </c:if>
                                <c:if test="${pageId != 5}">
                                <div class="menu-links">
                                    </c:if>
                <a href="${pageContext.request.contextPath}/user/update?editUserId=${user.userId}">Zmiana danych użytkownika</a>
            </div>
            <div class="menu-links">
                <a href="${pageContext.request.contextPath}/logout">Wyloguj</a>
            </div>
        </div>
