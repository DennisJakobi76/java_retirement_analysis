package com.dj.retirementanalysis.controller;

import com.dj.retirementanalysis.helper.ChartPngBuilder;
import com.dj.retirementanalysis.models.RetirementAnalysis;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import freemarker.template.Template;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.*;


@Controller
public class RetirementAnalysisController {

    private final List<RetirementAnalysis> analyses = new ArrayList<>();

    public RetirementAnalysisController() {
        // Brutto pro Monat
        // gesetzliche Rente
        // sonstige Einnahmen
        // betriebliche Vorsorge
        // private Vorsorge
        RetirementAnalysis analysisMaxMustermann = new RetirementAnalysis(
                "Max Mustermann",
                2025, 2050,
                4800,   // Brutto pro Monat
                1450,   // gesetzliche Rente
                540,    // sonstige Einnahmen
                400,    // betriebliche Vorsorge
                250      // private Vorsorge
        );
        analysisMaxMustermann.calculateAll(12.41, 8, 21);

        // Brutto pro Monat
        // gesetzliche Rente
        // sonstige Einnahmen
        // betriebliche Vorsorge
        // private Vorsorge
        RetirementAnalysis analysisMaximeMustermann = new RetirementAnalysis(
                "Maxime Mustermann",
                2025, 2050,
                5300,   // Brutto pro Monat
                1750,   // gesetzliche Rente
                300,    // sonstige Einnahmen
                500,    // betriebliche Vorsorge
                400     // private Vorsorge
        );
        analysisMaximeMustermann.calculateAll(12.41, 8, 21);

        analyses.add(analysisMaxMustermann);
        analyses.add(analysisMaximeMustermann);

        if (analyses.size() > 1) {
            RetirementAnalysis combinedAnalysis = new RetirementAnalysis(
                "Gemeinsamer Haushalt",
                Math.min(analysisMaxMustermann.getAnalysisYear(), analysisMaximeMustermann.getAnalysisYear()),
                Math.max(analysisMaxMustermann.getProjectionYear(), analysisMaximeMustermann.getProjectionYear()),
                analysisMaxMustermann.getGrossMonthlyIncome() + analysisMaximeMustermann.getNetMonthlyIncome(),
                analysisMaxMustermann.getStatutoryPension() + analysisMaximeMustermann.getStatutoryPension(),
                analysisMaxMustermann.getOtherIncome() + analysisMaximeMustermann.getOtherIncome(),
                analysisMaxMustermann.getOccupationalPension() + analysisMaximeMustermann.getOccupationalPension(),
                analysisMaxMustermann.getPrivatePension() + analysisMaximeMustermann.getPrivatePension()
            );
            combinedAnalysis.calculateAll(12.41, 8, 21);
            analyses.add(combinedAnalysis);
        }
    }

    @GetMapping("/analysis")
    public String getAnalysis(Model model) throws Exception {
        for (RetirementAnalysis analysis : analyses) {
            File out = new File(analysis.getFilePath()+analysis.getChartPath());
            ChartPngBuilder.buildPng(
                    analysis.getStatutoryPension(), analysis.getOtherIncome(), analysis.getOccupationalPension(), analysis.getPrivatePension(),
                    analysis.getStatutoryPensionProjection(), analysis.getOtherIncomeProjection(), analysis.getOccupationalPensionProjection(), analysis.getPrivatePensionProjection(),
                    analysis.getNetMonthlyIncome(), analysis.getNetMonthlyIncomeProjection(),
                    analysis.getTargetValueProjection(), analysis.getMinTargetProjection(),
                    out
            );
        }


        model.addAttribute("analyses", analyses);
        return "retirementanalysis"; // FTL
    }


    @Autowired
    private freemarker.template.Configuration freemarkerConfig;

    @GetMapping("/analysis/pdf")
    public void getAnalysisPdf(HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<>();
        model.put("analyses", analyses);
        String path = analyses.getFirst().getFilePath();
        Template template = freemarkerConfig.getTemplate("retirementanalysis.ftl");
        StringWriter stringWriter = new StringWriter();
        template.process(model, stringWriter);
        String html = stringWriter.toString();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=auswertung_vorsorge.pdf");

        try (OutputStream os = response.getOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();

            String baseUrl = new File(path).toURI().toURL().toString();
            builder.withHtmlContent(html, baseUrl);

            builder.toStream(os);
            builder.run();
        }



    }
}
