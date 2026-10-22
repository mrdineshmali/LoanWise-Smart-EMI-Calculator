package com.dineshmali.smartemicalculator.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.dineshmali.smartemicalculator.domain.model.EmiResult

object ShareUtils {

    fun buildSummaryText(
        loanTitle: String,
        principal: Double,
        annualRate: Double,
        tenureMonths: Int,
        result: EmiResult
    ): String {
        return """
            📊 *Smart EMI Calculation Summary*
            ------------------------------------
            🏷️ Loan Type: $loanTitle
            💰 Principal Amount: ${CurrencyFormatter.formatInr(principal)}
            📈 Interest Rate: ${CurrencyFormatter.formatPercentage(annualRate)} p.a.
            ⏳ Tenure: ${CurrencyFormatter.formatTenure(tenureMonths)}
            
            ------------------------------------
            💳 *Monthly EMI: ${CurrencyFormatter.formatInr(result.monthlyEmi)}*
            🔥 Total Interest: ${CurrencyFormatter.formatInr(result.totalInterest)}
            💸 Total Payment: ${CurrencyFormatter.formatInr(result.totalPayment)}
            ------------------------------------
            Calculated using Smart EMI Calculator App 📱
        """.trimIndent()
    }

    fun shareCalculation(context: Context, text: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share EMI Summary")
        context.startActivity(shareIntent)
    }

    fun copyToClipboard(context: Context, text: String, label: String = "EMI Summary") {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
    }
}
