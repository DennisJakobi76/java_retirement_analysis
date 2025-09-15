package com.dj.retirementanalysis.helper;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ChartUtils;

import org.jfree.data.category.DefaultCategoryDataset;

import org.jfree.chart.annotations.CategoryTextAnnotation;
import org.jfree.chart.ui.TextAnchor;


import java.awt.*;
import java.io.File;


public class ChartPngBuilder {

    public static void buildPng(
            double gesetzlich25, double sonstige25, double betrieblich25, double privat25,
            double gesetzlich50, double sonstige50, double betrieblich50, double privat50,
            double netto25, double netto50,
            double richt25, double richt50,
            double mind25,  double mind50,
            File outFile
    ) throws Exception {

        String J2025 = "2025";
        String J2050 = "2050";

        DefaultCategoryDataset bars = new DefaultCategoryDataset();
        bars.addValue(gesetzlich25, "Gesetzliche Rente", J2025);
        bars.addValue(sonstige25,   "Sonstige Einnahmen", J2025);
        bars.addValue(betrieblich25,"Betriebliche & geförderte Vorsorge", J2025);
        bars.addValue(privat25,     "Private Vorsorge", J2025);

        bars.addValue(gesetzlich50, "Gesetzliche Rente", J2050);
        bars.addValue(sonstige50,   "Sonstige Einnahmen", J2050);
        bars.addValue(betrieblich50,"Betriebliche & geförderte Vorsorge", J2050);
        bars.addValue(privat50,     "Private Vorsorge", J2050);

        double sum25 = gesetzlich25 + sonstige25 + betrieblich25 + privat25;
        double sum50 = gesetzlich50 + sonstige50 + betrieblich50 + privat50;
        double cap25 = Math.max(0, netto25 - sum25);
        double cap50 = Math.max(0, netto50 - sum50);
        bars.addValue(cap25, "Netto-Einkommen", J2025);
        bars.addValue(cap50, "Netto-Einkommen", J2050);

        JFreeChart chart = ChartFactory.createStackedBarChart(
                "Altersvorsorge: Einnahmen, Netto-Einkommen, Richtwert & Mindestsoll",
                "Jahr",
                "€ / Monat",
                bars
        );

        CategoryPlot plot = chart.getCategoryPlot();
        StackedBarRenderer r = (StackedBarRenderer) plot.getRenderer();
        r.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter()); // flat
        r.setShadowVisible(false);
        r.setDefaultItemLabelsVisible(false);
        r.setDrawBarOutline(false);

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

        r.setSeriesPaint(bars.getRowIndex("Gesetzliche Rente"), new Color(44, 160, 44));
        r.setSeriesPaint(bars.getRowIndex("Sonstige Einnahmen"), new Color(127, 201, 127));
        r.setSeriesPaint(bars.getRowIndex("Betriebliche & geförderte Vorsorge"), new Color(31, 122, 31));
        r.setSeriesPaint(bars.getRowIndex("Private Vorsorge"), new Color(0, 128, 128));
        r.setSeriesPaint(bars.getRowIndex("Netto-Einkommen"), new Color(102, 187, 102));

        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setAutoRangeIncludesZero(true);
        yAxis.setTickUnit(new org.jfree.chart.axis.NumberTickUnit(200));
        yAxis.setNumberFormatOverride(new java.text.DecimalFormat("#,##0"));

        plot.setInsets(new RectangleInsets(8, 8, 8, 8));
        plot.setOutlineVisible(false);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(new Color(220, 220, 220));

        BasicStroke solid2 = new BasicStroke(2f);

//        plot.addAnnotation(new CategoryShortLineAnnotation(J2025, richt25, new Color(25,118,210), solid2, 1.16));
        plot.addAnnotation(new CategoryShortLineAnnotation(J2050, richt50, new Color(25,118,210), solid2, 1.16));

//        plot.addAnnotation(new CategoryShortLineAnnotation(J2025, mind25, new Color(253,216,53), solid2, 1.16));
        plot.addAnnotation(new CategoryShortLineAnnotation(J2050, mind50, new Color(253,216,53), solid2, 1.16));

//        String gap2025Label = "Lücke: " + (int)(richt25 - sum25) + " €";
//        CategoryTextAnnotation gapAnno2025 =
//                new CategoryTextAnnotation(gap2025Label, J2025, richt25 - 1);
//        gapAnno2025.setFont(new Font("SansSerif", Font.BOLD, 12));
//        gapAnno2025.setTextAnchor(TextAnchor.TOP_CENTER);
//        plot.addAnnotation(gapAnno2025);

        String gap2050Label = "Lücke: " + (int)(richt50 - sum50) + " €";
        CategoryTextAnnotation gapAnno2050 =
                new CategoryTextAnnotation(gap2050Label, J2050, richt50 - 1);
        gapAnno2050.setFont(new Font("SansSerif", Font.BOLD, 12));
        gapAnno2050.setTextAnchor(TextAnchor.TOP_CENTER);
        plot.addAnnotation(gapAnno2050);

        ChartUtils.saveChartAsPNG(outFile, chart, 820, 520);
    }
}
