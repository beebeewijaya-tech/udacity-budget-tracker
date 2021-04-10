package com.beebee.mybudget.screens.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.beebee.mybudget.repository.auth.AuthRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class SignupViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepository()

    val emailText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()

    private val _success = MutableLiveData<String>()
    val success : LiveData<String>
        get() = _success

    private val _error = MutableLiveData<String>()
    val error : LiveData<String>
        get() = _error

    fun submiRegister() {
        Timber.i("Result Hello")
        viewModelScope.launch {
            if (emailText.value.isNullOrEmpty() || passwordText.value.isNullOrEmpty()) {
                _error.value = "Please fill all the form above"
            } else {
                val result = authRepository.registerUser(
                    email = emailText.value!!,
                    password = passwordText.value!!
                )

                if (result.email.isNullOrEmpty()) {
                    _error.value = "Something Went Wrrong!"
                } else {
                    _success.value = "Successfully Registered!"
                }
            }
        }
    }

    fun clearMessage() {
        _error.value = ""
        _success.value = ""
    }

    fun clearAll() {
        emailText.value = ""
        passwordText.value = ""
    }
}