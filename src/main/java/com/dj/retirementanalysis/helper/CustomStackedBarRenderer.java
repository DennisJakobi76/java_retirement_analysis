package com.dj.retirementanalysis.helper;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.CategoryDataset;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class CustomStackedBarRenderer extends StackedBarRenderer {

    public static class CategoryLine {
        public final Comparable<?> categoryKey;
        public final double value;
        public final Paint paint;
        public final Stroke stroke;
        public final double widthFactor;

        public CategoryLine(Comparable<?> categoryKey, double value, Paint paint, Stroke stroke, double widthFactor) {
            this.categoryKey = categoryKey;
            this.value = value;
            this.paint = paint;
            this.stroke = stroke;
            this.widthFactor = widthFactor;
        }
    }

    private final List<CategoryLine> lines = new ArrayList<>();

    public void addCategoryLine(Comparable<?> categoryKey, double value, Paint paint, Stroke stroke, double widthFactor) {
        lines.add(new CategoryLine(categoryKey, value, paint, stroke, widthFactor));
    }

    @Override
    public void drawItem(Graphics2D g2,
                         CategoryItemRendererState state,
                         Rectangle2D dataArea,
                         CategoryPlot plot,
                         CategoryAxis domainAxis,
                         ValueAxis rangeAxis,
                         CategoryDataset dataset,
                         int row,
                         int column,
                         int pass) {
        super.drawItem(g2, state, dataArea, plot, domainAxis, rangeAxis, dataset, row, column, pass);

        if (pass == 1 && row == dataset.getRowCount() - 1) {
            Comparable<?> colKey = dataset.getColumnKey(column);
            
            double catStart = domainAxis.getCategoryStart(column, dataset.getColumnCount(), dataArea, plot.getDomainAxisEdge());
            double catEnd   = domainAxis.getCategoryEnd(column, dataset.getColumnCount(), dataArea, plot.getDomainAxisEdge());
            double catWidth = catEnd - catStart;
            double catCenter = domainAxis.getCategoryMiddle(column, dataset.getColumnCount(), dataArea, plot.getDomainAxisEdge());

            for (CategoryLine cl : lines) {
                if (!cl.categoryKey.equals(colKey)) continue;

                Stroke oldS = g2.getStroke();
                Paint oldP = g2.getPaint();

                g2.setStroke(cl.stroke);
                g2.setPaint(cl.paint);

                if (plot.getOrientation() == PlotOrientation.VERTICAL) {
                    double y = rangeAxis.valueToJava2D(cl.value, dataArea, plot.getRangeAxisEdge());
                    double half = (catWidth * cl.widthFactor) / 2.0;
                    double x1 = Math.max(catCenter - half, dataArea.getMinX());
                    double x2 = Math.min(catCenter + half, dataArea.getMaxX());
                    g2.draw(new Line2D.Double(x1, y, x2, y));
                } else {
                    double x = rangeAxis.valueToJava2D(cl.value, dataArea, plot.getRangeAxisEdge());
                    double half = (catWidth * cl.widthFactor) / 2.0;
                    double yCenter = domainAxis.getCategoryMiddle(column, dataset.getColumnCount(), dataArea, plot.getDomainAxisEdge());
                    double y1 = Math.max(yCenter - half, dataArea.getMinY());
                    double y2 = Math.min(yCenter + half, dataArea.getMaxY());
                    g2.draw(new Line2D.Double(x, y1, x, y2));
                }

                g2.setStroke(oldS);
                g2.setPaint(oldP);
            }
        }
    }
}
