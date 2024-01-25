package com.example.abcjobsnav.repositories

import android.app.Application
import com.android.volley.VolleyError
import com.example.abcjobsnav.models.Entrevista
import com.example.abcjobsnav.network.NetworkServiceAdapter
import org.json.JSONObject

class EntrevistaRepository (val application: Application){
    suspend fun refreshData(params: JSONObject, evId: Int, token: String):List<Entrevista>{
        //Determinar la fuente de datos que se va a utilizar. Si es necesario consultar la red, ejecutar el siguiente código
        return NetworkServiceAdapter.getInstance(application).getEntrevistasCandidato(params, evId, token)
        /*NetworkServiceAdapter.getInstance(application).getEntrevistasCandidato(params, evId, token, {
            callback(it)
        },
            onError
        )*/
    }
}

