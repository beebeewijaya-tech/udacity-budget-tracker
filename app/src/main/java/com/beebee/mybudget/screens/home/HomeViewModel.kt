package com.beebee.mybudget.screens.home

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.beebee.mybudget.database.getInstance
import com.beebee.mybudget.repository.budget.BudgetRepository
import com.beebee.mybudget.repository.expense.ExpenseRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    val application: Application,
    val activity: Activity
) : ViewModel() {
    private val database = getInstance(application)
    private val budgetRepository = BudgetRepository(database, activity)
    private val expenseRepository = ExpenseRepository(database, activity)

    fun logout() {
        viewModelScope.launch {
            budgetRepository.clearBudget()
            expenseRepository.clearExpense()
        }
    }

    class HomeViewModelFactory (
        val application: Application,
        val activity: Activity
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(application, activity) as T
            }
            throw Exception("Viewmodel not recognized")
        }
    }
}