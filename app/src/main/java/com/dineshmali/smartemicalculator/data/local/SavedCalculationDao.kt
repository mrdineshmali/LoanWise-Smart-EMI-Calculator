package com.dineshmali.smartemicalculator.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedCalculationDao {

    @Query("SELECT * FROM saved_calculations ORDER BY timestamp DESC")
    fun getAllCalculations(): Flow<List<SavedCalculationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalculation(entity: SavedCalculationEntity): Long

    @Delete
    suspend fun deleteCalculation(entity: SavedCalculationEntity)

    @Query("DELETE FROM saved_calculations WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE saved_calculations SET title = :newTitle WHERE id = :id")
    suspend fun updateTitle(id: Long, newTitle: String)
}
