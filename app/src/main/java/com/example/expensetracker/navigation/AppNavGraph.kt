package com.example.expensetracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.expensetracker.data.Expense
import com.example.expensetracker.ui.screens.AddExpenseScreen
import com.example.expensetracker.ui.screens.EditExpenseScreen
import com.example.expensetracker.ui.screens.ExpenseListScreen
import com.example.expensetracker.viewmodel.ExpenseViewModel

@Composable
fun AppNavGraph(viewModel: ExpenseViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LIST
    ) {
        composable(Routes.LIST) {
            ExpenseListScreen(
                viewModel = viewModel,
                onAddClick = { navController.navigate(Routes.ADD) },
                onExpenseClick = { expense ->
                    navController.navigate(Routes.editRoute(expense.id))
                }
            )
        }

        composable(Routes.ADD) {
            AddExpenseScreen(
                onSave = { expense ->
                    viewModel.insert(expense)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.EDIT,
            arguments = listOf(navArgument("expenseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getInt("expenseId") ?: 0
            val expense by produceState<Expense?>(initialValue = null, expenseId) {
                value = viewModel.getExpenseById(expenseId)
            }
            expense?.let { loadedExpense ->
                EditExpenseScreen(
                    expense = loadedExpense,
                    onSave = { updated ->
                        viewModel.update(updated)
                        navController.popBackStack()
                    },
                    onDelete = {
                        viewModel.delete(loadedExpense)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }

    }
}
