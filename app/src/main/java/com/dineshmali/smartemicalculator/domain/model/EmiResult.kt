package com.dineshmali.smartemicalculator.domain.model

data class EmiResult(
    val monthlyEmi: Double,
    val totalInterest: Double,
    val totalPayment: Double,
    val principalAmount: Double,
    val principalPercentage: Float,
    val interestPercentage: Float,
    val schedule: List<AmortizationScheduleItem> = emptyList()
)
