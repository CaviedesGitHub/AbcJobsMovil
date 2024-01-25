package com.example.abcjobsnav.repositories

import android.app.Application
import android.util.Log
import com.example.abcjobsnav.models.Candidato
import com.example.abcjobsnav.models.Entrevista
import com.example.abcjobsnav.network.CacheManager
import com.example.abcjobsnav.network.NetworkServiceAdapter

class ResultEvRepository (val application: Application){
    suspend fun refreshData(idEv: Int, token: String): Entrevista{
        var potentialResp = CacheManager.getInstance(application.applicationContext).getEntrevista(idEv)
        if(potentialResp==null){
            Log.d("testing Cache decision", "get from network")
            var EV = NetworkServiceAdapter.getInstance(application).getEntrevista(idEv, token)
            CacheManager.getInstance(application.applicationContext).addEntrevista(idEv, EV)
            return EV
        }
        else{
            Log.d("testing Cache decision", "return Object identified by ${potentialResp.id} id from cache")
            return potentialResp
        }
    }
}