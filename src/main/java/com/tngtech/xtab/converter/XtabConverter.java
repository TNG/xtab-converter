package com.tngtech.xtab.converter;

import com.google.common.base.Charsets;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.XmlDataSetWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkArgument;

public class XtabConverter {

    public static void main(String[] args) throws Exception {
        checkArgument(args.length == 1, "usage: java ... <path to source file>");
        convertXlsToXtab(args[0]);
    }

    static void convertXlsToXtab(String pathname) throws IOException, DataSetException {
        File sourceFilePath = new File(pathname);
        IDataSet dataSet = new XlsDataSet(sourceFilePath);

        Path sourcePath = sourceFilePath.toPath();
        Path targetPath = sourcePath.resolveSibling(sourcePath.getFileName().toString().replaceFirst("\\.xls", ".xtab"));
        BufferedWriter writer = Files.newBufferedWriter(targetPath, Charsets.UTF_8);
        writer.write("<?xml version='1.0' encoding='" + Charsets.UTF_8.name() + "'?>\n");
        XmlDataSetWriter xmlWriter = new XmlDataSetWriter(writer);
        xmlWriter.write(dataSet);
    }
}
