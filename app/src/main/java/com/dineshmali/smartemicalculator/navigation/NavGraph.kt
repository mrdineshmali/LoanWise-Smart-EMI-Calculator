package com.dineshmali.smartemicalculator.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dineshmali.smartemicalculator.data.repository.CalculationRepository
import com.dineshmali.smartemicalculator.domain.model.LoanType
import com.dineshmali.smartemicalculator.ui.screens.CalculatorScreen
import com.dineshmali.smartemicalculator.ui.screens.CompareLoansScreen
import com.dineshmali.smartemicalculator.ui.screens.DeveloperDetailsScreen
import com.dineshmali.smartemicalculator.ui.screens.HomeScreen
import com.dineshmali.smartemicalculator.ui.screens.PrivacyPolicyScreen
import com.dineshmali.smartemicalculator.ui.screens.SavedCalculationsScreen
import com.dineshmali.smartemicalculator.ui.screens.SettingsScreen
import com.dineshmali.smartemicalculator.ui.screens.SplashScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    repository: CalculationRepository,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onSelectLoanType = { loanType ->
                    navController.navigate(Screen.Calculator.createRoute(loanType))
                },
                onNavigateToCompare = {
                    navController.navigate(Screen.Compare.route)
                },
                onNavigateToSaved = {
                    navController.navigate(Screen.Saved.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToDeveloperDetails = {
                    navController.navigate(Screen.DeveloperDetails.route)
                }
            )
        }

        composable(
            route = Screen.Calculator.route,
            arguments = listOf(navArgument("loanType") { type = NavType.StringType })
        ) { backStackEntry ->
            val loanTypeName = backStackEntry.arguments?.getString("loanType") ?: LoanType.PERSONAL.name
            val loanType = try {
                LoanType.valueOf(loanTypeName)
            } catch (e: Exception) {
                LoanType.PERSONAL
            }

            CalculatorScreen(
                loanType = loanType,
                repository = repository,
                onNavigateBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                onNavigateToSaved = { navController.navigate(Screen.Saved.route) }
            )
        }

        composable(Screen.Compare.route) {
            CompareLoansScreen(
                onNavigateBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable(Screen.Saved.route) {
            SavedCalculationsScreen(
                repository = repository,
                onNavigateBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
                onNavigateToPrivacyPolicy = { navController.navigate(Screen.PrivacyPolicy.route) },
                onNavigateToDeveloperDetails = { navController.navigate(Screen.DeveloperDetails.route) },
                onNavigateBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable(Screen.PrivacyPolicy.route) {
            PrivacyPolicyScreen(
                onNavigateBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable(Screen.DeveloperDetails.route) {
            DeveloperDetailsScreen(
                onNavigateBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}
