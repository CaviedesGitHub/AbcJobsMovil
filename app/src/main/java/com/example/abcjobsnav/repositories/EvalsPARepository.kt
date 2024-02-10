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
import com.example.abcjobsnav.models.Candidato
import com.example.abcjobsnav.models.Evaluacion
import com.example.abcjobsnav.network.CacheManager

class EvalsPARepository (val application: Application){
    suspend fun refreshData(perfilProyId: Int, token: String, cache: Boolean):List<Evaluacion>{
        var potentialResp = listOf<Evaluacion>()
        if (cache){
           potentialResp = CacheManager.getInstance(application.applicationContext).getEvalsPuestoAsig(perfilProyId)
        }
        if(potentialResp.isEmpty()){
            try{
                Log.d("testing Cache decision", "get from network")
                var lstEvals = NetworkServiceAdapter.getInstance(application).getEvalsPuesto(perfilProyId, token)
                Log.d("testing Cache decision2", "get from network")
                CacheManager.getInstance(application.applicationContext).addEvalsPuestoAsig(perfilProyId, lstEvals)
                Log.d("testing Cache decision3", "get from network")
                return lstEvals
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

    suspend fun evalCreate(params: JSONObject, token: String): Evaluacion {
        try{
            val eval: Evaluacion = NetworkServiceAdapter.getInstance(application).evalCreate(params, token)
            return eval
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
}