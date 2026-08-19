package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_analyses")
data class SavedAnalysisEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ticker: String,
    val companyName: String,
    val period: String,
    val unit: String,
    val marketPrice: Double,
    val sharesOutstanding: Double,
    val revenue: Double,
    val grossProfit: Double,
    val operatingIncome: Double,
    val netIncome: Double,
    val operatingCashFlow: Double,
    val capex: Double,
    val cashAndEquivalents: Double,
    val currentAssets: Double,
    val totalAssets: Double,
    val currentLiabilities: Double,
    val totalDebt: Double,
    val totalEquity: Double,
    val targetPrice: Double,
    val consensus: String,
    val calculatedGrade: String,
    val roe: Double,
    val der: Double,
    val piotroskiScore: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "team_notes")
data class TeamNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ticker: String,
    val authorName: String,
    val authorRole: String,
    val content: String,
    val consensus: String,
    val timestamp: Long = System.currentTimeMillis()
)
