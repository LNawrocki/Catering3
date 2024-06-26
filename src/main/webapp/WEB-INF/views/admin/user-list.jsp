<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="pl">
<head>
    <meta charset="UTF-8"/>
    <jsp:include page="../fragments/title.jsp"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style_user_list.css">
</head>

<body>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/menu-admin.jsp"/>

<div class="main-block">
    <div class="day-block">
        <table>
            <tr>
                <td>
                    <div class="day">
                        Znaleziono: ${usersList.size()}
                    </div>
                </td>
                <form action="/admin/userList/searchId" method="post">
                    <td>
                        ID: <input type="number" name="searchId">
                        <button id="loadingButton" type="submit">Wyszukaj</button>
                    </td>
                </form>
                <form action="/admin/userList/searchUsername" method="post">
                    <td>
                        Login: <input type="text" name="searchUsername">
                        <button id="loadingButton" type="submit">Wyszukaj</button>
                    </td>
                </form>
                <form action="/admin/userList/searchDepartment" method="post">
                    <td>
                        Dział: <select name="searchDepartmentId">
                        <c:forEach var="department" items="${departments}">
                            <option value="${department.id}">${department.name}</option>
                        </c:forEach>
                    </select>
                        <button id="loadingButton" type="submit">Wyszukaj</button>
                    </td>
                </form>
                <form action="/admin/userList/searchClean" method="post">
                    <td>
                        <button id="loadingButton" type="submit" style="background: lime">Wyczyść</button>
                    </td>
                </form>
            </tr>
        </table>
    </div>
</div>
<div class="main-block">
    <div class="day-block">
        <table>
            <tr>
                <td>
                    <div class="day" style="color: darkorange; font-weight: bold;">
                        <div>
                            ID
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day" style="color: darkorange; font-weight: bold;">
                        <div>
                            Login
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day" style="color: darkorange; font-weight: bold;">
                        <div>
                            Imię
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day" style="color: darkorange; font-weight: bold;">
                        <div>
                            Nazwisko
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day" style="color: darkorange; font-weight: bold;">
                        <div>
                            Dział
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day" style="color: darkorange; font-weight: bold;">
                        <div>
                            Stan
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day" style="color: darkorange; font-weight: bold;">
                        <div>
                            Stan konta
                        </div>
                    </div>
                </td>
                <td colspan="2">
                    <div class="day" style="color: darkorange; font-weight: bold;">
                        <div>
                            Akcja
                        </div>
                    </div>
                </td>
            </tr>
            <c:forEach var="user" items="${usersList}">
                <tr>
                    <td>
                        <div class="day">
                            <div>
                                    ${user.userId}
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="day">
                            <div>
                                    ${user.username}
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="day">
                            <div>
                                    ${user.firstName}
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="day">
                            <div>
                                    ${user.lastName}
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="day">
                            <div>
                                    ${user.department.name}
                            </div>
                        </div>
                    </td>
                    <td>
                        <c:if test="${user.active == false}">
                        <div class="day" style="box-shadow: 0px 0px 15px rgb(229,62,62) inset;">
                            </c:if>
                            <c:if test="${user.active == true}">
                            <div class="day" style="box-shadow: 0px 0px 15px rgb(166,229,135) inset;">
                                </c:if>
                                <div>
                                    <c:choose>
                                        <c:when test="${user.active eq true}">
                                            Aktywne
                                        </c:when>
                                        <c:when test="${user.active eq false}">
                                            Nieaktywne
                                        </c:when>
                                    </c:choose>
                                </div>
                            </div>
                    </td>
                    <td>
                        <div class="day">
                            <div>
                                    ${user.credit} zł
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="day">
                            <div style="display: flex; flex-direction: row; justify-content: space-between; width: 400px">
                                <form action="/admin/userUpdate" method="get"
                                      style="padding: 1px; margin: 1px">
                                    <button id="loadingButton" name="editUserId" value="${user.userId}"
                                            style="font-size: medium; border-radius: 5px; border-width: 1px; color: blueviolet; font-weight: bold">
                                        Edytuj
                                    </button>
                                    <div id="loadingIcon" style="display: none;">Ładowanie...</div>

                                </form>
                                <form action="/admin/deleteUser/confirm" method="post"
                                      style="padding: 1px; margin: 1px">
                                    <button id="loadingButton" name="deleteUserId" value="${user.userId}"
                                            style="font-size: medium; border-radius: 5px; border-width: 1px; color: red; font-weight: bold">
                                        Usuń
                                    </button>
                                </form>

                                <form action="/admin/financialAccount" method="GET"
                                      style="padding: 1px; margin: 1px">
                                    <button id="loadingButton" name="userId" value="${user.userId}"
                                            style="font-size: medium; border-radius: 5px; border-width: 1px; color: limegreen; font-weight: bold">
                                        Stan konta
                                    </button>
                                </form>

                                <form action="/admin/newOrder/orderForUser" method="GET"
                                      style="padding: 1px; margin: 1px">
                                    <button id="loadingButton" name="orderUserId" value="${user.userId}"
                                            style="font-size: medium; border-radius: 5px; border-width: 1px; font-weight: bold">
                                        Zamów
                                    </button>
                                </form>

                            </div>
                        </div>
                    </td>
                    <td>
                        <c:if test="${user.email.length() > 0}">
                            <form action="/admin/userList/sendMessage" method="GET"
                                  style="padding: 1px; margin: 1px">
                                <button id="loadingButton" name="userId" value="${user.userId}"
                                        style="font-size: medium; border-radius: 5px; border-width: 1px; color: #ff0000; font-weight: bold">
                                    Wiadomość
                                </button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td>
                    <div class="day">
                        <div>
                            Suma wszystkich stanów kont:
                        </div>
                    </div>
                </td>
                <td></td>
                <td>
                    <div class="day">
                        <div>
                            ${accountsCreditSum} zł
                        </div>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</div>
<div class="main-block">
    <div class="day-block">
        <table>
            <tr>
                <form action="/admin/getUsersListInXls" method="post">
                    <td>
                        <button type="submit">Pobierz listę wszystkich użytkowników</button>
                    </td>
                    <div id="loadingIcon" style="display: none;">Ładowanie...</div>
                </form>
            </tr>
        </table>
    </div>
</div>
<jsp:include page="../fragments/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/app1.js"></script>
</body>
</html>
