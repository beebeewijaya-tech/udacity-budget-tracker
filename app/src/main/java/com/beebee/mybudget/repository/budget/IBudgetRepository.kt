package com.beebee.mybudget.repository.budget

import com.beebee.mybudget.domain.Budget

interface IBudgetRepository {
    suspend fun getListBudget()
    suspend fun saveBudget(budget: Budget)
    suspend fun updateBudget(budgetId: String, currentExpense: Int)
    suspend fun clearBudget()
}