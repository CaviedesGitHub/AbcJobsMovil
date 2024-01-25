package com.example.abcjobsnav.repositories

import android.app.Application
import com.android.volley.VolleyError
import com.example.abcjobsnav.models.Entrevista
import com.example.abcjobsnav.network.NetworkServiceAdapter
import org.json.JSONObject

class EntrevistaRepository (val application: Application){
    fun refreshData(params: JSONObject, evId: Int, token: String, callback: (List<Entrevista>)->Unit, onError: (VolleyError)->Unit) {
        //Determinar la fuente de datos que se va a utilizar. Si es necesario consultar la red, ejecutar el siguiente código
        NetworkServiceAdapter.getInstance(application).getEntrevistasCandidato(params, evId, token, {
            //Guardar los albumes de la variable it en un almacén de datos local para uso futuro
            callback(it)
        },
            onError
        )
    }
}

