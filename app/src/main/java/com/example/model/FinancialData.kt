package com.example.model

enum class FinancialUnit(val label: String, val multiplierName: String) {
    BILLION("Miliar (Rp M)", "Miliar Rupiah"),
    MILLION("Juta (Rp Jt)", "Juta Rupiah"),
    RAW("Satuan (Rp)", "Rupiah Utuh")
}

enum class MetricStatus {
    SAFE,       // Hijau
    MODERATE,   // Kuning
    RISK        // Merah
}

enum class CompanyGrade(
    val code: String,
    val title: String,
    val description: String
) {
    GRADE_A(
        "GRADE A",
        "Premium / Wonderful Company",
        "Kinerja keuangan sangat prima. Profitabilitas tinggi (ROE > 15%), struktur permodalan aman (DER < 1.0x), arus kas operasi solid melebihi laba bersih, dan skor Piotroski kuat (>= 7)."
    ),
    GRADE_B(
        "GRADE B",
        "Moderate / Fair Company",
        "Kinerja keuangan wajar dan stabil. Profitabilitas moderat (ROE 8-15%), rasio hutang terkendali (DER 1.0-1.5x), dan skor fundamental cukup baik (Piotroski 4-6)."
    ),
    GRADE_C(
        "GRADE C",
        "High Risk / Value Trap",
        "Perhatian! Terindikasi risiko keuangan tinggi atau jebakan nilai (value trap). ROE rendah (< 8%), hutang berbunga membengkak (DER > 2.0x), atau kualitas arus kas lemah (CFO/NI < 0.8x)."
    )
}

enum class TeamConsensus(val label: String, val description: String) {
    BUY("BUY / AKUMULASI", "Rekomendasi beli dengan prospek fundamental kuat dan valuasi menarik."),
    WATCHLIST("WATCHLIST / PANTAU", "Pantau perkembangan kinerja kuartal berikutnya atau tunggu harga koreksi."),
    AVOID("AVOID / HINDARI", "Hindari emiten karena risiko solvabilitas tinggi atau kualitas kas rapuh.")
}

data class FinancialInput(
    val ticker: String = "BBCA",
    val companyName: String = "Bank Central Asia Tbk",
    val period: String = "FY 2024",
    val unit: FinancialUnit = FinancialUnit.BILLION,
    val marketPrice: Double = 9800.0,
    val sharesOutstanding: Double = 123.28, // In billion shares if unit is billion
    // Laporan Laba Rugi
    val revenue: Double = 104500.0,
    val grossProfit: Double = 78200.0,
    val operatingIncome: Double = 60100.0, // EBIT
    val netIncome: Double = 48600.0,
    // Arus Kas & Neraca
    val operatingCashFlow: Double = 54200.0, // CFO
    val capex: Double = 7500.0,
    val cashAndEquivalents: Double = 45000.0,
    val currentAssets: Double = 310000.0,
    val totalAssets: Double = 1408000.0,
    val currentLiabilities: Double = 195000.0,
    val totalDebt: Double = 120000.0, // Total Utang Berbunga
    val totalEquity: Double = 245000.0,
    // Team & Valuation Notes
    val targetPrice: Double = 11500.0,
    val consensus: TeamConsensus = TeamConsensus.BUY
)

data class RatioMetric(
    val id: String,
    val name: String,
    val category: String,
    val formulaDisplay: String,
    val value: Double,
    val formattedValue: String,
    val benchmarkSafe: String,
    val status: MetricStatus,
    val statusNote: String
)

data class PiotroskiCriterion(
    val number: Int,
    val title: String,
    val description: String,
    val isPassed: Boolean,
    val detailValue: String
)

data class GradeRuleCheck(
    val title: String,
    val requiredBenchmark: String,
    val actualValue: String,
    val isPassed: Boolean,
    val isCriticalRisk: Boolean = false
)

data class CalculationEngineResult(
    val roe: Double,
    val npm: Double,
    val gpm: Double,
    val opm: Double,
    val roa: Double,
    val der: Double,
    val currentRatio: Double,
    val cfoToNetIncome: Double,
    val fcf: Double,
    val fcfYield: Double,
    val marketCap: Double,
    val piotroskiScore: Int,
    val piotroskiCriteria: List<PiotroskiCriterion>,
    val grade: CompanyGrade,
    val gradeRuleChecks: List<GradeRuleCheck>,
    val metricsList: List<RatioMetric>,
    val summaryDiagnosis: String
)

data class TeamNote(
    val id: Long = 0,
    val analysisTicker: String,
    val authorName: String,
    val authorRole: String,
    val content: String,
    val consensus: TeamConsensus,
    val timestamp: Long = System.currentTimeMillis()
)
