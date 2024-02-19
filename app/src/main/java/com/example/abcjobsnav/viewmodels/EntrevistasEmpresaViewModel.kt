package com.example.abcjobsnav.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.volley.VolleyError
import com.example.abcjobsnav.models.Entrevista
import com.example.abcjobsnav.models.ListaEntrevista
import com.example.abcjobsnav.network.NetworkServiceAdapter
import com.example.abcjobsnav.repositories.EntrevistasEmpresaRepository
import org.json.JSONObject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EntrevistasEmpresaViewModel(application: Application) :  AndroidViewModel(application) {
    private val entrevistaEmpresaRepository = EntrevistasEmpresaRepository(application)

    private val _entrevistas = MutableLiveData<ListaEntrevista>()
    val entrevistas: LiveData<ListaEntrevista>
        get() = _entrevistas

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private var _errorText = MutableLiveData<String>("")
    val errorText:LiveData<String>
        get() = _errorText

    public var num_pag: Int = 1
    public var total_pags: Int = 0
    public var max_items: Int = 20
    public var nom_proy: String =""
    public var nom_perfil: String =""
    public var nom_cand: String =""
    public var nom_emp: String =""

    init {
        //refreshDataFromNetwork()
    }

    public fun refreshDataFromNetwork(idEmp: Int, token: String, max: Int, num_pag: Int,
                                      proyecto: String, perfil: String,
                                      candidato: String, empresa: String) {
        val postParams = mapOf<String, Any>(
            "max" to max,
            "num_pag" to num_pag,
            "order" to "ASC",
            "proyecto" to proyecto,
            "perfil" to perfil,
            "candidato" to candidato,
            "empresa" to empresa
        )

        try {
            viewModelScope.launch(Dispatchers.Default){
                withContext(Dispatchers.IO){
                    try{
                        var data = entrevistaEmpresaRepository.refreshData(JSONObject(postParams), idEmp, token)
                        Log.d("testing after repo", "VM")
                        _entrevistas.postValue(data)
                        Log.d("testing after value", "VM")
                        _eventNetworkError.postValue(false)
                        _isNetworkErrorShown.postValue(false)
                        Log.d("testing after error", "VM")
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
            if (modelClass.isAssignableFrom(EntrevistasEmpresaViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EntrevistasEmpresaViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}