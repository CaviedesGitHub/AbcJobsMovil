package com.example.abcjobsnav.repositories

import android.app.Application
import android.util.Log
import com.android.volley.VolleyError
import com.example.abcjobsnav.models.Login
import com.example.abcjobsnav.network.NetworkServiceAdapter
import org.json.JSONObject

class LoginRepository (val application: Application){
    suspend fun refreshData(params: JSONObject):Login {
        try{
            val log: Login = NetworkServiceAdapter.getInstance(application).getLogin(params)
            return log
        }
        catch (e:Exception){
            Log.d("Testing Error Repository", e.toString())
            throw e
        }
        //return NetworkServiceAdapter.getInstance(application).getLogin(params)
    }
}