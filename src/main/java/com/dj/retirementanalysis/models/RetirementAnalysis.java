package com.dj.retirementanalysis.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RetirementAnalysis {

    private int analysisYear;
    private int projectionYear;
    private double inflationRate = 0.02;
    private double taxAndSocialDeductionRate = 0.25;

    private double grossMonthlyIncome;   // Brutto / Monat
    private double netMonthlyIncome;     // berechnet (ohne sonstige Einnahmen)
    private double targetValue;          // 80 % vom Netto

    private double statutoryPension;     // Gesetzliche Rente
    private double otherIncome;          // Sonstige Einnahmen
    private double occupationalPension;  // Betriebliche & geförderte Vorsorge
    private double privatePension;       // Private Vorsorge

    private double statutoryPensionProjection;
    private double otherIncomeProjection;
    private double occupationalPensionProjection;
    private double privatePensionProjection;
    private double targetValueProjection;

    private double gapCurrent;
    private double gapProjection;

    private double minTargetCurrent;
    private double minTargetProjection;

    public RetirementAnalysis(int analysisYear,
                              int projectionYear,
                              double grossMonthlyIncome,
                              double statutoryPension,
                              double otherIncome,
                              double occupationalPension,
                              double privatePension) {
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
        this.targetValue = netMonthlyIncome * 0.8;
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
        double reachedCurrent = statutoryPension + otherIncome + occupationalPension + privatePension;
        double reachedProjection = statutoryPensionProjection + otherIncomeProjection
                + occupationalPensionProjection + privatePensionProjection;

        this.gapCurrent = targetValue - reachedCurrent;
        this.gapProjection = targetValueProjection - reachedProjection;
    }

    public void calculateMinTarget(double minimumWage, int hoursPerDay, int daysPerMonth) {
        double base = minimumWage * hoursPerDay * daysPerMonth * (1 - taxAndSocialDeductionRate);
        int years = projectionYear - analysisYear;
        double factor = Math.pow(1 + inflationRate, years);

        this.minTargetCurrent = base;
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

    public double getTotalCurrent() {
        return statutoryPension + otherIncome + occupationalPension + privatePension;
    }

    public double getTotalProjection() {
        return statutoryPensionProjection + otherIncomeProjection + occupationalPensionProjection + privatePensionProjection;
    }


}