package com.dj.retirementanalysis.controller;

import com.dj.retirementanalysis.models.RetirementAnalysis;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.Arrays;

@Controller
public class RetirementAnalysisController {

    @GetMapping("/analysis")
    public String getAnalysis(Model model) throws IOException {

        RetirementAnalysis analysis = new RetirementAnalysis(
                2025, 2050,
                4000,   // Brutto pro Monat
                1450,   // gesetzliche Rente
                400,    // sonstige Einnahmen
                300,    // betriebliche Vorsorge
                50      // private Vorsorge
        );
        analysis.calculateAll(12.41, 8, 21);

        // Chart bauen
        CategoryChart chart = new CategoryChartBuilder()
                .width(800).height(400)
                .title("Altersvorsorge: 2025 vs. 2050")
                .xAxisTitle("Kategorie")
                .yAxisTitle("€ / Monat")
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setDecimalPattern("#,##0");

        var categories = Arrays.asList(
                "Gesetzliche Rente",
                "Sonstige Einnahmen",
                "Betriebliche Vorsorge",
                "Private Vorsorge"
        );

        chart.addSeries("2025", categories, Arrays.asList(
                analysis.getStatutoryPension(),
                analysis.getOtherIncome(),
                analysis.getOccupationalPension(),
                analysis.getPrivatePension()
        ));

        chart.addSeries("2050", categories, Arrays.asList(
                analysis.getStatutoryPensionProjection(),
                analysis.getOtherIncomeProjection(),
                analysis.getOccupationalPensionProjection(),
                analysis.getPrivatePensionProjection()
        ));

        // In /static speichern
        BitmapEncoder.saveBitmap(chart,
                "src/main/resources/static/chart",
                BitmapEncoder.BitmapFormat.PNG);

        model.addAttribute("analysis", analysis);
        return "retirementanalysis"; // FTL
    }
}
