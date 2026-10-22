package com.dineshmali.smartemicalculator.domain.model

data class AmortizationScheduleItem(
    val monthNumber: Int,
    val emiPayment: Double,
    val principalPaid: Double,
    val interestPaid: Double,
    val remainingBalance: Double
)
