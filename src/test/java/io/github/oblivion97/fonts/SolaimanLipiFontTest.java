package io.github.oblivion97.fonts;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.fonts.FontUtil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks that the font is registered with JasperReports from the classpath alone, and that a report using it
 * embeds the font in the exported PDF.
 */
class SolaimanLipiFontTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "SolaimanLipi | false | false | SolaimanLipi",
            "SolaimanLipi | true  | false | SolaimanLipi",
    })
    void registersAndEmbedsFont(String family, boolean bold, boolean italic, String expectedPdfFont) throws Exception {
        Collection<String> families = FontUtil.getInstance(DefaultJasperReportsContext.getInstance()).getFontFamilyNames();
        assertTrue(families.contains(family), "family not registered: " + family + " in " + families);

        String pdf = renderPdf(family, bold, italic);
        assertTrue(pdf.contains(expectedPdfFont), "PDF does not embed " + expectedPdfFont + " for " + family);
    }

    private static String renderPdf(String family, boolean bold, boolean italic) throws Exception {
        JasperDesign design = new JasperDesign();
        design.setName("font-test");
        design.setPageWidth(400);
        design.setPageHeight(100);
        design.setColumnWidth(360);
        design.setLeftMargin(20);
        design.setRightMargin(20);
        design.setTopMargin(20);
        design.setBottomMargin(20);

        JRDesignStaticText text = new JRDesignStaticText();
        text.setX(0);
        text.setY(0);
        text.setWidth(360);
        text.setHeight(30);
        text.setText("মোট টাকা: ১,২৩৪.৫৬ Total");
        text.setFontName(family);
        text.setBold(bold);
        text.setItalic(italic);

        JRDesignBand title = new JRDesignBand();
        title.setHeight(40);
        title.addElement(text);
        design.setTitle(title);

        JasperReport report = JasperCompileManager.compileReport(design);
        JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>(), new JREmptyDataSource());
        // PDF font names are ASCII in the font dictionaries; ISO-8859-1 keeps every byte as one char
        return new String(JasperExportManager.exportReportToPdf(print), StandardCharsets.ISO_8859_1);
    }
}
