/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xlstoxtabconverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;

/**
 *
 * @author sebastianmair
 */
public class XlsToXtabConverterTest {

    private static final String XLSFILENAME = "TestXls.xls";

    private String xlsFilePath;
    private String xtabFilePath;
    private IDataSet xlsDataSet;
    private IDataSet xtabDataSet;

    @Before
    public void setUp() {
        xlsFilePath = getClass().getResource(XLSFILENAME).getFile();
        try {
            xlsDataSet = new XlsDataSet(new File(xlsFilePath));
        } catch (IOException | DataSetException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void convertXlsToXtabTest() {
        try {
            XlsToXtabConverter.convertXlsToXtab(xlsFilePath, new ConvertOptions());
        } catch (IllegalArgumentException | FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        xtabFilePath = XlsToXtabConverter.getFileNameWithoutSuffix(xlsFilePath)
                + XlsToXtabConverter.XTABSUFFIX;
        try {
            xtabDataSet = new XmlDataSet(new FileReader(xtabFilePath));
        } catch (DataSetException | FileNotFoundException ex) {
            fail(ex.getMessage());
        }

        ITableIterator xlsIt = null;
        ITableIterator xtabIt = null;
        try {
            xlsIt = xlsDataSet.iterator();
            xtabIt = xtabDataSet.iterator();
        } catch (DataSetException ex) {
            fail(ex.getMessage());
        }

        try {
            while (xlsIt.next() && xtabIt.next()) {
                ITable xlsTab = xlsIt.getTable();
                ITable xtabTab = xtabIt.getTable();
                ITableMetaData xlsMeta = xlsTab.getTableMetaData();
                ITableMetaData xtabMeta = xtabTab.getTableMetaData();
                Assert.assertEquals(xlsMeta.getTableName(), xtabMeta.getTableName());

                int height = xlsTab.getRowCount();
                int width = xlsMeta.getColumns().length;
                Assert.assertEquals(height, xtabTab.getRowCount());
                Assert.assertEquals(width, xtabMeta.getColumns().length);

                for (int i = 0; i < width; i++) {
                    String column = xlsMeta.getColumns()[i].getColumnName();
                    Assert.assertEquals(column, xtabMeta.getColumns()[i].getColumnName());
                    for (int j = 0; j < height; j++) {
                        Assert.assertEquals(xlsTab.getValue(j, column), xtabTab.getValue(j, column));
                    }
                }
            }
            if (xlsIt.next() || xtabIt.next()) {
                fail("Not the same number of tables");
            }
        } catch (DataSetException ex) {
            fail(ex.getMessage());
        }
    }
}
