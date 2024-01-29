package com.example.abcjobsnav.repositories

import android.app.Application
import android.util.Log
import com.example.abcjobsnav.models.Login
import com.example.abcjobsnav.models.Signup
import com.example.abcjobsnav.network.NetworkServiceAdapter
import org.json.JSONObject

class SignupRepository (val application: Application){
    suspend fun refreshData(params: JSONObject):Signup {
        try{
            val sign: Signup = NetworkServiceAdapter.getInstance(application).userSignup(params)
            return sign
        }
        catch (e:Exception){
            Log.d("Testing Error Repository", e.toString())
            throw e
        }
        //return NetworkServiceAdapter.getInstance(application).userSignup(params)
    }
}