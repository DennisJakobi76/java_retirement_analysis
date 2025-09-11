package com.dj.retirementanalysis.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.dj.retirementanalysis.models.RetirementAnalysis;

@Controller
public class RetirementAnalysisController {

    @GetMapping("/analysis")
    public String getAnalysis(Model model){
        RetirementAnalysis analysis = new RetirementAnalysis(
                2025,      // analysisYear
                2050,                 // projectionYear
                4000,                 // grossMonthlyIncome
                1450,                 // statutoryPension
                400,                  // otherIncome
                300,                  // occupationalPension
                50);                  // privatePension

        // Berechnung durchführen
        analysis.calculateAll(12.41, 8, 21);

        // ins Modell legen
        model.addAttribute("analysis", analysis);
        return "retirementanalysis"; // -> retirementanalysis.ftl
    }
}
