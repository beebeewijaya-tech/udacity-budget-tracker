package com.beebee.mybudget.repository.budget

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.beebee.mybudget.database.MainDatabase
import com.beebee.mybudget.database.asDomainModel
import com.beebee.mybudget.domain.Budget
import com.beebee.mybudget.network.NetworkBudgetPayload
import com.beebee.mybudget.network.ServiceWithToken
import com.beebee.mybudget.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

class BudgetRepository(private val database: MainDatabase, private val activity: Activity): IBudgetRepository {
    val listBudget: LiveData<List<Budget>> = Transformations.map(database.budgetDao.getAllListBudget()) {
        it.asDomainModel()
    }

    override suspend fun getListBudget() {
        withContext(Dispatchers.IO) {
            val result = ServiceWithToken(activity).budgetService.getExpenseList().asDatabaseModel()

            Timber.i("Hello ${result.toString()}")
            database.budgetDao.insertAllBudget(result)
        }
    }

    override suspend fun saveBudget(budget: Budget) {
        withContext(Dispatchers.IO) {
            val budgetPayload = NetworkBudgetPayload(budget.name, budget.amount, budget.currentExpense)
            val result = ServiceWithToken(activity).budgetService.saveBudget(budgetPayload).asDatabaseModel()

            database.budgetDao.insertBudget(result)
        }
    }

    override suspend fun updateBudget(budgetId: String, currentExpense: Int) {
        withContext(Dispatchers.IO) {
            val budgetDetail = database.budgetDao.getDetailBudget(budgetId)
            val newExpense = budgetDetail.currentExpense + currentExpense
            database.budgetDao.updateBudget(budgetId, newExpense)
        }
    }

    override suspend fun clearBudget() {
        withContext(Dispatchers.IO) {
            database.budgetDao.clearBudget()
        }
    }
}