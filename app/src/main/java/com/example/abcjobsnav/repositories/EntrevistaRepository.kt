package com.example.abcjobsnav.repositories

import android.app.Application
import android.util.Log
import com.android.volley.VolleyError
import com.example.abcjobsnav.models.Entrevista
import com.example.abcjobsnav.network.NetworkServiceAdapter
import org.json.JSONObject
import android.content.Context
import android.util.SparseArray
import androidx.collection.ArrayMap
import androidx.collection.LruCache
import androidx.collection.arrayMapOf
import com.example.abcjobsnav.network.CacheManager

class EntrevistaRepository (val application: Application){
    suspend fun refreshData(params: JSONObject, evId: Int, token: String):List<Entrevista>{
        var potentialResp = CacheManager.getInstance(application.applicationContext).getEntrevistasCandidato(evId)
        if(potentialResp.isEmpty()){
            Log.d("testing Cache decision", "get from network")
            var evs = NetworkServiceAdapter.getInstance(application).getEntrevistasCandidato(params, evId, token)
            CacheManager.getInstance(application.applicationContext).addEntrevistasCandidato(evId, evs)
            return evs
        }
        else{
            Log.d("testing Cache decision", "return ${potentialResp.size} elements from cache")
            return potentialResp
        }
    }
}

