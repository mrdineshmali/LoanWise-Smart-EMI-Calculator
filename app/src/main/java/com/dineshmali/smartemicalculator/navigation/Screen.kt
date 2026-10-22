package com.dineshmali.smartemicalculator.navigation

import com.dineshmali.smartemicalculator.domain.model.LoanType

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Calculator : Screen("calculator/{loanType}") {
        fun createRoute(loanType: LoanType) = "calculator/${loanType.name}"
    }
    object Compare : Screen("compare")
    object Saved : Screen("saved")
    object Settings : Screen("settings")
    object PrivacyPolicy : Screen("privacy_policy")
    object DeveloperDetails : Screen("developer_details")
}
