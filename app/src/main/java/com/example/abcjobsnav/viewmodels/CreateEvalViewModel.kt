package com.example.abcjobsnav.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.volley.VolleyError
import com.example.abcjobsnav.models.Evaluacion
import com.example.abcjobsnav.repositories.EvalsPARepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class CreateEvalViewModel(application: Application) :  AndroidViewModel(application) {
    private val evalRepository = EvalsPARepository(application)
    private val _evaluacion = MutableLiveData<Evaluacion>()

    val evaluacion: LiveData<Evaluacion>
        get() = _evaluacion

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
    public var idCand: Int = 0
    public var tokenUser: String = ""
    public var candidato: String = ""
    public var idEmp: Int = 0

    init {
        //refreshDataFromNetwork("", "")
    }
    public fun refreshDataFromNetwork(
        idPerfilProy: Int,
        idCand: Int,
        year: Int,
        month: Int,
        valuation: String,
        note: String,
        tokenUser: String
    ) {
        val postParams = mapOf<String, Any>(
            "idPerfilProy" to idPerfilProy,
            "year" to year,
            "month" to month,
            "id_cand" to idCand,
            "valuation" to valuation,
            "note" to note
        )
        Log.d("testing refreshData", "Inicio CreateEvalViewModel")
        try {
            viewModelScope.launch(Dispatchers.Default){
                withContext(Dispatchers.IO){
                    try{
                        var data = evalRepository.evalCreate(JSONObject(postParams), tokenUser)
                        _evaluacion.postValue(data)
                        _eventNetworkError.postValue(false)
                        _isNetworkErrorShown.postValue(false)
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
                        _errorText.postValue(e.toString()+"$"+"nullllll")  //_eventNetworkError.postValue(true)
                        _isNetworkErrorShown.postValue(false)
                        //throw e  Causa Error: Ultima instancia es esta.
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
            if (modelClass.isAssignableFrom(CreateEvalViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CreateEvalViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}