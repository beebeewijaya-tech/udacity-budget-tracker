package com.beebee.mybudget.screens.save_budget

import android.app.Activity
import android.app.Application
import androidx.lifecycle.*
import com.beebee.mybudget.database.getInstance
import com.beebee.mybudget.domain.Budget
import com.beebee.mybudget.repository.budget.BudgetRepository
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@FragmentScoped
class SaveBudgetViewModel @Inject constructor(
    val application: Application,
    val activity: Activity
) : ViewModel() {
    private val database = getInstance(application)
    private val budgetRepository = BudgetRepository(database, activity)

    val budgetName = MutableLiveData<String>()
    val budgetAmount = MutableLiveData<String>()

    private val _success = MutableLiveData<String>()
    val success : LiveData<String>
        get() = _success

    private val _error = MutableLiveData<String>()
    val error : LiveData<String>
        get() = _error

    fun submitBudget() {
        viewModelScope.launch {
            if (budgetName.value.isNullOrEmpty() || budgetAmount.value.isNullOrEmpty()) {
                _error.value = "Please fill all the form above"
            } else {
                val budget = Budget(
                    name = budgetName.value!!,
                    amount = budgetAmount.value!!.toInt(),
                    currentExpense = 0
                )
                try {
                    budgetRepository.saveBudget(budget)
                    _success.value = "Successfully Save Budget!"
                } catch (e: Exception) {
                    _error.value = e.localizedMessage
                }
            }
        }
    }

    fun clearMessage() {
        _error.value = ""
        _success.value = ""
    }

    class SaveBudgetViewModelFactory(
        val application: Application,
        private val activity: Activity
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SaveBudgetViewModel::class.java)) {
                return SaveBudgetViewModel(application, activity) as T
            }
            throw Exception("View model not recognized")
        }
    }
}