package com.example.abcjobsnav.repositories

import android.app.Application
import android.util.Log
import com.android.volley.VolleyError
import com.example.abcjobsnav.network.NetworkServiceAdapter
import org.json.JSONObject
import android.content.Context
import android.util.SparseArray
import androidx.collection.ArrayMap
import androidx.collection.LruCache
import androidx.collection.arrayMapOf
import com.example.abcjobsnav.models.CandidatoSel
import com.example.abcjobsnav.network.CacheManager

class CumplenRepository (val application: Application){
    suspend fun refreshData(perfilId: Int, token: String):List<CandidatoSel>{
        var potentialResp = CacheManager.getInstance(application.applicationContext).getCumplenPerfil(perfilId)
        if(potentialResp.isEmpty()){
            try{
                Log.d("testing Cache decision", "get from network")
                var lstCand = NetworkServiceAdapter.getInstance(application).getCumplenPerfil(perfilId, token)
                Log.d("testing Cache decision2", "get from network")
                CacheManager.getInstance(application.applicationContext).addCumplenPerfil(perfilId, lstCand)
                Log.d("testing Cache decision3", "get from network")
                return lstCand
            }
            catch (e:VolleyError){
                Log.d("Testing Error Repository", e.toString())
                throw e
            }
            catch (e:Exception){
                Log.d("Testing Error Repository", e.toString())
                throw e
            }
        }
        else{
            Log.d("testing Cache decision", "return ${potentialResp.size} elements from cache")
            return potentialResp
        }
    }
}