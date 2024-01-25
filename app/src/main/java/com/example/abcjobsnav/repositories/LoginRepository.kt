package com.example.abcjobsnav.repositories

import android.app.Application
import com.android.volley.VolleyError
import com.example.abcjobsnav.models.Login
import com.example.abcjobsnav.network.NetworkServiceAdapter
import org.json.JSONObject

class LoginRepository (val application: Application){
    suspend fun refreshData(params: JSONObject):Login {
        return NetworkServiceAdapter.getInstance(application).getLogin(params)
    }
}