package org.dbunit.dataset.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.dbunit.dataset.AbstractDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultTableIterator;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.OrderedTableNameMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Patch for the DBUnit XlsDataSet which does not return the milliseconds
 * since 1970 when a dateformatted Excel cell is encoutered.
 *
 * Instead, it returns an ISO formatted datetime string yyyy-MM-dd hh:mm:ss[.ffffffff]
 */
public class XlsDataSetWithDatePatched extends AbstractDataSet
{
  /**
   * Logger for this class
   */
  private static final Logger logger = LoggerFactory.getLogger(XlsDataSet.class);

  private final OrderedTableNameMap _tables;


  /**
   * Creates a new XlsDataSet object that loads the specified Excel document.
   */
  public XlsDataSetWithDatePatched(File file) throws IOException, DataSetException
  {
      this(new FileInputStream(file));
  }

  /**
   * Creates a new XlsDataSet object that loads the specified Excel document.
   */
  public XlsDataSetWithDatePatched(InputStream in) throws IOException, DataSetException
  {
      _tables = super.createTableNameMap();

      Workbook workbook;
      try {
          workbook = WorkbookFactory.create(in);
      } catch (InvalidFormatException e) {
          throw new IOException(e);
      }

      int sheetCount = workbook.getNumberOfSheets();
      for (int i = 0; i < sheetCount; i++)
      {
          /*
           * patch here
           */
          ITable table = new XlsTableWithDatePatched(workbook.getSheetName(i),
                  workbook.getSheetAt(i));
          /*
           * end patch
           */
          _tables.add(table.getTableMetaData().getTableName(), table);
      }
  }

  /**
   * Write the specified dataset to the specified Excel document.
   */
  public static void write(IDataSet dataSet, OutputStream out)
          throws IOException, DataSetException
  {
      logger.debug("write(dataSet={}, out={}) - start", dataSet, out);

      new XlsDataSetWriter().write(dataSet, out);
  }


  ////////////////////////////////////////////////////////////////////////////
  // AbstractDataSet class

  protected ITableIterator createIterator(boolean reversed)
          throws DataSetException
  {
      if(logger.isDebugEnabled())
          logger.debug("createIterator(reversed={}) - start", String.valueOf(reversed));

      ITable[] tables = (ITable[]) _tables.orderedValues().toArray(new ITable[0]);
      return new DefaultTableIterator(tables, reversed);
  }
}
