package com.dineshmali.smartemicalculator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dineshmali.smartemicalculator.data.repository.CalculationRepository
import com.dineshmali.smartemicalculator.domain.calculator.EmiCalculator
import com.dineshmali.smartemicalculator.domain.model.SavedCalculation
import com.dineshmali.smartemicalculator.ui.components.AdBannerPlaceholder
import com.dineshmali.smartemicalculator.ui.theme.PrimaryBlue
import com.dineshmali.smartemicalculator.utils.CurrencyFormatter
import com.dineshmali.smartemicalculator.utils.ShareUtils
import com.dineshmali.smartemicalculator.viewmodel.SavedCalculationsViewModel
import com.dineshmali.smartemicalculator.viewmodel.SavedCalculationsViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedCalculationsScreen(
    repository: CalculationRepository,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: SavedCalculationsViewModel = viewModel(
        factory = SavedCalculationsViewModelFactory(repository)
    )
    val savedList by viewModel.savedCalculations.collectAsState()

    var editingItem by remember { mutableStateOf<SavedCalculation?>(null) }
    var editTitleText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Saved Calculations",
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
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (savedList.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No saved calculations yet 📂",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Calculations saved from any calculator screen will appear here.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(savedList, key = { it.id }) { item ->
                        SavedItemCard(
                            item = item,
                            onRename = {
                                editingItem = item
                                editTitleText = item.title
                            },
                            onDelete = { viewModel.deleteCalculation(item.id) },
                            onShare = {
                                val result = EmiCalculator.calculate(
                                    item.principal,
                                    item.annualInterestRate,
                                    item.tenureMonths
                                )
                                val text = ShareUtils.buildSummaryText(
                                    item.title,
                                    item.principal,
                                    item.annualInterestRate,
                                    item.tenureMonths,
                                    result
                                )
                                ShareUtils.shareCalculation(context, text)
                            }
                        )
                    }
                }
            }
        }
    }

    // Rename Dialog
    editingItem?.let { item ->
        AlertDialog(
            onDismissRequest = { editingItem = null },
            title = { Text("Rename Calculation") },
            text = {
                OutlinedTextField(
                    value = editTitleText,
                    onValueChange = { editTitleText = it },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.renameCalculation(item.id, editTitleText)
                        editingItem = null
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingItem = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SavedItemCard(
    item: SavedCalculation,
    onRename: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )
                Row {
                    IconButton(onClick = onRename) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Rename")
                    }
                    IconButton(onClick = onShare) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Principal", style = MaterialTheme.typography.labelMedium)
                    Text(CurrencyFormatter.formatInr(item.principal), style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
                }
                Column {
                    Text("Monthly EMI", style = MaterialTheme.typography.labelMedium)
                    Text(
                        CurrencyFormatter.formatInr(item.monthlyEmi),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
                Column {
                    Text("Interest Rate", style = MaterialTheme.typography.labelMedium)
                    Text(CurrencyFormatter.formatPercentage(item.annualInterestRate), style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
                }
            }
        }
    }
}
