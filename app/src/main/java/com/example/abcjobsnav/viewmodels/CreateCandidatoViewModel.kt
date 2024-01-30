package com.example.abcjobsnav.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.volley.VolleyError
import com.example.abcjobsnav.models.Candidato
import com.example.abcjobsnav.repositories.CandidatoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class CreateCandidatoViewModel(application: Application) :  AndroidViewModel(application) {
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
        //refreshDataFromNetwork("", "")
    }
    public fun refreshDataFromNetwork(
        nombres: String,
        apellidos: String,
        documento: String,
        fecha_nac: String,
        email: String,
        phone: String,
        ciudad: String,
        direccion: String,
        imagen: String,
        id_usuario: Int,
        num_perfil: Int,
        token: String
        ) {
        val postParams = mapOf<String, Any>(
            "nombres" to nombres,
            "apellidos" to apellidos,
            "documento" to documento,
            "fecha_nac" to fecha_nac,
            "email" to email,
            "phone" to phone,
            "ciudad" to ciudad,
            "direccion" to direccion,
            "imagen" to imagen,
            "id_usuario" to id_usuario,
            "num_perfil" to num_perfil
        )
        Log.d("testing refreshData", "Inicio SignupViewModel")
        try {
            viewModelScope.launch(Dispatchers.Default){
                withContext(Dispatchers.IO){
                    try{
                        var data = candidatoRepository.candCreate(JSONObject(postParams), token)
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
            if (modelClass.isAssignableFrom(CreateCandidatoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CreateCandidatoViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}