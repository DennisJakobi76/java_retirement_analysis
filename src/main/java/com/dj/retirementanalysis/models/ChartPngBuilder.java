package com.dj.retirementanalysis.models;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ChartUtils;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.annotations.CategoryAnnotation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.event.AnnotationChangeListener;
import org.jfree.chart.event.AnnotationChangeEvent;


import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;

import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.data.category.CategoryDataset;

public class ChartPngBuilder {




    public static class NameValueLabelGenerator implements CategoryItemLabelGenerator {

        private final double total2025;
        private final double total2050;

        public NameValueLabelGenerator(double total2025, double total2050) {
            this.total2025 = total2025;
            this.total2050 = total2050;
        }

        @Override
        public String generateRowLabel(CategoryDataset dataset, int row) {
            return dataset.getRowKey(row).toString();
        }

        @Override
        public String generateColumnLabel(CategoryDataset dataset, int column) {
            return dataset.getColumnKey(column).toString();
        }

        @Override
        public String generateLabel(CategoryDataset dataset, int row, int column) {
            String name = dataset.getRowKey(row).toString();
            Number value = dataset.getValue(row, column);

            // Für Netto-Einkommen Sonderlogik
            if ("Netto-Einkommen".equals(name)) {
                if ("2025".equals(dataset.getColumnKey(column).toString())) {
                    return "Netto-Einkommen: " + (int) total2025;
                } else {
                    return "Netto-Einkommen: " + (int) total2050;
                }
            }

            return name + ": " + (value != null ? value.intValue() : "");
        }
    }


    // Kleine Annotation-Klasse für "kurze Linie im jeweiligen Jahr"
    public static class CategoryShortLineAnnotation implements CategoryAnnotation {
        private final Comparable<?> category;
        private final double y;
        private final Paint paint;
        private final Stroke stroke;
        // Anteil der Kategorienbreite (0..1), z. B. 0.85 = 85% der Breite
        private final double widthFraction;

        public CategoryShortLineAnnotation(Comparable<?> category, double y,
                                           Paint paint, Stroke stroke, double widthFraction) {
            this.category = category;
            this.y = y;
            this.paint = paint;
            this.stroke = stroke;
            this.widthFraction = widthFraction;
        }

        @Override
        public void addChangeListener(AnnotationChangeListener listener) {
            // keine Listener-Unterstützung nötig
        }

        @Override
        public void removeChangeListener(AnnotationChangeListener listener) {
            // keine Listener-Unterstützung nötig
        }


        @Override
        public void draw(Graphics2D g2,
                         CategoryPlot plot,
                         Rectangle2D dataArea,
                         CategoryAxis domainAxis,
                         ValueAxis rangeAxis) {

            CategoryDataset dataset = plot.getDataset();
            if (dataset == null) return;

            int colIdx = dataset.getColumnIndex(category);
            if (colIdx < 0) return;

            int colCount = dataset.getColumnCount();

            double xMid = domainAxis.getCategoryMiddle(
                    colIdx, colCount, dataArea, plot.getDomainAxisEdge()
            );
            double catStart = domainAxis.getCategoryStart(
                    colIdx, colCount, dataArea, plot.getDomainAxisEdge()
            );
            double catEnd = domainAxis.getCategoryEnd(
                    colIdx, colCount, dataArea, plot.getDomainAxisEdge()
            );
            double halfLen = (catEnd - catStart) * widthFraction / 2.0;

            double xx1 = xMid - halfLen;
            double xx2 = xMid + halfLen;
            double yy  = rangeAxis.valueToJava2D(y, dataArea, plot.getRangeAxisEdge());

            Stroke oldS = g2.getStroke();
            Paint  oldP = g2.getPaint();

            g2.setPaint(paint);
            g2.setStroke(stroke);
            g2.draw(new java.awt.geom.Line2D.Double(xx1, yy, xx2, yy));

            g2.setPaint(oldP);
            g2.setStroke(oldS);
        }
    }

