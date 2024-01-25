package com.example.abcjobsnav.network

import android.content.Context
import com.example.abcjobsnav.models.Entrevista
import com.example.abcjobsnav.models.Candidato

class CacheManager(context: Context) {
    companion object{
        var instance: CacheManager? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: CacheManager(context).also {
                    instance = it
                }
            }
    }
    private var evsCandidato: HashMap<Int, List<Entrevista>> = hashMapOf()
    private var lstCandidatos: HashMap<Int, Candidato> = hashMapOf()
    fun addEntrevistasCandidato(idUser: Int, evs: List<Entrevista>){
        if (!evsCandidato.containsKey(idUser)){
            evsCandidato[idUser] = evs
        }
    }
    fun getEntrevistasCandidato(idUser: Int) : List<Entrevista>{
        return if (evsCandidato.containsKey(idUser)) evsCandidato[idUser]!! else listOf<Entrevista>()
    }


    fun addCandidato(idUser: Int, cand: Candidato){
        if (!lstCandidatos.containsKey(idUser)){
            lstCandidatos[idUser] = cand
        }
    }
    fun getCandidato(idUser: Int) : Candidato?{
        return if (lstCandidatos.containsKey(idUser)) lstCandidatos[idUser]!! else null
    }
}