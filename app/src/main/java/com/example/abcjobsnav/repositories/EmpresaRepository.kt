package com.example.abcjobsnav.repositories

import android.app.Application
import android.util.Log
import com.android.volley.VolleyError
import com.example.abcjobsnav.models.Empresa
import com.example.abcjobsnav.network.CacheManager
import com.example.abcjobsnav.network.NetworkServiceAdapter
import org.json.JSONObject

class EmpresaRepository (val application: Application){
    suspend fun refreshData(idUser: Int, token: String): Empresa{
        var potentialResp = CacheManager.getInstance(application.applicationContext).getEmpresa(idUser)
        if(potentialResp==null){
            try{
                Log.d("testing Cache decision", "get from network")
                var emp = NetworkServiceAdapter.getInstance(application).getEmpresa(idUser, token)
                CacheManager.getInstance(application.applicationContext).addEmpresa(idUser, emp)
                return emp
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
            Log.d("testing Cache decision", "return Object identified by ${potentialResp.id} id from cache")
            return potentialResp
        }
    }
}