    public static void buildPng(
            // Eingaben aus deinem Modell
            double gesetzlich25, double sonstige25, double betrieblich25, double privat25,
            double gesetzlich50, double sonstige50, double betrieblich50, double privat50,
            double netto25, double netto50,
            double richt25, double richt50,
            double mind25,  double mind50,
            File outFile
    ) throws Exception {

        // Kategorien = Jahre (wie in deiner JS-Variante)
        String J2025 = "2025";
        String J2050 = "2050";

        // Stacked-Dataset (Komponenten)
        DefaultCategoryDataset bars = new DefaultCategoryDataset();
        bars.addValue(gesetzlich25, "Gesetzliche Rente", J2025);
        bars.addValue(sonstige25,   "Sonstige Einnahmen", J2025);
        bars.addValue(betrieblich25,"Betriebliche & geförderte Vorsorge", J2025);
        bars.addValue(privat25,     "Private Vorsorge", J2025);

        bars.addValue(gesetzlich50, "Gesetzliche Rente", J2050);
        bars.addValue(sonstige50,   "Sonstige Einnahmen", J2050);
        bars.addValue(betrieblich50,"Betriebliche & geförderte Vorsorge", J2050);
        bars.addValue(privat50,     "Private Vorsorge", J2050);

        // Netto (damit Gesamthöhe = Netto)
        double sum25 = gesetzlich25 + sonstige25 + betrieblich25 + privat25;
        double sum50 = gesetzlich50 + sonstige50 + betrieblich50 + privat50;
        double cap25 = Math.max(0, netto25 - sum25);
        double cap50 = Math.max(0, netto50 - sum50);
        bars.addValue(cap25, "Netto-Einkommen", J2025);
        bars.addValue(cap50, "Netto-Einkommen", J2050);

        // Basis-Chart
        JFreeChart chart = ChartFactory.createStackedBarChart(
                "Altersvorsorge: Einnahmen, Netto-Einkommen, Richtwert & Mindestsoll",
                "Jahr",
                "€ / Monat",
                bars
        );

        // Plot/Renderer
        CategoryPlot plot = chart.getCategoryPlot();
        StackedBarRenderer r = (StackedBarRenderer) plot.getRenderer();
        r.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter()); // flat
        r.setShadowVisible(false);
        r.setDefaultItemLabelsVisible(false); // erstmal ohne Beschriftungen
        r.setDrawBarOutline(false);

        // === HIER: Labels aktivieren ===

        double total2025 = sum25 + cap25;
        double total2050 = sum50 + cap50;

        r.setDefaultItemLabelGenerator(new NameValueLabelGenerator(total2025, total2050));
        r.setDefaultItemLabelsVisible(true);
        r.setDefaultItemLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        r.setDefaultItemLabelPaint(Color.BLACK);
        r.setDefaultPositiveItemLabelPosition(
                new org.jfree.chart.labels.ItemLabelPosition(
                        org.jfree.chart.labels.ItemLabelAnchor.CENTER,
                        org.jfree.chart.ui.TextAnchor.CENTER
                )
        );

        // Farben wie in deiner JS-Lösung (Grün-Töne)
        r.setSeriesPaint(bars.getRowIndex("Gesetzliche Rente"), new Color(44, 160, 44));
        r.setSeriesPaint(bars.getRowIndex("Sonstige Einnahmen"), new Color(127, 201, 127));
        r.setSeriesPaint(bars.getRowIndex("Betriebliche & geförderte Vorsorge"), new Color(31, 122, 31));
        r.setSeriesPaint(bars.getRowIndex("Private Vorsorge"), new Color(0, 128, 128));
        r.setSeriesPaint(bars.getRowIndex("Netto-Einkommen"), new Color(102, 187, 102));

        // Achse: 100er Schritte
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setAutoRangeIncludesZero(true);
        yAxis.setTickUnit(new org.jfree.chart.axis.NumberTickUnit(300));
        yAxis.setNumberFormatOverride(new java.text.DecimalFormat("#,##0"));

        // Optik
        plot.setInsets(new RectangleInsets(8, 8, 8, 8));
        plot.setOutlineVisible(false);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(new Color(220, 220, 220));

        // --- Referenz-Linien: kurze Segmente pro Jahr (wie in JS) ---
        BasicStroke solid2 = new BasicStroke(2f);

        // Blaue Linie = Richtwert
        plot.addAnnotation(new CategoryShortLineAnnotation(J2025, richt25, new Color(25,118,210), solid2, 1.16));
        plot.addAnnotation(new CategoryShortLineAnnotation(J2050, richt50, new Color(25,118,210), solid2, 1.16));

        // Gelbe Linie = Mindestsoll (DIN 77230)
        plot.addAnnotation(new CategoryShortLineAnnotation(J2025, mind25, new Color(253,216,53), solid2, 1.16));
        plot.addAnnotation(new CategoryShortLineAnnotation(J2050, mind50, new Color(253,216,53), solid2, 1.16));

        // PNG schreiben
        ChartUtils.saveChartAsPNG(outFile, chart, 820, 520);
    }
}
