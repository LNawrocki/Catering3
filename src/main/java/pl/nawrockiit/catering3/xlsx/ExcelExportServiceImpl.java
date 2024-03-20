package pl.nawrockiit.catering3.xlsx;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.nawrockiit.catering3.actualOrder.ActualOrder;
import pl.nawrockiit.catering3.actualOrder.ActualOrderRepository;
import pl.nawrockiit.catering3.actualOrder.ActualOrderService;
import pl.nawrockiit.catering3.financial.FinancialDepartmentSummary;
import pl.nawrockiit.catering3.financial.FinancialService;
import pl.nawrockiit.catering3.newMenu.NewMenu;
import pl.nawrockiit.catering3.newMenu.NewMenuService;
import pl.nawrockiit.catering3.newOrder.NewOrder;
import pl.nawrockiit.catering3.newOrder.NewOrderRepository;
import pl.nawrockiit.catering3.newOrder.NewOrderService;
import pl.nawrockiit.catering3.orderSummary.OrderSummary;
import pl.nawrockiit.catering3.orderSummary.OrderSummaryService;
import pl.nawrockiit.catering3.price.Price;
import pl.nawrockiit.catering3.price.PriceService;
import pl.nawrockiit.catering3.user.User;
import pl.nawrockiit.catering3.user.UserService;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class ExcelExportServiceImpl implements ExcelExportService {

    //    private static final Logger log = getLogger(CsvExportService.class);
    private final Map<String, String> companyData;
    private final ActualOrderRepository actualOrderRepository;
    private final ActualOrderService actualOrderService;
    private final NewOrderRepository newOrderRepository;
    private final NewOrderService newOrderService;
    private final FinancialService financialService;
    private final OrderSummaryService orderSummaryService;
    private final UserService userService;
    private final PriceService priceService;
    private final NewMenuService newMenuService;

    public ExcelExportServiceImpl(@Value("#{${companyData}}") Map<String, String> companyData, ActualOrderRepository actualOrderRepository, ActualOrderService actualOrderService, NewOrderRepository newOrderRepository, NewOrderService newOrderService, FinancialService financialService, OrderSummaryService orderSummaryService, UserService userService, PriceService priceService, NewMenuService newMenuService) {
        this.companyData = companyData;
        this.actualOrderRepository = actualOrderRepository;
        this.actualOrderService = actualOrderService;
        this.newOrderRepository = newOrderRepository;
        this.newOrderService = newOrderService;
        this.financialService = financialService;
        this.orderSummaryService = orderSummaryService;
        this.userService = userService;
        this.priceService = priceService;
        this.newMenuService = newMenuService;
    }


    @Override
    public void getOrderSummeryInXls(HttpServletResponse response) throws IOException {

        List<OrderSummary> orderSummaryList = Optional.ofNullable(orderSummaryService.findAll()).orElse(new ArrayList<>(1));
        List<ActualOrder> actualOrderMeals = actualOrderService.findAll();
        Integer orderSummaryFirstShiftQty = 0;
        Integer orderSummarySecondShiftQty = 0;
        for (OrderSummary orderSummary : orderSummaryList) {
            orderSummaryFirstShiftQty = orderSummaryFirstShiftQty + orderSummary.getFirstShiftQuantity();
            orderSummarySecondShiftQty = orderSummarySecondShiftQty + orderSummary.getSecondShiftQuantity();
        }

        BigDecimal firstShiftPriceSum = BigDecimal.valueOf(0);
        BigDecimal secondShiftPriceSum = BigDecimal.valueOf(0);
        for (ActualOrder actualOrderMeal : actualOrderMeals) {
            if (actualOrderMeal.getShiftMon() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(actualOrderMeal.getPriceMon());
            } else if (actualOrderMeal.getShiftMon() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(actualOrderMeal.getPriceMon());
            }
            if (actualOrderMeal.getShiftTue() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(actualOrderMeal.getPriceTue());
            } else if (actualOrderMeal.getShiftTue() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(actualOrderMeal.getPriceTue());
            }
            if (actualOrderMeal.getShiftWed() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(actualOrderMeal.getPriceWed());
            } else if (actualOrderMeal.getShiftWed() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(actualOrderMeal.getPriceWed());
            }
            if (actualOrderMeal.getShiftThu() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(actualOrderMeal.getPriceThu());
            } else if (actualOrderMeal.getShiftThu() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(actualOrderMeal.getPriceThu());
            }
            if (actualOrderMeal.getShiftFri() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(actualOrderMeal.getPriceFri());
            } else if (actualOrderMeal.getShiftFri() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(actualOrderMeal.getPriceFri());
            }
        }

        String[] headers = {"Dzień tygodnia", "nr", "Menu", "Liczba Obiadów", "Numer osoby: " + companyData.get("companyMarker") + " + numer osoby np. " + companyData.get("companyMarker") + "37"};
        int rowCounter = 0;

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(companyData.get("companyAddress"));

        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 1500);
        sheet.setColumnWidth(2, 25000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 12500);

        CellStyle style = workbook.createCellStyle();

        sheet.addMergedRegion(new CellRangeAddress(
                0, // pierwszy wiersz (0-based)
                0, // ostatni wiersz (0-based)
                0, // pierwsza kolumna (0-based)
                4  // ostatnia kolumna (0-based)
        ));

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Dane dostawcy");
        cell.setCellStyle(style);
        rowCounter++;

        sheet.addMergedRegion(new CellRangeAddress(
                rowCounter, // pierwszy wiersz (0-based)
                rowCounter, // ostatni wiersz (0-based)
                0, // pierwsza kolumna (0-based)
                1  // ostatnia kolumna (0-based)
        ));

        row = sheet.createRow(rowCounter);
        cell = row.createCell(0);
        cell.setCellValue(companyData.get("companyAddress"));
        cell.setCellStyle(style);

        sheet.addMergedRegion(new CellRangeAddress(
                rowCounter, // pierwszy wiersz (0-based)
                rowCounter, // ostatni wiersz (0-based)
                2, // pierwsza kolumna (0-based)
                3  // ostatnia kolumna (0-based)
        ));

        cell = row.createCell(2);
        cell.setCellValue("I zmiana");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue(orderSummaryList.isEmpty() ? "Okres - ( -) " : "okres " + orderSummaryList.get(0).getPeriod() + " ( " + orderSummaryList.get(0).getPeriodDate() + " )");
        cell.setCellStyle(style);
//        rowCounter++;
//
//        row = sheet.createRow(rowCounter);
//        cell = row.createCell(4);
//        cell.setCellValue( "OZNACZENIE NUMERÓW: " + companyData.get("companyMarker"));
//        cell.setCellStyle(style);

        rowCounter++;

        Row header = sheet.createRow(rowCounter);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setWrapText(true);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        headerStyle.setFont(font);

        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(headerStyle);
        }
        style.setWrapText(true);
        rowCounter++;

        for (int i = 0; i < orderSummaryService.findAll().size(); i++) {

            Integer dayId = orderSummaryList.get(i).getDayId();
            String day = "";
            switch (dayId) {
                case 1:
                    day = "Poniedziałek";
                    break;
                case 2:
                    day = "Wtorek";
                    break;
                case 3:
                    day = "Środa";
                    break;
                case 4:
                    day = "Czwartek";
                    break;
                case 5:
                    day = "Piątek";
                    break;
            }

            row = sheet.createRow(rowCounter);
            cell = row.createCell(0);
            cell.setCellValue(day);
            cell.setCellStyle(style);
            cell = row.createCell(1);
            cell.setCellValue(orderSummaryList.get(i).getMealNo());
            cell.setCellStyle(style);
            cell = row.createCell(2);
            cell.setCellValue(orderSummaryList.get(i).getMealName());
            cell.setCellStyle(style);
            cell = row.createCell(3);
            cell.setCellValue(orderSummaryList.get(i).getFirstShiftQuantity());
            cell.setCellStyle(style);
            cell = row.createCell(4);
//            String[] idsFirstShiftArray = orderSummaryList.get(i).getFirstShiftUsersId().split(", ");
//            Arrays.sort(idsFirstShiftArray);
//            StringBuilder idsFirstShiftSorted = new StringBuilder();
//            for (String id : idsFirstShiftArray) {
//                idsFirstShiftSorted.append(id).append(", ");
//            }
//            cell.setCellValue(idsFirstShiftSorted.toString());
            cell.setCellValue(orderSummaryList.get(i).getFirstShiftUsersId());
            cell.setCellStyle(style);

            rowCounter++;
        }
        row = sheet.createRow(rowCounter);
        cell = row.createCell(2);
        cell.setCellValue("Ilośc posiłków I zmiana");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue(orderSummaryFirstShiftQty);
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("Łączna kwota za posiłki: " + firstShiftPriceSum + " zł.");
        cell.setCellStyle(style);

        rowCounter++;
        rowCounter++;

        sheet.addMergedRegion(new CellRangeAddress(
                rowCounter, // pierwszy wiersz (0-based)
                rowCounter, // ostatni wiersz (0-based)
                0, // pierwsza kolumna (0-based)
                4  // ostatnia kolumna (0-based)
        ));

        row = sheet.createRow(rowCounter);
        cell = row.createCell(0);
        cell.setCellValue("Dane dostawcy");
        cell.setCellStyle(style);
        rowCounter++;

        sheet.addMergedRegion(new CellRangeAddress(
                rowCounter, // pierwszy wiersz (0-based)
                rowCounter, // ostatni wiersz (0-based)
                0, // pierwsza kolumna (0-based)
                1  // ostatnia kolumna (0-based)
        ));

        row = sheet.createRow(rowCounter);
        cell = row.createCell(0);
        cell.setCellValue(companyData.get("companyAddress"));
        cell.setCellStyle(style);

        sheet.addMergedRegion(new CellRangeAddress(
                rowCounter, // pierwszy wiersz (0-based)
                rowCounter, // ostatni wiersz (0-based)
                2, // pierwsza kolumna (0-based)
                3  // ostatnia kolumna (0-based)
        ));

        cell = row.createCell(2);
        cell.setCellValue("II zmiana");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue(orderSummaryList.isEmpty() ? "Okres - ( -) " : "okres " + orderSummaryList.get(0).getPeriod() + " ( " + orderSummaryList.get(0).getPeriodDate() + " )");
        cell.setCellStyle(style);
        rowCounter++;

        header = sheet.createRow(rowCounter);
        headerStyle = workbook.createCellStyle();
        headerStyle.setWrapText(true);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        headerStyle.setFont(font);

        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(headerStyle);
        }
        style.setWrapText(true);
        rowCounter++;

        for (int i = 0; i < orderSummaryList.size(); i++) {
            if (orderSummaryList.get(i).getSecondShiftQuantity() != 0) {
                Integer dayId = orderSummaryList.get(i).getDayId();
                String day = "";
                switch (dayId) {
                    case 1:
                        day = "Poniedziałek";
                        break;
                    case 2:
                        day = "Wtorek";
                        break;
                    case 3:
                        day = "Środa";
                        break;
                    case 4:
                        day = "Czwartek";
                        break;
                    case 5:
                        day = "Piątek";
                        break;
                }
                row = sheet.createRow(rowCounter);
                cell = row.createCell(0);
                cell.setCellValue(day);
                cell.setCellStyle(style);
                cell = row.createCell(1);
                cell.setCellValue(orderSummaryList.get(i).getMealNo());
                cell.setCellStyle(style);
                cell = row.createCell(2);
                cell.setCellValue(orderSummaryList.get(i).getMealName());
                cell.setCellStyle(style);
                cell = row.createCell(3);
                cell.setCellValue(orderSummaryList.get(i).getSecondShiftQuantity());
                cell.setCellStyle(style);
                cell = row.createCell(4);
                cell.setCellValue(orderSummaryList.get(i).getSecondShiftUsersId());
                cell.setCellStyle(style);
                rowCounter++;
            }

        }
        row = sheet.createRow(rowCounter);
        cell = row.createCell(2);
        cell.setCellValue("Ilośc posiłków II zmiana");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue(orderSummarySecondShiftQty);
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("Łączna kwota za posiłki: " + secondShiftPriceSum + " zł.");
        cell.setCellStyle(style);

        OutputStream out = response.getOutputStream();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("wykaz_zamówień_" + companyData.get("companyAddress") + "_" + LocalDateTime.now() + ".xlsx", "UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        workbook.write(out);
        workbook.close();
        out.flush();
        out.close();
    }

    @Override
    public void getUsersListInXls(HttpServletResponse response) throws IOException {

        int rowCounter = 0;
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFFont fontTableTitle = workbook.createFont();
        fontTableTitle.setFontName("Arial");
        fontTableTitle.setFontHeightInPoints((short) 12);
        fontTableTitle.setBold(true);

        XSSFFont fontHeader = workbook.createFont();
        fontHeader.setFontName("Arial");
        fontHeader.setFontHeightInPoints((short) 11);
        fontHeader.setBold(true);

        XSSFFont fontContent = workbook.createFont();
        fontContent.setFontName("Arial");
        fontContent.setFontHeightInPoints((short) 10);
        fontContent.setBold(false);


        CellStyle styleTableTitle = workbook.createCellStyle();
        styleTableTitle.setFillForegroundColor(IndexedColors.WHITE1.getIndex());
        styleTableTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleTableTitle.setFont(fontTableTitle);
        styleTableTitle.setWrapText(false);
        styleTableTitle.setBorderBottom(BorderStyle.THIN);
        styleTableTitle.setBorderTop(BorderStyle.THIN);
        styleTableTitle.setBorderRight(BorderStyle.THIN);
        styleTableTitle.setBorderLeft(BorderStyle.THIN);


        CellStyle styleHeader = workbook.createCellStyle();
        styleHeader.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeader.setFont(fontHeader);
        styleHeader.setWrapText(false);
        styleHeader.setBorderBottom(BorderStyle.THIN);
        styleHeader.setBorderTop(BorderStyle.THIN);
        styleHeader.setBorderRight(BorderStyle.THIN);
        styleHeader.setBorderLeft(BorderStyle.THIN);

        CellStyle styleContent = workbook.createCellStyle();
        styleContent.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleContent.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleContent.setFont(fontContent);
        styleContent.setWrapText(false);
        styleContent.setBorderBottom(BorderStyle.THIN);
        styleContent.setBorderTop(BorderStyle.THIN);
        styleContent.setBorderRight(BorderStyle.THIN);
        styleContent.setBorderLeft(BorderStyle.THIN);

        Sheet sheet = workbook.createSheet("Lista osób");
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 2000);
        sheet.setColumnWidth(4, 8000);
        sheet.setColumnWidth(5, 4500);
        sheet.setColumnWidth(6, 4500);


        sheet.addMergedRegion(new CellRangeAddress(
                0, // first row (0-based)
                0, // last row (0-based)
                0, // first column(0-based)
                5  // last column (0-based)
        ));

        Row row = sheet.createRow(rowCounter);
        Cell cell = row.createCell(0);
        cell.setCellStyle(styleTableTitle);
        cell.setCellValue("Lista osób " + companyData.get("companyName") + ", " + companyData.get("companyAddress") + " KW" + (LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear()) + " " + LocalDate.now()));
        rowCounter++;

        Row header = sheet.createRow(rowCounter);

        String[] headers = {"Nazwisko", "Imię", "Login", "ID", "Dział", "", "Stan konta (zł)"};
        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(styleHeader);
        }
        rowCounter++;
        List<User> usersListByLastName = userService.findUsersByLastName();

        for (int i = 0; i < usersListByLastName.size(); i++) {
            row = sheet.createRow(rowCounter);
            cell = row.createCell(0);
            cell.setCellValue(usersListByLastName.get(i).getLastName());
            cell.setCellStyle(styleContent);
            cell = row.createCell(1);
            cell.setCellValue(usersListByLastName.get(i).getFirstName());
            cell.setCellStyle(styleContent);
            cell = row.createCell(2);
            cell.setCellValue(usersListByLastName.get(i).getUsername());
            cell.setCellStyle(styleContent);
            cell = row.createCell(3);
            cell.setCellValue(usersListByLastName.get(i).getUserId());
            cell.setCellStyle(styleContent);
            cell = row.createCell(4);
            cell.setCellValue(usersListByLastName.get(i).getDepartment().getName());
            cell.setCellStyle(styleContent);

            cell = row.createCell(6);
            cell.setCellValue(usersListByLastName.get(i).getCredit().toString());
            cell.setCellStyle(styleContent);
            rowCounter++;
        }

        row = sheet.createRow(rowCounter);
        cell = row.createCell(5);
        cell.setCellValue("Łączna suma:");
        cell.setCellStyle(styleContent);

        BigDecimal accountsCreditSum = BigDecimal.valueOf(0);
        for (User user : usersListByLastName) {
            accountsCreditSum = accountsCreditSum.add(user.getCredit());
        }
        cell = row.createCell(6);
        cell.setCellValue(accountsCreditSum.toString());
        cell.setCellStyle(styleContent);


        OutputStream out = response.getOutputStream();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("Lista_użytkowników_" + companyData.get("companyAddress") + "_" + LocalDateTime.now() + ".xlsx", "UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        workbook.write(out);
        workbook.close();
        out.flush();
        out.close();
    }

    @Override
    public void getDataBeforeCloseWeekInXls(HttpServletResponse response) throws IOException {

//        #####USERS LIST#########
        int rowCounter = 0;
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFFont fontTableTitle = workbook.createFont();
        fontTableTitle.setFontName("Arial");
        fontTableTitle.setFontHeightInPoints((short) 12);
        fontTableTitle.setBold(true);

        XSSFFont fontHeader = workbook.createFont();
        fontHeader.setFontName("Arial");
        fontHeader.setFontHeightInPoints((short) 11);
        fontHeader.setBold(true);

        XSSFFont fontContent = workbook.createFont();
        fontContent.setFontName("Arial");
        fontContent.setFontHeightInPoints((short) 10);
        fontContent.setBold(false);


        CellStyle styleTableTitle = workbook.createCellStyle();
        styleTableTitle.setFillForegroundColor(IndexedColors.WHITE1.getIndex());
        styleTableTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleTableTitle.setFont(fontTableTitle);
        styleTableTitle.setWrapText(false);
        styleTableTitle.setBorderBottom(BorderStyle.THIN);
        styleTableTitle.setBorderTop(BorderStyle.THIN);
        styleTableTitle.setBorderRight(BorderStyle.THIN);
        styleTableTitle.setBorderLeft(BorderStyle.THIN);


        CellStyle styleHeader = workbook.createCellStyle();
        styleHeader.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeader.setFont(fontHeader);
        styleHeader.setWrapText(false);
        styleHeader.setBorderBottom(BorderStyle.THIN);
        styleHeader.setBorderTop(BorderStyle.THIN);
        styleHeader.setBorderRight(BorderStyle.THIN);
        styleHeader.setBorderLeft(BorderStyle.THIN);

        CellStyle styleContent = workbook.createCellStyle();
        styleContent.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleContent.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleContent.setFont(fontContent);
        styleContent.setWrapText(false);
        styleContent.setBorderBottom(BorderStyle.THIN);
        styleContent.setBorderTop(BorderStyle.THIN);
        styleContent.setBorderRight(BorderStyle.THIN);
        styleContent.setBorderLeft(BorderStyle.THIN);

        Sheet sheet = workbook.createSheet("Lista osób");
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 2000);
        sheet.setColumnWidth(4, 8000);
        sheet.setColumnWidth(5, 4500);
        sheet.setColumnWidth(6, 4500);


        sheet.addMergedRegion(new CellRangeAddress(
                0, // first row (0-based)
                0, // last row (0-based)
                0, // first column(0-based)
                6  // last column (0-based)
        ));

        Row row = sheet.createRow(rowCounter);
        Cell cell = row.createCell(0);
        cell.setCellStyle(styleTableTitle);
        cell.setCellValue("Lista osób " + companyData.get("companyName") + ", " + companyData.get("companyAddress") + " KW" + (LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear()) + " " + LocalDate.now()));
        rowCounter++;

        Row header = sheet.createRow(rowCounter);

        String[] headers = {"Nazwisko", "Imię", "Login", "ID", "Dział", "", "Stan konta (zł)"};
        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(styleHeader);
        }
        for (int i = 1; i <= 6; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(styleTableTitle);
        }
        rowCounter++;
        List<User> usersListByLastName = userService.findUsersByLastName();

        for (int i = 0; i < usersListByLastName.size(); i++) {
            row = sheet.createRow(rowCounter);
            cell = row.createCell(0);
            cell.setCellValue(usersListByLastName.get(i).getLastName());
            cell.setCellStyle(styleContent);
            cell = row.createCell(1);
            cell.setCellValue(usersListByLastName.get(i).getFirstName());
            cell.setCellStyle(styleContent);
            cell = row.createCell(2);
            cell.setCellValue(usersListByLastName.get(i).getUsername());
            cell.setCellStyle(styleContent);
            cell = row.createCell(3);
            cell.setCellValue(usersListByLastName.get(i).getUserId());
            cell.setCellStyle(styleContent);
            cell = row.createCell(4);
            cell.setCellValue(usersListByLastName.get(i).getDepartment().getName());
            cell.setCellStyle(styleContent);

            cell = row.createCell(6);
            cell.setCellValue(usersListByLastName.get(i).getCredit().toString());
            cell.setCellStyle(styleContent);
            rowCounter++;
        }

        row = sheet.createRow(rowCounter);
        cell = row.createCell(5);
        cell.setCellValue("Łączna suma:");
        cell.setCellStyle(styleContent);

        BigDecimal accountsCreditSum = BigDecimal.valueOf(0);
        for (User user : usersListByLastName) {
            accountsCreditSum = accountsCreditSum.add(user.getCredit());
        }
        cell = row.createCell(6);
        cell.setCellValue(accountsCreditSum.toString());
        cell.setCellStyle(styleContent);

