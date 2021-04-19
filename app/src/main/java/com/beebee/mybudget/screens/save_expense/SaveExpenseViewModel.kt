package com.beebee.mybudget.screens.save_expense

import android.app.Activity
import android.app.Application
import androidx.lifecycle.*
import com.beebee.mybudget.database.getInstance
import com.beebee.mybudget.domain.Budget
import com.beebee.mybudget.domain.Expense
import com.beebee.mybudget.repository.budget.BudgetRepository
import com.beebee.mybudget.repository.expense.ExpenseRepository
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@FragmentScoped
class SaveExpenseViewModel @Inject constructor(
    val application: Application,
    val activity: Activity
) : ViewModel() {
    private val database = getInstance(application)
    private val budgetRepository = BudgetRepository(database, activity)
    private val expenseRepository = ExpenseRepository(database, activity)

    val budgetList = budgetRepository.listBudget

    val expenseName = MutableLiveData<String>()
    val expenseAmount = MutableLiveData<String>()
    val expenseDescription = MutableLiveData<String>()

    private val _success = MutableLiveData<String>()
    val success: LiveData<String>
        get() = _success

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error


    fun submitExpense(budgetId: String, budgetName: String) {
        viewModelScope.launch {
            if (expenseName.value.isNullOrEmpty()
                || expenseAmount.value.isNullOrEmpty()
                || expenseDescription.value.isNullOrEmpty()
                || budgetId.isNullOrEmpty()
            ) {
                _error.value = "Please fill all the form above"
            } else {
                val expense = Expense(
                    name = expenseName.value!!,
                    expense = expenseAmount.value!!,
                    budgetId = budgetId,
                    description = expenseDescription.value!!
                )
                try {
                    expenseRepository.insertExpense(expense, budgetName)
                    budgetRepository.updateBudget(budgetId, expenseAmount.value!!.toInt())
                    _success.value = "Successfully Save Expense!"
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

    class SaveExpenseViewModelFactory(
        val application: Application,
        val activity: Activity
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SaveExpenseViewModel::class.java)) {
                return SaveExpenseViewModel(application, activity) as T
            }
            throw Exception("View model not recognized")
        }
    }
}