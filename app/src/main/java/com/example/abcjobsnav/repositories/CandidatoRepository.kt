package com.example.abcjobsnav.repositories

import android.app.Application
import android.util.Log
import com.android.volley.VolleyError
import com.example.abcjobsnav.models.Candidato
import com.example.abcjobsnav.models.Entrevista
import com.example.abcjobsnav.models.Signup
import com.example.abcjobsnav.network.CacheManager
import com.example.abcjobsnav.network.NetworkServiceAdapter
import org.json.JSONObject

class CandidatoRepository (val application: Application){
    suspend fun refreshData(idUser: Int, token: String): Candidato{
        var potentialResp = CacheManager.getInstance(application.applicationContext).getCandidato(idUser)
        if(potentialResp==null){
            Log.d("testing Cache decision", "get from network")
            var cand = NetworkServiceAdapter.getInstance(application).getCandidato(idUser, token)
            CacheManager.getInstance(application.applicationContext).addCandidato(idUser, cand)
            return cand
        }
        else{
            Log.d("testing Cache decision", "return Object identified by ${potentialResp.id} id from cache")
            return potentialResp
        }
    }

    suspend fun candCreate(params: JSONObject, token: String): Candidato {
        return NetworkServiceAdapter.getInstance(application).candCreate(params, token)
    }
}