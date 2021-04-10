package com.beebee.mybudget.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.beebee.mybudget.domain.Budget
import com.beebee.mybudget.domain.Expense

@Entity(tableName = "expense_table")
data class ExpenseEntities(
    @PrimaryKey
    val id: String,
    val name: String,
    val expense: String,
    val budgetId: String,
    val description: String,
    val createdAt: String
)

@Entity(tableName = "budget_table")
data class BudgetEntities(
    @PrimaryKey
    val id: String,
    val name: String,
    val currentExpense: Int,
    val amount: Int,
)

fun List<ExpenseEntities>.asDomainModel(): List<Expense> {
    return map {
        Expense(
            id = it.id,
            name = it.name,
            expense = it.expense,
            budgetId = it.budgetId,
            description = it.description,
            createdAt = it.createdAt
        )
    }
}

@JvmName("asDomainModelBudgetEntities")
fun List<BudgetEntities>.asDomainModel(): List<Budget> {
    return map {
        Budget(
            id = it.id,
            name = it.name,
            currentExpense = it.currentExpense,
            amount = it.amount,
        )
    }
}