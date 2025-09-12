package com.dj.retirementanalysis.helper;

import com.dj.retirementanalysis.models.RetirementAnalysis;
import org.openpdf.text.*;
import org.openpdf.text.Font;
import org.openpdf.text.Image;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class PdfExporter {

    public static void exportRetirementAnalysisPdf(RetirementAnalysis analysis, File pdfFile, File chartFile, List<Map<String, Object>> tableData) throws Exception {
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
        document.open();

        Font titleFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Paragraph title = new Paragraph("Auswertung Altersvorsorge", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        Font normalFont = new Font(Font.HELVETICA, 10);
        Paragraph intro = new Paragraph(
                "Diese Auswertung wurde in Anlehnung an die DIN 77230 erstellt.\n" +
                        "Im Diagramm sind zwei wichtige Referenzwerte dargestellt: der empfohlene Richtwert, " +
                        "der bei etwa 80 % des letzten Nettoeinkommens liegt (blaue Linie), sowie der nach DIN 77230 " +
                        "berechnete Mindestsollwert, der sich am gesetzlichen Mindestlohn orientiert (gelbe Linie).\n\n" +
                        "Die Hochrechnung berücksichtigt die Entwicklung bis zum Jahr " + analysis.getProjectionYear() +
                        " unter Annahme einer jährlichen Inflationsrate von " + (analysis.getInflationRate() * 100) + " %.\n" +
                        "Dabei werden die einzelnen Vorsorgebausteine – gesetzliche Rente, sonstige Einnahmen, " +
                        "betriebliche sowie private Vorsorge – ausgewertet.", normalFont
        );
        intro.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(intro);
        document.add(Chunk.NEWLINE);

        // Tabelle wie im HTML-Template
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(80);
        table.setWidths(new float[]{3, 2, 2});

        Font headerFont = new Font(Font.HELVETICA, 11, Font.BOLD, Color.WHITE);
        PdfPCell h1 = new PdfPCell(new Phrase("Vorsorgequelle", headerFont));
        PdfPCell h2 = new PdfPCell(new Phrase("Bereits erreicht " + analysis.getAnalysisYear() + " (€ / Monat)", headerFont));
        PdfPCell h3 = new PdfPCell(new Phrase("Projektion " + analysis.getProjectionYear() + " (€ / Monat)", headerFont));
        h1.setBackgroundColor(Color.BLUE);
        h2.setBackgroundColor(Color.BLUE);
        h3.setBackgroundColor(Color.BLUE);
        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        h3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(h1);
        table.addCell(h2);
        table.addCell(h3);

        // Zeilen wie im Template
        Font cellFont = new Font(Font.HELVETICA, 10);
        table.addCell("Gesetzliche Rente");
        table.addCell(String.format("%,.2f", analysis.getStatutoryPension()));
        table.addCell(String.format("%,.2f", analysis.getStatutoryPensionProjection()));

        table.addCell("Sonstige Einnahmen");
        table.addCell(String.format("%,.2f", analysis.getOtherIncome()));
        table.addCell(String.format("%,.2f", analysis.getOtherIncomeProjection()));

        table.addCell("Betriebliche & geförderte Vorsorge");
        table.addCell(String.format("%,.2f", analysis.getOccupationalPension()));
        table.addCell(String.format("%,.2f", analysis.getOccupationalPensionProjection()));

        table.addCell("Private Vorsorge");
        table.addCell(String.format("%,.2f", analysis.getPrivatePension()));
        table.addCell(String.format("%,.2f", analysis.getPrivatePensionProjection()));

        PdfPCell sumCell = new PdfPCell(new Phrase("Gesamtsumme Vorsorge", new Font(Font.HELVETICA, 11, Font.BOLD)));
        sumCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(sumCell);
        table.addCell(String.format("%,.2f", analysis.getTotalCurrent()));
        table.addCell(String.format("%,.2f", analysis.getTotalProjection()));

        PdfPCell netCell = new PdfPCell(new Phrase("Netto-Einkommen", new Font(Font.HELVETICA, 11, Font.BOLD)));
        netCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(netCell);
        table.addCell(String.format("%,.2f", analysis.getNetMonthlyIncome()));
        table.addCell(String.format("%,.2f", analysis.getNetMonthlyIncomeProjection()));

        PdfPCell targetCell = new PdfPCell(new Phrase("Richtwert (80% Netto)", new Font(Font.HELVETICA, 11, Font.BOLD)));
        targetCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(targetCell);
        table.addCell(String.format("%,.2f", analysis.getTargetValue()));
        table.addCell(String.format("%,.2f", analysis.getTargetValueProjection()));

        PdfPCell minCell = new PdfPCell(new Phrase("Mindestsoll", new Font(Font.HELVETICA, 11, Font.BOLD)));
        minCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(minCell);
        table.addCell(String.format("%,.2f", analysis.getMinTargetCurrent()));
        table.addCell(String.format("%,.2f", analysis.getMinTargetProjection()));

        PdfPCell gapCell = new PdfPCell(new Phrase("Versorgungslücke", new Font(Font.HELVETICA, 11, Font.BOLD)));
        gapCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(gapCell);
        table.addCell(String.format("%,.2f", analysis.getGapCurrent()));
        table.addCell(String.format("%,.2f", analysis.getGapProjection()));

        document.add(table);
        document.add(Chunk.NEWLINE);

        Image chartImg = Image.getInstance(chartFile.getAbsolutePath());
        chartImg.scaleToFit(400, 320);
        chartImg.setAlignment(Image.ALIGN_CENTER);
        document.add(chartImg);
        document.add(Chunk.NEWLINE);

        Paragraph outro = new Paragraph(
                "Aus der Gegenüberstellung ergibt sich eine Versorgungslücke: " +
                        "Trotz solider Basis durch die gesetzliche Rente und sonstige Einnahmen " +
                        "reichen die betrieblichen und privaten Vorsorgeleistungen aktuell nicht aus, " +
                        "um den Richtwert vollständig zu erreichen.\n\n" +
                        "Die Lücke beträgt im Jahr " + analysis.getAnalysisYear() + " rund " + String.format("%,.0f", analysis.getGapCurrent()) + " €, " +
                        "im Jahr " + analysis.getProjectionYear() + " – inflationsbedingt – etwa " +
                        String.format("%,.0f", analysis.getGapProjection()) + " €.\n" +
                        "Eine gezielte Stärkung der betrieblichen und privaten Vorsorge ist daher empfehlenswert.", normalFont
        );
        outro.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(outro);

        document.close();
    }
}


