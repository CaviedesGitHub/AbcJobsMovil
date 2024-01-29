package com.example.abcjobsnav.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.abcjobsnav.models.Signup
import com.example.abcjobsnav.repositories.SignupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class SignupViewModel(application: Application) :  AndroidViewModel(application) {
    private val signupRepository = SignupRepository(application)

    private val _signup = MutableLiveData<Signup>()
    val signup: LiveData<Signup>
        get() = _signup

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

    public fun refreshDataFromNetwork(nombre: String, password: String, password2: String, tipo: String) {
        val postParams = mapOf<String, Any>(
            "nombre" to nombre,
            "password" to password,
            "password2" to password2,
            "tipo" to tipo
        )
        Log.d("testing refreshData", "Inicio SignupViewModel")
        try {
            viewModelScope.launch(Dispatchers.Default){
                withContext(Dispatchers.IO){
                    try{
                        var data = signupRepository.refreshData(JSONObject(postParams))
                        _signup.postValue(data)
                        _eventNetworkError.postValue(false)
                        _isNetworkErrorShown.postValue(false)
                    }
                    catch (e:Exception){ //se procesa la excepcion
                        Log.d("Testing Error LVM", e.toString())
                        _errorText.postValue(e.toString())  //_eventNetworkError.postValue(true)
                        _isNetworkErrorShown.postValue(false)
                        //throw e  Causa Error: Ultima instancia es esta.
                        Log.d("Testing Error LVM2", e.toString())
                    }
                }
                //_eventNetworkError.postValue(false)
                //_isNetworkErrorShown.postValue(false)
            }
        }
        catch (e:Exception){ //se procesa la excepcion
            Log.d("Error", e.toString())
            _eventNetworkError.value = true
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SignupViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}