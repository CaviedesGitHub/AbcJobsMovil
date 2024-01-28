package com.example.abcjobsnav.repositories

import android.app.Application
import com.example.abcjobsnav.models.Signup
import com.example.abcjobsnav.network.NetworkServiceAdapter
import org.json.JSONObject

class SignupRepository (val application: Application){
    suspend fun refreshData(params: JSONObject):Signup {
        return NetworkServiceAdapter.getInstance(application).userSignup(params)
    }
}