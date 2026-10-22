package com.dineshmali.smartemicalculator.domain.calculator

import com.dineshmali.smartemicalculator.domain.model.AmortizationScheduleItem
import com.dineshmali.smartemicalculator.domain.model.EmiResult
import com.dineshmali.smartemicalculator.domain.model.LoanComparisonResult
import kotlin.math.pow

object EmiCalculator {

    /**
     * Calculates the reducing-balance EMI, Total Interest, Total Payment,
     * Principal vs Interest percentages, and complete Amortization Schedule.
     */
    fun calculate(
        principal: Double,
        annualInterestRate: Double,
        tenureMonths: Int
    ): EmiResult {
        if (principal <= 0.0 || tenureMonths <= 0) {
            return EmiResult(
                monthlyEmi = 0.0,
                totalInterest = 0.0,
                totalPayment = 0.0,
                principalAmount = 0.0,
                principalPercentage = 100f,
                interestPercentage = 0f,
                schedule = emptyList()
            )
        }

        // Case: Zero Interest Loan
        if (annualInterestRate <= 0.0) {
            val emi = principal / tenureMonths
            val schedule = mutableListOf<AmortizationScheduleItem>()
            var remaining = principal
            for (month in 1..tenureMonths) {
                val principalPaid = if (month == tenureMonths) remaining else emi
                remaining -= principalPaid
                schedule.add(
                    AmortizationScheduleItem(
                        monthNumber = month,
                        emiPayment = emi,
                        principalPaid = principalPaid,
                        interestPaid = 0.0,
                        remainingBalance = if (remaining < 0) 0.0 else remaining
                    )
                )
            }
            return EmiResult(
                monthlyEmi = emi,
                totalInterest = 0.0,
                totalPayment = principal,
                principalAmount = principal,
                principalPercentage = 100f,
                interestPercentage = 0f,
                schedule = schedule
            )
        }

        // Standard Reducing-Balance EMI formula
        val r = annualInterestRate / 12 / 100
        val n = tenureMonths.toDouble()
        val compoundFactor = (1 + r).pow(n)

        if (compoundFactor <= 1.0 || compoundFactor.isNaN() || (compoundFactor - 1.0) == 0.0) {
            val emi = principal / tenureMonths
            return EmiResult(
                monthlyEmi = emi,
                totalInterest = 0.0,
                totalPayment = principal,
                principalAmount = principal,
                principalPercentage = 100f,
                interestPercentage = 0f,
                schedule = emptyList()
            )
        }

        val monthlyEmi = (principal * r * compoundFactor) / (compoundFactor - 1)
        val totalPayment = monthlyEmi * n
        val totalInterest = totalPayment - principal

        val principalPct = ((principal / totalPayment) * 100).toFloat()
        val interestPct = ((totalInterest / totalPayment) * 100).toFloat()

        // Generate monthly amortization schedule
        val schedule = mutableListOf<AmortizationScheduleItem>()
        var remainingBalance = principal

        for (month in 1..tenureMonths) {
            val interestForMonth = remainingBalance * r
            var principalForMonth = monthlyEmi - interestForMonth
            var emiForMonth = monthlyEmi

            if (month == tenureMonths || remainingBalance - principalForMonth < 0) {
                principalForMonth = remainingBalance
                emiForMonth = principalForMonth + interestForMonth
                remainingBalance = 0.0
            } else {
                remainingBalance -= principalForMonth
            }

            schedule.add(
                AmortizationScheduleItem(
                    monthNumber = month,
                    emiPayment = emiForMonth,
                    principalPaid = principalForMonth,
                    interestPaid = interestForMonth,
                    remainingBalance = if (remainingBalance < 0) 0.0 else remainingBalance
                )
            )
        }

        return EmiResult(
            monthlyEmi = monthlyEmi,
            totalInterest = totalInterest,
            totalPayment = totalPayment,
            principalAmount = principal,
            principalPercentage = principalPct,
            interestPercentage = interestPct,
            schedule = schedule
        )
    }

    /**
     * Compares two loan scenarios and returns detailed metrics on differences.
     */
    fun compare(
        p1: Double, r1: Double, n1: Int,
        p2: Double, r2: Double, n2: Int
    ): LoanComparisonResult {
        val emi1 = calculate(p1, r1, n1)
        val emi2 = calculate(p2, r2, n2)

        val emiDiff = kotlin.math.abs(emi1.monthlyEmi - emi2.monthlyEmi)
        val interestDiff = kotlin.math.abs(emi1.totalInterest - emi2.totalInterest)
        val totalDiff = kotlin.math.abs(emi1.totalPayment - emi2.totalPayment)

        val cheaperIndex = when {
            emi1.totalPayment < emi2.totalPayment -> 1
            emi2.totalPayment < emi1.totalPayment -> 2
            else -> 0
        }

        return LoanComparisonResult(
            loan1Emi = emi1,
            loan2Emi = emi2,
            emiDifference = emiDiff,
            interestDifference = interestDiff,
            totalPaymentDifference = totalDiff,
            cheaperLoanIndex = cheaperIndex
        )
    }
}
