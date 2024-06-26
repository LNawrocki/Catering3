<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="pl">
<head>
    <meta charset="UTF-8"/>
    <jsp:include page="../fragments/title.jsp"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style_admin_new_order_list.css">
</head>

<body>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/menu-admin.jsp"/>

<div class="main-block">
    <div class="day-block">
        <h3>Lista zamówień ${period} ( ${periodDate} )</h3>
    </div>
</div>

<div id="loadingIcon" style="display: none;">Ładowanie...</div>

<div class="main-block">
    <div class="day-block">
        <table>
            <div class="day">
                Ilość zamówień: ${newOrders.size()}
            </div>
            <tr>
                <form action="/admin/newOrder/searchIsPaid" method="post">
                    <td>
                        <div class="day">
                            <select name="searchIsPaid" style="width: 200px; text-align: center">
                                <option value="">nie opłac. / opłac.</option>
                                <option value="false">nie opłacone</option>
                                <option value="true">opłacone</option>
                            </select>
                            <button type="submit">Wyszukaj</button>
                        </div>
                    </td>
                </form>
                <form action="/admin/newOrder/searchDepartment" method="post">
                    <td>
                        <div class="day">
                            <select name="searchDepartmentId" style="text-align: center">
                                <option value="0">działy:</option>
                                <c:forEach var="department" items="${departments}">
                                    <option value="${department.id}">${department.name}</option>
                                </c:forEach>
                            </select>
                            <button type="submit">Wyszukaj</button>
                        </div>
                    </td>
                </form>
                <form action="/admin/newOrder/searchUsername" method="post">
                    <td>
                        <div class="day">
                            <input type="text" name="searchUsername" placeholder="login:"
                                   style="width: 150px; text-align: center">
                            <button type="submit">Wyszukaj</button>
                        </div>
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
<!--
                <td>
                    <div class="day">
                        <div>
                            ID
                        </div>
                    </div>
                </td>
--!>
                <td>
                    <div class="day">
                        <div>
                            Dzień
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day">
                        <div>
                            Dania
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day">
                        <div>
                            Cena
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day">
                        <div>
                            Zmiana
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day">
                        <div>
                            Do zapłaty
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day">
                        <div>
                            Zapłacono
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day">
                        <div>
                            Dane osoby
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day">
                        <div>
                            Akcja
                        </div>
                    </div>
                </td>
            </tr>
            <c:forEach var="newOrder" items="${newOrders}">
                <tr>
<!--
                    <td>
                        <div class="day">
                            <div>
                                    ${newOrder.id}
                            </div>
                        </div>
                    </td>
