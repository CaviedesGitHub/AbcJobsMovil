package com.example.abcjobsnav.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.volley.VolleyError
import com.example.abcjobsnav.models.Evaluacion
import com.example.abcjobsnav.network.NetworkServiceAdapter
import com.example.abcjobsnav.repositories.EvalsPARepository
import org.json.JSONObject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EvalsPAViewModel(application: Application) :  AndroidViewModel(application) {
    private val evalspaRepository = EvalsPARepository(application)

    private val _lstEvalsPuesto = MutableLiveData<List<Evaluacion>>()
    val lstEvalsPuesto: LiveData<List<Evaluacion>>
        get() = _lstEvalsPuesto

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private var _errorText = MutableLiveData<String>("")
    val errorText:LiveData<String>
        get() = _errorText

    public var cache: Boolean = false
    public var idPerfilProy: Int = 0
    public var idCand: Int = 0
    public var tokenUser: String = ""
    init {
        //refreshDataFromNetwork()
    }

    public fun refreshDataFromNetwork(perfilProyId: Int, token: String) {
        try {
            viewModelScope.launch(Dispatchers.Default){
                withContext(Dispatchers.IO){
                    try{
                        var data = evalspaRepository.refreshData(perfilProyId, token, cache)
                        Log.d("Testing 1Evals ", "viewModel")
                        _lstEvalsPuesto.postValue(data)
                        Log.d("Testing 2Evals viewModel", "viewModel")
                        _eventNetworkError.postValue(false)
                        _isNetworkErrorShown.postValue(false)
                        Log.d("Testing 3Evals viewModel", "viewModel")
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
            if (modelClass.isAssignableFrom(EvalsPAViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EvalsPAViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}