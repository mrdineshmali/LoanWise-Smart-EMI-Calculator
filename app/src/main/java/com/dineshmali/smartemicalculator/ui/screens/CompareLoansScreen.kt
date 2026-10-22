package com.dineshmali.smartemicalculator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dineshmali.smartemicalculator.ui.components.AdBannerPlaceholder
import com.dineshmali.smartemicalculator.ui.theme.PrimaryBlue
import com.dineshmali.smartemicalculator.utils.CurrencyFormatter
import com.dineshmali.smartemicalculator.viewmodel.CompareViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareLoansScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: CompareViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Compare Loan Offers",
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
            // Loan 1 Input Card
            LoanInputCard(
                title = "Loan Offer 1 🏷️",
                amount = uiState.amount1,
                rate = uiState.rate1,
                tenureYrs = uiState.tenure1Yrs,
                onChanged = { a, r, t -> viewModel.onLoan1Changed(a, r, t) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Loan 2 Input Card
            LoanInputCard(
                title = "Loan Offer 2 🏷️",
                amount = uiState.amount2,
                rate = uiState.rate2,
                tenureYrs = uiState.tenure2Yrs,
                onChanged = { a, r, t -> viewModel.onLoan2Changed(a, r, t) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Comparison Summary Results
            uiState.comparisonResult?.let { res ->
                Text(
                    text = "Comparison Summary",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (res.cheaperLoanIndex != 0) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFDCFCE7) // Light Green
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Recommended",
                                tint = Color(0xFF15803D)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Loan Offer ${res.cheaperLoanIndex} is cheaper overall! You save ${CurrencyFormatter.formatInr(res.totalPaymentDifference)} in total payout.",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF14532D)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Comparison Table
                Card(
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ComparisonRow(
                            label = "Monthly EMI",
                            val1 = CurrencyFormatter.formatInr(res.loan1Emi.monthlyEmi),
                            val2 = CurrencyFormatter.formatInr(res.loan2Emi.monthlyEmi),
                            diff = CurrencyFormatter.formatInr(res.emiDifference)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        ComparisonRow(
                            label = "Total Interest",
                            val1 = CurrencyFormatter.formatInr(res.loan1Emi.totalInterest),
                            val2 = CurrencyFormatter.formatInr(res.loan2Emi.totalInterest),
                            diff = CurrencyFormatter.formatInr(res.interestDifference)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        ComparisonRow(
                            label = "Total Payout",
                            val1 = CurrencyFormatter.formatInr(res.loan1Emi.totalPayment),
                            val2 = CurrencyFormatter.formatInr(res.loan2Emi.totalPayment),
                            diff = CurrencyFormatter.formatInr(res.totalPaymentDifference)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoanInputCard(
    title: String,
    amount: String,
    rate: String,
    tenureYrs: String,
    onChanged: (String, String, String) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { onChanged(it, rate, tenureYrs) },
                label = { Text("Loan Amount (₹)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = rate,
                    onValueChange = { onChanged(amount, it, tenureYrs) },
                    label = { Text("Rate (% p.a.)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = tenureYrs,
                    onValueChange = { onChanged(amount, rate, it) },
                    label = { Text("Tenure (Years)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ComparisonRow(
    label: String,
    val1: String,
    val2: String,
    diff: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Offer 1", style = MaterialTheme.typography.labelMedium)
                Text(val1, style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp))
            }
            Column {
                Text("Offer 2", style = MaterialTheme.typography.labelMedium)
                Text(val2, style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp))
            }
            Column {
                Text("Diff", style = MaterialTheme.typography.labelMedium)
                Text(diff, style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp, color = MaterialTheme.colorScheme.primary))
            }
        }
    }
}
