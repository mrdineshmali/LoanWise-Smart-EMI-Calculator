package com.dineshmali.smartemicalculator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dineshmali.smartemicalculator.domain.calculator.EmiCalculator
import com.dineshmali.smartemicalculator.domain.model.LoanComparisonResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CompareUiState(
    val amount1: String = "1000000",
    val rate1: String = "9.0",
    val tenure1Yrs: String = "15",

    val amount2: String = "1000000",
    val rate2: String = "8.5",
    val tenure2Yrs: String = "15",

    val comparisonResult: LoanComparisonResult? = null
)

class CompareViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CompareUiState())
    val uiState: StateFlow<CompareUiState> = _uiState.asStateFlow()

    init {
        recalculateComparison()
    }

    fun onLoan1Changed(amount: String, rate: String, tenureYrs: String) {
        _uiState.update { it.copy(amount1 = amount, rate1 = rate, tenure1Yrs = tenureYrs) }
        recalculateComparison()
    }

    fun onLoan2Changed(amount: String, rate: String, tenureYrs: String) {
        _uiState.update { it.copy(amount2 = amount, rate2 = rate, tenure2Yrs = tenureYrs) }
        recalculateComparison()
    }

    private fun recalculateComparison() {
        val state = _uiState.value
        val p1 = state.amount1.toDoubleOrNull() ?: 0.0
        val r1 = state.rate1.toDoubleOrNull() ?: 0.0
        val n1 = (state.tenure1Yrs.toIntOrNull() ?: 0) * 12

        val p2 = state.amount2.toDoubleOrNull() ?: 0.0
        val r2 = state.rate2.toDoubleOrNull() ?: 0.0
        val n2 = (state.tenure2Yrs.toIntOrNull() ?: 0) * 12

        if (p1 > 0 && n1 > 0 && p2 > 0 && n2 > 0) {
            val result = EmiCalculator.compare(p1, r1, n1, p2, r2, n2)
            _uiState.update { it.copy(comparisonResult = result) }
        }
    }
}
