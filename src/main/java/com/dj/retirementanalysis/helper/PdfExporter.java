package com.dj.retirementanalysis.models;

import com.dj.retirementanalysis.models.RetirementAnalysis;
import org.openpdf.text.*;
import org.openpdf.text.pdf.*;
import org.openpdf.text.*;
import org.openpdf.text.Font;
import org.openpdf.text.Image;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;

public class PdfExporter {

    public static void exportRetirementAnalysisPdf(RetirementAnalysis analysis, File pdfFile, File chartFile) throws Exception {
        Document document = new Document(PageSize.A4, 36, 36, 54, 36); // A4 mit Rändern
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

        document.open();

        // Titel
        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Auswertung Altersvorsorge", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(Chunk.NEWLINE);

        // Einleitender Text
        Font normalFont = new Font(Font.HELVETICA, 11);
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

        // Tabelle
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(80);
        table.setWidths(new float[]{3, 2, 2});

        Font headerFont = new Font(Font.HELVETICA, 11, Font.BOLD, Color.WHITE);

        PdfPCell h1 = new PdfPCell(new Phrase("Vorsorgequelle", headerFont));
        PdfPCell h2 = new PdfPCell(new Phrase("2025 (€ / Monat)", headerFont));
        PdfPCell h3 = new PdfPCell(new Phrase(analysis.getProjectionYear() + " (€ / Monat)", headerFont));

        h1.setBackgroundColor(Color.BLUE);
        h2.setBackgroundColor(Color.BLUE);
        h3.setBackgroundColor(Color.BLUE);

        table.addCell(h1);
        table.addCell(h2);
        table.addCell(h3);

        table.addCell("Gesetzliche Rente");
        table.addCell(String.valueOf(analysis.getStatutoryPension()));
        table.addCell(String.valueOf(analysis.getStatutoryPensionProjection()));

        table.addCell("Sonstige Einnahmen");
        table.addCell(String.valueOf(analysis.getOtherIncome()));
        table.addCell(String.valueOf(analysis.getOtherIncomeProjection()));

        table.addCell("Betriebliche & geförderte Vorsorge");
        table.addCell(String.valueOf(analysis.getOccupationalPension()));
        table.addCell(String.valueOf(analysis.getOccupationalPensionProjection()));

        table.addCell("Private Vorsorge");
        table.addCell(String.valueOf(analysis.getPrivatePension()));
        table.addCell(String.valueOf(analysis.getPrivatePensionProjection()));

        PdfPCell sumCell = new PdfPCell(new Phrase("Gesamtsumme", new Font(Font.HELVETICA, 11, Font.BOLD)));
        table.addCell(sumCell);
        table.addCell(String.valueOf(analysis.getTotalCurrent()));
        table.addCell(String.valueOf(analysis.getTotalProjection()));

        document.add(table);

        document.add(Chunk.NEWLINE);

        // Diagramm einfügen
        Image chartImg = Image.getInstance(chartFile.getAbsolutePath());
        chartImg.scaleToFit(500, 300);
        chartImg.setAlignment(Image.ALIGN_CENTER);
        document.add(chartImg);

        document.add(Chunk.NEWLINE);

        // Fazit
        Paragraph outro = new Paragraph(
                "Aus der Gegenüberstellung ergibt sich eine Versorgungslücke: " +
                        "Trotz solider Basis durch die gesetzliche Rente und sonstige Einnahmen " +
                        "reichen die betrieblichen und privaten Vorsorgeleistungen aktuell nicht aus, " +
                        "um den Richtwert vollständig zu erreichen.\n\n" +
                        "Die Lücke beträgt im Jahr 2025 rund " + (int)analysis.getGapCurrent() + " €, " +
                        "im Jahr " + analysis.getProjectionYear() + " – inflationsbedingt – etwa " +
                        (int)analysis.getGapProjection() + " €.\n" +
                        "Eine gezielte Stärkung der betrieblichen und privaten Vorsorge ist daher empfehlenswert.", normalFont
        );
        outro.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(outro);

        document.close();
    }
}

