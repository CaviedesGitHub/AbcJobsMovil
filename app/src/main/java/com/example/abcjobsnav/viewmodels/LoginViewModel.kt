package com.example. abcjobsnav.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.abcjobsnav.models.Login
import com.example.abcjobsnav.repositories.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private var _errorText = MutableLiveData<String>("")
    val errorText:LiveData<String>
        get() = _errorText

    init {
        //refreshDataFromNetwork("", "")
    }

    /*public suspend fun refreshDataFromNetwork(nombre: String, password: String) {
        val postParams = mapOf<String, Any>(
            "nombre" to nombre,
            "password" to password,
        )
        Log.d("testing refreshData", "Inicio ViewModel")
        try {
              var data = loginRepository.refreshData(JSONObject(postParams))
              Log.d("testing datos", data.toString())
              _login.postValue(data)
              Log.d("testing _login LVM", _login.toString())
              Log.d("testing _login LVM", _login.value.toString())
              Log.d("testing login LVM", login.toString())
              Log.d("testing login LVM", login.value.toString())
              //Log.d("testing datos LVM", login.value!!.tipo)
              _eventNetworkError.postValue(false)
              _isNetworkErrorShown.postValue(false)
         }
        catch (e:Exception){ //se procesa la excepcion
            Log.d("Error", e.toString())
            _eventNetworkError.value = true
            throw e
        }
    }*/

    public fun refreshDataFromNetwork(nombre: String, password: String) {
        val postParams = mapOf<String, Any>(
            "nombre" to nombre,
            "password" to password,
        )
        Log.d("testing refreshData", "Inicio ViewModel")
        try {
            viewModelScope.launch(Dispatchers.Default){
                withContext(Dispatchers.IO){
                    try{
                        var data = loginRepository.refreshData(JSONObject(postParams))
                        _login.postValue(data)
                        _eventNetworkError.postValue(false)
                        _isNetworkErrorShown.postValue(false)
                    }
                    catch (e:Exception){ //se procesa la excepcion
                        Log.d("Testing Error LVM", e.toString())
                        _errorText.postValue(e.toString())
                        //_eventNetworkError.postValue(true)
                        _isNetworkErrorShown.postValue(false)
                        //throw e  Causa Error: Ultima instancia es esta.
                        Log.d("Testing Error LVM2", e.toString())
                    }
                }
            }
        }
        catch (e:Exception){ //se procesa la excepcion
            Log.d("Testing ErrorOrig LVM ", e.toString())
            //_eventNetworkError.value = true
        }
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