//        #####NEW MENU#########
        sheet = workbook.createSheet("Lista obiadów");
        rowCounter = 0;

        sheet.setColumnWidth(0, 2000);
        sheet.setColumnWidth(1, 25000);
        sheet.setColumnWidth(2, 3000);
        sheet.setColumnWidth(3, 2000);
        sheet.setColumnWidth(4, 3000);
        sheet.setColumnWidth(5, 7000);

        sheet.addMergedRegion(new CellRangeAddress(
                0, // pierwszy wiersz (0-based)
                0, // ostatni wiersz (0-based)
                0, // pierwsza kolumna (0-based)
                5  // ostatnia kolumna (0-based)
        ));

        row = sheet.createRow(rowCounter);
        cell = row.createCell(0);
        List<NewMenu> newMenuList = Optional.ofNullable(newMenuService.findAll()).orElse(new ArrayList<>(1));

        if (!newMenuService.findAll().isEmpty()) {
            cell.setCellValue("Nowe menu " + companyData.get("companyName") + ", " + companyData.get("companyAddress") + " " + newMenuList.get(0).getPeriod() + " ( " + newMenuList.get(0).getPeriodDate() + " )");
        } else {
            cell.setCellValue("Nowe menu " + companyData.get("companyName") + ", " + companyData.get("companyAddress") + "- ( - )");
        }
        cell.setCellStyle(styleTableTitle);
        for (int i = 1; i <= 3; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(styleTableTitle);
        }
        rowCounter++;

        header = sheet.createRow(rowCounter);
        headers = new String[]{"Id", "Nazwa", "Cena", "Dzień", "KW", "Data"};
        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(styleHeader);
        }
        rowCounter++;

        for (int i = 0; i < newMenuList.size(); i++) {
            row = sheet.createRow(rowCounter);
            cell = row.createCell(0);
            cell.setCellValue(newMenuList.get(i).getMealNo());
            cell.setCellStyle(styleContent);
            cell = row.createCell(1);
            cell.setCellValue(newMenuList.get(i).getMealName());
            cell.setCellStyle(styleContent);
            cell = row.createCell(2);
            cell.setCellValue(newMenuList.get(i).getMealPrice().toString());
            cell.setCellStyle(styleContent);
            cell = row.createCell(3);
            cell.setCellValue(newMenuList.get(i).getDayId());
            cell.setCellStyle(styleContent);
            cell = row.createCell(4);
            cell.setCellValue(newMenuList.get(i).getPeriod());
            cell.setCellStyle(styleContent);
            cell = row.createCell(5);
            cell.setCellValue(newMenuList.get(i).getPeriodDate());
            cell.setCellStyle(styleContent);
            rowCounter++;
        }

        //        #####Actual and new orders#########
        sheet = workbook.createSheet("Nowe_i_bierzące_zamówienia");
        rowCounter = 0;

        headers = new String[]{"ID", "okres", "login", "zapłacono", "kwota",
                "nr Pn", "nazwa Pn", "cena Pn", "zmiana Pn",
                "nr Wt", "nazwa Wt", "cena Wt", "zmiana Wt",
                "nr Śr", "nazwa Śr", "cena Śr", "zmiana Śr",
                "nr Czw", "nazwa Czw", "cena Czw", "zmiana Czw",
                "nr Pt", "nazwa Pt", "cena Pt", "zmiana Pt", "dofinansowanie (%)"};

        for (int i = 0; i <= headers.length; i++) {
            sheet.setColumnWidth(i, 3300);
        }
        sheet.setColumnWidth(25, 6000);

        sheet.addMergedRegion(new CellRangeAddress(
                0, // pierwszy wiersz (0-based)
                0, // ostatni wiersz (0-based)
                0, // pierwsza kolumna (0-based)
                25  // ostatnia kolumna (0-based)
        ));

        row = sheet.createRow(rowCounter);
        cell = row.createCell(0);

        List<NewOrder> newOrderList = Optional.ofNullable(newOrderRepository.findAll()).orElse(new ArrayList<>(1));
        if (!newOrderService.findAll().isEmpty()) {
            cell.setCellValue("Nowe zamówienia " + companyData.get("companyName") + ", " + companyData.get("companyAddress") + " " + newOrderList.get(0).getPeriod() + " ( " + newOrderList.get(0).getPeriodDate() + " )");
        } else {
            cell.setCellValue("Nowe zamówienia " + companyData.get("companyName") + ", " + companyData.get("companyAddress") + "- ( - )");
        }
        cell.setCellStyle(styleTableTitle);
        for (int i = 1; i <= 25; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(styleTableTitle);
        }
        rowCounter++;

        header = sheet.createRow(rowCounter);
        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(styleHeader);
        }
        rowCounter++;

        for (int i = 0; i < newOrderList.size(); i++) {
            row = sheet.createRow(rowCounter);
            rowCounter++;
            cell = row.createCell(0);
            cell.setCellValue(newOrderList.get(i).getId());
            cell.setCellStyle(styleContent);
            cell = row.createCell(1);
            cell.setCellValue(newOrderList.get(i).getPeriod());
            cell.setCellStyle(styleContent);
            cell = row.createCell(2);
            cell.setCellValue(newOrderList.get(i).getUser().getUsername());
            cell.setCellStyle(styleContent);
            cell = row.createCell(3);
            if (newOrderList.get(i).getIsPaid()) {
                cell.setCellValue("TAK");
            } else if (!newOrderList.get(i).getIsPaid()) {
                cell.setCellValue("NIE");
            }
            cell.setCellStyle(styleContent);
            cell = row.createCell(4);
            cell.setCellValue(newOrderList.get(i).getToPay().doubleValue());
            cell.setCellStyle(styleContent);
            cell = row.createCell(5);
            cell.setCellValue(newOrderList.get(i).getMealMon());
            cell.setCellStyle(styleContent);
            cell = row.createCell(6);
            cell.setCellValue(newOrderList.get(i).getMealMonName());
            cell.setCellStyle(styleContent);
            cell = row.createCell(7);
            cell.setCellValue(newOrderList.get(i).getPriceMon().doubleValue());
            cell.setCellStyle(styleContent);
            cell = row.createCell(8);
            cell.setCellValue(newOrderList.get(i).getShiftMon());
            cell.setCellStyle(styleContent);
            cell = row.createCell(9);
            cell.setCellValue(newOrderList.get(i).getMealTue());
            cell.setCellStyle(styleContent);
            cell = row.createCell(10);
            cell.setCellValue(newOrderList.get(i).getMealTueName());
            cell.setCellStyle(styleContent);
            cell = row.createCell(11);
            cell.setCellValue(newOrderList.get(i).getPriceTue().doubleValue());
            cell.setCellStyle(styleContent);
            cell = row.createCell(12);
            cell.setCellValue(newOrderList.get(i).getShiftTue());
            cell.setCellStyle(styleContent);
            cell = row.createCell(13);
            cell.setCellValue(newOrderList.get(i).getMealWed());
            cell.setCellStyle(styleContent);
            cell = row.createCell(14);
            cell.setCellValue(newOrderList.get(i).getMealWedName());
            cell.setCellStyle(styleContent);
            cell = row.createCell(15);
            cell.setCellValue(newOrderList.get(i).getPriceWed().doubleValue());
            cell.setCellStyle(styleContent);
            cell = row.createCell(16);
            cell.setCellValue(newOrderList.get(i).getShiftWed());
            cell.setCellStyle(styleContent);
            cell = row.createCell(17);
            cell.setCellValue(newOrderList.get(i).getMealThu());
            cell.setCellStyle(styleContent);
            cell = row.createCell(18);
            cell.setCellValue(newOrderList.get(i).getMealThuName());
            cell.setCellStyle(styleContent);
            cell = row.createCell(19);
            cell.setCellValue(newOrderList.get(i).getPriceThu().doubleValue());
            cell.setCellStyle(styleContent);
            cell = row.createCell(20);
            cell.setCellValue(newOrderList.get(i).getShiftThu());
            cell.setCellStyle(styleContent);
            cell = row.createCell(21);
            cell.setCellValue(newOrderList.get(i).getMealFri());
            cell.setCellStyle(styleContent);
            cell = row.createCell(22);
            cell.setCellValue(newOrderList.get(i).getMealFriName());
            cell.setCellStyle(styleContent);
            cell = row.createCell(23);
            cell.setCellValue(newOrderList.get(i).getPriceFri().doubleValue());
            cell.setCellStyle(styleContent);
            cell = row.createCell(24);
            cell.setCellValue(newOrderList.get(i).getShiftFri());
            cell.setCellStyle(styleContent);
            cell = row.createCell(25);
            cell.setCellValue(newOrderList.get(i).getUser().getDepartment().getPaymentPerc());
            cell.setCellStyle(styleContent);
        }
        rowCounter++;

        row = sheet.createRow(rowCounter);
        cell = row.createCell(0);

        sheet.addMergedRegion(new CellRangeAddress(
                rowCounter, // pierwszy wiersz (0-based)
                rowCounter, // ostatni wiersz (0-based)
                0, // pierwsza kolumna (0-based)
                25  // ostatnia kolumna (0-based)
        ));

        List<ActualOrder> actualOrderList = Optional.ofNullable(actualOrderRepository.findAll()).orElse(new ArrayList<>(1));
        if (!actualOrderService.findAll().isEmpty()) {
            cell.setCellValue("Bierzące zamówienia " + companyData.get("companyName") + ", " + companyData.get("companyAddress") + " " + actualOrderList.get(0).getPeriod() + " ( " + actualOrderList.get(0).getPeriodDate() + " )");
        } else {
            cell.setCellValue("Bierzące zamówienia " + companyData.get("companyName") + ", " + companyData.get("companyAddress") + "- ( - )");
        }
        cell.setCellStyle(styleTableTitle);
        for (int i = 1; i <= 25; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(styleTableTitle);
        }
        rowCounter++;

        header = sheet.createRow(rowCounter);
        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(styleHeader);
        }
        rowCounter++;

        for (int i = 0; i < actualOrderList.size(); i++) {
            row = sheet.createRow(rowCounter);
            cell = row.createCell(0);
            cell.setCellValue(actualOrderList.get(i).getId());
            cell.setCellStyle(styleContent);
            cell = row.createCell(1);
            cell.setCellValue(actualOrderList.get(i).getPeriod());
            cell.setCellStyle(styleContent);
            cell = row.createCell(2);
            cell.setCellValue(actualOrderList.get(i).getUser().getUsername());
            cell.setCellStyle(styleContent);
            cell = row.createCell(3);
            if (actualOrderList.get(i).getIsPaid()) {
                cell.setCellValue("TAK");
            } else if (!actualOrderList.get(i).getIsPaid()) {
                cell.setCellValue("NIE");
            }
            cell.setCellStyle(styleContent);
            cell = row.createCell(4);
            cell.setCellValue(actualOrderList.get(i).getToPay().doubleValue());
            cell.setCellStyle(styleContent);
            cell = row.createCell(5);
            cell.setCellValue(actualOrderList.get(i).getMealMon());
            cell.setCellStyle(styleContent);
            cell = row.createCell(6);
            cell.setCellValue(actualOrderList.get(i).getMealMonName());
            cell.setCellStyle(styleContent);
            cell = row.createCell(7);
            cell.setCellValue(actualOrderList.get(i).getPriceMon().doubleValue());
            cell.setCellStyle(styleContent);
            cell = row.createCell(8);
            cell.setCellValue(actualOrderList.get(i).getShiftMon());
            cell.setCellStyle(styleContent);
            cell = row.createCell(9);
            cell.setCellValue(actualOrderList.get(i).getMealTue());
            cell.setCellStyle(styleContent);
            cell = row.createCell(10);
            cell.setCellValue(actualOrderList.get(i).getMealTueName());
            cell.setCellStyle(styleContent);
            cell = row.createCell(11);
            cell.setCellValue(actualOrderList.get(i).getPriceTue().doubleValue());
            cell.setCellStyle(styleContent);
            cell = row.createCell(12);
            cell.setCellValue(actualOrderList.get(i).getShiftTue());
            cell.setCellStyle(styleContent);
            cell = row.createCell(13);
            cell.setCellValue(actualOrderList.get(i).getMealWed());
            cell.setCellStyle(styleContent);
            cell = row.createCell(14);
            cell.setCellValue(actualOrderList.get(i).getMealWedName());
            cell.setCellStyle(styleContent);
            cell = row.createCell(15);
            cell.setCellValue(actualOrderList.get(i).getPriceWed().doubleValue());
            cell.setCellStyle(styleContent);
            cell = row.createCell(16);
            cell.setCellValue(actualOrderList.get(i).getShiftWed());
            cell.setCellStyle(styleContent);
            cell = row.createCell(17);
            cell.setCellValue(actualOrderList.get(i).getMealThu());
            cell.setCellStyle(styleContent);
            cell = row.createCell(18);
            cell.setCellValue(actualOrderList.get(i).getMealThuName());
            cell.setCellStyle(styleContent);
            cell = row.createCell(19);
            cell.setCellValue(actualOrderList.get(i).getPriceThu().doubleValue());
            cell.setCellStyle(styleContent);
            cell = row.createCell(20);
            cell.setCellValue(actualOrderList.get(i).getShiftThu());
            cell.setCellStyle(styleContent);
            cell = row.createCell(21);
            cell.setCellValue(actualOrderList.get(i).getMealFri());
            cell.setCellStyle(styleContent);
            cell = row.createCell(22);
            cell.setCellValue(actualOrderList.get(i).getMealFriName());
            cell.setCellStyle(styleContent);
            cell = row.createCell(23);
            cell.setCellValue(actualOrderList.get(i).getPriceFri().doubleValue());
            cell.setCellStyle(styleContent);
            cell = row.createCell(24);
            cell.setCellValue(actualOrderList.get(i).getShiftFri());
            cell.setCellStyle(styleContent);
            cell = row.createCell(25);
            cell.setCellValue(actualOrderList.get(i).getUser().getDepartment().getPaymentPerc());
            cell.setCellStyle(styleContent);
            rowCounter++;
        }

        //        #####financial summary#########
        sheet = workbook.createSheet("Rozliczenie_księgowość");
        rowCounter = 0;

        headers = new String[]{"Dział", "Łączna kwota z dofinansowanie (zł)", "łączna kwota bez dofinansowania (zł)"};

        for (int i = 0; i <= headers.length; i++) {
            sheet.setColumnWidth(i, 11000);
        }

        sheet.addMergedRegion(new CellRangeAddress(
                rowCounter, // pierwszy wiersz (0-based)
                rowCounter, // ostatni wiersz (0-based)
                0, // pierwsza kolumna (0-based)
                3  // ostatnia kolumna (0-based)
        ));

        row = sheet.createRow(0);
        cell = row.createCell(0);
        if (!newOrderService.findAll().isEmpty()) {
            cell.setCellValue("Rozliczenie_księgowość_" + companyData.get("companyName") + ", " + companyData.get("companyAddress") + " " + newOrderService.findAll().get(0).getPeriod() + " ( " + newOrderService.findAll().get(0).getPeriodDate() + " )");
        } else {
            cell.setCellValue("Rozliczenie_księgowość - ( - )");
        }
        cell.setCellStyle(styleTableTitle);
        for (int i = 1; i <= 3; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(styleTableTitle);
        }
        rowCounter++;

        header = sheet.createRow(rowCounter);
        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(styleHeader);
        }
        rowCounter++;

        List<FinancialDepartmentSummary> financialDepartmentSummaries = financialService.getfinancialDepartmentSummaryList();
        for (int i = 0; i < financialDepartmentSummaries.size(); i++) {
            row = sheet.createRow(rowCounter);
            cell = row.createCell(0);
            cell.setCellValue(financialDepartmentSummaries.get(i).getDepartmentName());
            cell.setCellStyle(styleContent);
            cell = row.createCell(1);
            cell.setCellValue(financialDepartmentSummaries.get(i).getDepartmentSummaryDiscountPrice().doubleValue());
            cell.setCellStyle(styleContent);
            cell = row.createCell(2);
            cell.setCellValue(financialDepartmentSummaries.get(i).getDepartmentSummaryFullPrice().doubleValue());
            cell.setCellStyle(styleContent);
            rowCounter++;
        }

        BigDecimal sumOfDepartmentDiscountPrice = financialService.getSumOfDepartmentDiscountPrice();
        BigDecimal sumOfDepartmentFullPrice = financialService.getSumOfDepartmentFullPrice();
        BigDecimal refundation = financialService.getSumOfDepartmentFullPrice().subtract(financialService.getSumOfDepartmentDiscountPrice());

        row = sheet.createRow(rowCounter);
        cell = row.createCell(0);
        cell.setCellValue("Podsumowanie");
        cell.setCellStyle(styleContent);
        cell = row.createCell(1);
        cell.setCellValue("Od pracowników: " + sumOfDepartmentDiscountPrice + " zł");
        cell.setCellStyle(styleContent);
        cell = row.createCell(2);
        cell.setCellValue("Dla dostawcy: " + sumOfDepartmentFullPrice + " zł");
        cell.setCellStyle(styleContent);
        cell = row.createCell(3);
        cell.setCellValue("Kwota dofinansowania: " + refundation + " zł");
        cell.setCellStyle(styleContent);

        OutputStream out = response.getOutputStream();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("Podsumowanie " + companyData.get("companyName") + companyData.get("companyAddress") + " KW " + (LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear()) + 1) + " " + LocalDate.now() + ".xlsx", "UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        workbook.write(out);
        workbook.close();
        out.flush();
        out.close();
    }

    @Override
    public void getDataAfterCloseWeekInXls(HttpServletResponse response) throws IOException {
        int rowCounter = 0;
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFFont fontTableTitle = workbook.createFont();
        fontTableTitle.setFontName("Arial");
        fontTableTitle.setFontHeightInPoints((short) 12);
        fontTableTitle.setBold(true);

        XSSFFont fontHeader = workbook.createFont();
        fontHeader.setFontName("Arial");
        fontHeader.setFontHeightInPoints((short) 11);
        fontHeader.setBold(true);

        XSSFFont fontContent = workbook.createFont();
        fontContent.setFontName("Arial");
        fontContent.setFontHeightInPoints((short) 10);
        fontContent.setBold(false);


        CellStyle styleTableTitle = workbook.createCellStyle();
        styleTableTitle.setFillForegroundColor(IndexedColors.WHITE1.getIndex());
        styleTableTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleTableTitle.setFont(fontTableTitle);
        styleTableTitle.setWrapText(true);
        styleTableTitle.setBorderBottom(BorderStyle.THIN);
        styleTableTitle.setBorderTop(BorderStyle.THIN);
        styleTableTitle.setBorderRight(BorderStyle.THIN);
        styleTableTitle.setBorderLeft(BorderStyle.THIN);


        CellStyle styleHeader = workbook.createCellStyle();
        styleHeader.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeader.setFont(fontHeader);
        styleHeader.setWrapText(true);
        styleHeader.setBorderBottom(BorderStyle.THIN);
        styleHeader.setBorderTop(BorderStyle.THIN);
        styleHeader.setBorderRight(BorderStyle.THIN);
        styleHeader.setBorderLeft(BorderStyle.THIN);

        CellStyle styleContent = workbook.createCellStyle();
        styleContent.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleContent.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleContent.setFont(fontContent);
        styleContent.setWrapText(true);
        styleContent.setBorderBottom(BorderStyle.THIN);
        styleContent.setBorderTop(BorderStyle.THIN);
        styleContent.setBorderRight(BorderStyle.THIN);
        styleContent.setBorderLeft(BorderStyle.THIN);

        List<OrderSummary> orderSummaryList = Optional.ofNullable(orderSummaryService.findAll()).orElse(new ArrayList<>(1));
        List<ActualOrder> actualOrderMeals = actualOrderService.findAll();
        Integer orderSummaryFirstShiftQty = 0;
        Integer orderSummarySecondShiftQty = 0;
        for (OrderSummary orderSummary : orderSummaryList) {
            orderSummaryFirstShiftQty = orderSummaryFirstShiftQty + orderSummary.getFirstShiftQuantity();
            orderSummarySecondShiftQty = orderSummarySecondShiftQty + orderSummary.getSecondShiftQuantity();
        }

        BigDecimal firstShiftPriceSum = BigDecimal.valueOf(0);
        BigDecimal secondShiftPriceSum = BigDecimal.valueOf(0);
        for (ActualOrder actualOrderMeal : actualOrderMeals) {
            if (actualOrderMeal.getShiftMon() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(actualOrderMeal.getPriceMon());
            } else if (actualOrderMeal.getShiftMon() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(actualOrderMeal.getPriceMon());
            }
            if (actualOrderMeal.getShiftTue() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(actualOrderMeal.getPriceTue());
            } else if (actualOrderMeal.getShiftTue() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(actualOrderMeal.getPriceTue());
            }
            if (actualOrderMeal.getShiftWed() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(actualOrderMeal.getPriceWed());
            } else if (actualOrderMeal.getShiftWed() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(actualOrderMeal.getPriceWed());
            }
            if (actualOrderMeal.getShiftThu() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(actualOrderMeal.getPriceThu());
            } else if (actualOrderMeal.getShiftThu() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(actualOrderMeal.getPriceThu());
            }
            if (actualOrderMeal.getShiftFri() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(actualOrderMeal.getPriceFri());
            } else if (actualOrderMeal.getShiftFri() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(actualOrderMeal.getPriceFri());
            }
        }

        Sheet sheet = workbook.createSheet("wykaz zamówień");

        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 1500);
        sheet.setColumnWidth(2, 22000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 13000);

        sheet.addMergedRegion(new CellRangeAddress(
                0, // pierwszy wiersz (0-based)
                0, // ostatni wiersz (0-based)
                0, // pierwsza kolumna (0-based)
                4  // ostatnia kolumna (0-based)
        ));

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Dane dostawcy");
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(1);
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(2);
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(3);
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(4);
        cell.setCellStyle(styleTableTitle);

        rowCounter++;

        sheet.addMergedRegion(new CellRangeAddress(
                rowCounter, // pierwszy wiersz (0-based)
                rowCounter, // ostatni wiersz (0-based)
                0, // pierwsza kolumna (0-based)
                4  // ostatnia kolumna (0-based)
        ));

        row = sheet.createRow(rowCounter);
        cell = row.createCell(0);
        cell.setCellValue(companyData.get("companyAddress"));
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(1);
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(2);
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(3);
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(4);
        cell.setCellStyle(styleTableTitle);
        rowCounter++;

        row = sheet.createRow(rowCounter);
        cell = row.createCell(3);
        cell.setCellValue("I zmiana");
        cell.setCellStyle(styleTableTitle);

        cell = row.createCell(4);
        cell.setCellValue(orderSummaryList.isEmpty() ? "Okres - ( -) " : "okres " + orderSummaryList.get(0).getPeriod() + " ( " + orderSummaryList.get(0).getPeriodDate() + " )");
        cell.setCellStyle(styleTableTitle);
        rowCounter++;

        Row header = sheet.createRow(rowCounter);

        String[] headers = {"Dzień tygodnia", "nr", "Menu", "Liczba Obiadów", "Numer osoby: " + companyData.get("companyMarker") + " + numer osoby np. " + companyData.get("companyMarker") + "37"};
        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(styleHeader);
        }

        rowCounter++;


