package com.example.abcjobsnav.viewmodels


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.abcjobsnav.models.Candidato
import com.example.abcjobsnav.network.NetworkServiceAdapter
import com.example.abcjobsnav.repositories.CandidatoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

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
        try {
            viewModelScope.launch(Dispatchers.Default){
                withContext(Dispatchers.IO){
                    var data = candidatoRepository.refreshData(idUser, token)
                    _candidato.postValue(data)
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
            if (modelClass.isAssignableFrom(CandidatoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CandidatoViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}