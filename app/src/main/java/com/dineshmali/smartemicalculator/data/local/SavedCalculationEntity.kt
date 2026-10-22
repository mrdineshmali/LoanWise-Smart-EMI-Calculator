package com.dineshmali.smartemicalculator.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dineshmali.smartemicalculator.domain.model.LoanType
import com.dineshmali.smartemicalculator.domain.model.SavedCalculation

@Entity(tableName = "saved_calculations")
data class SavedCalculationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val loanType: String,
    val principal: Double,
    val annualInterestRate: Double,
    val tenureMonths: Int,
    val monthlyEmi: Double,
    val totalInterest: Double,
    val totalPayment: Double,
    val timestamp: Long
) {
    fun toDomainModel(): SavedCalculation {
        val type = try {
            LoanType.valueOf(loanType)
        } catch (e: Exception) {
            LoanType.PERSONAL
        }
        return SavedCalculation(
            id = id,
            title = title,
            loanType = type,
            principal = principal,
            annualInterestRate = annualInterestRate,
            tenureMonths = tenureMonths,
            monthlyEmi = monthlyEmi,
            totalInterest = totalInterest,
            totalPayment = totalPayment,
            timestamp = timestamp
        )
    }

    companion object {
        fun fromDomainModel(domain: SavedCalculation): SavedCalculationEntity {
            return SavedCalculationEntity(
                id = domain.id,
                title = domain.title,
                loanType = domain.loanType.name,
                principal = domain.principal,
                annualInterestRate = domain.annualInterestRate,
                tenureMonths = domain.tenureMonths,
                monthlyEmi = domain.monthlyEmi,
                totalInterest = domain.totalInterest,
                totalPayment = domain.totalPayment,
                timestamp = domain.timestamp
            )
        }
    }
}
