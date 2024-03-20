package pl.nawrockiit.catering3.xlsx;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ExcelExportService {

   void getOrderSummeryInXls(HttpServletResponse response) throws IOException;
   void getUsersListInXls(HttpServletResponse response) throws IOException;

   void getDataBeforeCloseWeekInXls(HttpServletResponse response) throws IOException;

   void getDataAfterCloseWeekInXls(HttpServletResponse response) throws IOException;

}