//            sheet.addMergedRegion(new CellRangeAddress(
//                    4, // pierwszy wiersz (0-based)
//                    4 + 7, // ostatni wiersz (0-based)
//                    0, // pierwsza kolumna (0-based)
//                    0  // ostatnia kolumna (0-based)
//            ));


        for (int i = 0; i < orderSummaryList.size(); i++) {

            if (orderSummaryList.get(i).getFirstShiftQuantity() != 0) {
                Integer dayId = orderSummaryList.get(i).getDayId();

                String day = "";
                switch (dayId) {
                    case 1:
                        day = "Poniedziałek";
                        break;
                    case 2:
                        day = "Wtorek";
                        break;
                    case 3:
                        day = "Środa";
                        break;
                    case 4:
                        day = "Czwartek";
                        break;
                    case 5:
                        day = "Piątek";
                        break;
                }

                row = sheet.createRow(rowCounter);
                cell = row.createCell(0);
                cell.setCellValue(day);
                cell.setCellStyle(styleContent);
                cell = row.createCell(1);
                cell.setCellValue(orderSummaryList.get(i).getMealNo());
                cell.setCellStyle(styleContent);
                cell = row.createCell(2);
                cell.setCellValue(orderSummaryList.get(i).getMealName());
                cell.setCellStyle(styleContent);
                cell = row.createCell(3);
                cell.setCellValue(orderSummaryList.get(i).getFirstShiftQuantity());
                cell.setCellStyle(styleContent);
                cell = row.createCell(4);
                cell.setCellValue(orderSummaryList.get(i).getFirstShiftUsersId());
                cell.setCellStyle(styleContent);

                rowCounter++;
            }
        }
        row = sheet.createRow(rowCounter);
        cell = row.createCell(2);
        cell.setCellValue("Ilość posiłków I zmiana");
        cell.setCellStyle(styleHeader);
        cell = row.createCell(3);
        cell.setCellValue(orderSummaryFirstShiftQty);
        cell.setCellStyle(styleHeader);
        cell = row.createCell(4);
        cell.setCellStyle(styleHeader);
        cell.setCellValue("Łączna kwota za posiłki: " + firstShiftPriceSum + " zł.");
        rowCounter++;

        rowCounter++;
        rowCounter++;

        sheet.addMergedRegion(new CellRangeAddress(
                rowCounter, // pierwszy wiersz (0-based)
                rowCounter, // ostatni wiersz (0-based)
                0, // pierwsza kolumna (0-based)
                4  // ostatnia kolumna (0-based)
        ));

        row = sheet.createRow(rowCounter);
        cell = row.createCell(0);
        cell.setCellValue("Dane dostawcy");
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(1);
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(2);
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(3);
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(4);
        cell.setCellStyle(styleTableTitle);

        rowCounter++;

        sheet.addMergedRegion(new CellRangeAddress(
                rowCounter, // pierwszy wiersz (0-based)
                rowCounter, // ostatni wiersz (0-based)
                0, // pierwsza kolumna (0-based)
                4  // ostatnia kolumna (0-based)
        ));

        row = sheet.createRow(rowCounter);
        cell = row.createCell(0);
        cell.setCellValue(companyData.get("companyAddress"));
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(1);
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(2);
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(3);
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(4);
        cell.setCellStyle(styleTableTitle);
        rowCounter++;

        row = sheet.createRow(rowCounter);
        cell = row.createCell(3);
        cell.setCellValue("II zmiana");
        cell.setCellStyle(styleTableTitle);

        cell = row.createCell(4);
        cell.setCellValue(orderSummaryList.isEmpty() ? "Okres - ( -) " : "okres " + orderSummaryList.get(0).getPeriod() + " ( " + orderSummaryList.get(0).getPeriodDate() + " )");
        cell.setCellStyle(styleTableTitle);
        rowCounter++;

        header = sheet.createRow(rowCounter);

        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(styleHeader);
        }
        rowCounter++;

        for (int i = 0; i < orderSummaryList.size(); i++) {
            if (orderSummaryList.get(i).getSecondShiftQuantity() != 0) {
                Integer dayId = orderSummaryList.get(i).getDayId();
                String day = "";
                switch (dayId) {
                    case 1:
                        day = "Poniedziałek";
                        break;
                    case 2:
                        day = "Wtorek";
                        break;
                    case 3:
                        day = "Środa";
                        break;
                    case 4:
                        day = "Czwartek";
                        break;
                    case 5:
                        day = "Piątek";
                        break;
                }
                row = sheet.createRow(rowCounter);
                cell = row.createCell(0);
                cell.setCellValue(day);
                cell.setCellStyle(styleContent);
                cell = row.createCell(1);
                cell.setCellValue(orderSummaryList.get(i).getMealNo());
                cell.setCellStyle(styleContent);
                cell = row.createCell(2);
                cell.setCellValue(orderSummaryList.get(i).getMealName());
                cell.setCellStyle(styleContent);
                cell = row.createCell(3);
                cell.setCellValue(orderSummaryList.get(i).getSecondShiftQuantity());
                cell.setCellStyle(styleContent);
                cell = row.createCell(4);
                cell.setCellValue(orderSummaryList.get(i).getSecondShiftUsersId());
                cell.setCellStyle(styleContent);
                rowCounter++;
            }

        }
        row = sheet.createRow(rowCounter);
        cell = row.createCell(2);
        cell.setCellValue("Ilość posiłków II zmiana");
        cell.setCellStyle(styleHeader);
        cell = row.createCell(3);
        cell.setCellValue(orderSummarySecondShiftQty);
        cell.setCellStyle(styleHeader);
        cell = row.createCell(4);
        cell.setCellValue("Łączna kwota za posiłki: " + secondShiftPriceSum + " zł.");
        cell.setCellStyle(styleHeader);


