package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.data.ExpenseDatabase
import com.example.expensetracker.data.ExpenseRepository
import com.example.expensetracker.navigation.AppNavGraph
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.expensetracker.viewmodel.ExpenseViewModel
import com.example.expensetracker.viewmodel.ExpenseViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = ExpenseDatabase.getDatabase(applicationContext)
        val repository = ExpenseRepository(database.expenseDao())
        val factory = ExpenseViewModelFactory(repository)

        setContent {
            ExpenseTrackerTheme {
                val viewModel: ExpenseViewModel = viewModel(factory = factory)
                AppNavGraph(viewModel = viewModel)

            }
        }
    }
}
