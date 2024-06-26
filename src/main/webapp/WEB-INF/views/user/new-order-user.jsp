<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="pl">
<head>
    <meta charset="UTF-8"/>
    <jsp:include page="../fragments/title.jsp"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style_menu_order.css">
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

<form:form method="post" modelAttribute="newOrder">
    <div class="main-block">
        <div class="day-block">
            <form:hidden path="period"/>
            <form:hidden path="periodDate"/>
            <form:hidden path="user"/>
            <form:hidden path="qtyMon"/>
            <form:hidden path="qtyTue"/>
            <form:hidden path="qtyWed"/>
            <form:hidden path="qtyThu"/>
            <form:hidden path="qtyFri"/>
            <form:hidden path="priceMon"/>
            <form:hidden path="priceTue"/>
            <form:hidden path="priceWed"/>
            <form:hidden path="priceThu"/>
            <form:hidden path="priceFri"/>
            <form:hidden path="toPay"/>
            <form:hidden path="isPaid"/>

        </div>
    </div>
    <div class="main-block">
        <div class="day-block">
            <table>
                <tr style="font-weight: bold">
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
                                Zamówienie ${period} <br>
                                    ${periodDate}
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
                </tr>
                <tr>
                    <td>
                        <div class="day">
                            <div>
                                Poniedziałek
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="meal">
                            <div>
                                <form:select path="mealMon" items="${newMenuMonday}" itemLabel="mealName"
                                             itemValue="mealNo"/>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="meal">
                            <div>
                                <form:radiobutton path="shiftMon" value="1" checked="checked"/>1
                                <c:if test="${companyData.get('companyMarker') == 'S'}">
                                    <form:radiobutton path="shiftMon" value="2"/>2
                                </c:if>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="day">
                            <div>
                                Wtorek
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="meal">
                            <div>
                                <form:select path="mealTue" items="${newMenuTuesday}" itemLabel="mealName"
                                             itemValue="mealNo"/>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="meal">
                            <div>
                                <form:radiobutton path="shiftTue" value="1" checked="checked"/>1
                                <c:if test="${companyData.get('companyMarker') == 'S'}">
                                    <form:radiobutton path="shiftTue" value="2"/>2
                                </c:if>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="day">
                            <div>
                                Środa
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="meal">
                            <div>
                                <form:select path="mealWed" items="${newMenuWednesday}" itemLabel="mealName"
                                             itemValue="mealNo"/>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="meal">
                            <div>
                                <form:radiobutton path="shiftWed" value="1" checked="checked"/>1
                                <c:if test="${companyData.get('companyMarker') == 'S'}">
                                    <form:radiobutton path="shiftWed" value="2"/>2
                                </c:if>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="day">
                            <div>
                                Czwartek
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="meal">
                            <div>
                                <form:select path="mealThu" items="${newMenuThursday}" itemLabel="mealName"
                                             itemValue="mealNo"/>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="meal">
                            <div>
                                <form:radiobutton path="shiftThu" value="1" checked="checked"/>1
                                <c:if test="${companyData.get('companyMarker') == 'S'}">
                                    <form:radiobutton path="shiftThu" value="2"/>2
                                </c:if>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="day">
                            <div>
                                Piątek
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="meal">
                            <div>
                                <form:select path="mealFri" items="${newMenuFriday}" itemLabel="mealName"
                                             itemValue="mealNo"/>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="meal">
                            <div>
                                <form:radiobutton path="shiftFri" value="1" checked="checked"/>1
                                <c:if test="${companyData.get('companyMarker') == 'S'}">
                                    <form:radiobutton path="shiftFri" value="2"/>2
                                </c:if>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                    </td>
                    <td>
                        <div class="meal">
                            <button id="loadingButton" type="submit">Zamów</button>
                            <button type="reset">reset</button>
                            <div id="loadingIcon" style="display: none;">Ładowanie...</div>
                        </div>

                    </td>
                    <td></td>
                </tr>
            </table>
        </div>
    </div>

</form:form>
<jsp:include page="../fragments/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/app1.js"></script>
</body>
</html>