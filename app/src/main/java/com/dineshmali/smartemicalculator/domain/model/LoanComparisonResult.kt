package com.dineshmali.smartemicalculator.domain.model

data class LoanComparisonResult(
    val loan1Emi: EmiResult,
    val loan2Emi: EmiResult,
    val emiDifference: Double,
    val interestDifference: Double,
    val totalPaymentDifference: Double,
    val cheaperLoanIndex: Int // 1 if Loan 1 is cheaper, 2 if Loan 2 is cheaper, 0 if equal
)
