package com.dj.retirementanalysis.models;

import lombok.AllArgsConstructor;

import lombok.Getter;

@Getter
public class RetirementAnalysis {

    // ========================
    // Stammdaten
    // ========================
    private int analysisYear;
    private int projectionYear;
    private double inflationRate = 0.02;            // Default 2 %
    private double taxAndSocialDeductionRate = 0.25; // Default 25 %

    // ========================
    // Einkommen & Richtwert
    // ========================
    private double grossMonthlyIncome;   // Brutto / Monat
    private double netMonthlyIncome;     // berechnet (ohne sonstige Einnahmen)
    private double targetValue;          // 80 % vom Netto

    // ========================
    // Vorsorgequellen (aktuell)
    // ========================
    private double statutoryPension;     // Gesetzliche Rente
    private double otherIncome;          // Sonstige Einnahmen
    private double occupationalPension;  // Betriebliche & geförderte Vorsorge
    private double privatePension;       // Private Vorsorge

    // ========================
    // Projektionen
    // ========================
    private double statutoryPensionProjection;
    private double otherIncomeProjection;
    private double occupationalPensionProjection;
    private double privatePensionProjection;
    private double targetValueProjection;

    // ========================
    // Versorgungslücken
    // ========================
    private double gapCurrent;
    private double gapProjection;

    // ========================
    // DIN 77230 Mindestsollwert
    // ========================
    private double minTargetCurrent;
    private double minTargetProjection;

    // ========================
    // Konstruktor (ohne Name)
    // ========================
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

    // ========================
    // Berechnungsmethoden
    // ========================

    /**
     * Netto-Einkommen nur aus Brutto (ohne sonstige Einnahmen)
     */
    public void calculateNetMonthlyIncome() {
        this.netMonthlyIncome = grossMonthlyIncome * (1 - taxAndSocialDeductionRate);
    }

    /**
     * Richtwert (80 % Netto)
     */
    public void calculateTargetValue() {
        this.targetValue = netMonthlyIncome * 0.8;
    }

    /**
     * Projektionen
     */
    public void calculateProjections() {
        int years = projectionYear - analysisYear;
        double factor = Math.pow(1 + inflationRate, years);

        this.statutoryPensionProjection = statutoryPension * factor;
        this.otherIncomeProjection = otherIncome * factor;
        this.occupationalPensionProjection = occupationalPension * factor;
        this.privatePensionProjection = privatePension * factor;
        this.targetValueProjection = targetValue * factor;
    }

    /**
     * Versorgungslücken
     */
    public void calculateGaps() {
        double reachedCurrent = statutoryPension + otherIncome + occupationalPension + privatePension;
        double reachedProjection = statutoryPensionProjection + otherIncomeProjection
                + occupationalPensionProjection + privatePensionProjection;

        this.gapCurrent = targetValue - reachedCurrent;
        this.gapProjection = targetValueProjection - reachedProjection;
    }

    /**
     * DIN 77230 Mindestsollwert
     */
    public void calculateMinTarget(double minimumWage, int hoursPerDay, int daysPerMonth) {
        double base = minimumWage * hoursPerDay * daysPerMonth * (1 - taxAndSocialDeductionRate);
        int years = projectionYear - analysisYear;
        double factor = Math.pow(1 + inflationRate, years);

        this.minTargetCurrent = base;
        this.minTargetProjection = base * factor;
    }

    /**
     * Komplettberechnung
     */
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

    public double getTargetValueProjection() {
        return targetValueProjection; // wird eh schon berechnet
    }

    public double getTotalCurrent() {
        return statutoryPension + otherIncome + occupationalPension + privatePension;
    }

    public double getTotalProjection() {
        return statutoryPensionProjection + otherIncomeProjection + occupationalPensionProjection + privatePensionProjection;
    }


}