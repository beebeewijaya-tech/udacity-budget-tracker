package com.beebee.mybudget.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ExpenseDao {
    @Query("SELECT * from expense_table")
    fun getAllListExpense(): LiveData<List<ExpenseEntities>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllExpense(expense: List<ExpenseEntities>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpense(expense: ExpenseEntities)

    @Query("DELETE FROM expense_table")
    fun clearExpense()
}

@Dao
interface BudgetDao {
    @Query("SELECT * from budget_table")
    fun getAllListBudget(): LiveData<List<BudgetEntities>>

    @Query("SELECT * from budget_table WHERE id = :id")
    fun getDetailBudget(id: String): BudgetEntities

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllBudget(expense: List<BudgetEntities>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBudget(expense: BudgetEntities)

    @Query("UPDATE budget_table SET currentExpense=:currentExpense WHERE id = :budgetId")
    fun updateBudget(budgetId: String, currentExpense: Int)

    @Query("DELETE FROM budget_table")
    fun clearBudget()
}

@Database(entities = [ExpenseEntities::class, BudgetEntities::class], version = 2)
abstract class MainDatabase: RoomDatabase() {
    abstract val expenseDao: ExpenseDao
    abstract val budgetDao: BudgetDao
}

private lateinit var INSTANCE: MainDatabase

fun getInstance(context: Context): MainDatabase {
    synchronized(MainDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext, MainDatabase::class.java, "my_budget")
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    return INSTANCE
}
