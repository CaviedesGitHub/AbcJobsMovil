package com.example.abcjobsnav.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.volley.VolleyError
import com.example.abcjobsnav.models.Empresa
import com.example.abcjobsnav.network.NetworkServiceAdapter
import com.example.abcjobsnav.repositories.EmpresaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class EmpresaViewModel(application: Application) :  AndroidViewModel(application) {
    private val empresaRepository = EmpresaRepository(application)

    private val _empresa = MutableLiveData<Empresa>()
    val empresa: LiveData<Empresa>
        get() = _empresa

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
        Log.d("testing EmpresaViewModel", "Inicio init")
        //refreshDataFromNetwork()
        Log.d("testing EmpresaViewModel", "Fin init")
    }

    public fun refreshDataFromNetwork(idUser: Int, token: String) {
        Log.d("testing EmpresaViewModel", "Inicio refreshDataFromNetwork")
        try {
            viewModelScope.launch(Dispatchers.Default){
                withContext(Dispatchers.IO){
                    try{
                        Log.d("Testing Error LVM", "Normal")
                        var data = empresaRepository.refreshData(idUser, token)
                        Log.d("Testing Error LVM", "Normal1")
                        _empresa.postValue(data)
                        Log.d("Testing Error LVM", "Normal2")
                        _eventNetworkError.postValue(false)
                        Log.d("Testing Error LVM", "Normal3")
                        _isNetworkErrorShown.postValue(false)
                        Log.d("Testing Error LVM", "Normal4")
                    }
                    catch (e: VolleyError){
                        Log.d("Testing Error LVM", "Error Volley")
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
            if (modelClass.isAssignableFrom(EmpresaViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EmpresaViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}