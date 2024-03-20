<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="pl">
<head>
    <meta charset="UTF-8"/>
    <jsp:include page="../fragments/title.jsp"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style_new_menu_edit.css">
</head>

<body>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/menu-admin.jsp"/>

<c:if test="${editDate == true}">
    <form:form action="/admin/newMenu/init" method="post" modelAttribute="newMenu">
        <div class="main-block">
            <div class="login-row">
                <h3>Nowe menu
                    <form:input path="period" value="${period}" size="15"/> np. na KW: 06 / na Poniedziałek
                    <form:input path="periodDate" value="${periodDate}" size="30"/> np. 2024-02-05 - 2024-02-09
                    <form:button id="loadingButton" style="background: lime">Wprowadź okres rozliczeniowy</form:button>
                </h3>
            </div>
            <div class="login-row" style="color: #FF0000; display: flex; flex-direction: column; align-self: center">
                <span style="align-self: center; font-weight: bold">Przed dodaniem posiłku wprowadź okres rozliczeniowy.</span>
                <span style="align-self: center">Jeśli będzie brakowało pozycji "Brak" w choć jednym dniu,</span>
                <span style="align-self: center">wszystkie pozycje "Brak" zostaną zastąpione domyślną wartością (nadchodzącym KW i zakresem dat).</span>
                <span style="align-self: center">Aby zmienić okres rozliczeniowy usuń dane z wszystkich dni, a następnie wprowadź nowy okres rozliczeniowy.</span>
                <span>&nbsp </span>
                <span style="align-self: center">Aby wprowadzić podwójne danie <strong>(np. 2x bułka....)</strong> przed nazwą dania wprowadź <strong>("2x " - 2x spacja)</strong> i podaj cenę za 2 dania czyli np. <strong>14zł</strong> (2 x 7zł = 14 zł).</span>
                <span style="align-self: center; font-weight: bold">Taki zapis gwarantuje prawidłowe zliczenie ilości posiłków oraz prawidłowy wykaz numerów użytkowników w podsumowaniu.</span>
                <span style="align-self: center">W taki sposób można obsłużyć dowolny rodzaj posiłku jako podwójny (i tylko podówjny).</span>
                <span style="align-self: center">Podwójne dania pojawiają się na listach jako osobne dania z osobnym numerem.</span>
            </div>
        </div>
    </form:form>
</c:if>
<c:if test="${editDate == false}">
    <div class="main-block">
        <div class="login-row">
            <h3>Nowe menu ${period} ( ${periodDate} )</h3>
        </div>
    </div>
</c:if>

<form:form action="/admin/newMenu/addMeal" method="post" modelAttribute="newMenu">

    <div class="main-block">
        <div class="login-row">
            <label for="mealNo">Numer potrawy:
                <form:input path="mealNo" value="${lastIndex}"/>
            </label>
        </div>
        <div class="login-row">
            <label>Nazwa:
                <form:input path="mealName" size="100"/>
            </label>
        </div>
        <div class="login-row">
            <label>Cena:
                <select name="mealPrice" style="font-size: 20px">
                    <c:forEach var="price" items="${prices}">
                        <option value="${price.price}">${price.price} zł</option>
                    </c:forEach>
                </select>
            </label>
        </div>
        <div class="login-row">
            <label>Dzień:
                <select name="dayId" style="font-size: 20px">
                    <option value="1" name="Poniedziałek">Poniedziałek</option>
                    <option value="2" name="Wtorek">Wtorek</option>
                    <option value="3" name="Środe">Środe</option>
                    <option value="4" name="Czwartek">Czwartek</option>
                    <option value="5" name="Piątek">Piątek</option>
                </select>
            </label>
        </div>

        <div class="login-row">
            <form:input path="period" value="${period}" hidden="hidden"/>
            <form:input path="periodDate" value="${periodDate}" hidden="hidden"/>
            <form:button id="loadingButton">Dodaj</form:button>
            <div id="loadingIcon" style="display: none;">Ładowanie...</div>

        </div>
    </div>
</form:form>

