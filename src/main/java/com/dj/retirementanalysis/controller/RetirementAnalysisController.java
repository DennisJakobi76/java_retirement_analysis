package com.dj.retirementanalysis.controller;

import com.dj.retirementanalysis.helper.ChartPngBuilder;
import com.dj.retirementanalysis.helper.PdfExporter;
import com.dj.retirementanalysis.models.RetirementAnalysis;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;


@Controller
public class RetirementAnalysisController {

    @GetMapping("/analysis")
    public String getAnalysis(Model model) throws Exception {

        RetirementAnalysis analysis = new RetirementAnalysis(
                2025, 2050,
                4000,   // Brutto pro Monat
                1450,   // gesetzliche Rente
                400,    // sonstige Einnahmen
                300,    // betriebliche Vorsorge
                50      // private Vorsorge
        );
        analysis.calculateAll(12.41, 8, 21);

        // Beispielwerte aus deinem Modell (hier nur demonstrativ)
        File out = new File("src/main/resources/static/chart.png");
        ChartPngBuilder.buildPng(
                analysis.getStatutoryPension(), analysis.getOtherIncome(), analysis.getOccupationalPension(), analysis.getPrivatePension(),
                analysis.getStatutoryPensionProjection(), analysis.getOtherIncomeProjection(), analysis.getOccupationalPensionProjection(), analysis.getPrivatePensionProjection(),
                analysis.getNetMonthlyIncome(), analysis.getNetMonthlyIncomeProjection(),   // falls du Getter gebaut hast; sonst selbst berechnen
                analysis.getTargetValue(), analysis.getTargetValueProjection(),
                analysis.getMinTargetCurrent(), analysis.getMinTargetProjection(),
                out
        );
        model.addAttribute("analysis", analysis);
        return "retirementanalysis"; // FTL
    }


    @GetMapping("/analysis/pdf")
    public void getAnalysisPdf(HttpServletResponse response) throws Exception {
        // 👉 Beispielwerte oder echte Werte berechnen:
        RetirementAnalysis analysis = new RetirementAnalysis(
                2025, 2050,
                4000,   // Brutto pro Monat
                1450,   // gesetzliche Rente
                400,    // sonstige Einnahmen
                300,    // betriebliche Vorsorge
                50      // private Vorsorge
        );

        // Chart erzeugen (temporär speichern)
        File chartFile = File.createTempFile("chart", ".png");
        ChartPngBuilder.buildPng(
                analysis.getStatutoryPension(), analysis.getOtherIncome(),
                analysis.getOccupationalPension(), analysis.getPrivatePension(),
                analysis.getStatutoryPensionProjection(), analysis.getOtherIncomeProjection(),
                analysis.getOccupationalPensionProjection(), analysis.getPrivatePensionProjection(),
                analysis.getNetMonthlyIncome(), analysis.getNetMonthlyIncomeProjection(),
                analysis.getTargetValue(), analysis.getTargetValueProjection(),
                analysis.getMinTargetCurrent(), analysis.getMinTargetProjection(),
                chartFile
        );

        // PDF erzeugen
        File pdfFile = File.createTempFile("altersvorsorge", ".pdf");
        PdfExporter.exportRetirementAnalysisPdf(analysis, pdfFile, chartFile);

        // PDF an Browser senden
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=altersvorsorge.pdf");
        try (java.io.FileInputStream fis = new java.io.FileInputStream(pdfFile);
             java.io.OutputStream os = response.getOutputStream()) {
            fis.transferTo(os);
        }
    }

}
