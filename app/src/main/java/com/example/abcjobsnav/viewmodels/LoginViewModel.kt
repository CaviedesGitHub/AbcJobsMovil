package com.example. abcjobsnav.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.abcjobsnav.models.Login
import com.example.abcjobsnav.repositories.LoginRepository
import org.json.JSONObject

class LoginViewModel(application: Application) :  AndroidViewModel(application) {
    private val loginRepository = LoginRepository(application)
    private val _login = MutableLiveData<Login>()

    val login: LiveData<Login>
        get() = _login

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        //refreshDataFromNetwork("", "")
    }

    public fun refreshDataFromNetwork(nombre: String, password: String) {
        val postParams = mapOf<String, Any>(
            "nombre" to nombre,
            "password" to password,
        )
        Log.d("testing refreshData", "Inicio ViewModel")
        loginRepository.refreshData(JSONObject(postParams), {
            Log.d("testing viewModel","Response getLogin ViewModel")
            Log.d("testing viewModel", it.toString())
            _login.postValue(it)
            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false
        },{
            _eventNetworkError.value = true
        })
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}