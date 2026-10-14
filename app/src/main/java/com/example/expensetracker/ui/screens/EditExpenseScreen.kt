package com.example.expensetracker.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.expensetracker.data.Expense
import com.example.expensetracker.ui.components.CategorySelector
import com.example.expensetracker.ui.components.DeleteConfirmDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExpenseScreen(
    expense: Expense,
    onSave: (Expense) -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)

    var title by rememberSaveable { mutableStateOf(expense.title) }
    var amount by rememberSaveable { mutableStateOf(expense.amount.toString()) }
    var category by rememberSaveable { mutableStateOf(expense.category) }
    var showError by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Expense") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount (₹)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Text("Category", style = MaterialTheme.typography.labelLarge)
            CategorySelector(
                selected = category,
                onSelect = { category = it }
            )

            if (showError) {
                if (title.isBlank() && amount.isBlank()) {
                    Text("Enter a title and amount", color = MaterialTheme.colorScheme.error)
                } else if (title.isBlank()) {
                    Text("Enter a title", color = MaterialTheme.colorScheme.error)
                } else if (amount.isBlank()) {
                    Text("Enter an amount", color = MaterialTheme.colorScheme.error)
                } else if (amount.toDoubleOrNull() == null || amount.toDoubleOrNull()!! <= 0) {
                    Text("Enter a valid amount", color = MaterialTheme.colorScheme.error)
                }
            }

            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull()

                    if (title.isBlank() || amountValue == null || amountValue <= 0) {
                        showError = true
                    } else {
                        showError = false

                        onSave(
                            expense.copy(
                                title = title.trim(),
                                amount = amountValue,
                                category = category
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update")
            }
        }
    }

    if (showDeleteDialog) {
        DeleteConfirmDialog(
            onConfirm = {
                showDeleteDialog = false
                onDelete()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}