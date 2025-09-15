<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Auswertung Vorsorge</title>
    <link rel="icon" type="image/png" href="logo.png"/>
    <style type="text/css">
        body {
            font-family: Helvetica, sans-serif;
            margin: 20px;
        }

        header {
            width: 100%;
            border-bottom: 2px solid #ccc;
            padding-bottom: 8px;
            margin-bottom: 20px;
        }

        h1 {
            margin-bottom: 20px;
            text-align: center;
        }

        .content {
            max-width: 90%;
            margin: 40px auto;
        }

        table {
            border-collapse: collapse;
            width: 60%;
            margin: 40px auto;
            text-align: center;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 6px;
            text-align: center;
        }

        th {
            background-color: #007bff;
            color: #ffffff;
        }

        .text-container{
            max-width: 60%;
            margin: 0 auto 20px auto;
            text-align: justify;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        .canvas-container {
            width: 60%;
            margin: 20px auto;
            text-align: center;
        }

        img.chart {
            max-width: 100%;
            height: auto;
            margin: 0 auto;
            display: block;
        }

        @page {
            size: A4 portrait;
            margin: -15mm 8mm 8mm 8mm;
        }

        @media print {
            body {
                font-size: 13px;
                margin: 0;
                transform: scale(0.85);
                transform-origin: top left;
            }

            header {
                margin-bottom: 16px;
                margin-top: 0;
                padding-bottom: 4px;
                padding-top: 0;
            }

            h1 {
                font-size: 14pt;
                margin-bottom: 12px;
                margin-top: 24px;
            }

            p {
                white-space: normal;
                word-spacing: normal;
            }

            .content {
                margin-top: 0;
            }

            .table-container,
            .canvas-container,
            .text-container {
                width: 100%;
                max-width: none;
            }

            table { width: 100%; }

            .table-container {
                margin: 0 auto;
                padding-bottom: 1px;
            }

            .canvas-container {
                margin: 0 auto 25px auto;
                padding-top: 1px;
                text-align: center;
                overflow: hidden;
            }

            img.chart {
                width: 100%;
                height: auto;
                display: block;
                margin: 0 auto;
            }
        }
    </style>
</head>
<body>

<header>
    <img src="logo.png" alt="Logo" height="50"/>
</header>

<#list analyses as analysis>

    <h1>Auswertung Altersvorsorge für ${analysis.name}</h1>

    <div class="content">
        <div class="text-container">
            <p>
                Diese Auswertung wurde in Anlehnung an die DIN 77230 erstellt.
                Im Diagramm sind zwei wichtige Referenzwerte dargestellt:
                der empfohlene Richtwert, der bei etwa 80 % des letzten Nettoeinkommens liegt (blaue Linie),
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
        <hr style="margin:40px 0; border:1px solid #ccc;"/>
    </#if>

</#list>

</body>
</html>
