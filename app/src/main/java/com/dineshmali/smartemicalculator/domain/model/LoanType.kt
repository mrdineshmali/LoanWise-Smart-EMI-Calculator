package com.dineshmali.smartemicalculator.domain.model

enum class LoanType(
    val title: String,
    val defaultAmount: Double,
    val defaultRate: Double,
    val defaultTenureYears: Int,
    val maxAmount: Double,
    val maxTenureYears: Int,
    val iconName: String
) {
    PERSONAL(
        title = "Personal Loan",
        defaultAmount = 500000.0,
        defaultRate = 11.5,
        defaultTenureYears = 3,
        maxAmount = 5000000.0,
        maxTenureYears = 7,
        iconName = "person"
    ),
    HOME(
        title = "Home Loan",
        defaultAmount = 3500000.0,
        defaultRate = 8.5,
        defaultTenureYears = 20,
        maxAmount = 50000000.0,
        maxTenureYears = 30,
        iconName = "home"
    ),
    CAR(
        title = "Car Loan",
        defaultAmount = 800000.0,
        defaultRate = 9.0,
        defaultTenureYears = 5,
        maxAmount = 10000000.0,
        maxTenureYears = 8,
        iconName = "car"
    ),
    BIKE(
        title = "Bike Loan",
        defaultAmount = 120000.0,
        defaultRate = 10.5,
        defaultTenureYears = 2,
        maxAmount = 500000.0,
        maxTenureYears = 5,
        iconName = "bike"
    )
}
