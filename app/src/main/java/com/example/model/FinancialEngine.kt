package com.example.model

import java.util.Locale

object FinancialEngine {

    fun calculate(input: FinancialInput): CalculationEngineResult {
        val rev = input.revenue
        val gp = input.grossProfit
        val ebit = input.operatingIncome
        val ni = input.netIncome
        val cfo = input.operatingCashFlow
        val capex = input.capex
        val curA = input.currentAssets
        val totA = input.totalAssets
        val curL = input.currentLiabilities
        val debt = input.totalDebt
        val eq = input.totalEquity

        // 1. Profitabilitas
        val roe = if (eq != 0.0) (ni / eq) * 100.0 else 0.0
        val npm = if (rev != 0.0) (ni / rev) * 100.0 else 0.0
        val gpm = if (rev != 0.0) (gp / rev) * 100.0 else 0.0
        val opm = if (rev != 0.0) (ebit / rev) * 100.0 else 0.0
        val roa = if (totA != 0.0) (ni / totA) * 100.0 else 0.0

        // 2. Solvabilitas & Likuiditas
        val der = if (eq > 0.0) debt / eq else 99.0
        val currentRatio = if (curL > 0.0) curA / curL else 0.0

        // 3. Kualitas Kas & FCF
        val cfoToNetIncome = if (ni > 0.0) cfo / ni else if (cfo > 0.0) 2.0 else 0.0
        val fcf = cfo - capex
        val marketCap = if (input.sharesOutstanding > 0.0 && input.marketPrice > 0.0) {
            input.sharesOutstanding * input.marketPrice
        } else {
            eq + debt
        }
        val fcfYield = if (marketCap > 0.0) (fcf / marketCap) * 100.0 else 0.0

        // 4. Piotroski F-Score (9 points)
        val p1 = ni > 0.0
        val p2 = cfo > 0.0
        val p3 = roa > 0.0
        val p4 = cfo > ni
        val p5 = der <= 1.5
        val p6 = currentRatio >= 1.2
        val p7 = gpm >= 15.0 || (gp > 0.0 && gp >= ebit)
        val p8 = totA > 0.0 && (rev / totA) >= 0.3
        val p9 = eq > 0.0 && der <= 1.0

        val piotroskiCriteria = listOf(
            PiotroskiCriterion(1, "Laba Bersih Positif", "Net Income > 0", p1, formatCurrency(ni, input.unit)),
            PiotroskiCriterion(2, "Arus Kas Operasi Positif", "CFO > 0", p2, formatCurrency(cfo, input.unit)),
            PiotroskiCriterion(3, "ROA Bernilai Positif", "ROA > 0%", p3, String.format(Locale.US, "%.2f%%", roa)),
            PiotroskiCriterion(4, "Kualitas Akrual (CFO > NI)", "Kas Operasi melampaui Laba Bersih", p4, String.format(Locale.US, "%.2fx", cfoToNetIncome)),
            PiotroskiCriterion(5, "Leverage Terkendali", "DER <= 1.5x", p5, String.format(Locale.US, "%.2fx", der)),
            PiotroskiCriterion(6, "Likuiditas Lancar", "Current Ratio >= 1.2x", p6, String.format(Locale.US, "%.2fx", currentRatio)),
            PiotroskiCriterion(7, "Margin Laba Sehat", "Gross Profit Margin Sehat", p7, String.format(Locale.US, "%.1f%%", gpm)),
            PiotroskiCriterion(8, "Efisiensi Perputaran Aset", "Asset Turnover >= 0.3x", p8, String.format(Locale.US, "%.2fx", if (totA > 0) rev / totA else 0.0)),
            PiotroskiCriterion(9, "Kekuatan Solvabilitas Ekuitas", "Ekuitas Positif & DER <= 1.0x", p9, String.format(Locale.US, "DER %.2fx", der))
        )
        val piotroskiScore = piotroskiCriteria.count { it.isPassed }

        // 5. Automated Classification Scoring (Grade A, B, C)
        val isRoeA = roe >= 15.0
        val isDerA = der <= 1.0
        val isCfoNiA = cfoToNetIncome >= 1.0
        val isPiotroskiA = piotroskiScore >= 7

        val isRoeC = roe < 8.0
        val isDerC = der > 2.0
        val isCfoNiC = cfoToNetIncome < 0.8
        val isPiotroskiC = piotroskiScore <= 3

        val isGradeC = isRoeC || isDerC || isCfoNiC || isPiotroskiC
        val isGradeA = isRoeA && isDerA && isCfoNiA && isPiotroskiA && !isGradeC

        val grade = when {
            isGradeA -> CompanyGrade.GRADE_A
            isGradeC -> CompanyGrade.GRADE_C
            else -> CompanyGrade.GRADE_B
        }

        val gradeChecks = listOf(
            GradeRuleCheck(
                title = "Profitabilitas (ROE)",
                requiredBenchmark = "Grade A: >15% | B: 8-15% | C: <8%",
                actualValue = String.format(Locale.US, "%.2f%%", roe),
                isPassed = roe >= 15.0,
                isCriticalRisk = roe < 8.0
            ),
            GradeRuleCheck(
                title = "Solvabilitas (DER)",
                requiredBenchmark = "Grade A: <1.0x | B: 1.0-1.5x | C: >2.0x",
                actualValue = String.format(Locale.US, "%.2fx", der),
                isPassed = der <= 1.0,
                isCriticalRisk = der > 2.0
            ),
            GradeRuleCheck(
                title = "Kualitas Kas (CFO / Net Income)",
                requiredBenchmark = "Grade A: >1.0x | B: 0.8-1.0x | C: <0.8x",
                actualValue = String.format(Locale.US, "%.2fx", cfoToNetIncome),
                isPassed = cfoToNetIncome >= 1.0,
                isCriticalRisk = cfoToNetIncome < 0.8
            ),
            GradeRuleCheck(
                title = "Piotroski F-Score",
                requiredBenchmark = "Grade A: >=7 | B: 4-6 | C: 0-3",
                actualValue = "$piotroskiScore / 9 Poin",
                isPassed = piotroskiScore >= 7,
                isCriticalRisk = piotroskiScore <= 3
            )
        )

        // Status determinations
        val roeStatus = when {
            roe >= 15.0 -> MetricStatus.SAFE
            roe >= 8.0 -> MetricStatus.MODERATE
            else -> MetricStatus.RISK
        }
        val npmStatus = when {
            npm >= 10.0 -> MetricStatus.SAFE
            npm >= 5.0 -> MetricStatus.MODERATE
            else -> MetricStatus.RISK
        }
        val derStatus = when {
            der <= 1.0 -> MetricStatus.SAFE
            der <= 1.5 -> MetricStatus.MODERATE
            else -> MetricStatus.RISK
        }
        val crStatus = when {
            currentRatio >= 1.5 -> MetricStatus.SAFE
            currentRatio >= 1.0 -> MetricStatus.MODERATE
            else -> MetricStatus.RISK
        }
        val cfoNiStatus = when {
            cfoToNetIncome >= 1.0 -> MetricStatus.SAFE
            cfoToNetIncome >= 0.8 -> MetricStatus.MODERATE
            else -> MetricStatus.RISK
        }

        val metrics = listOf(
            RatioMetric(
                id = "roe",
                name = "Return on Equity (ROE)",
                category = "Profitabilitas",
                formulaDisplay = "Laba Bersih / Ekuitas",
                value = roe,
                formattedValue = String.format(Locale.US, "%.2f%%", roe),
                benchmarkSafe = "> 15.0%",
                status = roeStatus,
                statusNote = when (roeStatus) {
                    MetricStatus.SAFE -> "Sangat Baik (Ekuitas menghasilkan laba prima)"
                    MetricStatus.MODERATE -> "Wajar / Moderat"
                    MetricStatus.RISK -> "Rendah (Imbal hasil ekuitas di bawah ekspektasi)"
                }
            ),
            RatioMetric(
                id = "npm",
                name = "Net Profit Margin (NPM)",
                category = "Profitabilitas",
                formulaDisplay = "Laba Bersih / Pendapatan",
                value = npm,
                formattedValue = String.format(Locale.US, "%.2f%%", npm),
                benchmarkSafe = "> 10.0%",
                status = npmStatus,
                statusNote = when (npmStatus) {
                    MetricStatus.SAFE -> "Margin Tebal (Efisiensi konversi omset tinggi)"
                    MetricStatus.MODERATE -> "Margin Standar (Cukup kompetitif)"
                    MetricStatus.RISK -> "Margin Tipis (Rentan terhadap kenaikan beban operasional)"
                }
            ),
            RatioMetric(
                id = "gpm",
                name = "Gross Profit Margin (GPM)",
                category = "Profitabilitas",
                formulaDisplay = "Laba Kotor / Pendapatan",
                value = gpm,
                formattedValue = String.format(Locale.US, "%.2f%%", gpm),
                benchmarkSafe = "> 25.0%",
                status = if (gpm >= 30.0) MetricStatus.SAFE else if (gpm >= 15.0) MetricStatus.MODERATE else MetricStatus.RISK,
                statusNote = "Daya penetapan harga & keunggulan kompetitif (Moat)"
            ),
            RatioMetric(
                id = "opm",
                name = "Operating Profit Margin (OPM)",
                category = "Profitabilitas",
                formulaDisplay = "Laba Usaha (EBIT) / Pendapatan",
                value = opm,
                formattedValue = String.format(Locale.US, "%.2f%%", opm),
                benchmarkSafe = "> 15.0%",
                status = if (opm >= 15.0) MetricStatus.SAFE else if (opm >= 8.0) MetricStatus.MODERATE else MetricStatus.RISK,
                statusNote = "Efisiensi biaya operasional inti sebelum pajak & bunga"
            ),
            RatioMetric(
                id = "der",
                name = "Debt to Equity Ratio (DER)",
                category = "Solvabilitas & Utang",
                formulaDisplay = "Total Utang Berbunga / Ekuitas",
                value = der,
                formattedValue = String.format(Locale.US, "%.2fx", der),
                benchmarkSafe = "< 1.00x",
                status = derStatus,
                statusNote = when (derStatus) {
                    MetricStatus.SAFE -> "Struktur Modal Sehat (Utang berbunga < Ekuitas)"
                    MetricStatus.MODERATE -> "Moderat (Perlu monitor beban bunga kredit)"
                    MetricStatus.RISK -> "Risiko Tinggi (Leverage berat, potensi insolvensi)"
                }
            ),
            RatioMetric(
                id = "cr",
                name = "Current Ratio",
                category = "Likuiditas Jangka Pendek",
                formulaDisplay = "Aset Lancar / Liabilitas Lancar",
                value = currentRatio,
                formattedValue = String.format(Locale.US, "%.2fx", currentRatio),
                benchmarkSafe = ">= 1.50x",
                status = crStatus,
                statusNote = when (crStatus) {
                    MetricStatus.SAFE -> "Likuiditas Kuat (Mampu melunasi hutang tempo pendek)"
                    MetricStatus.MODERATE -> "Cukup (Perlu menjaga arus kas kas masuk)"
                    MetricStatus.RISK -> "Ketat (< 1.0x, bahaya gagal bayar tagihan jatuh tempo)"
                }
            ),
            RatioMetric(
                id = "cfo_ni",
                name = "Kualitas Kas (CFO / Net Income)",
                category = "Kualitas Arus Kas",
                formulaDisplay = "Arus Kas Operasi / Laba Bersih",
                value = cfoToNetIncome,
                formattedValue = String.format(Locale.US, "%.2fx", cfoToNetIncome),
                benchmarkSafe = "> 1.00x",
                status = cfoNiStatus,
                statusNote = when (cfoNiStatus) {
                    MetricStatus.SAFE -> "Kas Riil Kuat (Laba bersih terkonversi menjadi uang tunai nyata)"
                    MetricStatus.MODERATE -> "Wajar (Terdapat piutang/akrual operasional moderat)"
                    MetricStatus.RISK -> "Peringatan Akrual (Laba akuntansi tinggi namun kas minim / macet)"
                }
            ),
            RatioMetric(
                id = "fcf",
                name = "Free Cash Flow (FCF)",
                category = "Arus Kas Bebas",
                formulaDisplay = "CFO - Belanja Modal (CAPEX)",
                value = fcf,
                formattedValue = formatCurrency(fcf, input.unit),
                benchmarkSafe = "> 0 (Positif)",
                status = if (fcf > 0) MetricStatus.SAFE else MetricStatus.RISK,
                statusNote = if (fcf > 0) "Arus kas bebas positif untuk dividen/ekspansi" else "Defisit kas bebas (CAPEX melebihi CFO)"
            ),
            RatioMetric(
                id = "fcf_yield",
                name = "FCF Yield",
                category = "Valuasi Arus Kas",
                formulaDisplay = "Free Cash Flow / Market Cap",
                value = fcfYield,
                formattedValue = String.format(Locale.US, "%.2f%%", fcfYield),
                benchmarkSafe = "> 5.0%",
                status = if (fcfYield >= 5.0) MetricStatus.SAFE else if (fcfYield >= 2.0) MetricStatus.MODERATE else MetricStatus.RISK,
                statusNote = "Imbal hasil arus kas bebas terhadap kapitalisasi pasar"
            )
        )

        val summary = when (grade) {
            CompanyGrade.GRADE_A -> "Emiten berstatus GRADE A (Wonderful Company). Memiliki ROE ${String.format(Locale.US, "%.1f%%", roe)}, struktur DER aman (${String.format(Locale.US, "%.2fx", der)}), rasio CFO/NI ${String.format(Locale.US, "%.2fx", cfoToNetIncome)}, serta skor Piotroski $piotroskiScore/9. Layak dipertimbangkan untuk investasi jangka panjang."
            CompanyGrade.GRADE_B -> "Emiten berstatus GRADE B (Fair Company). Kinerja stabil dengan ROE ${String.format(Locale.US, "%.1f%%", roe)} dan DER ${String.format(Locale.US, "%.2fx", der)}. Piotroski score $piotroskiScore/9 menunjukkan fundamental berimbang dengan beberapa pos yang perlu dipantau."
            CompanyGrade.GRADE_C -> "Emiten berstatus GRADE C (High Risk / Value Trap). Terindikasi tanda risiko: " +
                    listOfNotNull(
                        if (isRoeC) "ROE rendah (${String.format(Locale.US, "%.1f%%", roe)})" else null,
                        if (isDerC) "DER tinggi (${String.format(Locale.US, "%.2fx", der)})" else null,
                        if (isCfoNiC) "CFO/NI lemah (${String.format(Locale.US, "%.2fx", cfoToNetIncome)})" else null,
                        if (isPiotroskiC) "Piotroski score $piotroskiScore/9" else null
                    ).joinToString(", ") + ". Disarankan melakukan investigasi mendalam sebelum mengambil keputusan."
        }

        return CalculationEngineResult(
            roe = roe,
            npm = npm,
            gpm = gpm,
            opm = opm,
            roa = roa,
            der = der,
            currentRatio = currentRatio,
            cfoToNetIncome = cfoToNetIncome,
            fcf = fcf,
            fcfYield = fcfYield,
            marketCap = marketCap,
            piotroskiScore = piotroskiScore,
            piotroskiCriteria = piotroskiCriteria,
            grade = grade,
            gradeRuleChecks = gradeChecks,
            metricsList = metrics,
            summaryDiagnosis = summary
        )
    }

