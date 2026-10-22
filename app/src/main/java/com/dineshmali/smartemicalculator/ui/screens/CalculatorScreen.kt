package com.dineshmali.smartemicalculator.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.dineshmali.smartemicalculator.data.repository.CalculationRepository
import com.dineshmali.smartemicalculator.domain.model.LoanType
import com.dineshmali.smartemicalculator.ui.components.AdBannerPlaceholder
import com.dineshmali.smartemicalculator.ui.components.AmortizationTable
import com.dineshmali.smartemicalculator.ui.components.CurrencyInputField
import com.dineshmali.smartemicalculator.ui.components.DonutChart
import com.dineshmali.smartemicalculator.ui.components.EmiSummaryCard
import com.dineshmali.smartemicalculator.ui.components.QuickPreset
import com.dineshmali.smartemicalculator.ui.theme.PrimaryBlue
import com.dineshmali.smartemicalculator.utils.ShareUtils
import com.dineshmali.smartemicalculator.viewmodel.CalculatorViewModel
import com.dineshmali.smartemicalculator.viewmodel.CalculatorViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    loanType: LoanType,
    repository: CalculationRepository,
    onNavigateBack: () -> Unit,
    onNavigateToSaved: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: CalculatorViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = CalculatorViewModelFactory(repository, loanType)
    )
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "${uiState.loanType.title} Calculator",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.resetInputs() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue)
            )
        },
        bottomBar = {
            AdBannerPlaceholder()
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Inputs Card
            Card(
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // Loan Amount Input
                    val amountVal = uiState.amountInput.toFloatOrNull() ?: 0f
                    val amountPresets = listOf(
                        QuickPreset("₹1L", "100000"),
                        QuickPreset("₹5L", "500000"),
                        QuickPreset("₹10L", "1000000"),
                        QuickPreset("₹25L", "2500000"),
                        QuickPreset("₹50L", "5000000")
                    ).filter { it.value.toFloat() <= uiState.loanType.maxAmount }

                    CurrencyInputField(
                        label = "Loan Amount",
                        value = uiState.amountInput,
                        onValueChange = viewModel::onAmountChanged,
                        sliderValue = amountVal,
                        sliderRange = 10000f..uiState.loanType.maxAmount.toFloat(),
                        onSliderChange = viewModel::onAmountSliderChanged,
                        quickPresets = amountPresets,
                        errorMessage = uiState.amountError
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Interest Rate Input
                    val rateVal = uiState.rateInput.toFloatOrNull() ?: 0f
                    val ratePresets = listOf(
                        QuickPreset("8.5%", "8.5"),
                        QuickPreset("9.5%", "9.5"),
                        QuickPreset("10.5%", "10.5"),
                        QuickPreset("12.0%", "12.0")
                    )

                    CurrencyInputField(
                        label = "Interest Rate (% p.a.)",
                        value = uiState.rateInput,
                        onValueChange = viewModel::onRateChanged,
                        prefixSymbol = "%",
                        sliderValue = rateVal,
                        sliderRange = 1f..30f,
                        onSliderChange = viewModel::onRateSliderChanged,
                        quickPresets = ratePresets,
                        errorMessage = uiState.rateError
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tenure Input + Unit Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Loan Tenure Unit",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Row {
                            FilterChip(
                                selected = uiState.isTenureInYears,
                                onClick = { viewModel.toggleTenureUnit(true) },
                                label = { Text("Years") }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            FilterChip(
                                selected = !uiState.isTenureInYears,
                                onClick = { viewModel.toggleTenureUnit(false) },
                                label = { Text("Months") }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val tenureVal = uiState.tenureInput.toFloatOrNull() ?: 0f
                    val maxTenure = if (uiState.isTenureInYears) {
                        uiState.loanType.maxTenureYears.toFloat()
                    } else {
                        (uiState.loanType.maxTenureYears * 12).toFloat()
                    }

                    val tenurePresets = if (uiState.isTenureInYears) {
                        listOf(
                            QuickPreset("1 Yr", "1"),
                            QuickPreset("3 Yrs", "3"),
                            QuickPreset("5 Yrs", "5"),
                            QuickPreset("10 Yrs", "10"),
                            QuickPreset("20 Yrs", "20")
                        ).filter { it.value.toInt() <= uiState.loanType.maxTenureYears }
                    } else {
                        listOf(
                            QuickPreset("12 Mo", "12"),
                            QuickPreset("36 Mo", "36"),
                            QuickPreset("60 Mo", "60"),
                            QuickPreset("120 Mo", "120"),
                            QuickPreset("240 Mo", "240")
                        ).filter { it.value.toInt() <= uiState.loanType.maxTenureYears * 12 }
                    }

                    CurrencyInputField(
                        label = if (uiState.isTenureInYears) "Tenure in Years" else "Tenure in Months",
                        value = uiState.tenureInput,
                        onValueChange = viewModel::onTenureChanged,
                        prefixSymbol = "⏳",
                        suffixText = if (uiState.isTenureInYears) "Years" else "Months",
                        sliderValue = tenureVal,
                        sliderRange = 1f..maxTenure,
                        onSliderChange = viewModel::onTenureSliderChanged,
                        quickPresets = tenurePresets,
                        errorMessage = uiState.tenureError
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Calculated EMI Summary Card
            EmiSummaryCard(result = uiState.result)

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons Row (Save, Share, Copy)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        viewModel.saveCalculation { success ->
                            if (success) {
                                Toast.makeText(context, "Calculation Saved!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Please enter valid loan details to save", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    shape = MaterialTheme.shapes.small,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "Save",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (uiState.isSaved) "Saved" else "Save",
                            maxLines = 1,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                OutlinedButton(
                    onClick = {
                        val principal = uiState.amountInput.toDoubleOrNull() ?: 0.0
                        val rate = uiState.rateInput.toDoubleOrNull() ?: 0.0
                        val tenureVal = uiState.tenureInput.toIntOrNull() ?: 0
                        val tenureMonths = if (uiState.isTenureInYears) tenureVal * 12 else tenureVal

                        val text = ShareUtils.buildSummaryText(
                            uiState.loanType.title,
                            principal,
                            rate,
                            tenureMonths,
                            uiState.result
                        )
                        ShareUtils.shareCalculation(context, text)
                    },
                    shape = MaterialTheme.shapes.small,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Share",
                            maxLines = 1,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                OutlinedButton(
                    onClick = {
                        val principal = uiState.amountInput.toDoubleOrNull() ?: 0.0
                        val rate = uiState.rateInput.toDoubleOrNull() ?: 0.0
                        val tenureVal = uiState.tenureInput.toIntOrNull() ?: 0
                        val tenureMonths = if (uiState.isTenureInYears) tenureVal * 12 else tenureVal

                        val text = ShareUtils.buildSummaryText(
                            uiState.loanType.title,
                            principal,
                            rate,
                            tenureMonths,
                            uiState.result
                        )
                        ShareUtils.copyToClipboard(context, text)
                    },
                    shape = MaterialTheme.shapes.small,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Copy",
                            maxLines = 1,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Donut Breakdown Chart
            Card(
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Breakdown Analysis",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    DonutChart(
                        principalAmount = uiState.result.principalAmount,
                        interestAmount = uiState.result.totalInterest,
                        principalPercentage = uiState.result.principalPercentage,
                        interestPercentage = uiState.result.interestPercentage
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Amortization Schedule
            AmortizationTable(schedule = uiState.result.schedule)
        }
    }
}
