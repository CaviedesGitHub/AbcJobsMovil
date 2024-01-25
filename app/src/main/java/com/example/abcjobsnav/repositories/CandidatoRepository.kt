package com.example.abcjobsnav.repositories

import android.app.Application
import com.android.volley.VolleyError
import com.example.abcjobsnav.models.Candidato
import com.example.abcjobsnav.network.NetworkServiceAdapter

class CandidatoRepository (val application: Application){
    suspend fun refreshData(idUser: Int, token: String): Candidato {
        //Determinar la fuente de datos que se va a utilizar. Si es necesario consultar la red, ejecutar el siguiente código
        return NetworkServiceAdapter.getInstance(application).getCandidato(idUser, token)
        //NetworkServiceAdapter.getInstance(application).getCandidato(idUser, token, {
        //    callback(it)
        //},
        //    onError
        //)
    }
}