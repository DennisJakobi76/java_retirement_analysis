package com.dj.retirementanalysis.controller;

import com.dj.retirementanalysis.models.ChartPngBuilder;
import com.dj.retirementanalysis.models.RetirementAnalysis;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.IOException;


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



}
