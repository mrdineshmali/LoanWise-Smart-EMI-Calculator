package com.dineshmali.smartemicalculator.domain.model

data class SavedCalculation(
    val id: Long = 0,
    val title: String,
    val loanType: LoanType,
    val principal: Double,
    val annualInterestRate: Double,
    val tenureMonths: Int,
    val monthlyEmi: Double,
    val totalInterest: Double,
    val totalPayment: Double,
    val timestamp: Long = System.currentTimeMillis()
)
