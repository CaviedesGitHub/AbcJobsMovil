package com.example.abcjobsnav

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
import com.example.abcjobsnav.databinding.FragmentEvalsBinding
import com.example.abcjobsnav.models.Evaluacion
import com.example.abcjobsnav.ui.EntrevistasFragment
import com.example.abcjobsnav.ui.QualyFragmentDirections
import com.example.abcjobsnav.ui.SignupFragmentDirections
import com.example.abcjobsnav.ui.adapters.EvalsAdapter
import com.example.abcjobsnav.viewmodels.EvalsPAViewModel
import com.example.abcjobsnav.viewmodels.QualyViewModel
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ID_PARAM = "id_perfilProy"
private const val ID_PARAM_USER = "id_user"
private const val ID_PARAM_CAND = "id_cand"
private const val TOKEN_PARAM = "token_user"
private const val CACHE_PARAM = "cache"
private const val PARAM_CANDIDATO = "nom_cand"
private const val PARAM_PROYECTO = "nom_proy"
private const val PARAM_PERFIL = "nom_perfil"
private const val PARAM_FECHAINI = "fecha_inicio"
private const val PARAM_FECHAASIG = "fecha_asig"
private const val PARAM_IDEMP = "id_emp"

/**
 * A simple [Fragment] subclass.
 * Use the [EntrevistasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EvalsFragment : Fragment() {
    private var _binding: FragmentEvalsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: EvalsPAViewModel
    private var viewModelAdapter: EvalsAdapter? = null

    private var id: Int? = null
    private var id_user: Int? = null
    private var id_cand: Int? = null
    private var tokenUser: String? = null
    private var cache: String? = null
    private var candidato: String? = null
    private var proyecto: String? = null
    private var perfil: String? = null
    private var fecha_ini: String? = null
    private var fecha_asig: String? = null
    private var idEmp: Int? = null

    private var anno_ult: Int? = null
    private var mes_ult: Int? = null

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            id = it.getInt(ID_PARAM)
            id_user = it.getInt(ID_PARAM_USER)
            id_cand = it.getInt(ID_PARAM_CAND)
            tokenUser = it.getString(TOKEN_PARAM)
            cache = it.getString(CACHE_PARAM)
            candidato = it.getString(PARAM_CANDIDATO)
            proyecto = it.getString(PARAM_PROYECTO)
            perfil = it.getString(PARAM_PERFIL)
            fecha_ini = it.getString(PARAM_FECHAINI)
            fecha_asig = it.getString(PARAM_FECHAASIG)
            idEmp = it.getInt(PARAM_IDEMP)

            Log.d("testing idEmp param", idEmp.toString())
            Log.d("Testing Param tokenUser", tokenUser.toString())
            Log.d("Testing Param idUser", id_user.toString())
            Log.d("Testing Param fecha_ini", fecha_ini.toString())
            Log.d("Testing Param fecha_asig", fecha_asig.toString())

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("testing onCreateView", "Inicio")
        _binding = FragmentEvalsBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = EvalsAdapter()
        Log.d("testing onCreateView", "Fin")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("testing onViewCreated", "Inicio")
        recyclerView = binding.evalsPuestoAsigRv
        recyclerView.layoutManager = LinearLayoutManager(context)
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
        viewModel = ViewModelProvider(this, EvalsPAViewModel.Factory(activity.application)).get(EvalsPAViewModel::class.java)
        viewModel.refreshDataFromNetwork(id!!, tokenUser!!)
        viewModel.lstEvalsPuesto.observe(viewLifecycleOwner, Observer<List<Evaluacion>> {
            it.apply {
                Log.d("testing 1EvalsFrag", "Fin")
                viewModelAdapter!!.lstEvalsPuestoAsig = this
                Log.d("testing 2EvalsFrag", "Fin")
                binding.progressBar.visibility=View.GONE
                Log.d("testing 3EvalsFrag", "Fin")
                if (viewModel.lstEvalsPuesto.value.isNullOrEmpty()){
                    binding.txtMsgVacio.visibility=View.VISIBLE
                    binding.evalsPuestoAsigRv.visibility=View.INVISIBLE
                }
                else{
                    binding.txtMsgVacio.visibility=View.GONE
                    binding.evalsPuestoAsigRv.visibility=View.VISIBLE
                }
                var lAnno: Int = -1
                var lMes: Int = -1
                for (eval in this){
                    if (eval.anno>lAnno){
                        lAnno=eval.anno
                        lMes=eval.mes
                    }
                    else if(eval.anno==lAnno){
                        if (eval.mes>lMes){
                            lMes=eval.mes
                        }
                    }
                }
                anno_ult=lAnno
                mes_ult=lMes
                Log.d("testing anno_ult evalsFragment", anno_ult.toString())
                Log.d("testing mes_ult evalsFragment", mes_ult.toString())
                //lAnno.toString()+"-"+lMes.toString()
                Log.d("testing 4EvalsFrag", "Fin")
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
        viewModel.idCand=id_cand!!
        viewModel.tokenUser=tokenUser!!

        binding.txtEmpleado.text=candidato
        binding.txtProyecto.text=proyecto
        binding.txtPerfil.text=perfil

        binding.btnAddEvaluation.setOnClickListener(){
            Log.d("testing Evals", "Inicio")
            val action = EvalsFragmentDirections.actionEvalsFragmentToCreaEvalFragment(
                viewModel.idPerfilProy,
                viewModel.idCand,
                viewModel.tokenUser,
                candidato!!,
                proyecto!!,
                perfil!!,
                fecha_ini!!,
                fecha_asig!!,
                anno_ult!!,
                mes_ult!!,
                idEmp!!,
                id_user!!
            )
            Log.d("testing Evals2222", "Inicio")
            it.findNavController().navigate(action)
            Log.d("testing Evals333", "Inicio")
        }

        binding.btnBackListJobs.setOnClickListener(){
            val action = EvalsFragmentDirections.actionEvalsFragmentToQualyFragment(
                idEmp!!,
                id_user!!,
                tokenUser!!
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
                        mensaje=getString(R.string.evaluations_list_not_retrieved)+": "+msgBackend  //"User already exists"  //Unauthorized
                    }
                    else{
                        mensaje= getString(R.string.evaluations_list_not_retrieved)
                    }
                }
                else{
                    val delimiter = "."
                    val values=msg1.split(delimiter)
                    val lenguaje=lenguajeActivo()
                    if (lenguaje=="español"){
                        mensaje=getString(R.string.evaluations_list_not_retrieved)
                    }
                    else{
                        mensaje=getString(R.string.evaluations_list_not_retrieved)+": "+values[values.size-1]  //"Login Unsuccessful: Network Error"
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
            EvalsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}