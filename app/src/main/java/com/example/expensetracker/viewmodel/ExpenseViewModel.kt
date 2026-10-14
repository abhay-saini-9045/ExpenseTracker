package com.example.expensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.Expense
import com.example.expensetracker.data.ExpenseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    val allExpenses: StateFlow<List<Expense>> = repository.allExpenses
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalAmount: StateFlow<Double> = repository.totalAmount
        .map { it ?: 0.0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    fun insert(expense: Expense) {
        viewModelScope.launch {
            repository.insert(expense)
        }
    }

    fun update(expense: Expense) {
        viewModelScope.launch {
            repository.update(expense)
        }
    }

    fun delete(expense: Expense) {
        viewModelScope.launch {
            repository.delete(expense)
        }
    }

    suspend fun getExpenseById(id: Int): Expense? {
        return repository.getExpenseById(id)
    }
}


