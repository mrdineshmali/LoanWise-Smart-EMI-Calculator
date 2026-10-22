package com.dineshmali.smartemicalculator.data.repository

import com.dineshmali.smartemicalculator.data.local.SavedCalculationDao
import com.dineshmali.smartemicalculator.data.local.SavedCalculationEntity
import com.dineshmali.smartemicalculator.domain.model.SavedCalculation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalculationRepository(private val dao: SavedCalculationDao) {

    val allCalculations: Flow<List<SavedCalculation>> =
        dao.getAllCalculations().map { list ->
            list.map { it.toDomainModel() }
        }

    suspend fun saveCalculation(calculation: SavedCalculation): Long {
        return dao.insertCalculation(SavedCalculationEntity.fromDomainModel(calculation))
    }

    suspend fun deleteCalculation(id: Long) {
        dao.deleteById(id)
    }

    suspend fun updateTitle(id: Long, newTitle: String) {
        dao.updateTitle(id, newTitle)
    }
}
