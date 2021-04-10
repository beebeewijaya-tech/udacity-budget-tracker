package com.beebee.mybudget.repository.expense

import com.beebee.mybudget.domain.Expense

interface IExpenseRepository {
    suspend fun getExpenseList()
    suspend fun insertExpense(expense: Expense, budgetName: String)
    suspend fun clearExpense()
}