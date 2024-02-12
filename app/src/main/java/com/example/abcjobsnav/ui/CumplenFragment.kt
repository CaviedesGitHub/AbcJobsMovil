package com.example.abcjobsnav.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.abcjobsnav.R
import androidx.navigation.fragment.findNavController
import com.example.abcjobsnav.databinding.FragmentCumplenBinding
import com.example.abcjobsnav.models.CandidatoSel
import com.example.abcjobsnav.models.PerfilProyecto
import com.example.abcjobsnav.ui.adapters.CumplenAdapter
import com.example.abcjobsnav.viewmodels.CumplenViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ID_PARAM = "id_perfilProy"
private const val ID_PARAM_USER = "id_user"
private const val TOKEN_PARAM = "token_user"
private const val PARAM_PROYECTO = "nom_proy"
private const val PARAM_PERFIL = "nom_perfil"
private const val PARAM_FECHAINI = "fecha_ini"
private const val PARAM_ID_PERFIL = "id_perfil"
private const val PARAM_ID_EMP = "id_emp"

/**
 * A simple [Fragment] subclass.
 * Use the [EntrevistasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CumplenFragment : Fragment() {
    private var _binding: FragmentCumplenBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CumplenViewModel
    private var viewModelAdapter: CumplenAdapter? = null

    private var id: Int? = null
    private var id_user: Int? = null
    private var tokenUser: String? = null
    private var candidato: String? = null
    private var proyecto: String? = null
    private var perfil: String? = null
    private var fecha_ini: String? = null
    private var id_perfil: Int? = null
    private var id_emp: Int? = null

        // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var myView: View? = null
    private var origenBtn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            id = it.getInt(ID_PARAM)
            id_user = it.getInt(ID_PARAM_USER)
            tokenUser = it.getString(TOKEN_PARAM)
            proyecto = it.getString(PARAM_PROYECTO)
            perfil = it.getString(PARAM_PERFIL)
            fecha_ini = it.getString(PARAM_FECHAINI)
            id_perfil = it.getInt(PARAM_ID_PERFIL)
            id_emp = it.getInt(PARAM_ID_EMP)

            Log.d("Testing Param id=id_perfilProy", id.toString())
            Log.d("Testing Param idUser", id_user.toString())
            Log.d("Testing Param tokenUser", tokenUser.toString())
            Log.d("Testing Param proyecto", proyecto.toString())
            Log.d("Testing Param perfil", perfil.toString())
            Log.d("Testing Param fecha_ini", fecha_ini.toString())
            Log.d("Testing Param id_perfil", id_perfil.toString())
            Log.d("Testing Param id_emp", id_emp.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("testing onCreateView", "Inicio")
        _binding = FragmentCumplenBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = CumplenAdapter()
        Log.d("testing onCreateView", "Fin")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("testing onViewCreated", "Inicio")
        recyclerView = binding.cumplenRv
        recyclerView.layoutManager = LinearLayoutManager(context)  // val llm:LinearLayoutManager = LinearLayoutManager(context) // llm.isAutoMeasureEnabled = false
        recyclerView.adapter = viewModelAdapter
        Log.d("testing onViewCreated", "Fin")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("testing onActivityCreated", "Inicio")
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.abc_jobs)
        viewModel = ViewModelProvider(this, CumplenViewModel.Factory(activity.application)).get(CumplenViewModel::class.java)
        viewModel.refreshDataFromNetwork(id_perfil!!, tokenUser!!)
        viewModel.lstCand.observe(viewLifecycleOwner, Observer<List<CandidatoSel>> {
            it.apply {
                viewModelAdapter!!.lstCand = this
                binding.progressBarCumplen.visibility=View.GONE
                if (viewModel.lstCand.value.isNullOrEmpty()){
                    binding.txtMsgVacioCumplen.visibility=View.VISIBLE
                    binding.cumplenRv.visibility=View.INVISIBLE
                }
                else{
                    binding.txtMsgVacioCumplen.visibility=View.GONE
                    binding.cumplenRv.visibility=View.VISIBLE
                }
            }
        })
        viewModel.PerfilProyectoResp.observe(viewLifecycleOwner, Observer<PerfilProyecto> {
            it.apply {
                val mensaje= getString(R.string.assignment_successfully_completed)
                Toast.makeText(activity, mensaje, Toast.LENGTH_LONG).show()
                Thread.sleep(300)
                Log.d("testing cumPLen", "Observe")
                if (origenBtn) {
                    origenBtn = false
                    if (myView!=null){
                        val action = CumplenFragmentDirections.actionCumplenFragmentToAsignaFragment(
                            id_emp!!,
                            id_user!!,
                            tokenUser!!,
                            false
                        )
                        Navigation.findNavController(myView!!).navigate(action)
                    }
                }
            }
        })
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
        Log.d("testing onActivityCreated", "Fin")
        viewModel.errorText.observe(viewLifecycleOwner, Observer<String> {errorText ->
            onNetworkErrorMsg(errorText.toString())
        })

        viewModel.idPerfilProy=id!!
        viewModel.tokenUser=tokenUser!!
        viewModel.idPerfil=id_perfil!!

        binding.txtProyIni.text=fecha_ini
        binding.txtProyecto.text=proyecto
        binding.txtPerfil.text=perfil

        binding.btnAsignaCumplen.setOnClickListener(){
            //val ahora= Date()
            //val formatoDeseado = SimpleDateFormat("yyyy-MM-dd")
            //val fecha_asig = formatoDeseado.format(ahora)
            Log.d("testing id_cand", viewModelAdapter?.id_cand!!.toString())
            Log.d("testing fecha_ini!!", fecha_ini!!.toString())
            Log.d("testing viewModel.idPerfilProy", viewModel.idPerfilProy.toString())
            Log.d("testing tokenUser", viewModel.tokenUser)
            Log.d("testing id_perfil!!", id_perfil!!.toString())
            Log.d("testing viewModel.id_perfil!!", viewModel.idPerfil!!.toString())
            myView=it
            Log.d("testing", "inicio Create Candidato onClickListener")
            if (-1!=viewModelAdapter?.checkedPosition) {
                origenBtn=true
                viewModel.asignaCandidateDataFromNetwork(
                    viewModelAdapter?.id_cand!!,
                    fecha_ini!!,
                    viewModel.idPerfilProy,
                    viewModel.tokenUser)
                Log.d("testing OnClickListener", "LoginFragment after refresh")
            }
            else{
                val mensaje= getString(R.string.select_a_candidate)
                Toast.makeText(activity, mensaje, Toast.LENGTH_LONG).show()
            }
        }

        binding.btnCancelCumplen.setOnClickListener(){
            val action = CumplenFragmentDirections.actionCumplenFragmentToAsignaFragment(
                id_emp!!,
                id_user!!,
                tokenUser!!,
                true
            )
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    private fun onNetworkErrorMsg(msg: String) {
        Log.d("Testing funMensaje", msg)
        if (!msg.isNullOrEmpty()){
            val delimiter = "$"
            val values=msg.split(delimiter)
            val msg1:String=values[0]
            val msgBackend:String=values[1]
            Log.d("Testing msg1", msg1)
            Log.d("Testing msgBackend", msgBackend)
            var mensaje:String=""
            if(!viewModel.isNetworkErrorShown.value!!) {
                if (!msg.contains("nullllll")){
                    val lenguaje=lenguajeActivo()
                    if (lenguaje=="español"){
                        mensaje=getString(R.string.could_not_retrieve_list_of_matched_candidates)+": "+msgBackend  //"User already exists"  //Unauthorized
                    }
                    else{
                        mensaje= getString(R.string.could_not_retrieve_list_of_matched_candidates)
                    }
                }
                else{
                    val delimiter = "."
                    val values=msg1.split(delimiter)
                    val lenguaje=lenguajeActivo()
                    if (lenguaje=="español"){
                        mensaje=getString(R.string.could_not_retrieve_list_of_matched_candidates)
                    }
                    else{
                        mensaje=getString(R.string.could_not_retrieve_list_of_matched_candidates)+": "+values[values.size-1]  //"Login Unsuccessful: Network Error"
                    }
                }
                Toast.makeText(activity, mensaje, Toast.LENGTH_LONG).show()
                viewModel.onNetworkErrorShown()
            }
        }
    }

    private fun lenguajeActivo(): String {
        return Locale.getDefault().getDisplayLanguage()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EntrevistasFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CumplenFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}