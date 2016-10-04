package org.dbunit.dataset.excel;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.dbunit.dataset.DataSetException;
import org.joda.time.DateTime;

/**
 * Patch for the DBUnit XlsTable which does not return the milliseconds
 * since 1970 when a dateformatted Excelcell is encoutered.
 *
 * Instead, it returns an ISO formatted datetime string yyyy-MM-dd hh:mm:ss[.ffffffff]
 */
public class XlsTableWithDatePatched extends XlsTable
{

  private static final String DT_PATTERN_WITH_MILLIS = "YYYY-MM-dd HH:mm:ss.SSS";

  private static final String DT_PATTERN_WITHOUT_MILLIS="YYYY-MM-dd HH:mm:ss";

  public XlsTableWithDatePatched(String sheetName, Sheet sheet) throws DataSetException {
    super(sheetName, sheet);
  }

  protected Object getDateValue(Cell cell) {
      double numericValue = cell.getNumericCellValue();
      Date date = DateUtil.getJavaDate(numericValue);
      DateTime datetime = new DateTime(date.getTime());

      String dateTimeString = null;
      if(datetime.getMillisOfSecond() > 0) {
        dateTimeString = datetime.toString(DT_PATTERN_WITH_MILLIS) + "000000";
      } else {
        dateTimeString = datetime.toString(DT_PATTERN_WITHOUT_MILLIS);
      }

      return dateTimeString;
  }

}