--!>
                    <td>
                        <div class="day">
                            <div>
                                Poniedziałek<br>
                                Wtorek<br>
                                Środa<br>
                                Czwartek<br>
                                Piątek<br>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="day">
                            <div>
                                    ${newOrder.mealMon}. ${newOrder.mealMonName}<br>
                                    ${newOrder.mealTue}. ${newOrder.mealTueName}<br>
                                    ${newOrder.mealWed}. ${newOrder.mealWedName}<br>
                                    ${newOrder.mealThu}. ${newOrder.mealThuName}<br>
                                    ${newOrder.mealFri}. ${newOrder.mealFriName}<br>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="day">
                            <div>
                                    ${newOrder.priceMon} zł<br>
                                    ${newOrder.priceTue} zł<br>
                                    ${newOrder.priceWed} zł<br>
                                    ${newOrder.priceThu} zł<br>
                                    ${newOrder.priceFri} zł<br>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="day">
                            <div>
                                    ${newOrder.shiftMon}<br>
                                    ${newOrder.shiftTue}<br>
                                    ${newOrder.shiftWed}<br>
                                    ${newOrder.shiftThu}<br>
                                    ${newOrder.shiftFri}<br>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="day">
                            <div>
                                    ${newOrder.toPay} zł
                            </div>
                        </div>
                    </td>
                    <td>
                        <c:if test="${newOrder.isPaid == false}">
                        <div class="day" style="box-shadow: 0px 0px 15px rgb(229,62,62) inset;">
                            </c:if>
                            <c:if test="${newOrder.isPaid == true}">
                            <div class="day" style="box-shadow: 0px 0px 15px rgb(166,229,135) inset;">
                                </c:if>
                                <div>
                                    <c:choose>
                                        <c:when test="${newOrder.isPaid eq true}">
                                            TAK
                                        </c:when>
                                        <c:when test="${newOrder.isPaid eq false}">
                                            NIE
                                        </c:when>
                                    </c:choose>
                                    <form method="post" action="/admin/newOrder/list/paid">
                                        <input name="userIdUpdate" value="${newOrder.user.userId}" hidden>
                                        <c:choose>
                                            <c:when test="${newOrder.isPaid eq false}">
                                                <div>
                                                    <button id="loadingButton" type="submit" name="paid" value="true"
                                                            style="border-radius: 10px">
                                                        wpłata gotówki
                                                    </button>
                                                    <div id="loadingIcon" style="display: none;">Ładowanie...</div>
                                                    <br>
                                                </div>
                                            </c:when>
                                            <c:when test="${newOrder.isPaid eq true}">
                                                <div>
                                                    <button id="loadingButton" type="submit" name="paid" value="false"
                                                            style="border-radius: 10px">
                                                        zwrot gotówki
                                                    </button>
                                                </div>
                                            </c:when>
                                        </c:choose>
                                    </form>
                                    <form method="get" action="/admin/newOrder/list/settlement">
                                        <c:choose>
                                        <c:when test="${newOrder.isPaid eq false}">
                                        <div>
                                            <input type="text" name="newOrderUserId" value="${newOrder.user.userId}"
                                                   hidden="hidden">
                                                <%--                                            <input type="text" name="toPay" value="${newOrder.toPay}" hidden="hidden">--%>
                                            <button id="loadingButton" type="submit" style="border-radius: 10px">rozlicz</button>
                                        </div>
                                        </c:when>
                                        <c:when test="${newOrder.isPaid eq true}">
                                        <div>
                                            <input type="text" name="newOrderUserId" value="${newOrder.user.userId}"
                                                   hidden="hidden">
                                                <%--                                            <input type="text" name="toPay" value="${newOrder.toPay}" hidden="hidden">--%>
                                            <button id="loadingButton" type="submit" style="border-radius: 10px">cofnij rozliczenie</button>
                                        </div>
                                        </c:when>
                                        </c:choose>
                                    </form>
                                </div>
                            </div>

                    </td>
                    <td>
                        <div class="day">
                            <div>
                                Login: ${newOrder.user.username}<br>
                                ID: ${newOrder.user.userId}<br>
                                    ${newOrder.user.firstName}
                                    ${newOrder.user.lastName}<br>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="day">
                            <div>
                                <a href="/admin/newOrderList/delete?id=${newOrder.id}" style="color: red">Usuń</a>
                            </div>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
<div class="main-block">
    <div class="day-block">
                    <div class="day">
                        Łączna kwota: ${priceSum} zł || 1 zmiana: ${firstShiftPriceSum} zł || 2 zmiana: ${secondShiftPriceSum} zł
                    </div>
    </div>
<%--</div>--%>

<%--<div class="main-block">--%>
    <div class="day-block">
        <div class="day" style="box-shadow: 0px 0px 15px rgb(166,229,135) inset;">
            Łączna kwota opłaconych: ${paidSum} zł || 1 zmiana: ${firstShiftPaidSum} zł || 2 zmiana: ${secondShiftPaidSum} zł
        </div>
    </div>
<%--</div>--%>
<%--<div class="main-block">--%>
    <div class="day-block">
        <div class="day" style="box-shadow: 0px 0px 15px rgb(229,62,62) inset;">
            Łączna kwota nieopłaconych: ${notPaidSum} zł || 1 zmiana: ${firstShiftNotPaidSum} zł || 2 zmiana: ${secondShiftNotPaidSum} zł
        </div>
    </div>
</div>
<jsp:include page="../fragments/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/app1.js"></script>
</body>
</html>
