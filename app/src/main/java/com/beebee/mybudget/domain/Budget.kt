package com.beebee.mybudget.domain

import com.beebee.mybudget.database.BudgetEntities


data class Budget(
    val id: String? = "",
    val name: String,
    val currentExpense: Int,
    val amount: Int,
)

fun List<Budget>.asDatabaseModel(): List<BudgetEntities> {
    return map {
        BudgetEntities(
            id = it.id as String,
            name = it.name,
            currentExpense = it.currentExpense,
            amount = it.amount,
        )
    }
}