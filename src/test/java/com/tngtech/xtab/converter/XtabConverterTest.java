package com.tngtech.xtab.converter;

import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;

public class XtabConverterTest {

    @Test
    public void testXlsFileConversion() throws Exception {
        File XLS = fileOf("/origin.xls");
        File XTAB = fileOf("/origin.xls").toPath().resolveSibling("origin.xtab").toFile();
        File XTAB_FIXTURE = fileOf("/fixture.xtab");
        assertThat(XTAB).doesNotExist();

        XtabConverter.convertXlsToXtab(XLS.getAbsolutePath());

        assertThat(contentOf(XTAB)).isXmlEqualToContentOf(XTAB_FIXTURE);
        XTAB.deleteOnExit();
    }

    @Test
    public void testXlsDateConversion() throws Exception {
        File XLS = fileOf("/dates_and_timestamps_origin.xls");
        File XTAB = fileOf("/dates_and_timestamps_origin.xls").toPath().resolveSibling("dates_and_timestamps_origin.xtab").toFile();
        File XTAB_FIXTURE = fileOf("/dates_and_timestamps_fixture.xtab");
        assertThat(XTAB).doesNotExist();

        XtabConverter.convertXlsToXtab(XLS.getAbsolutePath());

        assertThat(contentOf(XTAB)).isXmlEqualToContentOf(XTAB_FIXTURE);
        XTAB.deleteOnExit();
    }

    private static File fileOf(String path) throws Exception {
        return new File(XtabConverterTest.class.getResource(path).toURI());
    }
}