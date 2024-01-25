package com.example.abcjobsnav.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.abcjobsnav.models.Entrevista
import com.example.abcjobsnav.network.NetworkServiceAdapter
import com.example.abcjobsnav.repositories.EntrevistaRepository
import org.json.JSONObject

class EntrevistaViewModel(application: Application) :  AndroidViewModel(application) {
    private val entrevistaRepository = EntrevistaRepository(application)

    private val _entrevistas = MutableLiveData<List<Entrevista>>()

    val entrevistas: LiveData<List<Entrevista>>
        get() = _entrevistas

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        //refreshDataFromNetwork()
    }

    public fun refreshDataFromNetwork(evId: Int, token: String) {
        val postParams = mapOf<String, Any>(
            "empresa" to "",
            "proyecto" to "",
            "perfil" to "",
            "contacto" to ""
        )

        entrevistaRepository.refreshData(JSONObject(postParams), evId, token, {
            _entrevistas.postValue(it)
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
            if (modelClass.isAssignableFrom(EntrevistaViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EntrevistaViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}