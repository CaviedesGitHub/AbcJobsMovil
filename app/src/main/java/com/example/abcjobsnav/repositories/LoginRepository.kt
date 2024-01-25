package com.example.abcjobsnav.repositories

import android.app.Application
import com.android.volley.VolleyError
import com.example.abcjobsnav.models.Login
import com.example.abcjobsnav.network.NetworkServiceAdapter
import org.json.JSONObject

class LoginRepository (val application: Application){
    fun refreshData(params: JSONObject, callback: (Login)->Unit, onError: (VolleyError)->Unit) {
        //Determinar la fuente de datos que se va a utilizar. Si es necesario consultar la red, ejecutar el siguiente código
        NetworkServiceAdapter.getInstance(application).getLogin(params, {
            //Guardar el login? de la variable it en un almacén de datos local para uso futuro
            callback(it)
        },
            onError
        )
    }
}