<div class="main-block">
    <div class="day-block">
        <table>
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
                        <ul>
                            <c:forEach var="mealMonday" items="${mealsMonday}">
                                <li> ${mealMonday.mealNo} ${mealMonday.mealName} ${mealMonday.mealPrice} zł
                                    <c:if test="${deleteButtonVisible == true}">
                                        <form action="/admin/newMenu/deleteMeal" method="post">
                                            <input name="mealNo" value="${mealMonday.mealNo}" hidden="hidden"/>
                                            <button id="loadingButton">Usuń</button>
                                        </form>
                                    </c:if>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </td>
                <c:if test="${deleteButtonVisible == true}">
                    <td>
                        <form action="/admin/newMenu/deleteDayMeals" method="post">
                            <input name="dayId" value="1" hidden="hidden"/>
                            <button id="loadingButton">Usuń dzień</button>
                        </form>
                    </td>
                </c:if>
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
                        <ul>
                            <c:forEach var="mealTuesday" items="${mealsTuesday}">
                                <li> ${mealTuesday.mealNo} ${mealTuesday.mealName} ${mealTuesday.mealPrice} zł
                                    <c:if test="${deleteButtonVisible == true}">
                                        <form action="/admin/newMenu/deleteMeal" method="post">
                                            <input name="mealNo" value="${mealTuesday.mealNo}" hidden="hidden"/>
                                            <button id="loadingButton">Usuń</button>
                                        </form>
                                    </c:if>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </td>
                <c:if test="${deleteButtonVisible == true}">
                    <td>
                        <form action="/admin/newMenu/deleteDayMeals" method="post">
                            <input name="dayId" value="2" hidden="hidden"/>
                            <button id="loadingButton">Usuń dzień</button>
                        </form>
                    </td>
                </c:if>
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
                        <ul>
                            <c:forEach var="mealWednesday" items="${mealsWednesday}">
                                <li>${mealWednesday.mealNo} ${mealWednesday.mealName} ${mealWednesday.mealPrice} zł
                                    <c:if test="${deleteButtonVisible == true}">
                                        <form action="/admin/newMenu/deleteMeal" method="post">
                                            <input name="mealNo" value="${mealWednesday.mealNo}" hidden="hidden"/>
                                            <button id="loadingButton">Usuń</button>
                                        </form>
                                    </c:if>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </td>
                <c:if test="${deleteButtonVisible == true}">
                    <td>
                        <form action="/admin/newMenu/deleteDayMeals" method="post">
                            <input name="dayId" value="3" hidden="hidden"/>
                            <button id="loadingButton">Usuń dzień</button>
                        </form>
                    </td>
                </c:if>
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
                        <ul>
                            <c:forEach var="mealThursday" items="${mealsThursday}">
                                <li>${mealThursday.mealNo} ${mealThursday.mealName} ${mealThursday.mealPrice} zł
                                    <c:if test="${deleteButtonVisible == true}">
                                        <form action="/admin/newMenu/deleteMeal" method="post">
                                            <input name="mealNo" value="${mealThursday.mealNo}" hidden="hidden"/>
                                            <button id="loadingButton">Usuń</button>
                                        </form>
                                    </c:if>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </td>
                <c:if test="${deleteButtonVisible == true}">
                    <td>
                        <form action="/admin/newMenu/deleteDayMeals" method="post">
                            <input name="dayId" value="4" hidden="hidden"/>
                            <button id="loadingButton">Usuń dzień</button>
                        </form>
                    </td>
                </c:if>
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
                        <ul>
                            <c:forEach var="mealFriday" items="${mealsFriday}">
                                <li>${mealFriday.mealNo} ${mealFriday.mealName} ${mealFriday.mealPrice} zł
                                    <c:if test="${deleteButtonVisible == true}">
                                        <form action="/admin/newMenu/deleteMeal" method="post">
                                            <input name="mealNo" value="${mealFriday.mealNo}" hidden="hidden"/>
                                            <button id="loadingButton">Usuń</button>
                                        </form>
                                    </c:if>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </td>
                <c:if test="${deleteButtonVisible == true}">
                    <td>
                        <form action="/admin/newMenu/deleteDayMeals" method="post">
                            <input name="dayId" value="5" hidden="hidden"/>
                            <button id="loadingButton">Usuń dzień</button>
                        </form>
                    </td>
                </c:if>
            </tr>
        </table>
    </div>
</div>
<div class="main-block">
    <div class="login-row" style="color: #FF0000; display: flex; flex-direction: column; align-self: center">
        <span style="align-self: center; font-weight: bold">Wczytaj plik pobrany w zakładce "Rozliczenia"</span>
        <form action="/admin/newMenu/upload" method="post" enctype="multipart/form-data">
            <input type="file" name="file" id="fileInput">
            <button id="loadingButton" type="submit">Wczytaj listę dań z pliku</button>
        </form>
    </div>
</div>

<jsp:include page="../fragments/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/app1.js"></script>
</body>
</html>
