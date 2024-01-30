package com.example.abcjobsnav.viewmodels


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.volley.VolleyError
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

    private var _errorText = MutableLiveData<String>("")
    val errorText:LiveData<String>
        get() = _errorText

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
                    try{
                        var data = candidatoRepository.refreshData(idUser, token)
                        _candidato.postValue(data)
                        _eventNetworkError.postValue(false)
                        _isNetworkErrorShown.postValue(false)
                    }
                    catch (e: VolleyError){
                        val responseBody: String = String(e.networkResponse.data)
                        val data: JSONObject = JSONObject(responseBody)
                        var mensaje: String
                        if (data.isNull("mensaje")){
                            mensaje="nullllll"
                        }
                        else{
                            mensaje = data.getString("mensaje")
                        }
                        _errorText.postValue(e.toString()+"$"+mensaje)  //_eventNetworkError.postValue(true)
                        _isNetworkErrorShown.postValue(false)
                    }
                    catch (e:Exception){ //se procesa la excepcion
                        Log.d("Testing Error LVM", e.toString())
                        _errorText.postValue(e.toString()+"$"+"nullllll")
                        _isNetworkErrorShown.postValue(false)
                        Log.d("Testing Error LVM2", e.toString())
                    }
                }
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