//###################################################################################
        sheet = workbook.createSheet("Rozliczenie obiadów");
        rowCounter = 0;

        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 2500);
        sheet.setColumnWidth(2, 3000);
        sheet.setColumnWidth(3, 3500);

        sheet.addMergedRegion(new CellRangeAddress(
                rowCounter, // pierwszy wiersz (0-based)
                rowCounter, // ostatni wiersz (0-based)
                0, // pierwsza kolumna (0-based)
                3  // ostatnia kolumna (0-based)
        ));

        row = sheet.createRow(rowCounter);
        cell = row.createCell(0);
        cell.setCellValue("Rozliczenie dla dostawcy.");
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(1);
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(2);
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(3);
        cell.setCellStyle(styleTableTitle);
        rowCounter++;

        sheet.addMergedRegion(new CellRangeAddress(
                rowCounter, // pierwszy wiersz (0-based)
                rowCounter, // ostatni wiersz (0-based)
                0, // pierwsza kolumna (0-based)
                3  // ostatnia kolumna (0-based)
        ));

        row = sheet.createRow(rowCounter);
        row.setHeight((short) 650);
        cell = row.createCell(0);
        cell.setCellStyle(styleTableTitle);
        if (!orderSummaryList.isEmpty()) {
            cell.setCellValue(companyData.get("companyName") + ", " + companyData.get("companyAddress") + " " + orderSummaryList.get(0).getPeriod() + " ( " + orderSummaryList.get(0).getPeriodDate() + " )");
        } else {
            cell.setCellValue(companyData.get("companyName") + ", " + companyData.get("companyAddress") + " - ( - )");
        }
