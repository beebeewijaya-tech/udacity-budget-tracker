package com.beebee.mybudget.repository.expense

import android.app.Activity
import android.app.NotificationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Transformations
import com.beebee.mybudget.database.ExpenseEntities
import com.beebee.mybudget.database.MainDatabase
import com.beebee.mybudget.database.asDomainModel
import com.beebee.mybudget.domain.Expense
import com.beebee.mybudget.network.NetworkExpensePayload
import com.beebee.mybudget.network.ServiceWithToken
import com.beebee.mybudget.network.asDatabaseModel
import com.beebee.mybudget.utils.sendNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class ExpenseRepository (private val database: MainDatabase, private val activity: Activity) : IExpenseRepository {
    val listExpense = Transformations.map(database.expenseDao.getAllListExpense()) {
        it.asDomainModel()
    }

    override suspend fun getExpenseList() {
        withContext(Dispatchers.IO) {
            val list = ServiceWithToken(activity).expenseService.getExpenseList().asDatabaseModel()

            database.expenseDao.insertAllExpense(list)
        }
    }


    override suspend fun insertExpense(expense: Expense, budgetName: String) {
        withContext(Dispatchers.IO) {
            val expensePayload = NetworkExpensePayload(
                name = expense.name,
                description = expense.description,
                expense = expense.expense.toInt(),
                budgetId = expense.budgetId
            )
            val result = ServiceWithToken(activity).expenseService.saveExpense(expensePayload)
            Timber.i("Hello ${result.toString()}")

            if (result?.overBudget == true) {
                val notificationManager = ContextCompat.getSystemService(activity, NotificationManager::class.java) as NotificationManager
                notificationManager.sendNotification(
                    "Your $budgetName is overspend!",
                    activity
                )
            }

            val databaseEntity = result.asDatabaseModel()
            val data = ExpenseEntities(
                id = databaseEntity.id,
                name = databaseEntity.name,
                description = databaseEntity.description,
                budgetId = budgetName,
                createdAt = databaseEntity.createdAt,
                expense = "- $ USD ${databaseEntity.expense}"
            )
            database.expenseDao.insertExpense(data)
        }
    }

    override suspend fun clearExpense() {
        withContext(Dispatchers.IO) {
            database.expenseDao.clearExpense()
        }
    }
}