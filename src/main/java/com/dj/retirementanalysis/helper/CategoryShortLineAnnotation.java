package com.dj.retirementanalysis.helper;

import org.jfree.chart.annotations.CategoryAnnotation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.AnnotationChangeListener;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class CategoryShortLineAnnotation implements CategoryAnnotation {
    private final Comparable<?> category;
    private final double y;
    private final Paint paint;
    private final Stroke stroke;

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
