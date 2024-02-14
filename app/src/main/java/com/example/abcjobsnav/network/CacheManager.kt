package com.example.abcjobsnav.network

import android.content.Context
import com.example.abcjobsnav.models.Entrevista
import com.example.abcjobsnav.models.Candidato
import com.example.abcjobsnav.models.CandidatoSel
import com.example.abcjobsnav.models.Empresa
import com.example.abcjobsnav.models.Evaluacion
import com.example.abcjobsnav.models.PerfilProyecto
import com.example.abcjobsnav.models.Puesto

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
    private var evsEmpresa: HashMap<Int, List<Entrevista>> = hashMapOf()
    private var evsCandidato: HashMap<Int, List<Entrevista>> = hashMapOf()
    private var puestosEmpresaConAsig: HashMap<Int, List<Puesto>> = hashMapOf()
    private var puestosEmpresaSinAsig: HashMap<Int, List<Puesto>> = hashMapOf()
    private var lstEvalsPuestoAsig: HashMap<Int, List<Evaluacion>> = hashMapOf()
    private var lstCumplenPerfil: HashMap<Int, List<CandidatoSel>> = hashMapOf()
    private var lstEmpresas: HashMap<Int, Empresa> = hashMapOf()
    private var lstCandidatos: HashMap<Int, Candidato> = hashMapOf()
    private var lstEntrevistas: HashMap<Int, Entrevista> = hashMapOf()

    fun addCumplenPerfil(idPerfil: Int, lstCand: List<CandidatoSel>){
        if (!lstCumplenPerfil.containsKey(idPerfil)){
            lstCumplenPerfil[idPerfil] = lstCand
        }
    }
    fun getCumplenPerfil(idPerfil: Int) : List<CandidatoSel>{
        return if (lstCumplenPerfil.containsKey(idPerfil)) lstCumplenPerfil[idPerfil]!! else listOf<CandidatoSel>()
    }
    fun addEvalsPuestoAsig(idPerfilProy: Int, lstEvals: List<Evaluacion>){
        if (!lstEvalsPuestoAsig.containsKey(idPerfilProy)){
            lstEvalsPuestoAsig[idPerfilProy] = lstEvals
        }
    }
    fun getEvalsPuestoAsig(idPerfilProy: Int) : List<Evaluacion>{
        return if (lstEvalsPuestoAsig.containsKey(idPerfilProy)) lstEvalsPuestoAsig[idPerfilProy]!! else listOf<Evaluacion>()
    }

    fun addPuestosEmpresaSinAsig(idEmp: Int, lstP: List<Puesto>){
        if (!puestosEmpresaSinAsig.containsKey(idEmp)){
            puestosEmpresaSinAsig[idEmp] = lstP
        }
    }
    fun getPuestosEmpresaSinAsig(idEmp: Int) : List<Puesto>{
        return if (puestosEmpresaSinAsig.containsKey(idEmp)) puestosEmpresaSinAsig[idEmp]!! else listOf<Puesto>()
    }

    fun addPuestosEmpresaConAsig(idEmp: Int, lstP: List<Puesto>){
        if (!puestosEmpresaConAsig.containsKey(idEmp)){
            puestosEmpresaConAsig[idEmp] = lstP
        }
    }
    fun getPuestosEmpresaConAsig(idEmp: Int) : List<Puesto>{
        return if (puestosEmpresaConAsig.containsKey(idEmp)) puestosEmpresaConAsig[idEmp]!! else listOf<Puesto>()
    }

    fun addEntrevistasEmpresa(idEmp: Int, evs: List<Entrevista>){
        if (!evsEmpresa.containsKey(idEmp)){
            evsEmpresa[idEmp] = evs
        }
    }
    fun getEntrevistasEmpresa(idEmp: Int) : List<Entrevista>{
        return if (evsEmpresa.containsKey(idEmp)) evsEmpresa[idEmp]!! else listOf<Entrevista>()
    }

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

    fun addEmpresa(idUser: Int, emp: Empresa){
        if (!lstEmpresas.containsKey(idUser)){
            lstEmpresas[idUser] = emp
        }
    }
    fun getEmpresa(idUser: Int) : Empresa?{
        return if (lstEmpresas.containsKey(idUser)) lstEmpresas[idUser]!! else null
    }

    fun addEntrevista(idEv: Int, EV: Entrevista){
        if (!lstEntrevistas.containsKey(idEv)){
            lstEntrevistas[idEv] = EV
        }
    }
    fun getEntrevista(idEv: Int) : Entrevista?{
        return if (lstEntrevistas.containsKey(idEv)) lstEntrevistas[idEv]!! else null
    }
}