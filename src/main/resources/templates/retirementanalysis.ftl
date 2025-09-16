<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Auswertung Vorsorge</title>
    <link rel="icon" type="image/png" href="logo.png"/>
    <link rel="stylesheet" href="/css/style.css"/>
    <link rel="stylesheet" href="classpath:/static/css/pdf_style.css"/>

</head>
<body>



<#list analyses as analysis>
    <header>
        <img src="logo.png" alt="Logo" height="50"/>
    </header>

    <h1>Auswertung Altersvorsorge für ${analysis.name}</h1>

    <div class="content">
        <div class="text-container">
            <p>
                Diese Auswertung wurde in Anlehnung an die DIN 77230 erstellt.
                Im Diagramm sind zwei wichtige Referenzwerte dargestellt:
                der empfohlene Richtwert, der bei der Höhe des letzten Nettoeinkommens liegt (blaue Linie),
                sowie der nach DIN 77230 berechnete Mindestsollwert, der sich am gesetzlichen Mindestlohn orientiert (gelbe Linie).
            </p>
            <p>
                Die Hochrechnung berücksichtigt die Entwicklung bis zum Jahr ${analysis.projectionYear?string["0"]}
                unter Annahme einer jährlichen Inflationsrate von ${(analysis.inflationRate * 100)?string["0.##"]}%.
                Dabei werden die einzelnen Vorsorgebausteine – gesetzliche Rente, sonstige Einnahmen,
                betriebliche sowie private Vorsorge – ausgewertet.
            </p>
        </div>

        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th>Vorsorgequelle</th>
                    <th>
                        Bereits erreicht ${analysis.analysisYear?string["0"]}<br/> (&#8364;/Monat)
                    </th>
                    <th>
                        Projektion ${analysis.projectionYear?string["0"]}<br/> (&#8364;/Monat)
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>Gesetzliche Rente</td>
                    <td>${analysis.statutoryPension?string["#,##0.00"]}&#160;&#8364;</td>
                    <td>${analysis.statutoryPensionProjection?string["#,##0.00"]}&#160;&#8364;</td>
                </tr>
                <tr>
                    <td>Sonstige Einnahmen</td>
                    <td>${analysis.otherIncome?string["#,##0.00"]}&#160;&#8364;</td>
                    <td>${analysis.otherIncomeProjection?string["#,##0.00"]}&#160;&#8364;</td>
                </tr>
                <tr>
                    <td>Betriebliche &amp; geförderte Vorsorge</td>
                    <td>${analysis.occupationalPension?string["#,##0.00"]}&#160;&#8364;</td>
                    <td>${analysis.occupationalPensionProjection?string["#,##0.00"]}&#160;&#8364;</td>
                </tr>
                <tr>
                    <td>Private Vorsorge</td>
                    <td>${analysis.privatePension?string["#,##0.00"]}&#160;&#8364;</td>
                    <td>${analysis.privatePensionProjection?string["#,##0.00"]}&#160;&#8364;</td>
                </tr>
                <tr>
                    <td><b>Gesamtsumme Vorsorge</b></td>
                    <td>${(analysis.statutoryPension + analysis.otherIncome + analysis.occupationalPension + analysis.privatePension)?string["#,##0.00"]}&#160;&#8364;</td>
                    <td>${(analysis.statutoryPensionProjection + analysis.otherIncomeProjection + analysis.occupationalPensionProjection + analysis.privatePensionProjection)?string["#,##0.00"]}&#160;&#8364;</td>
                </tr>
                <tr>
                    <td><b>Netto-Einkommen</b></td>
                    <td>${analysis.netMonthlyIncome?string["#,##0.00"]}&#160;&#8364;</td>
                    <td>${analysis.netMonthlyIncomeProjection?string["#,##0.00"]}&#160;&#8364;</td>
                </tr>
                <tr>
                    <td><b>Richtwert (80% Netto)</b></td>
                    <td></td>
                    <td>${analysis.targetValueProjection?string["#,##0.00"]}&#160;&#8364;</td>
                </tr>
                <tr>
                    <td><b>Mindestsoll</b></td>
                    <td></td>
                    <td>${analysis.minTargetProjection?string["#,##0.00"]}&#160;&#8364;</td>
                </tr>
                <tr>
                    <td><b>Versorgungslücke</b></td>
                    <td></td>
                    <td>${analysis.gapProjection?string["#,##0.00"]}&#160;&#8364;</td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="canvas-container">
            <img class="chart" src="${analysis.chartPath}" alt="Diagramm Altersvorsorge"/>
        </div>

        <div class="text-container">
            <p>
                Aus der Gegenüberstellung ergibt sich eine Versorgungslücke:
                Trotz solider Basis durch die gesetzliche Rente und sonstige Einnahmen reichen die betrieblichen
                und privaten Vorsorgeleistungen aktuell nicht aus, um den Richtwert vollständig zu erreichen.
                Die Lücke beträgt im Jahr ${analysis.projectionYear?string["0"]} – inflationsbedingt – etwa ${analysis.gapProjection?string["#,##0"]}&#8239;&#8364;.
                Eine gezielte Stärkung der betrieblichen und privaten Vorsorge ist daher empfehlenswert.
            </p>
        </div>
    </div>

    <#if analysis_has_next>

        <div class="page-break"></div>
    </#if>

</#list>


</body>
</html>
