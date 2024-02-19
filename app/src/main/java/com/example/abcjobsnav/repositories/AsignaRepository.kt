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

class AsignaRepository (val application: Application){
    suspend fun refreshData(params: JSONObject, empId: Int, token: String, cache: Boolean):ListaPuesto{
        var potentialResp = ListaPuesto(0, listOf<Puesto>())
        var idHash: Int=0
        val strEmpId=empId.toString()
        val strNumPag=params.getInt("num_pag").toString()
        val strProy=params.getString("proyecto")
        val strPerfil=params.getString("perfil")
        val strCand=params.getString("candidato")
        val strEmp=params.getString("empresa")
        val str=strEmpId+"_"+strNumPag+"_"+strProy+"_"+strPerfil+"_"+strCand+"_"+strEmp
        idHash=str.hashCode()
        Log.d("Testing Cache idHash AsignaAbc", idHash.toString())
        if (cache){
            potentialResp = CacheManager.getInstance(application.applicationContext).getlstPuestosEmpresaNoAsig(idHash)
            Log.d("Testing Cache idHash AsignaAbc", "Cache true")
        }
        if(potentialResp.lista.isEmpty()){
            try{
                Log.d("testing Cache decision", "get from network")
                var lstP = NetworkServiceAdapter.getInstance(application).getPuestosEmpresaNoAsig(params, empId, token)
                CacheManager.getInstance(application.applicationContext).addlstPuestosEmpresaNoAsig(idHash, lstP)
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
            Log.d("testing Cache decision", "return ${potentialResp.lista.size} elements from cache")
            return potentialResp
        }
    }
}