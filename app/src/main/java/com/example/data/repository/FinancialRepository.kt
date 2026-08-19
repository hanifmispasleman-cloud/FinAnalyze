package com.example.data.repository

import com.example.data.db.FinancialDao
import com.example.data.db.SavedAnalysisEntity
import com.example.data.db.TeamNoteEntity
import com.example.model.FinancialInput
import com.example.model.FinancialUnit
import com.example.model.TeamConsensus
import com.example.model.TeamNote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FinancialRepository(private val dao: FinancialDao) {

    val savedAnalyses: Flow<List<SavedAnalysisEntity>> = dao.getAllSavedAnalyses()

    fun getNotesForTicker(ticker: String): Flow<List<TeamNote>> =
        dao.getNotesForTicker(ticker).map { list ->
            list.map { entity ->
                TeamNote(
                    id = entity.id,
                    analysisTicker = entity.ticker,
                    authorName = entity.authorName,
                    authorRole = entity.authorRole,
                    content = entity.content,
                    consensus = try { TeamConsensus.valueOf(entity.consensus) } catch (e: Exception) { TeamConsensus.BUY },
                    timestamp = entity.timestamp
                )
            }
        }

    suspend fun saveAnalysis(input: FinancialInput, grade: String, roe: Double, der: Double, piotroski: Int): Long {
        val entity = SavedAnalysisEntity(
            ticker = input.ticker,
            companyName = input.companyName,
            period = input.period,
            unit = input.unit.name,
            marketPrice = input.marketPrice,
            sharesOutstanding = input.sharesOutstanding,
            revenue = input.revenue,
            grossProfit = input.grossProfit,
            operatingIncome = input.operatingIncome,
            netIncome = input.netIncome,
            operatingCashFlow = input.operatingCashFlow,
            capex = input.capex,
            cashAndEquivalents = input.cashAndEquivalents,
            currentAssets = input.currentAssets,
            totalAssets = input.totalAssets,
            currentLiabilities = input.currentLiabilities,
            totalDebt = input.totalDebt,
            totalEquity = input.totalEquity,
            targetPrice = input.targetPrice,
            consensus = input.consensus.name,
            calculatedGrade = grade,
            roe = roe,
            der = der,
            piotroskiScore = piotroski
        )
        return dao.insertAnalysis(entity)
    }

    suspend fun deleteAnalysis(id: Long) {
        dao.deleteAnalysisById(id)
    }

    suspend fun addTeamNote(note: TeamNote): Long {
        val entity = TeamNoteEntity(
            ticker = note.analysisTicker,
            authorName = note.authorName,
            authorRole = note.authorRole,
            content = note.content,
            consensus = note.consensus.name,
            timestamp = note.timestamp
        )
        return dao.insertNote(entity)
    }

    suspend fun deleteNote(id: Long) {
        dao.deleteNoteById(id)
    }
}
