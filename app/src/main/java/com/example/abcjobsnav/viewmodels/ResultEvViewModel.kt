package com.example.abcjobsnav.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.abcjobsnav.models.Entrevista
import com.example.abcjobsnav.repositories.ResultEvRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ResultEvViewModel(application: Application) :  AndroidViewModel(application) {
    private val resultEvRepository = ResultEvRepository(application)
    private val _entrevista = MutableLiveData<Entrevista>()

    val entrevista: LiveData<Entrevista>
        get() = _entrevista

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        Log.d("testing ResultEvViewModel", "Inicio init")
        //refreshDataFromNetwork()
        Log.d("testing ResultEvViewModel", "Fin init")
    }

    public fun refreshDataFromNetwork(idEv: Int, token: String) {
        Log.d("testing ResultEvViewModel", "Inicio refreshDataFromNetwork")
        try {
            viewModelScope.launch(Dispatchers.Default){
                withContext(Dispatchers.IO){
                    var data = resultEvRepository.refreshData(idEv, token)
                    _entrevista.postValue(data)
                }
                _eventNetworkError.postValue(false)
                _isNetworkErrorShown.postValue(false)
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
            if (modelClass.isAssignableFrom(ResultEvViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ResultEvViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}