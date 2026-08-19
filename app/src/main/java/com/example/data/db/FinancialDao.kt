package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FinancialDao {

    // Saved Analyses
    @Query("SELECT * FROM saved_analyses ORDER BY timestamp DESC")
    fun getAllSavedAnalyses(): Flow<List<SavedAnalysisEntity>>

    @Query("SELECT * FROM saved_analyses WHERE ticker = :ticker ORDER BY timestamp DESC LIMIT 1")
    suspend fun getAnalysisByTicker(ticker: String): SavedAnalysisEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnalysis(analysis: SavedAnalysisEntity): Long

    @Query("DELETE FROM saved_analyses WHERE id = :id")
    suspend fun deleteAnalysisById(id: Long)

    // Team Notes
    @Query("SELECT * FROM team_notes WHERE ticker = :ticker ORDER BY timestamp DESC")
    fun getNotesForTicker(ticker: String): Flow<List<TeamNoteEntity>>

    @Query("SELECT * FROM team_notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<TeamNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: TeamNoteEntity): Long

    @Query("DELETE FROM team_notes WHERE id = :id")
    suspend fun deleteNoteById(id: Long)
}
