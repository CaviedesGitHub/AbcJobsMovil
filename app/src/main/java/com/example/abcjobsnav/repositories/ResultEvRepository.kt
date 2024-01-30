package com.example.abcjobsnav.repositories

import android.app.Application
import android.util.Log
import com.android.volley.VolleyError
import com.example.abcjobsnav.models.Candidato
import com.example.abcjobsnav.models.Entrevista
import com.example.abcjobsnav.network.CacheManager
import com.example.abcjobsnav.network.NetworkServiceAdapter

class ResultEvRepository (val application: Application){
    suspend fun refreshData(idEv: Int, token: String): Entrevista{
        var potentialResp = CacheManager.getInstance(application.applicationContext).getEntrevista(idEv)
        if(potentialResp==null){
            try{
                Log.d("testing Cache decision", "get from network")
                var EV = NetworkServiceAdapter.getInstance(application).getEntrevista(idEv, token)
                CacheManager.getInstance(application.applicationContext).addEntrevista(idEv, EV)
                return EV
            }
            catch (e: VolleyError){
                Log.d("Testing Error Repository", e.toString())
                throw e
            }
            catch (e:Exception){
                Log.d("Testing Error Repository", e.toString())
                throw e
            }
        }
        else{
            Log.d("testing Cache decision", "return Object identified by ${potentialResp.id} id from cache")
            return potentialResp
        }
    }
}