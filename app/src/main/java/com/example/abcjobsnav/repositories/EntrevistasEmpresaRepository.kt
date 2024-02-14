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

class EntrevistasEmpresaRepository (val application: Application){
    suspend fun refreshData(params: JSONObject, idEmp: Int, token: String):List<Entrevista>{
        var potentialResp = CacheManager.getInstance(application.applicationContext).getEntrevistasEmpresa(idEmp)
        if(potentialResp.isEmpty()){
            try{
                Log.d("testing Cache decision", "get from network")
                var evs = NetworkServiceAdapter.getInstance(application).getEntrevistasEmpresa(params, idEmp, token)
                CacheManager.getInstance(application.applicationContext).addEntrevistasEmpresa(idEmp, evs)
                return evs
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