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
import com.example.abcjobsnav.models.Puesto
import com.example.abcjobsnav.network.CacheManager

class AsignaRepository (val application: Application){
    suspend fun refreshData(params: JSONObject, empId: Int, token: String):List<Puesto>{
        var potentialResp = CacheManager.getInstance(application.applicationContext).getPuestosEmpresaSinAsig(empId)
        if(potentialResp.isEmpty()){
            try{
                Log.d("testing Cache decision", "get from network")
                var lstP = NetworkServiceAdapter.getInstance(application).getPuestosEmpresaNoAsig(params, empId, token)
                CacheManager.getInstance(application.applicationContext).addPuestosEmpresaSinAsig(empId, lstP)
                return lstP
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