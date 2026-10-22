package com.dineshmali.smartemicalculator.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {

    private val inrFormatterWithSymbol: DecimalFormat by lazy {
        try {
            val locale = Locale("en", "IN")
            (NumberFormat.getCurrencyInstance(locale) as DecimalFormat).apply {
                positivePrefix = "₹ "
                negativePrefix = "- ₹ "
                maximumFractionDigits = 0
            }
        } catch (e: Exception) {
            DecimalFormat("₹ #,##,##0")
        }
    }

    private val inrFormatterNoSymbol: DecimalFormat by lazy {
        try {
            val locale = Locale("en", "IN")
            (NumberFormat.getCurrencyInstance(locale) as DecimalFormat).apply {
                positivePrefix = ""
                negativePrefix = "- "
                maximumFractionDigits = 0
            }
        } catch (e: Exception) {
            DecimalFormat("#,##,##0")
        }
    }

    /**
     * Formats a double value into Indian Rupee format (e.g. ₹ 1,50,000)
     */
    fun formatInr(amount: Double, includeSymbol: Boolean = true): String {
        if (amount.isNaN() || amount.isInfinite()) {
            return if (includeSymbol) "₹ 0" else "0"
        }
        return try {
            if (includeSymbol) {
                synchronized(inrFormatterWithSymbol) {
                    inrFormatterWithSymbol.format(amount)
                }
            } else {
                synchronized(inrFormatterNoSymbol) {
                    inrFormatterNoSymbol.format(amount)
                }
            }
        } catch (e: Exception) {
            val symbol = if (includeSymbol) "₹ " else ""
            "$symbol${String.format(Locale.US, "%.0f", amount)}"
        }
    }

    /**
     * Formats interest rate percentage (e.g. 8.5%)
     */
    fun formatPercentage(rate: Double): String {
        return String.format(Locale.US, "%.2f%%", rate)
    }

    /**
     * Converts tenure in months to years and months readable string
     */
    fun formatTenure(months: Int): String {
        val years = months / 12
        val remainingMonths = months % 12
        return when {
            years > 0 && remainingMonths > 0 -> "$years yrs $remainingMonths mos"
            years > 0 -> "$years yrs ($months mos)"
            else -> "$months mos"
        }
    }
}
