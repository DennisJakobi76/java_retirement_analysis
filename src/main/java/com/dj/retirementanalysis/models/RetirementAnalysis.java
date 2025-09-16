package com.dj.retirementanalysis.models;

import lombok.Getter;

@Getter
public class RetirementAnalysis {

    private final String name;
    private final String filePath="src/main/resources/static/";
    private final String chartImgName;
    private final int analysisYear;
    private final int projectionYear;
    private final double inflationRate = 0.02;
    private final double taxAndSocialDeductionRate = 0.25;

    private final double grossMonthlyIncome;    // Brutto / Monat
    private double netMonthlyIncome;            // berechnet (ohne sonstige Einnahmen)
    private double targetValue;                 // 80 % vom Netto plus 25% Abzüge (also: Netto)

    private final double statutoryPension;      // Gesetzliche Rente
    private final double otherIncome;           // Sonstige Einnahmen
    private final double occupationalPension;   // Betriebliche & geförderte Vorsorge
    private final double privatePension;        // Private Vorsorge

    private double statutoryPensionProjection;
    private double otherIncomeProjection;
    private double occupationalPensionProjection;
    private double privatePensionProjection;
    private double targetValueProjection;

    private double gapProjection;

    private double minTargetProjection;

    public RetirementAnalysis(
                              String name,
                              int analysisYear,
                              int projectionYear,
                              double grossMonthlyIncome,
                              double statutoryPension,
                              double otherIncome,
                              double occupationalPension,
                              double privatePension) {
        this.name = name;
        this.chartImgName = name.toLowerCase().replace(" ","_")+"_chart.png";
        this.analysisYear = analysisYear;
        this.projectionYear = projectionYear;
        this.grossMonthlyIncome = grossMonthlyIncome;
        this.statutoryPension = statutoryPension;
        this.otherIncome = otherIncome;
        this.occupationalPension = occupationalPension;
        this.privatePension = privatePension;
    }

    public void calculateNetMonthlyIncome() {
        this.netMonthlyIncome = grossMonthlyIncome * (1 - taxAndSocialDeductionRate);
    }

    public void calculateTargetValue() {
//        this.targetValue = netMonthlyIncome * 0.8;
        this.targetValue = netMonthlyIncome * 0.8 * (1 + taxAndSocialDeductionRate);
    }

    public void calculateProjections() {
        int years = projectionYear - analysisYear;
        double factor = Math.pow(1 + inflationRate, years);

        this.statutoryPensionProjection = statutoryPension * factor;
        this.otherIncomeProjection = otherIncome * factor;
        this.occupationalPensionProjection = occupationalPension * factor;
        this.privatePensionProjection = privatePension * factor;
        this.targetValueProjection = targetValue * factor;
    }

    public void calculateGaps() {
        double reachedProjection = statutoryPensionProjection + otherIncomeProjection
                + occupationalPensionProjection + privatePensionProjection;

        this.gapProjection = targetValueProjection - reachedProjection;
    }

    public void calculateMinTarget(double minimumWage, int hoursPerDay, int daysPerMonth) {
        double base = minimumWage * hoursPerDay * daysPerMonth * (1 - taxAndSocialDeductionRate);
        int years = projectionYear - analysisYear;
        double factor = Math.pow(1 + inflationRate, years);

        this.minTargetProjection = base * factor;
    }


    public void calculateAll(double minimumWage, int hoursPerDay, int daysPerMonth) {
        calculateNetMonthlyIncome();
        calculateTargetValue();
        calculateProjections();
        calculateGaps();
        calculateMinTarget(minimumWage, hoursPerDay, daysPerMonth);
    }

    public double getNetMonthlyIncomeProjection() {
        int years = projectionYear - analysisYear;
        double factor = Math.pow(1 + inflationRate, years);
        return netMonthlyIncome * factor;
    }
}