<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="pl">
<head>
    <meta charset="UTF-8"/>
    <jsp:include page="../fragments/title.jsp"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style_new_order_user_check.css">
</head>

<body>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/menu-user.jsp"/>

<div class="main-block">
    <div class="login-row">
        <div class="day">
            Witaj ${user.firstName} ${user.lastName} ( ID: ${user.userId} )
        </div>
        <div class="day">
            Stan konta: ${user.credit} zł
        </div>
        <c:if test="${receivables != 0}">
        <div class="day" style="color: red">
            </c:if>
            <c:if test="${receivables == 0}">
            <div class="day">
                </c:if>
                Należności: ${receivables} zł
            </div>
        </div>
    </div>
</div>
<div class="main-block">
    <div class="login-row">
        Twoje zamówienie ${period} ( ${periodDate} )
    </div>
</div>

<div class="main-block">
    <div class="day-block">
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
            <tr>
                <td>
                    <div class="day">
                        <div>
                            ${newOrder.id}
                        </div>
                    </div>
                </td>
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
                                </div>
                        </div>
                </td>
                <td>
                    <div class="day">
                        <div>
                            ID: ${newOrder.user.userId}<br>
                            ${newOrder.user.firstName}
                            ${newOrder.user.lastName}<br>
                            Login: ${newOrder.user.username}
                        </div>
                    </div>
                </td>
                <c:if test="${newOrder.isPaid == false}">
                    <td>
                        <div class="day">
                            <div>
                                <a href="/user/newOrder/delete?id=${newOrder.id}">Usuń</a>
                            </div>
                        </div>
                    </td>
                </c:if>
            </tr>
        </table>
    </div>
</div>
<jsp:include page="../fragments/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/app1.js"></script>
</body>
</html>
