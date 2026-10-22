package com.dineshmali.smartemicalculator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dineshmali.smartemicalculator.data.repository.CalculationRepository
import com.dineshmali.smartemicalculator.domain.model.SavedCalculation
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SavedCalculationsViewModel(
    private val repository: CalculationRepository
) : ViewModel() {

    val savedCalculations: StateFlow<List<SavedCalculation>> =
        repository.allCalculations.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteCalculation(id: Long) {
        viewModelScope.launch {
            repository.deleteCalculation(id)
        }
    }

    fun renameCalculation(id: Long, newTitle: String) {
        viewModelScope.launch {
            if (newTitle.isNotBlank()) {
                repository.updateTitle(id, newTitle)
            }
        }
    }
}

class SavedCalculationsViewModelFactory(
    private val repository: CalculationRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SavedCalculationsViewModel(repository) as T
    }
}
