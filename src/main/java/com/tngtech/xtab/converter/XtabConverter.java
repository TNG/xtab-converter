package com.tngtech.xtab.converter;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSetWithDatePatched;
import org.dbunit.dataset.xml.XmlDataSetWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class XtabConverter {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("usage: java ... <path to source file>");
        }
        convertXlsToXtab(args[0]);
    }

    static void convertXlsToXtab(String pathname) throws Exception {
        File sourceFilePath = new File(pathname);
        IDataSet dataSet = readXlsAsDataSet(sourceFilePath);
        Path targetPath = deriveTargetPathFromSource(sourceFilePath);
        writeDataSetAsXtab(dataSet, targetPath);
    }

    private static XlsDataSetWithDatePatched readXlsAsDataSet(File sourceFilePath) throws Exception {
        return new XlsDataSetWithDatePatched(sourceFilePath);
    }

    private static Path deriveTargetPathFromSource(File source) {
        Path sourcePath = source.toPath();
        String targetFileName = sourcePath.getFileName().toString().replaceFirst("\\.xls", ".xtab");
        return sourcePath.resolveSibling(targetFileName);
    }

    private static void writeDataSetAsXtab(IDataSet dataSet, Path targetPath) throws Exception {
        Charset encoding = Charset.forName("UTF-8");
        BufferedWriter writer = Files.newBufferedWriter(targetPath, encoding);
        writer.write("<?xml version=\"1.0\" encoding=\"" + encoding.name().toLowerCase() + "\"?>\n");
        XmlDataSetWriter xmlWriter = new XmlDataSetWriter(writer);
        xmlWriter.write(dataSet);
    }
}
