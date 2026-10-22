package com.dineshmali.smartemicalculator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dineshmali.smartemicalculator.data.repository.CalculationRepository
import com.dineshmali.smartemicalculator.domain.calculator.EmiCalculator
import com.dineshmali.smartemicalculator.domain.model.EmiResult
import com.dineshmali.smartemicalculator.domain.model.LoanType
import com.dineshmali.smartemicalculator.domain.model.SavedCalculation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

data class CalculatorUiState(
    val loanType: LoanType = LoanType.PERSONAL,
    val amountInput: String = "500000",
    val rateInput: String = "11.5",
    val tenureInput: String = "3",
    val isTenureInYears: Boolean = true,
    val amountError: String? = null,
    val rateError: String? = null,
    val tenureError: String? = null,
    val result: EmiResult = EmiResult(0.0, 0.0, 0.0, 0.0, 100f, 0f),
    val isSaved: Boolean = false
)

class CalculatorViewModel(
    private val repository: CalculationRepository,
    initialLoanType: LoanType
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState(loanType = initialLoanType))
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    init {
        setLoanType(initialLoanType)
    }

    fun setLoanType(type: LoanType) {
        _uiState.update {
            it.copy(
                loanType = type,
                amountInput = type.defaultAmount.toInt().toString(),
                rateInput = type.defaultRate.toString(),
                tenureInput = type.defaultTenureYears.toString(),
                isTenureInYears = true,
                amountError = null,
                rateError = null,
                tenureError = null
            )
        }
        recalculate()
    }

    fun onAmountChanged(newAmount: String) {
        _uiState.update { it.copy(amountInput = newAmount, isSaved = false) }
        recalculate()
    }

    fun onAmountSliderChanged(sliderValue: Float) {
        onAmountChanged(sliderValue.toInt().toString())
    }

    fun onRateChanged(newRate: String) {
        _uiState.update { it.copy(rateInput = newRate, isSaved = false) }
        recalculate()
    }

    fun onRateSliderChanged(sliderValue: Float) {
        val formatted = String.format(Locale.US, "%.1f", sliderValue)
        onRateChanged(formatted)
    }

    fun onTenureChanged(newTenure: String) {
        _uiState.update { it.copy(tenureInput = newTenure, isSaved = false) }
        recalculate()
    }

    fun onTenureSliderChanged(sliderValue: Float) {
        onTenureChanged(sliderValue.toInt().toString())
    }

    fun toggleTenureUnit(isYears: Boolean) {
        val currentTenure = _uiState.value.tenureInput.toIntOrNull() ?: 0
        val newTenureStr = if (isYears) {
            val yrs = (currentTenure / 12).coerceAtLeast(1)
            yrs.toString()
        } else {
            val mos = (currentTenure * 12).coerceAtLeast(12)
            mos.toString()
        }

        _uiState.update {
            it.copy(
                isTenureInYears = isYears,
                tenureInput = newTenureStr,
                isSaved = false
            )
        }
        recalculate()
    }

    fun resetInputs() {
        val type = _uiState.value.loanType
        setLoanType(type)
    }

    fun saveCalculation(customTitle: String? = null, onResult: (Boolean) -> Unit = {}) {
        val state = _uiState.value
        val principal = state.amountInput.toDoubleOrNull()
        val rate = state.rateInput.toDoubleOrNull()
        val tenureVal = state.tenureInput.toIntOrNull()
        val tenureMonths = if (state.isTenureInYears) (tenureVal ?: 0) * 12 else (tenureVal ?: 0)

        if (principal == null || principal <= 0 || rate == null || rate < 0 || tenureVal == null || tenureVal <= 0 || state.amountError != null || state.rateError != null || state.tenureError != null) {
            onResult(false)
            return
        }

        viewModelScope.launch {
            val title = customTitle.takeUnless { it.isNull_or_blank() }
                ?: "${state.loanType.title} (${state.result.monthlyEmi.toInt()}/mo)"

            val savedItem = SavedCalculation(
                title = title,
                loanType = state.loanType,
                principal = principal,
                annualInterestRate = rate,
                tenureMonths = tenureMonths,
                monthlyEmi = state.result.monthlyEmi,
                totalInterest = state.result.totalInterest,
                totalPayment = state.result.totalPayment
            )

            repository.saveCalculation(savedItem)
            _uiState.update { it.copy(isSaved = true) }
            onResult(true)
        }
    }

    private fun recalculate() {
        val state = _uiState.value

        var amountErr: String? = null
        var rateErr: String? = null
        var tenureErr: String? = null

        val principal = state.amountInput.toDoubleOrNull()
        if (principal == null || principal <= 0) {
            amountErr = "Enter a valid amount (> 0)"
        } else if (principal > state.loanType.maxAmount) {
            amountErr = "Amount exceeds max limit"
        }

        val rate = state.rateInput.toDoubleOrNull()
        if (rate == null || rate < 0) {
            rateErr = "Enter valid rate (≥ 0%)"
        } else if (rate > 50) {
            rateErr = "Rate seems too high"
        }

        val tenure = state.tenureInput.toIntOrNull()
        val tenureMonths = if (state.isTenureInYears) (tenure ?: 0) * 12 else (tenure ?: 0)

        if (tenure == null || tenure <= 0) {
            tenureErr = "Enter valid tenure"
        } else if (state.isTenureInYears && tenure > state.loanType.maxTenureYears) {
            tenureErr = "Tenure exceeds max limit"
        } else if (!state.isTenureInYears && tenureMonths > state.loanType.maxTenureYears * 12) {
            tenureErr = "Tenure exceeds max limit"
        }

        _uiState.update {
            it.copy(
                amountError = amountErr,
                rateError = rateErr,
                tenureError = tenureErr
            )
        }

        if (amountErr == null && rateErr == null && tenureErr == null && principal != null && rate != null) {
            val result = EmiCalculator.calculate(principal, rate, tenureMonths)
            _uiState.update { it.copy(result = result) }
        }
    }

    private fun String?.isNull_or_blank(): Boolean = this == null || this.trim().isEmpty()
}

class CalculatorViewModelFactory(
    private val repository: CalculationRepository,
    private val initialLoanType: LoanType
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CalculatorViewModel(repository, initialLoanType) as T
    }
}
