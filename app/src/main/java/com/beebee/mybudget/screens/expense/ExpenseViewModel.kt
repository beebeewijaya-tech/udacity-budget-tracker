package com.beebee.mybudget.screens.expense

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.*
import com.beebee.mybudget.database.getInstance
import com.beebee.mybudget.domain.Expense
import com.beebee.mybudget.repository.expense.ExpenseRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class ExpenseViewModel(
    val application: Application,
    val activity: Activity
) : ViewModel() {
    private val database = getInstance(application)
    private val expenseRepository = ExpenseRepository(database, activity)

    val listExpense = expenseRepository.listExpense

    init {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true

        if (isConnected) {
            viewModelScope.launch {
                expenseRepository.getExpenseList()
            }
        }
    }

    class ExpenseViewModelFactory(private val application: Application, private val activity: Activity) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
                return ExpenseViewModel(application, activity) as T
            }
            throw Exception("View model not recognized")
        }
    }
}