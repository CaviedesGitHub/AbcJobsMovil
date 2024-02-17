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
import com.example.abcjobsnav.models.Evaluacion
import com.example.abcjobsnav.models.ListaPuesto
import com.example.abcjobsnav.models.Puesto
import com.example.abcjobsnav.network.CacheManager

class AsignaAbcRepository (val application: Application){
    suspend fun refreshData(params: JSONObject, userId: Int, token: String, cache: Boolean): ListaPuesto {
        Log.d("Testing Cache AsignaAbc", "inicio")
        var potentialResp = ListaPuesto(0, listOf<Puesto>())
        var idHash: Int=0
        val strNumPag=params.getInt("num_pag").toString()
        val strProy=params.getString("proyecto")
        val strPerfil=params.getString("perfil")
        val strCand=params.getString("candidato")
        val strEmp=params.getString("empresa")
        val str=strNumPag+"_"+strProy+"_"+strPerfil+"_"+strCand+"_"+strEmp
        idHash=str.hashCode()
        Log.d("Testing Cache idHash AsignaAbc", idHash.toString())
        if (cache){
            potentialResp = CacheManager.getInstance(application.applicationContext).getLstPuestosABCSinAsig(idHash)
            Log.d("Testing Cache idHash AsignaAbc", "Cache true")
        }
        if(potentialResp.lista.isEmpty()){
            try {
                Log.d("testing Cache decision", "get from network")
                var lstP = NetworkServiceAdapter.getInstance(application)
                    .getPuestosNoAsig(params, userId, token)
                CacheManager.getInstance(application.applicationContext).addLstPuestosABCSinAsig(idHash, lstP)
                return lstP
            } catch (e: VolleyError) {
                Log.d("Testing Error Repository", e.toString())
                throw e
            } catch (e: Exception) {
                Log.d("Testing Error Repository", e.toString())
                throw e
            }
        }
        else{
            Log.d("testing Cache decision", "return ${potentialResp.lista.size} elements from cache")
            return potentialResp
        }
    }
}