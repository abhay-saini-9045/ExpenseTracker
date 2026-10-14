package com.example.expensetracker.navigation

object Routes {
    const val LIST = "list"
    const val ADD = "add"
    const val EDIT = "edit/{expenseId}"

    fun editRoute(id: Int) = "edit/$id"
}