<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <title>Altersvorsorge One-Pager</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        header { border-bottom: 2px solid #ccc; margin-bottom: 20px; padding-bottom: 8px; }
        h1 { margin-bottom: 20px; }
        .text-container { max-width: 800px; margin-bottom: 20px; line-height: 1.5; }
        table { border-collapse: collapse; width: 60%; margin: 20px 0; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }
        th { background-color: #007bff; color: white; }
        tr:nth-child(even) { background-color: #f2f2f2; }
    </style>
</head>
<body>

<header>
    <img src="logo.png" alt="Logo" height="50">
</header>

<h1>Auswertung Altersvorsorge</h1>

<div class="text-container">
    <p>
        Diese Auswertung wurde in Anlehnung an die DIN 77230 erstellt.
        Im Diagramm sind zwei wichtige Referenzwerte dargestellt:
        der empfohlene Richtwert, der bei etwa 80 % des letzten Nettoeinkommens liegt (blaue Linie),
        sowie der nach DIN 77230 berechnete Mindestsollwert, der sich am gesetzlichen Mindestlohn orientiert (gelbe Linie).
    </p>
    <p>
        Die Hochrechnung berücksichtigt die Entwicklung bis zum Jahr ${analysis.projectionYear}
        unter Annahme einer jährlichen Inflationsrate von ${(analysis.inflationRate * 100)?string["0.##"]}%.
        Dabei werden die einzelnen Vorsorgebausteine – gesetzliche Rente, sonstige Einnahmen,
        betriebliche sowie private Vorsorge – ausgewertet.
    </p>
</div>

<table>
    <thead>
    <tr>
        <th>Vorsorgequelle</th>
        <th>Bereits erreicht ${analysis.analysisYear}<br>(€ / Monat)</th>
        <th>Projektion ${analysis.projectionYear}<br>(€ / Monat)</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>Gesetzliche Rente</td>
        <td>${analysis.statutoryPension?string["#,##0.00"]}</td>
        <td>${analysis.statutoryPensionProjection?string["#,##0.00"]}</td>
    </tr>
    <tr>
        <td>Sonstige Einnahmen</td>
        <td>${analysis.otherIncome?string["#,##0.00"]}</td>
        <td>${analysis.otherIncomeProjection?string["#,##0.00"]}</td>
    </tr>
    <tr>
        <td>Betriebliche &amp; geförderte Vorsorge</td>
        <td>${analysis.occupationalPension?string["#,##0.00"]}</td>
        <td>${analysis.occupationalPensionProjection?string["#,##0.00"]}</td>
    </tr>
    <tr>
        <td>Private Vorsorge</td>
        <td>${analysis.privatePension?string["#,##0.00"]}</td>
        <td>${analysis.privatePensionProjection?string["#,##0.00"]}</td>
    </tr>
    <tr>
        <td><b>Gesamtsumme Vorsorge</b></td>
        <td>${(analysis.statutoryPension + analysis.otherIncome + analysis.occupationalPension + analysis.privatePension)?string["#,##0.00"]}</td>
        <td>${(analysis.statutoryPensionProjection + analysis.otherIncomeProjection + analysis.occupationalPensionProjection + analysis.privatePensionProjection)?string["#,##0.00"]}</td>
    </tr>
    <tr>
        <td><b>Netto-Einkommen</b></td>
        <td>${analysis.netMonthlyIncome?string["#,##0.00"]}</td>
        <td>${analysis.netMonthlyIncomeProjection?string["#,##0.00"]}</td>

    </tr>
    <tr>
        <td><b>Richtwert (80% Netto)</b></td>
        <td>${analysis.targetValue?string["#,##0.00"]}</td>
        <td>${analysis.targetValueProjection?string["#,##0.00"]}</td>
    </tr>
    <tr>
        <td><b>DIN 77230 Mindestsoll</b></td>
        <td>${analysis.minTargetCurrent?string["#,##0.00"]}</td>
        <td>${analysis.minTargetProjection?string["#,##0.00"]}</td>
    </tr>
    <tr>
        <td><b>Versorgungslücke</b></td>
        <td>${analysis.gapCurrent?string["#,##0.00"]}</td>
        <td>${analysis.gapProjection?string["#,##0.00"]}</td>
    </tr>
    </tbody>
</table>

<div class="text-container">
    <p>
        Aus der Gegenüberstellung ergibt sich eine Versorgungslücke:
        Trotz solider Basis durch die gesetzliche Rente und sonstige Einnahmen reichen die betrieblichen
        und privaten Vorsorgeleistungen aktuell nicht aus, um den Richtwert vollständig zu erreichen.
        Die Lücke beträgt im Jahr ${analysis.analysisYear} rund ${analysis.gapCurrent?string["#,##0"]} €,
        im Jahr ${analysis.projectionYear} – inflationsbedingt – etwa ${analysis.gapProjection?string["#,##0"]} €.
        Eine gezielte Stärkung der betrieblichen und privaten Vorsorge ist daher empfehlenswert.
    </p>
</div>

</body>
</html>
