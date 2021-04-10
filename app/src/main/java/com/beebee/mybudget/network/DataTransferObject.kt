package com.beebee.mybudget.network

import com.beebee.mybudget.database.BudgetEntities
import com.beebee.mybudget.database.ExpenseEntities
import com.beebee.mybudget.domain.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkUser(
    val email: String? = "",
    val password: String? = "",
    val token: String? = "",
    val message: String? = "",
)

@JsonClass(generateAdapter = true)
data class NetworkExpense(
    val id: String,
    val name: String,
    val expense: String,

    @Json(name = "budget_id")
    val budgetId: String,
    val description: String,
    val createdAt: String,

    @Json(name = "user_email")
    val userEmail: String,

    val overBudget: Boolean? = null
)


@JsonClass(generateAdapter = true)
data class NetworkBudget(
    val id: String,
    val name: String,
    val amount: Int,

    @Json(name = "current_expense")
    val currentExpense: Int,

    @Json(name = "user_email")
    val userEmail: String
)

@JsonClass(generateAdapter = true)
data class NetworkBudgetPayload (
    val name: String,
    val amount: Int,

    @Json(name = "current_expense")
    val currentExpense: Int,
)

@JsonClass(generateAdapter = true)
data class NetworkExpensePayload (
    val name: String,
    val expense: Int,

    @Json(name = "budget_id")
    val budgetId: String,
    val description: String,
)


fun NetworkUser.asDomainModel() : User {
    return let {
        User (
            email = it.email,
            token = it.token
        )
    }
}

fun List<NetworkExpense>.asDatabaseModel() : List<ExpenseEntities> {
    return map {
        ExpenseEntities (
            id = it.id,
            name = it.name,
            expense = it.expense,
            budgetId = it.budgetId,
            description = it.description,
            createdAt = it.createdAt
        )
    }
}

@JvmName("asDatabaseModelNetworkBudget")
fun List<NetworkBudget>.asDatabaseModel() : List<BudgetEntities> {
    return map {
        BudgetEntities (
            id = it.id,
            name = it.name,
            currentExpense = it.currentExpense,
            amount = it.amount,
        )
    }
}

fun NetworkBudget.asDatabaseModel(): BudgetEntities {
    return let {
        BudgetEntities (
            id = it.id,
            name = it.name,
            currentExpense = it.currentExpense,
            amount = it.amount,
        )
    }
}

fun NetworkExpense.asDatabaseModel() : ExpenseEntities {
    return let {
        ExpenseEntities (
            id = it.id,
            name = it.name,
            expense = it.expense,
            budgetId = it.budgetId,
            description = it.description,
            createdAt = it.createdAt
        )
    }
}