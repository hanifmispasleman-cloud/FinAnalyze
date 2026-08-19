package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.SavedAnalysisEntity
import com.example.data.repository.FinancialRepository
import com.example.model.CalculationEngineResult
import com.example.model.FinancialEngine
import com.example.model.FinancialInput
import com.example.model.FinancialUnit
import com.example.model.TeamConsensus
import com.example.model.TeamNote
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class FinancialViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FinancialRepository

    private val _inputState = MutableStateFlow(FinancialEngine.PRESET_COMPANIES[0])
    val inputState: StateFlow<FinancialInput> = _inputState.asStateFlow()

    private val _selectedTab = MutableStateFlow(0) // 0: Form Input, 1: Rasio & Skor, 2: Klasifikasi Grade, 3: Workspace Tim
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    val calculationResult: StateFlow<CalculationEngineResult> = _inputState
        .combine(_inputState) { input, _ ->
            FinancialEngine.calculate(input)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = FinancialEngine.calculate(_inputState.value)
        )

    val savedAnalyses: StateFlow<List<SavedAnalysisEntity>>

    val teamNotes: StateFlow<List<TeamNote>>

    init {
        val db = AppDatabase.getDatabase(application)
        repository = FinancialRepository(db.financialDao())

        savedAnalyses = repository.savedAnalyses.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        teamNotes = _inputState.flatMapLatest { input ->
            repository.getNotesForTicker(input.ticker)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Seed initial sample notes and saved analysis if DB is empty
        viewModelScope.launch {
            // Check and seed initial notes for BBCA and ICBP
            seedInitialDemoData()
        }
    }

    private suspend fun seedInitialDemoData() {
        // Prepopulate demo team notes for BBCA
        repository.addTeamNote(
            TeamNote(
                analysisTicker = "BBCA",
                authorName = "Reyhan Adhitama",
                authorRole = "Lead Financial Analyst",
                content = "Kualitas aset & CASA prima (>80%). ROE di atas 20% konsisten dengan risiko kredit rendah (NPL < 1.9%). Rekomendasi Accumulate dengan target Rp11.500.",
                consensus = TeamConsensus.BUY,
                timestamp = System.currentTimeMillis() - 86400000L
            )
        )
        repository.addTeamNote(
            TeamNote(
                analysisTicker = "BBCA",
                authorName = "Nadia Safitri",
                authorRole = "Risk & Portfolio Specialist",
                content = "Arus kas operasional kuat melampaui net income (CFO/NI > 1.1x). Piotroski F-Score 8/9 sangat solid.",
                consensus = TeamConsensus.BUY,
                timestamp = System.currentTimeMillis() - 36000000L
            )
        )
    }

    fun setSelectedTab(index: Int) {
        _selectedTab.value = index
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun showMessage(msg: String) {
        _snackbarMessage.value = msg
    }

    fun updateInput(reducer: (FinancialInput) -> FinancialInput) {
        _inputState.value = reducer(_inputState.value)
    }

    fun updateTicker(ticker: String) = updateInput { it.copy(ticker = ticker.uppercase().trim()) }
    fun updateCompanyName(name: String) = updateInput { it.copy(companyName = name) }
    fun updatePeriod(period: String) = updateInput { it.copy(period = period) }
    fun updateUnit(unit: FinancialUnit) = updateInput { it.copy(unit = unit) }
    fun updateMarketPrice(price: Double) = updateInput { it.copy(marketPrice = price) }
    fun updateSharesOutstanding(shares: Double) = updateInput { it.copy(sharesOutstanding = shares) }

    fun updateRevenue(v: Double) = updateInput { it.copy(revenue = v) }
    fun updateGrossProfit(v: Double) = updateInput { it.copy(grossProfit = v) }
    fun updateOperatingIncome(v: Double) = updateInput { it.copy(operatingIncome = v) }
    fun updateNetIncome(v: Double) = updateInput { it.copy(netIncome = v) }

    fun updateOperatingCashFlow(v: Double) = updateInput { it.copy(operatingCashFlow = v) }
    fun updateCapex(v: Double) = updateInput { it.copy(capex = v) }
    fun updateCashAndEquivalents(v: Double) = updateInput { it.copy(cashAndEquivalents = v) }
    fun updateCurrentAssets(v: Double) = updateInput { it.copy(currentAssets = v) }
    fun updateTotalAssets(v: Double) = updateInput { it.copy(totalAssets = v) }
    fun updateCurrentLiabilities(v: Double) = updateInput { it.copy(currentLiabilities = v) }
    fun updateTotalDebt(v: Double) = updateInput { it.copy(totalDebt = v) }
    fun updateTotalEquity(v: Double) = updateInput { it.copy(totalEquity = v) }

    fun updateTargetPrice(price: Double) = updateInput { it.copy(targetPrice = price) }
    fun updateConsensus(consensus: TeamConsensus) = updateInput { it.copy(consensus = consensus) }

    fun loadPreset(preset: FinancialInput) {
        _inputState.value = preset
        showMessage("Memuat data emiten: ${preset.ticker} - ${preset.companyName}")
    }

    fun loadSavedAnalysis(entity: SavedAnalysisEntity) {
        _inputState.value = FinancialInput(
            ticker = entity.ticker,
            companyName = entity.companyName,
            period = entity.period,
            unit = try { FinancialUnit.valueOf(entity.unit) } catch (e: Exception) { FinancialUnit.BILLION },
            marketPrice = entity.marketPrice,
            sharesOutstanding = entity.sharesOutstanding,
            revenue = entity.revenue,
            grossProfit = entity.grossProfit,
            operatingIncome = entity.operatingIncome,
            netIncome = entity.netIncome,
            operatingCashFlow = entity.operatingCashFlow,
            capex = entity.capex,
            cashAndEquivalents = entity.cashAndEquivalents,
            currentAssets = entity.currentAssets,
            totalAssets = entity.totalAssets,
            currentLiabilities = entity.currentLiabilities,
            totalDebt = entity.totalDebt,
            totalEquity = entity.totalEquity,
            targetPrice = entity.targetPrice,
            consensus = try { TeamConsensus.valueOf(entity.consensus) } catch (e: Exception) { TeamConsensus.BUY }
        )
        showMessage("Analisis tersimpan untuk ${entity.ticker} berhasil dimuat.")
    }

    fun resetToBlank() {
        _inputState.value = FinancialInput(
            ticker = "BARU",
            companyName = "Nama Perusahaan",
            period = "FY 2024",
            unit = FinancialUnit.BILLION,
            marketPrice = 1000.0,
            sharesOutstanding = 10.0,
            revenue = 0.0,
            grossProfit = 0.0,
            operatingIncome = 0.0,
            netIncome = 0.0,
            operatingCashFlow = 0.0,
            capex = 0.0,
            cashAndEquivalents = 0.0,
            currentAssets = 0.0,
            totalAssets = 0.0,
            currentLiabilities = 0.0,
            totalDebt = 0.0,
            totalEquity = 0.0,
            targetPrice = 1000.0,
            consensus = TeamConsensus.WATCHLIST
        )
        showMessage("Formulir telah direset ke formulir kosong.")
    }

    fun saveCurrentAnalysis() {
        viewModelScope.launch {
            val calc = calculationResult.value
            val current = _inputState.value
            repository.saveAnalysis(
                input = current,
                grade = calc.grade.code,
                roe = calc.roe,
                der = calc.der,
                piotroski = calc.piotroskiScore
            )
            showMessage("Analisis ${current.ticker} berhasil disimpan ke database!")
        }
    }

    fun deleteSavedAnalysis(id: Long) {
        viewModelScope.launch {
            repository.deleteAnalysis(id)
            showMessage("Analisis berhasil dihapus.")
        }
    }

    fun addTeamNote(authorName: String, authorRole: String, content: String, consensus: TeamConsensus) {
        if (authorName.isBlank() || content.isBlank()) {
            showMessage("Mohon isi nama analis dan catatan.")
            return
        }
        viewModelScope.launch {
            val note = TeamNote(
                analysisTicker = _inputState.value.ticker,
                authorName = authorName.trim(),
                authorRole = authorRole.ifBlank { "Financial Analyst" }.trim(),
                content = content.trim(),
                consensus = consensus,
                timestamp = System.currentTimeMillis()
            )
            repository.addTeamNote(note)
            showMessage("Catatan tim untuk ${_inputState.value.ticker} berhasil ditambahkan.")
        }
    }

    fun deleteTeamNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
            showMessage("Catatan telah dihapus.")
        }
    }

    fun generateShareReport(): String {
        val input = _inputState.value
        val res = calculationResult.value
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale("id", "ID"))
        val dateStr = sdf.format(Date())

        return buildString {
            appendLine("📊 *LAPORAN ANALISIS FUNDAMENTAL - FinAnalyzer*")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("🏢 *Emiten*: ${input.ticker} (${input.companyName})")
            appendLine("📅 *Periode*: ${input.period} | *Satuan*: ${input.unit.label}")
            appendLine("💰 *Harga Saham*: Rp ${String.format(Locale.US, "%,.0f", input.marketPrice)}")
            appendLine("🎯 *Target Price*: Rp ${String.format(Locale.US, "%,.0f", input.targetPrice)}")
            appendLine("👥 *Konsensus Tim*: ${input.consensus.label}")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("🏆 *KLASIFIKASI*: ${res.grade.code} (${res.grade.title})")
            appendLine("⭐ *Piotroski F-Score*: ${res.piotroskiScore}/9 Poin")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("📈 *RINGKASAN RASIO UTAMA*:")
            appendLine("• ROE: ${String.format(Locale.US, "%.2f%%", res.roe)} (Safe: >15%)")
            appendLine("• NPM: ${String.format(Locale.US, "%.2f%%", res.npm)} (Safe: >10%)")
            appendLine("• GPM: ${String.format(Locale.US, "%.2f%%", res.gpm)} | OPM: ${String.format(Locale.US, "%.2f%%", res.opm)}")
            appendLine("• DER: ${String.format(Locale.US, "%.2fx", res.der)} (Safe: <1.0x)")
            appendLine("• Current Ratio: ${String.format(Locale.US, "%.2fx", res.currentRatio)} (Safe: >=1.5x)")
            appendLine("• Kualitas Kas (CFO/NI): ${String.format(Locale.US, "%.2fx", res.cfoToNetIncome)} (Safe: >1.0x)")
            appendLine("• Free Cash Flow: ${FinancialEngine.formatCurrency(res.fcf, input.unit)}")
            appendLine("• FCF Yield: ${String.format(Locale.US, "%.2f%%", res.fcfYield)}")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("📝 *DIAGNOSIS SISTEM*:")
            appendLine(res.summaryDiagnosis)
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("🕒 Generated on: $dateStr")
        }
    }
}
