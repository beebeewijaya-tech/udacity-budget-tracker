package com.beebee.mybudget.screens.budget

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.*
import com.beebee.mybudget.database.getInstance
import com.beebee.mybudget.domain.Budget
import com.beebee.mybudget.repository.budget.BudgetRepository
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@FragmentScoped
class BudgetViewModel @Inject constructor(
    val application: Application,
    val activity: Activity
) : ViewModel() {
    private val database = getInstance(application)
    private val budgetRepository = BudgetRepository(database, activity)

    val listBudget = budgetRepository.listBudget

    init {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true

        if (isConnected) {
            viewModelScope.launch {
                budgetRepository.getListBudget()
            }
        }
    }

    class BudgetViewModelFactory(
        val application: Application,
        private val activity: Activity
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
                return BudgetViewModel(application, activity) as T
            }
            throw Exception("View model not recognized")
        }
    }
}