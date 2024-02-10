package com.example.abcjobsnav.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.volley.VolleyError
import com.example.abcjobsnav.models.Puesto
import com.example.abcjobsnav.network.NetworkServiceAdapter
import com.example.abcjobsnav.repositories.QualyRepository
import org.json.JSONObject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QualyViewModel(application: Application) :  AndroidViewModel(application) {
    private val qualyRepository = QualyRepository(application)

    private val _puestosEmpAsig = MutableLiveData<List<Puesto>>()
    val puestosEmpAsig: LiveData<List<Puesto>>
        get() = _puestosEmpAsig

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
        //refreshDataFromNetwork()
    }

    public fun refreshDataFromNetwork(empId: Int, token: String) {
        val postParams = mapOf<String, Any>(
            "max" to 20,
            "num_pag" to 1,
            "order" to "ASC",
            "proyecto" to "",
            "perfil" to "",
            "candidato" to ""
        )
        try {
            viewModelScope.launch(Dispatchers.Default){
                withContext(Dispatchers.IO){
                    try{
                        var data = qualyRepository.refreshData(JSONObject(postParams), empId, token)
                        _puestosEmpAsig.postValue(data)
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
            if (modelClass.isAssignableFrom(QualyViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return QualyViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}