    fun formatCurrency(amount: Double, unit: FinancialUnit): String {
        val unitSuffix = when (unit) {
            FinancialUnit.BILLION -> " M"
            FinancialUnit.MILLION -> " Jt"
            FinancialUnit.RAW -> ""
        }
        return String.format(Locale.US, "Rp %,.1f%s", amount, unitSuffix)
    }

    val PRESET_COMPANIES = listOf(
        FinancialInput(
            ticker = "BBCA",
            companyName = "Bank Central Asia Tbk",
            period = "FY 2024 (Audited)",
            unit = FinancialUnit.BILLION,
            marketPrice = 9800.0,
            sharesOutstanding = 123.28,
            revenue = 104500.0,
            grossProfit = 83200.0,
            operatingIncome = 61500.0,
            netIncome = 48600.0,
            operatingCashFlow = 53800.0,
            capex = 6500.0,
            cashAndEquivalents = 55000.0,
            currentAssets = 340000.0,
            totalAssets = 1408000.0,
            currentLiabilities = 210000.0,
            totalDebt = 115000.0,
            totalEquity = 248000.0,
            targetPrice = 11500.0,
            consensus = TeamConsensus.BUY
        ),
        FinancialInput(
            ticker = "ICBP",
            companyName = "Indofood CBP Sukses Makmur Tbk",
            period = "FY 2024",
            unit = FinancialUnit.BILLION,
            marketPrice = 11250.0,
            sharesOutstanding = 11.66,
            revenue = 69800.0,
            grossProfit = 25100.0,
            operatingIncome = 14200.0,
            netIncome = 9500.0,
            operatingCashFlow = 11200.0,
            capex = 3800.0,
            cashAndEquivalents = 16500.0,
            currentAssets = 38900.0,
            totalAssets = 122000.0,
            currentLiabilities = 19500.0,
            totalDebt = 42000.0,
            totalEquity = 60500.0,
            targetPrice = 13000.0,
            consensus = TeamConsensus.BUY
        ),
        FinancialInput(
            ticker = "ASII",
            companyName = "Astra International Tbk",
            period = "FY 2024",
            unit = FinancialUnit.BILLION,
            marketPrice = 5150.0,
            sharesOutstanding = 40.48,
            revenue = 318000.0,
            grossProfit = 69500.0,
            operatingIncome = 43200.0,
            netIncome = 33800.0,
            operatingCashFlow = 37500.0,
            capex = 22000.0,
            cashAndEquivalents = 61000.0,
            currentAssets = 175000.0,
            totalAssets = 450000.0,
            currentLiabilities = 125000.0,
            totalDebt = 140000.0,
            totalEquity = 252000.0,
            targetPrice = 6200.0,
            consensus = TeamConsensus.WATCHLIST
        ),
        FinancialInput(
            ticker = "ZTRP",
            companyName = "High Debt Distressed Corp (Sample Risk)",
            period = "FY 2024",
            unit = FinancialUnit.BILLION,
            marketPrice = 120.0,
            sharesOutstanding = 25.0,
            revenue = 18500.0,
            grossProfit = 1600.0,
            operatingIncome = 350.0,
            netIncome = 120.0,
            operatingCashFlow = 70.0,
            capex = 450.0,
            cashAndEquivalents = 310.0,
            currentAssets = 4200.0,
            totalAssets = 24000.0,
            currentLiabilities = 6500.0,
            totalDebt = 14500.0,
            totalEquity = 3000.0,
            targetPrice = 85.0,
            consensus = TeamConsensus.AVOID
        )
    )
}
