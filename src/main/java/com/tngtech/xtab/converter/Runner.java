/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tngtech.xtab.converter;

import java.io.FileNotFoundException;

/**
 * This class runs the whole converting.
 *
 * @author sebastianmair
 */
public class Runner {

    private static final String MSGFILEPATH = "Path of the xls-file: ";
    private static final String MSGSAMEFOLDER
            = "Save xtab-file in the same folder? -> ";
    private static final String MSGFOLDERPATH = "Folder path of the xtab-file: ";
    private static final String MSGSAMENAME
            = "Name xtab-file with same name as xls-file? -> ";
    private static final String MSGFILENAME = "Name of the xtab-file: ";

    public static void main(String[] args) {
        if (args.length == 0) {
            runConverting();
        } else {
            runConverting(args[0].trim());
        }
    }

    private static void runConverting() {
        while (true) {
            String xlsFilePath;
            ConvertOptions.PathOption pathOption;
            ConvertOptions.NameOption nameOption;
            Reader.printStopMsg();
            try {
                xlsFilePath = Reader.readString(MSGFILEPATH);

                if (!Reader.readBoolean(MSGSAMEFOLDER)) {
                    pathOption = ConvertOptions.PathOption.DifferentPath.
                            withPath(Reader.readString(MSGFOLDERPATH));
                } else {
                    pathOption = ConvertOptions.PathOption.SamePath;
                }

                if (!Reader.readBoolean(MSGSAMENAME)) {
                    nameOption = ConvertOptions.NameOption.DifferentName.
                            withName(Reader.readString(MSGFILENAME));
                } else {
                    nameOption = ConvertOptions.NameOption.SameName;
                }
            } catch (Exception ex) {
                return;
            }

            try {
                XlsToXtabConverter.convertXlsToXtab(xlsFilePath,
                        new ConvertOptions(pathOption, nameOption));
                break;
            } catch (FileNotFoundException ex) {
                System.out.println(ex.getMessage());
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                break;
            }
        }
    }

    private static void runConverting(String s) {
        try {
            XlsToXtabConverter.convertXlsToXtab(s, new ConvertOptions());
        } catch (FileNotFoundException | IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
