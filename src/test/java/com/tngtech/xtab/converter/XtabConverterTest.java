package com.tngtech.xtab.converter;

import com.google.common.base.Throwables;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;

public class XtabConverterTest {

    private static final File XLS = fileOf("/origin.xls");
    private static final File XTAB = fileOf("/origin.xls").toPath().resolveSibling("origin.xtab").toFile();
    private static final File XTAB_FIXTURE = fileOf("/fixture.xtab");

    @Test
    public void testXlsFileConversion() throws Exception {
        assertThat(XTAB).doesNotExist();

        XtabConverter.convertXlsToXtab(XLS.getAbsolutePath());

        assertThat(contentOf(XTAB)).isXmlEqualToContentOf(XTAB_FIXTURE);
        XTAB.deleteOnExit();
    }

    private static File fileOf(String path) {
        try {
            return new File(XtabConverterTest.class.getResource(path).toURI());
        } catch (URISyntaxException e) {
            throw Throwables.propagate(e);
        }
    }
}