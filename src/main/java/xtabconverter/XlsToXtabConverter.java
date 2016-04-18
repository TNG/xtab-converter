/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xtabconverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.XmlDataSetWriter;

/**
 * This class enables converting a xls-file to a xtab-file (a special xml-file)
 *
 * @author sebastianmair
 */
public class XlsToXtabConverter {

    public static final String XTABSUFFIX = ".xtab";
    private static final String ENCODING = "UTF-8";

    public static void convertXlsToXtab(String xlsFilePath,
            ConvertOptions options) throws IllegalArgumentException,
            FileNotFoundException {
        String xtabFilePath = createXtabFilePath(xlsFilePath, options);
        createXtabFile(getDataSetFromXlsFile(xlsFilePath), xtabFilePath);
    }

    private static String createXtabFilePath(String xlsFilePath,
            ConvertOptions options) {
        final String exMsg = "The given ConvertOptions is invalid";
        File xlsFile = new File(xlsFilePath);
        String xtabFilePath;
        switch (options.getPathOption()) {
            case SamePath:
                xtabFilePath = xlsFile.getParent() + File.separator;
                break;
            case DifferentPath:
                xtabFilePath = options.getPathOption().getPath();
                break;
            default:
                throw new IllegalArgumentException(exMsg);
        }

        switch (options.getNameOption()) {
            case SameName:
                xtabFilePath += getFileNameWithoutSuffix(xlsFile.getName())
                        + XTABSUFFIX;
                break;
            case DifferentName:
                xtabFilePath += options.getNameOption().getName();
                break;
            default:
                throw new IllegalArgumentException(exMsg);
        }

        return xtabFilePath;
    }

    public static String getFileNameWithoutSuffix(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    private static IDataSet getDataSetFromXlsFile(String xlsFilePath)
            throws IllegalArgumentException, FileNotFoundException {
        IDataSet dataset;
        try {
            dataset = new XlsDataSet(new File(xlsFilePath));
        } catch (IOException ex) {
            throw new FileNotFoundException("The xls-file with the given "
                    + "path does not exist");
        } catch (DataSetException ex) {
            throw new IllegalArgumentException("The xls-file with the given "
                    + "path has a wrong format");
        }
        return dataset;
    }

    private static void createXtabFile(IDataSet dataset, String xtabFilePath)
            throws IllegalArgumentException, FileNotFoundException {
        try {
            XmlDataSetWriter xmlWriter = new XmlDataSetWriter(
                    new FileWriter(new File(xtabFilePath)), ENCODING);
            xmlWriter.write(dataset);
        } catch (IOException ex) {
            throw new FileNotFoundException("The given path does not exist");
        } catch (DataSetException ex) {
            throw new IllegalArgumentException("The given dataset is invalid");
        }
    }

}
