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
import com.example.abcjobsnav.models.ListaEntrevista
import com.example.abcjobsnav.models.ListaPuesto
import com.example.abcjobsnav.models.Puesto
import com.example.abcjobsnav.network.CacheManager

class EntrevistasEmpresaRepository (val application: Application){
    suspend fun refreshData(params: JSONObject, idEmp: Int, token: String):ListaEntrevista{
        //var potentialResp = ListaPuesto(0, listOf<Puesto>())
        var idHash: Int=0
        val strEmpId=idEmp.toString()
        val strNumPag=params.getInt("num_pag").toString()
        val strProy=params.getString("proyecto")
        val strPerfil=params.getString("perfil")
        val strCand=params.getString("candidato")
        val strEmp=params.getString("empresa")
        val str=strEmpId+"_"+strNumPag+"_"+strProy+"_"+strPerfil+"_"+strCand+"_"+strEmp
        idHash=str.hashCode()
        var potentialResp = CacheManager.getInstance(application.applicationContext).getlstEntrevistasEmpresa(idHash)
        if(potentialResp.total_reg==0){
            try{
                Log.d("testing Cache decision", "get from network")
                var evs = NetworkServiceAdapter.getInstance(application).getEntrevistasEmpresa(params, idEmp, token)
                Log.d("testing Cache decision2", "get from network2")
                CacheManager.getInstance(application.applicationContext).addlstEntrevistasEmpresa(idEmp, evs)
                Log.d("testing Cache decision3", "get from network3")
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
            Log.d("testing Cache decision", "return ${potentialResp.lista.size} elements from cache")
            return potentialResp
        }
    }
}