package com.beebee.mybudget.network

import android.app.Activity
import com.beebee.mybudget.domain.AuthUser
import com.beebee.mybudget.utils.Constants
import com.beebee.mybudget.utils.Token
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import timber.log.Timber

interface IAuthService {
    @Headers("Content-Type: application/json")
    @POST("/dev/auth/login")
    suspend fun loginUser(@Body body: AuthUser) : NetworkUser

    @Headers("Content-Type: application/json")
    @POST("/dev/auth/register")
    suspend fun registerUser(@Body body: AuthUser) : NetworkUser
}

interface IExpenseService {
    @GET("/dev/expense")
    suspend fun getExpenseList() : List<NetworkExpense>

    @Headers("Content-Type: application/json")
    @POST("/dev/expense")
    suspend fun saveExpense(@Body body: NetworkExpensePayload) : NetworkExpense
}

interface IBudgetService {
    @GET("/dev/budget")
    suspend fun getExpenseList() : List<NetworkBudget>

    @Headers("Content-Type: application/json")
    @POST("/dev/budget")
    suspend fun saveBudget(@Body body: NetworkBudgetPayload) : NetworkBudget
}

val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object AuthService {
    private val interceptor =
        HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build();

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(Constants.BASE_URL)
        .client(client)
        .build()

    val authService: IAuthService by lazy {
        retrofit.create(IAuthService::class.java)
    }
}

class ServiceWithToken(activity: Activity) {
    private val client = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
        val token = Token.getToken(activity)

        Timber.i("TOKEN $token")
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", token)
            .build()

        chain.proceed(request)
    }).build();

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(Constants.BASE_URL)
        .client(client)
        .build()

    val expenseService: IExpenseService by lazy {
        retrofit.create(IExpenseService::class.java)
    }

    val budgetService: IBudgetService by lazy {
        retrofit.create(IBudgetService::class.java)
    }
}