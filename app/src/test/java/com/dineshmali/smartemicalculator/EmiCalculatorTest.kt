package com.dineshmali.smartemicalculator

import com.dineshmali.smartemicalculator.domain.calculator.EmiCalculator
import com.dineshmali.smartemicalculator.utils.CurrencyFormatter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EmiCalculatorTest {

    @Test
    fun testStandardPersonalLoanEmiCalculation() {
        val principal = 500000.0 // ₹ 5 Lakhs
        val annualRate = 11.5    // 11.5% p.a.
        val tenureMonths = 36    // 3 Years

        val result = EmiCalculator.calculate(principal, annualRate, tenureMonths)

        // Expected EMI ~ ₹ 16,489
        assertEquals(16489.0, result.monthlyEmi, 50.0)
        assertTrue(result.totalPayment > principal)
        assertEquals(result.totalPayment - principal, result.totalInterest, 0.1)
        assertEquals(36, result.schedule.size)
        assertEquals(0.0, result.schedule.last().remainingBalance, 1.0)
    }

    @Test
    fun testHomeLoanEmiCalculation() {
        val principal = 3500000.0 // ₹ 35 Lakhs
        val annualRate = 8.5      // 8.5% p.a.
        val tenureMonths = 240    // 20 Years

        val result = EmiCalculator.calculate(principal, annualRate, tenureMonths)

        // Expected EMI ~ ₹ 30,370
        assertEquals(30370.0, result.monthlyEmi, 50.0)
        assertEquals(240, result.schedule.size)
        assertEquals(0.0, result.schedule.last().remainingBalance, 1.0)
    }

    @Test
    fun testZeroInterestLoanCalculation() {
        val principal = 120000.0 // ₹ 1.2 Lakhs
        val annualRate = 0.0     // 0% Interest
        val tenureMonths = 12    // 1 Year

        val result = EmiCalculator.calculate(principal, annualRate, tenureMonths)

        assertEquals(10000.0, result.monthlyEmi, 0.01)
        assertEquals(0.0, result.totalInterest, 0.01)
        assertEquals(120000.0, result.totalPayment, 0.01)
        assertEquals(100f, result.principalPercentage, 0.01f)
        assertEquals(0f, result.interestPercentage, 0.01f)
    }

    @Test
    fun testInvalidInputsEdgeCases() {
        val resultZeroPrincipal = EmiCalculator.calculate(0.0, 10.0, 12)
        assertEquals(0.0, resultZeroPrincipal.monthlyEmi, 0.01)

        val resultZeroTenure = EmiCalculator.calculate(50000.0, 10.0, 0)
        assertEquals(0.0, resultZeroTenure.monthlyEmi, 0.01)
    }

    @Test
    fun testIndianCurrencyFormatter() {
        val formatted = CurrencyFormatter.formatInr(150000.0)
        assertTrue(formatted.contains("1,50,000") || formatted.contains("150,000"))
    }

    @Test
    fun testLoanComparisonEngine() {
        val result = EmiCalculator.compare(
            p1 = 1000000.0, r1 = 9.5, n1 = 180, // Offer 1: Higher rate
            p2 = 1000000.0, r2 = 8.5, n2 = 180  // Offer 2: Lower rate
        )

        assertEquals(2, result.cheaperLoanIndex) // Loan 2 should be cheaper
        assertTrue(result.interestDifference > 0)
    }
}
