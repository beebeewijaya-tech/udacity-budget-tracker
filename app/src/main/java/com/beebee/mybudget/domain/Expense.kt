package com.beebee.mybudget.domain

import com.beebee.mybudget.database.ExpenseEntities

data class Expense(
    val id: String? = "",
    val name: String,
    val expense: String,
    val budgetId: String,
    val description: String,
    val createdAt: String? = ""
)

fun List<Expense>.asDatabaseModel(): List<ExpenseEntities> {
    return map {
        ExpenseEntities(
            id = it.id as String,
            name = it.name,
            expense = it.expense,
            budgetId = it.budgetId,
            description = it.description,
            createdAt = it.createdAt as String
        )
    }
}