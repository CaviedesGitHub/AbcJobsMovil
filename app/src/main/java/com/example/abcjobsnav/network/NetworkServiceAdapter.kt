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
import com.example.abcjobsnav.models.Login
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
                            id_usuario=resp.getInt("id_usuario"))
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
                id_usuario=resp.getInt("id_usuario"))
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
                    response.getJSONObject("candidato").getInt("id")
                } else if (response.getString("tipo")=="EMPRESA"){
                    response.getJSONObject("empresa").getInt("id")
                } else{
                    0
                }
                //MutableLiveData<Login>()
                val log= Login(token=response.getString("token"),
                    response.getInt("id"),
                    response.getString("tipo"),
                    idTipo)
                contResp.resume(log) // onComplete(log)
            },
            {
                Log.d("testing","VolleyError getLogin NetworkServiceAdapter")
                contResp.resumeWithException(it) //throw it   //onError(it)
            })
        )
    }

    suspend fun getEntrevistasCandidato(body: JSONObject, evId: Int, token: String)=suspendCoroutine<List<Entrevista>>{contResp ->
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