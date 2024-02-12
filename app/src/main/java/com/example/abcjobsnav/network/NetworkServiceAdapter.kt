package com.example.abcjobsnav.network

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import com.example.abcjobsnav.models.Entrevista
import com.example.abcjobsnav.models.Candidato
import com.example.abcjobsnav.models.CandidatoSel
import com.example.abcjobsnav.models.Empresa
import com.example.abcjobsnav.models.Evaluacion
import com.example.abcjobsnav.models.Login
import com.example.abcjobsnav.models.PerfilProyecto
import com.example.abcjobsnav.models.Puesto
import com.example.abcjobsnav.models.Signup
import java.awt.font.NumericShaper

import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NetworkServiceAdapter constructor(context: Context) {
    companion object{
        const val BASE_URL= "http://10.0.2.2:5000/" //""https://vynils-back-heroku.herokuapp.com/"
        var instance: NetworkServiceAdapter? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: NetworkServiceAdapter(context).also {
                    instance = it
                }
            }
    }
    private val requestQueue: RequestQueue by lazy {
        // applicationContext keeps you from leaking the Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }

    suspend fun asignaCand(body: JSONObject, idProyPerfil: Int, token: String)=suspendCoroutine<PerfilProyecto>{contResp ->
        Log.d("testing","Inicio asignaCand NetworkServiceAdapter")
        val jsonReq = object: JsonObjectRequest(Request.Method.POST, BASE_URL+"empresas/proyectos/perfiles/asignacion/$idProyPerfil", body,
            Response.Listener<JSONObject> { response ->
                Log.d("testing","Response asignaCand NetworkServiceAdapter")
                Log.d("testing Response", response.toString())
                val resp=response.getJSONObject("perfilProyecto")
                val pp= PerfilProyecto(
                    id=resp.getInt("id"),
                    nombre= resp.getString("nombre"),
                    id_proy= resp.getInt("id_proy"),
                    id_perfil= resp.getInt("id_perfil"),
                    id_cand= resp.getInt("id_cand"),
                    fecha_asig= resp.getString("fecha_asig"))
                contResp.resume(pp)
            },
            {
                Log.d("testing","VolleyError createCandidato NetworkServiceAdapter")
                contResp.resumeWithException(it) //throw it   //onError(it)
            }){
            override fun getHeaders(): MutableMap<String, String> {
                Log.d("testing","Inicio getHeaders")
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                Log.d("testing", headers.toString())
                return headers  //return super.getHeaders()  // throws AuthFailureError
            }
        };
        requestQueue.add(jsonReq)
    }

    suspend fun getCumplenPerfil(perfilId:Int, token: String)=suspendCoroutine<List<CandidatoSel>>{ contResp ->
        Log.d("testing","Inicio getCumplenPerfil NetworkServiceAdapter")
        Log.d("testing perfilId", perfilId.toString())
        Log.d("testing cumplenPerfil", "QUE PASA?")
        try{
            val strReq = object: StringRequest(Request.Method.GET, BASE_URL+"cumplenPerfil/$perfilId",
                Response.Listener<String> { responseX ->
                    Log.d("testing","Response getCumplenPerfil NetworkServiceAdapter")
                    Log.d("testing", responseX.toString())
                    val resp = JSONObject(responseX)
                    val respAux = resp.getJSONObject("Respuesta")
                    Log.d("testing Respuesta", respAux.toString())
                    val lstResp = respAux.getJSONArray("Seleccion")
                    Log.d("testing lstResp", lstResp.toString())
                    Log.d("testing","Response3 getCumplenPerfil NetworkServiceAdapter")
                    val list = mutableListOf<CandidatoSel>()
                    Log.d("testing","Response31 getCumplenPerfil NetworkServiceAdapter")
                    for (i in 0 until lstResp.length()) {
                        Log.d("testing","Response32 getCumplenPerfil NetworkServiceAdapter")
                        val item = lstResp.getJSONObject(i)
                        val lstHabils=item.getJSONArray("lstHabils")
                        var strLstHabilsTec:String=""
                        var strLstHabilsBlan:String=""
                        var strLstHabilsPers:String=""
                        for (j in 0 until lstHabils.length()) {
                            val itemHab = lstHabils.getJSONObject(j)
                            var nombreHabil:String=""
                            nombreHabil=itemHab.getString("nombre")
                            if (nombreHabil.get(nombreHabil.length-1)== '\n'){  //or [index]
                                nombreHabil=nombreHabil.substring(0, nombreHabil.length-1)
                            }
                            if(itemHab.getString("tipo")=="TECNICA"){
                                if(strLstHabilsTec==""){
                                    strLstHabilsTec=nombreHabil
                                }
                                else{
                                    strLstHabilsTec=strLstHabilsTec+", "+nombreHabil
                                }
                            }
                            else if(itemHab.getString("tipo")=="BLANDA"){
                                if(strLstHabilsBlan==""){
                                    strLstHabilsBlan=nombreHabil
                                }
                                else{
                                    strLstHabilsBlan=strLstHabilsBlan+", "+nombreHabil
                                }
                            }
                            else if(itemHab.getString("tipo")=="PERSONALIDAD"){
                                if(strLstHabilsPers==""){
                                    strLstHabilsPers=nombreHabil
                                }
                                else{
                                    strLstHabilsPers=strLstHabilsPers+", "+nombreHabil
                                }
                            }
                            else {
                            }
                        }
                        Log.d("testing","Response33 getCumplenPerfil NetworkServiceAdapter")
                        list.add(i, CandidatoSel(
                            id_cand = item.getInt("id_cand"),
                            nombres=item.getString("nombres"),
                            apellidos=item.getString("apellidos"),
                            fecha_nac=item.getString("fecha_nac"),
                            email=item.getString("email"),
                            phone=item.getString("phone"),
                            ciudad=item.getString("ciudad"),
                            direccion=item.getString("direccion"),
                            imagen=item.getString("imagen"),
                            id_perfil=item.getInt("id_perfil"),
                            Calificacion = item.getInt("Calificacion"),
                            habilsTec=strLstHabilsTec,
                            habilsBlan=strLstHabilsBlan,
                            habilsPers=strLstHabilsPers))
                    }
                    Log.d("testing","Response4 getCumplenPerfil NetworkServiceAdapter")
                    contResp.resume(list) //onComplete(list)
                },
                {
                    Log.d("testing","VolleyError getCumplenPerfil NetworkServiceAdapter")
                    contResp.resumeWithException(it) //throw it   //onError(it)
                }){
                override fun getHeaders(): MutableMap<String, String> {
                    Log.d("testing","Inicio getHeaders")
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    headers["Authorization"] = "Bearer $token"
                    Log.d("testing", headers.toString())
                    return headers  //return super.getHeaders()  // throws AuthFailureError
                }
            };
            requestQueue.add(strReq)
        }
        catch (e:Exception) {
            Log.d("Testing CP Error", e.toString())
        }
    }

    suspend fun evalCreate(body: JSONObject, token: String)=suspendCoroutine<Evaluacion>{contResp ->
        Log.d("testing","Inicio userCreate NetworkServiceAdapter")
        val jsonReq = object: JsonObjectRequest(Request.Method.POST, BASE_URL+"empresas/proyectos/perfiles/evaluaciones", body,
            Response.Listener<JSONObject> { response ->
                Log.d("testing","Response evalCreate NetworkServiceAdapter")
                Log.d("testing Response", response.toString())
                val eval= Evaluacion(
                    id = response.getInt("id"),
                    idPerfilProy = response.getInt("idPerfilProy"),
                    id_cand = response.getInt("id_cand"),
                    anno = response.getInt("anno"),
                    mes = response.getInt("mes"),
                    strmes = response.getString("strmes"),
                    candidato = "",
                    valoracion = response.getInt("valoracion"),
                    calificacion = response.getString("calificacion"),
                    nota = response.getString("nota") )
                contResp.resume(eval)
            },
            {
                Log.d("testing","VolleyError evalCreate NetworkServiceAdapter")
                contResp.resumeWithException(it) //throw it   //onError(it)
            }){
            override fun getHeaders(): MutableMap<String, String> {
                Log.d("testing","Inicio getHeaders")
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                Log.d("testing", headers.toString())
                return headers  //return super.getHeaders()  // throws AuthFailureError
            }
        };
        requestQueue.add(jsonReq)
    }
    suspend fun getEmpresa( idUser: Int, token: String)=suspendCoroutine<Empresa>{contResp ->
        Log.d("testing","Inicio getEmpresa NetworkServiceAdapter")
        Log.d("testing usuario",idUser.toString())
        Log.d("testing token",token)
        val strReq = object: StringRequest(Request.Method.GET, BASE_URL+"miempresa/$idUser",
            Response.Listener<String>{
                    response ->
                val resp = JSONObject(response)
                val emp= Empresa(
                    id=resp.getInt("id"),
                    nombre=resp.getString("nombre"),
                    tipo=resp.getString("tipo"),
                    contacto=resp.getString("contacto"),
                    correo=resp.getString("correo"),
                    celular=resp.getString("celular"),
                    pais=resp.getString("pais"),
                    ciudad=resp.getString("ciudad"),
                    direccion=resp.getString("direccion"),
                    id_usuario=resp.getInt("id_usuario"))
                contResp.resume(emp) //onComplete(list)
            },
            {
                Log.d("testing","VolleyError getEmpresa NetworkServiceAdapter")
                contResp.resumeWithException(it) //throw it   //onError(it)
            }){
            override fun getHeaders(): MutableMap<String, String> {
                Log.d("testing getEmpresa","Inicio getHeaders")
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                Log.d("testing", headers.toString())
                return headers  //return super.getHeaders()  // throws AuthFailureError
            }
        };

        val jsonReq = object: JsonObjectRequest(Request.Method.GET, BASE_URL+"miempresa/$idUser", null,
            Response.Listener<JSONObject>{
                resp -> val emp= Empresa(
                id=resp.getInt("id"),
                nombre=resp.getString("nombre"),
                tipo=resp.getString("tipo"),
                contacto=resp.getString("contacto"),
                correo=resp.getString("correo"),
                celular=resp.getString("celular"),
                pais=resp.getString("pais"),
                ciudad=resp.getString("ciudad"),
                direccion=resp.getString("direccion"),
                id_usuario=resp.getInt("id_usuario"))
                contResp.resume(emp) //onComplete(list)
            },
            {
                Log.d("testing","VolleyError getEmpresa NetworkServiceAdapter")
                contResp.resumeWithException(it) //throw it   //onError(it)
            }){
            override fun getHeaders(): MutableMap<String, String> {
                Log.d("testing get Empresa","Inicio getHeaders")
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                Log.d("testing getEmpresa", headers.toString())
                return headers  //return super.getHeaders()  // throws AuthFailureError
            }
        }
        requestQueue.add(strReq)
    }

    suspend fun candCreate(body: JSONObject, token: String)=suspendCoroutine<Candidato>{contResp ->
        Log.d("testing","Inicio userCreate NetworkServiceAdapter")
        val jsonReq = object: JsonObjectRequest(Request.Method.POST, BASE_URL+"candidatos", body,
            Response.Listener<JSONObject> { response ->
                Log.d("testing","Response Signup NetworkServiceAdapter")
                Log.d("testing Response", response.toString())
                val resp=response.getJSONObject("Candidato")
                val cand= Candidato(
                    id=resp.getInt("id"),
                    nombres=resp.getString("nombres"),
                    apellidos=resp.getString("apellidos"),
                    documento=resp.getString("documento"),
                    fecha_nac=resp.getString("fecha_nac"),
                    email=resp.getString("email"),
                    phone=resp.getString("phone"),
                    ciudad=resp.getString("ciudad"),
                    direccion=resp.getString("direccion"),
                    imagen=resp.getString("imagen"),
                    id_usuario=resp.getInt("id_usuario"),
                    num_perfil=resp.getInt("num_perfil"))
                contResp.resume(cand)
            },
            {
                Log.d("testing","VolleyError createCandidato NetworkServiceAdapter")
                contResp.resumeWithException(it) //throw it   //onError(it)
            }){
            override fun getHeaders(): MutableMap<String, String> {
                Log.d("testing","Inicio getHeaders")
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                Log.d("testing", headers.toString())
                return headers  //return super.getHeaders()  // throws AuthFailureError
            }
        };
        requestQueue.add(jsonReq)
    }

    suspend fun candCreateSigSeguridad(body: JSONObject, token: String)=suspendCoroutine<Candidato>{contResp ->
        Log.d("testing","Inicio userCreate NetworkServiceAdapter")
        requestQueue.add(postRequest("candidatos", body,
            Response.Listener<JSONObject> { response ->
                Log.d("testing","Response Signup NetworkServiceAdapter")
                Log.d("testing Response", response.toString())
                val resp=response.getJSONObject("Candidato")
                val cand= Candidato(
                    id=resp.getInt("id"),
                    nombres=resp.getString("nombres"),
                    apellidos=resp.getString("apellidos"),
                    documento=resp.getString("documento"),
                    fecha_nac=resp.getString("fecha_nac"),
                    email=resp.getString("email"),
                    phone=resp.getString("phone"),
                    ciudad=resp.getString("ciudad"),
                    direccion=resp.getString("direccion"),
                    imagen=resp.getString("imagen"),
                    id_usuario=resp.getInt("id_usuario"),
                    num_perfil=resp.getInt("num_perfil"))
                contResp.resume(cand)
            },
            {
                Log.d("testing","VolleyError Create Cand NetworkServiceAdapter")
                contResp.resumeWithException(it)
            })
        )
    }
    suspend fun userSignup(body: JSONObject)=suspendCoroutine<Signup>{contResp ->
        Log.d("testing","Inicio Signup NetworkServiceAdapter")
        requestQueue.add(postRequest("auth/signup", body,
            Response.Listener<JSONObject> { response ->
                Log.d("testing","Response Signup NetworkServiceAdapter")
                Log.d("testing Response", response.toString())
                val sign= Signup(mensaje=response.getString("mensaje"),
                    token=response.getString("token"),
                    id=response.getInt("id"),
                    tipo=response.getString("tipo"))
                contResp.resume(sign)
            },
            {
                Log.d("testing","VolleyError1 Signup NetworkServiceAdapter")
                val responseBody: String = String(it.networkResponse.data)
                Log.d("testing",responseBody.toString())
                Log.d("testing","VolleyError2 Signup NetworkServiceAdapter")
                val data: JSONObject = JSONObject(responseBody)
                Log.d("testing","VolleyError3 Signup NetworkServiceAdapter")
                Log.d("testing",data.toString())
                //val errors: JSONArray = data.getJSONArray("errors")
                //Log.d("testing","VolleyError4 Signup NetworkServiceAdapter")
                //val jsonMessage: JSONObject = errors.getJSONObject(0)
                //Log.d("testing","VolleyError5 Signup NetworkServiceAdapter")
                //val message: String = jsonMessage.getString("message")
                //Log.d("testing",message)
                Log.d("testing",it.networkResponse.statusCode.toString())
                contResp.resumeWithException(it)
            })
        )
    }

    suspend fun getEntrevista( idEv: Int, token: String)=suspendCoroutine<Entrevista>{contResp ->
        Log.d("testing","Inicio getEntrevista NetworkServiceAdapter")
        Log.d("testing idEv",idEv.toString())
        Log.d("testing token",token)
        val strReq = object: StringRequest(Request.Method.GET, BASE_URL+"entrevistas/$idEv",
            Response.Listener<String>{ response ->
                Log.d("testing response", "Responde Bien")
                val resp0 = JSONObject(response)
                Log.d("testing response", resp0.toString())
                val resp= resp0.getJSONObject("Entrevista")
                Log.d("testing response", resp.toString())
                val EV= Entrevista(id = resp.getInt("id"),
                    candidato = resp.getString("candidato"),
                    nom_empresa = resp.getString("nom_empresa"),
                    nom_proyecto = resp.getString("nom_proyecto"),
                    nom_perfil = resp.getString("nom_perfil"),
                    cuando = resp.getString("cuando"),
                    contacto = resp.getString("contacto"),
                    calificacion = resp.getString("calificacion"),
                    anotaciones = resp.getString("anotaciones"),
                    id_cand = resp.getInt("id_cand"),
                    idPerfilProy = resp.getInt("id_perfilproy"),
                    id_perfil = resp.getInt("id_perfil"),
                    Num = 0,
                    valoracion = 0)
                contResp.resume(EV) //onComplete(list)
            },
            {
                Log.d("testing","VolleyError getEntrevista NetworkServiceAdapter")
                contResp.resumeWithException(it) //throw it   //onError(it)
            }){
            override fun getHeaders(): MutableMap<String, String> {
                Log.d("testing","Inicio getHeaders")
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                Log.d("testing", headers.toString())
                return headers  //return super.getHeaders()  // throws AuthFailureError
            }
        };
        requestQueue.add(strReq)
    }

    suspend fun getCandidato( idUser: Int, token: String)=suspendCoroutine<Candidato>{contResp ->
        Log.d("testing","Inicio getCandidato NetworkServiceAdapter")
        Log.d("testing usuario",idUser.toString())
        Log.d("testing token",token)
        val strReq = object: StringRequest(Request.Method.GET, BASE_URL+"micandidato/$idUser",
            Response.Listener<String>{
                    response ->
                        val resp = JSONObject(response)
                        val cand= Candidato(
                            id=resp.getInt("id"),
                            nombres=resp.getString("nombres"),
                            apellidos=resp.getString("apellidos"),
                            documento=resp.getString("documento"),
                            fecha_nac=resp.getString("fecha_nac"),
                            email=resp.getString("email"),
                            phone=resp.getString("phone"),
                            ciudad=resp.getString("ciudad"),
                            direccion=resp.getString("direccion"),
                            imagen=resp.getString("imagen"),
                            id_usuario=resp.getInt("id_usuario"),
                            num_perfil=resp.getInt("num_perfil"))
                        contResp.resume(cand) //onComplete(list)
            },
            {
                Log.d("testing","VolleyError getCandidato NetworkServiceAdapter")
                contResp.resumeWithException(it) //throw it   //onError(it)
            }){
            override fun getHeaders(): MutableMap<String, String> {
                Log.d("testing","Inicio getHeaders")
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                Log.d("testing", headers.toString())
                return headers  //return super.getHeaders()  // throws AuthFailureError
            }
        };

        val jsonReq = object: JsonObjectRequest(Request.Method.GET, BASE_URL+"micandidato/$idUser", null,
            Response.Listener<JSONObject>{
                resp -> val cand= Candidato(
                id=resp.getInt("id"),
                nombres=resp.getString("nombres"),
                apellidos=resp.getString("apellidos"),
                documento=resp.getString("documento"),
                fecha_nac=resp.getString("fecha_nac"),
                email=resp.getString("email"),
                phone=resp.getString("phone"),
                ciudad=resp.getString("ciudad"),
                direccion=resp.getString("direccion"),
                imagen=resp.getString("imagen"),
                id_usuario=resp.getInt("id_usuario"),
                num_perfil=resp.getInt("num_perfil"))
                contResp.resume(cand) //onComplete(list)
            },
            {
                Log.d("testing","VolleyError getCandidato NetworkServiceAdapter")
                contResp.resumeWithException(it) //throw it   //onError(it)
            }){
            override fun getHeaders(): MutableMap<String, String> {
                Log.d("testing","Inicio getHeaders")
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                Log.d("testing", headers.toString())
                return headers  //return super.getHeaders()  // throws AuthFailureError
            }
        }

        requestQueue.add(strReq)

        //requestQueue.add(getRequest("/micandidato/$idUser",
        //    Response.Listener<String>  { response ->
        //        Log.d("testing","Response getCandidato NetworkServiceAdapter")
        //        val resp = JSONObject(response)
        //        val cand= Candidato(
        //            id=resp.getInt("id"),
        //            nombres=resp.getString("nombres"),
        //            apellidos=resp.getString("apellidos"),
        //            documento=resp.getString("documento"),
        //            fecha_nac=resp.getString("fecha_nac"),
        //            email=resp.getString("email"),
        //            phone=resp.getString("phone"),
        //            ciudad=resp.getString("ciudad"),
        //            direccion=resp.getString("direccion"),
        //            imagen=resp.getString("imagen"),
        //            id_usuario=resp.getInt("id_usuario"))
        //        onComplete(cand)
        //    },
        //    {
        //        Log.d("testing","VolleyError getCandidato NetworkServiceAdapter")
        //        onError(it)
        //    })
        //)
    }

    suspend fun getLogin(body: JSONObject)=suspendCoroutine<Login>{contResp ->
        Log.d("testing","Inicio getLogin NetworkServiceAdapter")
        requestQueue.add(postRequest("auth/login", body,
            Response.Listener<JSONObject> { response ->
                Log.d("testing","Response getLogin NetworkServiceAdapter")
                Log.d("testing Response", response.toString())
                val idTipo = if (response.getString("tipo")=="CANDIDATO"){
                    if (!response.isNull("candidato")){
                        val jsonCandidato=response.getJSONObject("candidato")
                        if (!jsonCandidato.isNull("id")){
                            jsonCandidato.getInt("id")
                        }
                        else{
                            0
                        }
                        //response.getJSONObject("candidato").getInt("id")
                    }
                    else{
                        0
                    }
                } else if (response.getString("tipo")=="EMPRESA"){
                    if(!response.isNull("empresa")){
                        val jsonEmpresa=response.getJSONObject("empresa")
                        if (!jsonEmpresa.isNull("id")){
                            jsonEmpresa.getInt("id")
                        }
                        else{
                            0
                        }
                    }
                    else{
                        0
                    }
                } else{
                    0
                }
                //MutableLiveData<Login>()
                val log= Login(token=response.getString("token"),
                    id=response.getInt("id"),
                    tipo=response.getString("tipo"),
                    idTipo=idTipo)
                contResp.resume(log) // onComplete(log)
            },
            {
                Log.d("testing error login","VolleyError getLogin NetworkServiceAdapter")
                Log.d("testing error login",it.toString())
                contResp.resumeWithException(it) //throw it   //onError(it)
            })
        )
    }

    suspend fun getEvalsPuesto(perfilProyId:Int, token: String)=suspendCoroutine<List<Evaluacion>>{ contResp ->
        Log.d("testing","Inicio getEvalsPuesto NetworkServiceAdapter")
        val strReq = object: StringRequest(Request.Method.GET, BASE_URL+"empresas/proyectos/perfiles/evaluaciones/$perfilProyId",
            Response.Listener<String> { response ->
                Log.d("testing","Response getEvalsPuesto NetworkServiceAdapter")
                Log.d("testing", response.toString())

                val resp = JSONObject(response)
                val lstResp = resp.getJSONArray("lstEvals")
                Log.d("testing","Response3 getEvalsPuesto NetworkServiceAdapter")
                val list = mutableListOf<Evaluacion>()
                for (i in 0 until lstResp.length()) {
                    val item = lstResp.getJSONObject(i)
                    list.add(i, Evaluacion(
                        id = item.getInt("id"),
                        idPerfilProy = item.getInt("idPerfilProy"),
                        id_cand = item.getInt("id_cand"),
                        anno = item.getInt("anno"),
                        mes = item.getInt("mes"),
                        strmes = item.getString("strmes"),
                        candidato = item.getString("candidato"),
                        valoracion = item.getInt("valoracion"),
                        calificacion = item.getString("calificacion"),
                        nota = item.getString("nota") ))
                }
                Log.d("testing","Response4 getEvalsPuesto NetworkServiceAdapter")
                contResp.resume(list) //onComplete(list)
            },
            {
                Log.d("testing","VolleyError getEvalsPuesto NetworkServiceAdapter")
                contResp.resumeWithException(it) //throw it   //onError(it)
            }){
            override fun getHeaders(): MutableMap<String, String> {
                Log.d("testing","Inicio getHeaders")
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                Log.d("testing", headers.toString())
                return headers  //return super.getHeaders()  // throws AuthFailureError
            }
        };
        requestQueue.add(strReq)
    }

    suspend fun getPuestosEmpresaNoAsig(body: JSONObject, empId:Int, token: String)=suspendCoroutine<List<Puesto>>{ contResp ->
        Log.d("testing","Inicio getPuestosEmpresaAsig NetworkServiceAdapter")
        val jsonReq = object: JsonObjectRequest(Request.Method.POST, BASE_URL+"empresas/$empId/puestosNoAsig", body,
            Response.Listener<JSONObject> { response ->
                Log.d("testing","Response getPuestosEmpresaNoAsig NetworkServiceAdapter")
                Log.d("testing", response.toString())
                val cont=response.getInt("totalCount")
                Log.d("testing", cont.toString())
                val resp = response.getJSONArray("Puestos")
                Log.d("testing","Response3 getPuestosEmpresaNoAsig NetworkServiceAdapter")
                val list = mutableListOf<Puesto>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    var strImagen: String=""
                    if (!item.isNull("imagen")){
                        strImagen=item.getString("imagen")
                    }
                    else{
                        strImagen=""
                    }
                    list.add(i, Puesto(
                        Num = item.getInt("Num"),
                        id = item.getInt("id"),
                        nom_proyecto = item.getString("nom_proyecto"),
                        nom_perfil = item.getString("nom_perfil"),
                        id_perfil = item.getInt("id_perfil"),
                        id_cand = item.getInt("id_cand"),
                        candidato = item.getString("candidato"),
                        fecha_inicio = item.getString("fecha_inicio"),
                        fecha_asig = item.getString("fecha_asig"),
                        imagen = strImagen))
                }
                Log.d("testing","Response4 getPuestosEmpresaNoAsig NetworkServiceAdapter")
                contResp.resume(list) //onComplete(list)
            },
            {
                Log.d("testing","VolleyError getPuestosEmpresaNoAsig NetworkServiceAdapter")
                contResp.resumeWithException(it) //throw it   //onError(it)
            }){
            override fun getHeaders(): MutableMap<String, String> {
                Log.d("testing","Inicio getHeaders")
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                Log.d("testing", headers.toString())
                return headers  //return super.getHeaders()  // throws AuthFailureError
            }
        };
        requestQueue.add(jsonReq)
    }
    suspend fun getPuestosEmpresaAsig(body: JSONObject, empId:Int, token: String)=suspendCoroutine<List<Puesto>>{ contResp ->
        Log.d("testing","Inicio getPuestosEmpresaAsig NetworkServiceAdapter")
        val jsonReq = object: JsonObjectRequest(Request.Method.POST, BASE_URL+"empresas/$empId/puestosAsig", body,
            Response.Listener<JSONObject> { response ->
                Log.d("testing","Response getPuestosEmpresaAsig NetworkServiceAdapter")
                Log.d("testing", response.toString())
                val cont=response.getInt("totalCount")
                Log.d("testing", cont.toString())
                val resp = response.getJSONArray("Puestos")
                Log.d("testing","Response3 getPuestosEmpresaAsig NetworkServiceAdapter")
                val list = mutableListOf<Puesto>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    var strImagen: String=""
                    if (!item.isNull("imagen")){
                        strImagen=item.getString("imagen")
                    }
                    else{
                        strImagen=""
                    }
                    list.add(i, Puesto(
                        Num = item.getInt("Num"),
                        id = item.getInt("id"),
                        nom_proyecto = item.getString("nom_proyecto"),
                        nom_perfil = item.getString("nom_perfil"),
                        id_perfil = item.getInt("id_perfil"),
                        id_cand = item.getInt("id_cand"),
                        candidato = item.getString("candidato"),
                        fecha_inicio = item.getString("fecha_inicio"),
                        fecha_asig = item.getString("fecha_asig"),
                        imagen = strImagen))
                }
                Log.d("testing","Response4 getPuestosEmpresaAsig NetworkServiceAdapter")
                contResp.resume(list) //onComplete(list)
            },
            {
                Log.d("testing","VolleyError getPuestosEmpresaAsig NetworkServiceAdapter")
                contResp.resumeWithException(it) //throw it   //onError(it)
            }){
            override fun getHeaders(): MutableMap<String, String> {
                Log.d("testing","Inicio getHeaders")
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                Log.d("testing", headers.toString())
                return headers  //return super.getHeaders()  // throws AuthFailureError
            }
        };
        requestQueue.add(jsonReq)
    }

    suspend fun getEntrevistasCandidato(body: JSONObject, evId: Int, token: String)=suspendCoroutine<List<Entrevista>>{contResp ->
        Log.d("testing","Inicio getEntrevistasCandidato NetworkServiceAdapter")
        val jsonReq = object: JsonObjectRequest(Request.Method.POST, BASE_URL+"entrevistasCandidato/$evId", body,
            Response.Listener<JSONObject> { response ->
                Log.d("testing","Response getEntrevistasCandidato NetworkServiceAdapter")
                Log.d("testing", response.toString())
                val cont=response.getInt("totalCount")
                Log.d("testing", cont.toString())
                val resp = response.getJSONArray("Entrevistas")
                Log.d("testing","Response3 getEntrevistas NetworkServiceAdapter")
                val list = mutableListOf<Entrevista>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    list.add(i, Entrevista(id = item.getInt("id"),
                        candidato = item.getString("candidato"),
                        nom_empresa = item.getString("nom_empresa"),
                        nom_proyecto = item.getString("nom_proyecto"),
                        nom_perfil = item.getString("nom_perfil"),
                        cuando = item.getString("cuando"),
                        contacto = item.getString("contacto"),
                        calificacion = item.getString("calificacion"),
                        anotaciones = item.getString("anotaciones"),
                        id_cand = item.getInt("id_cand"),
                        idPerfilProy = item.getInt("idPerfilProy"),
                        id_perfil = item.getInt("id_perfil"),
                        Num = item.getInt("Num"),
                        valoracion = 0 ))
                }
                Log.d("testing","Response4 getEntrevistas NetworkServiceAdapter")
                contResp.resume(list) //onComplete(list)
            },
            {
                Log.d("testing","VolleyError getEntrevista NetworkServiceAdapter")
                contResp.resumeWithException(it) //throw it   //onError(it)
            }){
            override fun getHeaders(): MutableMap<String, String> {
                Log.d("testing","Inicio getHeaders")
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                Log.d("testing", headers.toString())
                return headers  //return super.getHeaders()  // throws AuthFailureError
            }
        };
        requestQueue.add(jsonReq)
    }

    suspend fun getEntrevistasCandidatoSinSeguuridad(body: JSONObject, evId: Int, token: String)=suspendCoroutine<List<Entrevista>>{contResp ->
        Log.d("testing","Inicio getEntrevistasCandidato NetworkServiceAdapter")
        requestQueue.add(postRequest("entrevistasCandidato/$evId", body,
            Response.Listener<JSONObject> { response ->
                Log.d("testing","Response getEntrevistasCandidato NetworkServiceAdapter")
                Log.d("testing", response.toString())
                val cont=response.getInt("totalCount")
                Log.d("testing", cont.toString())
                val resp = response.getJSONArray("Entrevistas")
                Log.d("testing","Response3 getEntrevistas NetworkServiceAdapter")
                val list = mutableListOf<Entrevista>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    list.add(i, Entrevista(id = item.getInt("id"),
                        candidato = item.getString("candidato"),
                        nom_empresa = item.getString("nom_empresa"),
                        nom_proyecto = item.getString("nom_proyecto"),
                        nom_perfil = item.getString("nom_perfil"),
                        cuando = item.getString("cuando"),
                        contacto = item.getString("contacto"),
                        calificacion = item.getString("calificacion"),
                        anotaciones = item.getString("anotaciones"),
                        id_cand = item.getInt("id_cand"),
                        idPerfilProy = item.getInt("idPerfilProy"),
                        id_perfil = item.getInt("id_perfil"),
                        Num = item.getInt("Num"),
                        valoracion = 0 ))
                }
                Log.d("testing","Response4 getEntrevistas NetworkServiceAdapter")
                contResp.resume(list) //onComplete(list)
            },
            {
                Log.d("testing","VolleyError getEntrevistas NetworkServiceAdapter")
                //onError(it)
                contResp.resumeWithException(it) //throw it
            }))
    }

    fun getEntrevistas( onComplete:(resp:List<Entrevista>)->Unit , onError: (error:VolleyError)->Unit){
        Log.d("testing","Inicio getEntrevistas NetworkServiceAdapter")
        requestQueue.add(getRequest("entrevistasCandidato/801",
            { response ->
                Log.d("testing","Response getEntrevistas NetworkServiceAdapter")
                val resp = JSONArray(response)
                val list = mutableListOf<Entrevista>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)

                    list.add(i, Entrevista(id = item.getInt("id"),
                        candidato = item.getString("candidato"),
                        nom_empresa = item.getString("nom_empresa"),
                        nom_proyecto = item.getString("nom_proyecto"),
                        nom_perfil = item.getString("nom_perfil"),
                        cuando = item.getString("cuando"),
                        contacto = item.getString("contacto"),
                        calificacion = item.getString("calificacion"),
                        anotaciones = item.getString("anotaciones"),
                        id_cand = item.getInt("id_cand"),
                        idPerfilProy = item.getInt("idPerfilProy"),
                        id_perfil = item.getInt("id_perfil"),
                        Num = item.getInt("Num"),
                        valoracion = 0))
                }
                onComplete(list)
            },
            {
                Log.d("testing","VolleyError getEntrevistas NetworkServiceAdapter")
                onError(it)
            })
        )
    }
    //fun getCollectors(  onComplete:(resp:List<Collector>)->Unit , onError: (error:VolleyError)->Unit) {
    //    requestQueue.add(getRequest("collectors",
    //        Response.Listener<String> { response ->
    //            Log.d("tagb", response)
    //            val resp = JSONArray(response)
    //            val list = mutableListOf<Collector>()
    //            for (i in 0 until resp.length()) {
    //                val item = resp.getJSONObject(i)
    //                list.add(i, Collector(collectorId = item.getInt("id"),name = item.getString("name"), telephone = item.getString("telephone"), email = item.getString("email")))
    //            }
    //            onComplete(list)
    //        },
    //        Response.ErrorListener {
    //            onError(it)
    //        }))
    //}
    //fun getComments( albumId:Int, onComplete:(resp:List<Comment>)->Unit , onError: (error:VolleyError)->Unit) {
    //    requestQueue.add(getRequest("albums/$albumId/comments",
    //        Response.Listener<String> { response ->
    //            val resp = JSONArray(response)
    //            val list = mutableListOf<Comment>()
    //            var item:JSONObject? = null
    //            for (i in 0 until resp.length()) {
    //                item = resp.getJSONObject(i)
    //                Log.d("Response", item.toString())
    //                list.add(i, Comment(albumId = albumId, rating = item.getInt("rating").toString(), description = item.getString("description")))
    //            }
    //            onComplete(list)
    //        },
    //        Response.ErrorListener {
    //            onError(it)
    //        }))
    //}
    //fun postComment(body: JSONObject, albumId: Int,  onComplete:(resp:JSONObject)->Unit , onError: (error:VolleyError)->Unit){
    //    requestQueue.add(postRequest("albums/$albumId/comments",
    //        body,
    //        Response.Listener<JSONObject> { response ->
    //            onComplete(response)
    //        },
    //        Response.ErrorListener {
    //            onError(it)
    //        }))
    //}

    private fun getRequest(path:String, responseListener: Response.Listener<String>, errorListener: Response.ErrorListener): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL+path, responseListener,errorListener)
    }
    private fun postRequest(path: String, body: JSONObject,  responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener ):JsonObjectRequest{
        return  JsonObjectRequest(Request.Method.POST, BASE_URL+path, body, responseListener, errorListener)
    }
    private fun putRequest(path: String, body: JSONObject,  responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener ):JsonObjectRequest{
        return  JsonObjectRequest(Request.Method.PUT, BASE_URL+path, body, responseListener, errorListener)
    }
}