//        cell.setCellValue(companyData.get("companyName") + ", " + companyData.get("companyAddress") + " " + orderSummaryList.get(0).getPeriod() + " ( " + orderSummaryList.get(0).getPeriodDate() + " )");
        cell = row.createCell(1);
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(2);
        cell.setCellStyle(styleTableTitle);
        cell = row.createCell(3);
        cell.setCellStyle(styleTableTitle);
        rowCounter++;

        header = sheet.createRow(rowCounter);

        headers = new String[]{"Rodzaj", "Ilość (szt.)", "cena (zł)", "Kwota (zł)"};
        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(styleHeader);
        }
        rowCounter++;

        List<Price> priceList = priceService.findAll();
        List<OrderSummary> orderSummaryListCalculation = orderSummaryService.findAll();
        Integer counter = 0;
        Integer totalQty = 0;
        BigDecimal priceSum = BigDecimal.valueOf(0);
        BigDecimal totalSum = BigDecimal.valueOf(0);
        for (Price price : priceList) {
            if (!(price.getPrice().compareTo(BigDecimal.valueOf(0)) == 0)) {
                for (OrderSummary orderSummary : orderSummaryListCalculation) {

                    if (price.getPrice().compareTo(orderSummary.getMealPrice()) == 0) {
                        counter += orderSummary.getFirstShiftQuantity() + orderSummary.getSecondShiftQuantity();
                        if (price.getDescription().substring(0, 2).equals("2x")) {
                            priceSum = (price.getPrice().divide(BigDecimal.valueOf(2))).multiply(BigDecimal.valueOf(counter));
                        } else {
                            priceSum = price.getPrice().multiply(BigDecimal.valueOf(counter));
                        }
                    }
                }
                row = sheet.createRow(rowCounter);
                cell = row.createCell(0);
                cell.setCellValue(price.getDescription());
                cell.setCellStyle(styleContent);
                cell = row.createCell(1);
                cell.setCellValue(counter);
                cell.setCellStyle(styleContent);
                if (price.getDescription().substring(0, 2).equals("2x")) {
                    cell = row.createCell(2);
                    cell.setCellValue(price.getPrice().doubleValue() / 2);
                    cell.setCellStyle(styleContent);
                } else {
                    cell = row.createCell(2);
                    cell.setCellValue(price.getPrice().doubleValue());
                    cell.setCellStyle(styleContent);
                }
                cell = row.createCell(3);
                cell.setCellValue(priceSum.doubleValue());
                cell.setCellStyle(styleContent);
                totalSum = totalSum.add(priceSum);
                totalQty += counter;
                counter = 0;
                priceSum = BigDecimal.valueOf(0);
                rowCounter++;
            }
        }
        rowCounter++;

        row = sheet.createRow(rowCounter);
        cell = row.createCell(2);
        cell.setCellValue("suma ( zł ): ");
        cell.setCellStyle(styleContent);
        cell = row.createCell(3);
        cell.setCellValue(totalSum.doubleValue());
        cell.setCellStyle(styleContent);

        rowCounter++;

        row = sheet.createRow(rowCounter);
        cell = row.createCell(2);
        cell.setCellValue("ilość ( szt.) : ");
        cell.setCellStyle(styleContent);
        cell = row.createCell(3);
        cell.setCellValue(totalQty);
        cell.setCellStyle(styleContent);

        OutputStream out = response.getOutputStream();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("Rozliczenie_ " + companyData.get("companyName") + companyData.get("companyAddress") + " KW " + (LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear()) + 1) + " " + LocalDate.now() + ".xlsx", "UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        workbook.write(out);
        workbook.close();
        out.flush();
        out.close();
    }
}
