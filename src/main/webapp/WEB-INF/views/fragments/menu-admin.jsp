<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="main-menu-block">
    <c:if test="${pageId == 2}">
    <div class="menu-links" style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
        </c:if>
        <c:if test="${pageId != 2}">
        <div class="menu-links">
            </c:if>
            <a href="${pageContext.request.contextPath}/admin/home">Nowe Menu</a>
        </div>
        <c:if test="${pageId == 3}">
        <div class="menu-links" style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
            </c:if>
            <c:if test="${pageId != 3}">
            <div class="menu-links">
                </c:if>
                <a href="${pageContext.request.contextPath}/admin/newOrder">Zamów obiad</a>
            </div>
            <c:if test="${pageId == 4}">
            <div class="menu-links" style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
                </c:if>
                <c:if test="${pageId != 4}">
                <div class="menu-links">
                    </c:if>
                    <a href="${pageContext.request.contextPath}/admin/actualOrder">Sprawdź obiad</a>
                </div>
                <div class="menu-links">
                    <a href="${pageContext.request.contextPath}/logout">Wyloguj</a>
                </div>
            </div>
            <div class="main-menu-block">
                <c:if test="${pageId == 5}">
                <div class="menu-links" style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
                    </c:if>
                    <c:if test="${pageId != 5}">
                    <div class="menu-links">
                        </c:if>
                        <a href="${pageContext.request.contextPath}/admin/userList">Lista użytkowników</a>
                    </div>
                    <c:if test="${pageId == 6}">
                    <div class="menu-links" style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
                        </c:if>
                        <c:if test="${pageId != 6}">
                        <div class="menu-links">
                            </c:if>
                            <a href="${pageContext.request.contextPath}/admin/addUser">Dodaj użytkownika</a>
                        </div>
                        <c:if test="${pageId == 7}">
                        <div class="menu-links" style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
                            </c:if>
                            <c:if test="${pageId != 7}">
                            <div class="menu-links">
                                </c:if>
                                <a href="${pageContext.request.contextPath}/admin/department">Edycja działów</a>
                            </div>
                            <c:if test="${pageId == 8}">
                            <div class="menu-links" style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
                                </c:if>
                                <c:if test="${pageId != 8}">
                                <div class="menu-links">
                                    </c:if>
                                    <a href="${pageContext.request.contextPath}/admin/newMenu/edit">Edycja
                                        menu</a>
                                </div>
                                <c:if test="${pageId == 9}">
                                <div class="menu-links" style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
                                    </c:if>
                                    <c:if test="${pageId != 9}">
                                    <div class="menu-links">
                                        </c:if>
                                        <a href="${pageContext.request.contextPath}/admin/newOrder/list">Lista
                                            nowych
                                            zamówień</a>
                                    </div>
                                    <c:if test="${pageId == 10}">
                                    <div class="menu-links"
                                         style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
                                        </c:if>
                                        <c:if test="${pageId != 10}">
                                        <div class="menu-links">
                                            </c:if>
                                            <a href="${pageContext.request.contextPath}/admin/actualOrder/list">Lista
                                                zamówień</a>
                                        </div>
                                        <c:if test="${pageId == 11}">
                                        <div class="menu-links"
                                             style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
                                            </c:if>
                                            <c:if test="${pageId != 11}">
                                            <div class="menu-links">
                                                </c:if>
                                                <a href="${pageContext.request.contextPath}/admin/financial">Rozliczenia</a>
                                            </div>
                                            <c:if test="${pageId == 12}">
                                            <div class="menu-links"
                                                 style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
                                                </c:if>
                                                <c:if test="${pageId != 12}">
                                                <div class="menu-links">
                                                    </c:if>
                                                    <a href="${pageContext.request.contextPath}/admin/orderSummary">Wykaz
                                                        zamówień</a>
                                                </div>
                                                <c:if test="${pageId == 13}">
                                                <div class="menu-links"
                                                     style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
                                                    </c:if>
                                                    <c:if test="${pageId != 13}">
                                                    <div class="menu-links">
                                                        </c:if>
                                                        <a href="${pageContext.request.contextPath}/admin/config">Ustawienia</a>
                                                    </div>
                                                    <c:if test="${pageId == 14}">
                                                    <div class="menu-links"
                                                         style="box-shadow: 0px 0px 15px rgb(0, 0, 255) inset">
                                                        </c:if>
                                                        <c:if test="${pageId != 14}">
                                                        <div class="menu-links">
                                                            </c:if>
                                                            <a href="${pageContext.request.contextPath}/admin/price">Lista
                                                                cen</a>
                                                        </div>
                                                    </div>

