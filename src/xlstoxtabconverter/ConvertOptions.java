/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xlstoxtabconverter;

import java.io.File;

/**
 * Contains different convert options
 *
 * @author sebastianmair
 */
public class ConvertOptions {

    private final PathOption pathOption;
    private final NameOption nameOption;

    public ConvertOptions(PathOption _pathOption, NameOption _nameOption) {
        pathOption = _pathOption;
        nameOption = _nameOption;
    }

    public ConvertOptions() {
        this(PathOption.SamePath, NameOption.SameName);
    }

    public PathOption getPathOption() {
        return pathOption;
    }

    public NameOption getNameOption() {
        return nameOption;
    }

    /**
     * Option that describes how the path of the converted file should be
     * created
     */
    public enum PathOption {
        /**
         * The converted file should have the same path as the origin file
         */
        SamePath,
        /**
         * The converted file should have a different path from the origin file
         */
        DifferentPath;

        private String path;

        public String getPath() {
            return path;
        }

        public PathOption withPath(String _path) {
            path = correctFilePath(_path);
            return this;
        }

        /**
         * This method adds the path separator to the end of the path if
         * necessary
         *
         * @param filePath
         * @return corrected file path
         */
        private static String correctFilePath(String filePath) {
            if (filePath.endsWith(File.separator)) {
                return filePath;
            } else {
                return filePath + File.separator;
            }
        }
    }

    /**
     * Option that describes how the file name of the converted file should be
     * created
     */
    public enum NameOption {
        /**
         * The converted file should have the same file name as the origin file
         */
        SameName,
        /**
         * The converted file should have a different file name from the origin
         * file
         */
        DifferentName;

        private String name;

        public String getName() {
            return name;
        }

        public NameOption withName(String _name) {
            name = correctXtabFileName(_name);
            return this;
        }

        /**
         * This method adds the xtab-suffix to the given file-name if it is
         * necessary
         *
         * @param xtabFileName
         * @return corrected file name
         */
        private static String correctXtabFileName(String xtabFileName) {
            if (!xtabFileName.endsWith(XlsToXtabConverter.XTABSUFFIX)) {
                xtabFileName += XlsToXtabConverter.XTABSUFFIX;
            }
            return xtabFileName;
        }
    }
}
