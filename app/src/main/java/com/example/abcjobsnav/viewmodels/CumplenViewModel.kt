package com.example.abcjobsnav.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.volley.VolleyError
import com.example.abcjobsnav.models.CandidatoSel
import com.example.abcjobsnav.models.Evaluacion
import com.example.abcjobsnav.models.PerfilProyecto
import com.example.abcjobsnav.network.NetworkServiceAdapter
import com.example.abcjobsnav.repositories.CumplenRepository
import com.example.abcjobsnav.repositories.EvalsPARepository
import org.json.JSONObject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CumplenViewModel(application: Application) :  AndroidViewModel(application) {
    private val cumplenRepository = CumplenRepository(application)

    private val _PerfilProyectoResp = MutableLiveData<PerfilProyecto>()
    val PerfilProyectoResp: LiveData<PerfilProyecto>
        get() = _PerfilProyectoResp

    private val _lstCand = MutableLiveData<List<CandidatoSel>>()
    val lstCand: LiveData<List<CandidatoSel>>
        get() = _lstCand

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private var _errorText = MutableLiveData<String>("")
    val errorText:LiveData<String>
        get() = _errorText

    public var idPerfilProy: Int = 0
    public var idPerfil: Int = 0
    public var idCand: Int = 0
    public var tokenUser: String = ""

    init {
        //refreshDataFromNetwork()
    }

    public fun asignaCandidateDataFromNetwork(id_cand: Int, fecha_ini: String, idPerfilProy: Int, token: String) {
        val postParams = mapOf<String, Any>(
            "id_cand" to id_cand,
            "fecha_inicio" to fecha_ini
        )
        try {
            viewModelScope.launch(Dispatchers.Default){
                withContext(Dispatchers.IO){
                    try{
                        var data = cumplenRepository.asignaCandidato(JSONObject(postParams), idPerfilProy, token)
                        Log.d("Testing 1Cumplen ", "viewModel")
                        _PerfilProyectoResp.postValue(data)
                        Log.d("Testing 2Cumplen viewModel", "viewModel")
                        _eventNetworkError.postValue(false)
                        _isNetworkErrorShown.postValue(false)
                        Log.d("Testing 3Cumplen viewModel", "viewModel")
                    }
                    catch (e: VolleyError){
                        var mensaje: String
                        if (e.networkResponse!=null){
                            val responseBody: String = String(e.networkResponse.data)
                            val data: JSONObject = JSONObject(responseBody)
                            if (data.isNull("mensaje")){
                                mensaje="nullllll"
                            }
                            else{
                                mensaje = data.getString("mensaje")
                            }
                            _errorText.postValue(e.toString()+"$"+mensaje)  //_eventNetworkError.postValue(true)
                            _isNetworkErrorShown.postValue(false)
                        }
                        else{
                            mensaje = "network Error"
                        }
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

    public fun refreshDataFromNetwork(perfilId: Int, token: String) {
        try {
            viewModelScope.launch(Dispatchers.Default){
                withContext(Dispatchers.IO){
                    try{
                        var data = cumplenRepository.refreshData(perfilId, token)
                        Log.d("Testing 1Cumplen ", "viewModel")
                        _lstCand.postValue(data)
                        Log.d("Testing 2Cumplen viewModel", "viewModel")
                        _eventNetworkError.postValue(false)
                        _isNetworkErrorShown.postValue(false)
                        Log.d("Testing 3Cumplen viewModel", "viewModel")
                    }
                    catch (e: VolleyError){
                        var mensaje: String
                        if (e.networkResponse!=null){
                            val responseBody: String = String(e.networkResponse.data)
                            val data: JSONObject = JSONObject(responseBody)
                            if (data.isNull("mensaje")){
                                mensaje="nullllll"
                            }
                            else{
                                mensaje = data.getString("mensaje")
                            }
                            _errorText.postValue(e.toString()+"$"+mensaje)  //_eventNetworkError.postValue(true)
                            _isNetworkErrorShown.postValue(false)
                        }
                        else{
                            mensaje = "network Error"
                        }
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
            if (modelClass.isAssignableFrom(CumplenViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CumplenViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}