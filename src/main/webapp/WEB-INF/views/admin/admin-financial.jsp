<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="pl">
<head>
    <meta charset="UTF-8"/>
    <jsp:include page="../fragments/title.jsp"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style_admin_financial.css">
</head>

<body>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/menu-admin.jsp"/>

<div class="main-block">
    <div class="day-block">
        <h3>Podsumowanie zamówień ${period} ( ${periodDate} )</h3>
    </div>
</div>
<div class="main-block">
    <div class="day-block">
        <table>
            <tr>
                <td>
                    <div class="day" style="color: #C73EE0FF; font-weight: bold;">
                        <div>
                            Dział:
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day" style="color: #C73EE0FF; font-weight: bold;">
                        <div>
                            Łączna kwota za obiady z dofinansowaniem
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day" style="color: #C73EE0FF; font-weight: bold;">
                        <div>
                            Łączna kwota za obiady bez dofinansowania
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day" style="color: #C73EE0FF; font-weight: bold;">
                        <div>
                            Ilość nieopłaconych obiadów
                        </div>
                    </div>
                </td>
            </tr>
            <c:forEach var="financialDepartmentSummary" items="${financialDepartmentSummaryList}">
                <tr>
                    <td>
                        <div class="day">
                            <div>
                                    ${financialDepartmentSummary.departmentName}
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="day">
                            <div>
                                    ${financialDepartmentSummary.departmentSummaryDiscountPrice} zł
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="day">
                            <div>
                                    ${financialDepartmentSummary.departmentSummaryFullPrice} zł
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="day" style="color: #FF0000; font-weight: bold;">
                            <div>
                                    ${financialDepartmentSummary.notPaidOrders}
                            </div>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td>
                    <div class="day" style="color: #C73EE0FF; font-weight: bold;">
                        <div>
                            Podsumowanie:
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day" style="color: #C73EE0FF; font-weight: bold;">
                        <div>
                            Od pracowników: ${sumOfDepartmentDiscountPrice} zł
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day" style="color: #C73EE0FF; font-weight: bold;">
                        <div>
                            Dla dostawcy: ${sumOfDepartmentFullPrice} zł
                        </div>
                    </div>
                </td>
                <td>
                    <div class="day" style="color: #C73EE0FF; font-weight: bold;">
                        <div>
                            Kwota dofinansowania: ${refundation} zł
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                </td>

            </tr>
        </table>
    </div>
</div>

<div class="main-block">
    <form action="/admin/financial/reminderNotPaid" method="post">
        <div class="day-block">
            <div class="day">
                <button id="loadingButton">Powiadom o niezapłaconych obiadach</button>
            </div>
        </div>
    </form>
    <div class="day-block">
        <div class="day">
           Lista użytkowników | Lista dań | Nowe i bierzące zamówienia | Rozliczenie nowych zamówień
        </div>
    </div>
    <form action="/admin/getDataBeforeCloseWeekInXl" method="post">
        <div class="day-block">
            <div class="day">
                <button>Pobierz plik Excel przed zamknięciem tygodnia</button>
            </div>
        </div>
    </form>
    <form action="/admin/financial/closeWeek" method="get">
        <div class="day-block">
            <div class="day">
                <button id="loadingButton">Przejdź do strony, na której zamkniesz tydzień i rozliczysz zamówienia</button>
            </div>
            <div id="loadingIcon" style="display: none;">Ładowanie...</div>
        </div>
    </form>
</div>


<jsp:include page="../fragments/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/app1.js"></script>
</body>
</html>