/*

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.vinyls_jetpack_application.models.Album
import com.example.vinyls_jetpack_application.models.Collector
import com.example.vinyls_jetpack_application.models.Comment
import org.json.JSONArray
import org.json.JSONObject

class NetworkServiceAdapter constructor(context: Context) {
    companion object{
        const val BASE_URL= "https://vynils-back-heroku.herokuapp.com/"
        var instance: NetworkServiceAdapter? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: NetworkServiceAdapter(context).also {
                    instance = it
                }
            }
    }
    private val requestQueue: RequestQueue by lazy {
        // applicationContext keeps you from leaking the Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }
    fun getAlbums(onComplete:(resp:List<Album>)->Unit, onError: (error:VolleyError)->Unit){
        requestQueue.add(getRequest("albums",
            Response.Listener<String> { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Album>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    list.add(i, Album(albumId = item.getInt("id"),name = item.getString("name"), cover = item.getString("cover"), recordLabel = item.getString("recordLabel"), releaseDate = item.getString("releaseDate"), genre = item.getString("genre"), description = item.getString("description")))
                }
                onComplete(list)
            },
            Response.ErrorListener {
                onError(it)
            }))
    }
    fun getCollectors(onComplete:(resp:List<Collector>)->Unit, onError: (error:VolleyError)->Unit) {
        requestQueue.add(getRequest("collectors",
            Response.Listener<String> { response ->
                Log.d("tagb", response)
                val resp = JSONArray(response)
                val list = mutableListOf<Collector>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    list.add(i, Collector(collectorId = item.getInt("id"),name = item.getString("name"), telephone = item.getString("telephone"), email = item.getString("email")))
                }
                onComplete(list)
            },
            Response.ErrorListener {
                onError(it)
                Log.d("", it.message.toString())
            }))
    }
    fun getComments(albumId:Int, onComplete:(resp:List<Comment>)->Unit, onError: (error:VolleyError)->Unit) {
        requestQueue.add(getRequest("albums/$albumId/comments",
            Response.Listener<String> { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Comment>()
                var item:JSONObject? = null
                for (i in 0 until resp.length()) {
                    item = resp.getJSONObject(i)
                    Log.d("Response", item.toString())
                    list.add(i, Comment(albumId = albumId, rating = item.getInt("rating").toString(), description = item.getString("description")))
                }
                onComplete(list)
            },
            Response.ErrorListener {
                onError(it)
            }))
    }
    fun postComment(body: JSONObject, albumId: Int,  onComplete:(resp:JSONObject)->Unit , onError: (error:VolleyError)->Unit){
        requestQueue.add(postRequest("albums/$albumId/comments",
            body,
            Response.Listener<JSONObject> { response ->
                onComplete(response)
            },
            Response.ErrorListener {
                onError(it)
            }))
    }
    private fun getRequest(path:String, responseListener: Response.Listener<String>, errorListener: Response.ErrorListener): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL+path, responseListener,errorListener)
    }
    private fun postRequest(path: String, body: JSONObject,  responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener ):JsonObjectRequest{
        return  JsonObjectRequest(Request.Method.POST, BASE_URL+path, body, responseListener, errorListener)
    }
    private fun putRequest(path: String, body: JSONObject,  responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener ):JsonObjectRequest{
        return  JsonObjectRequest(Request.Method.PUT, BASE_URL+path, body, responseListener, errorListener)
    }
} */