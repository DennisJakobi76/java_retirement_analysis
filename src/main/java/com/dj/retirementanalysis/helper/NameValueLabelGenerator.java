package com.dj.retirementanalysis.helper;

import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.data.category.CategoryDataset;


public class NameValueLabelGenerator implements CategoryItemLabelGenerator {

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
