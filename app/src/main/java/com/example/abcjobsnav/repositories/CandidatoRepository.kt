package com.example.abcjobsnav.repositories

import android.app.Application
import com.android.volley.VolleyError
import com.example.abcjobsnav.models.Candidato
import com.example.abcjobsnav.network.NetworkServiceAdapter

class CandidatoRepository (val application: Application){
    fun refreshData(idUser: Int, token: String, callback: (Candidato)->Unit, onError: (VolleyError)->Unit) {
        //Determinar la fuente de datos que se va a utilizar. Si es necesario consultar la red, ejecutar el siguiente código
        NetworkServiceAdapter.getInstance(application).getCandidato(idUser, token, {
            //Guardar el candidato? de la variable it en un almacén de datos local para uso futuro
            callback(it)
        },
            onError
        )
    }
}