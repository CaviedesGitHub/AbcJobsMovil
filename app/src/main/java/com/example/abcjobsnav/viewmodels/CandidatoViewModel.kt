package com.example.abcjobsnav.viewmodels


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.abcjobsnav.models.Candidato
import com.example.abcjobsnav.network.NetworkServiceAdapter
import com.example.abcjobsnav.repositories.CandidatoRepository

class CandidatoViewModel(application: Application) :  AndroidViewModel(application) {
    private val candidatoRepository = CandidatoRepository(application)
    private val _candidato = MutableLiveData<Candidato>()

    val candidato: LiveData<Candidato>
        get() = _candidato

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        Log.d("testing CandidatoViewModel", "Inicio init")
        //refreshDataFromNetwork()
        Log.d("testing CandidatoViewModel", "Fin init")
    }

    public fun refreshDataFromNetwork(idUser: Int, token: String) {
        Log.d("testing CandidatoViewModel", "Inicio refreshDataFromNetwork")
        candidatoRepository.refreshData(idUser, token, {
            Log.d("testing CandidatoViewModel","Response getCandidato ViewModel")
            Log.d("testing viewModel","Response getCandidato ViewModel")
            Log.d("testing viewModel", it.toString())
            _candidato.postValue(it)
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
            if (modelClass.isAssignableFrom(CandidatoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CandidatoViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}