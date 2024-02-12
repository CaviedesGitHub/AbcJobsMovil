package com.example.abcjobsnav.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.abcjobsnav.R
import androidx.lifecycle.Observer
import com.example.abcjobsnav.databinding.FragmentEmpresaBinding
import com.example.abcjobsnav.models.Empresa
import com.example.abcjobsnav.viewmodels.EmpresaViewModel
import com.squareup.picasso.Picasso
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ID_PARAM = "ID_PARAM"
private const val TOKEN_PARAM = "TOKEN_PARAM"

/**
 * A simple [Fragment] subclass.
 * Use the [CandidatoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmpresaFragment : Fragment() {
    private var _binding: FragmentEmpresaBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: EmpresaViewModel

    private var id: Int? = null
    private var tokenUser: String? = null
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("testing Candidatofragment", "Inicio onCreate")
        super.onCreate(savedInstanceState)
        Log.d("testing Candidatofragment", "Inicio onCreate2")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            id = it.getInt(ID_PARAM)
            tokenUser = it.getString(TOKEN_PARAM)
            Log.d("testing id Candidatofragment", id.toString())
        }
        Log.d("testing Candidatofragment", "Inicio onCreate3")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("testing Empresafragment", "Inicio onCreateView")
        _binding = FragmentEmpresaBinding.inflate(inflater, container, false)
        Log.d("testing Empresafragment", "Inicio onCreateView2")
        val view = binding.root
        Log.d("testing Empresafragment", "Inicio onCreateView3")
        return view
        //return inflater.inflate(R.layout.fragment_candidato, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("testing Empresafragment", "Inicio onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        Log.d("testing Empresafragment", "Inicio onActivityCreated2")
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        Log.d("testing Empresafragment", "Inicio onActivityCreated3")
        activity.actionBar?.title = getString(R.string.abc_jobs)
        Log.d("testing Empresafragment", "Inicio onActivityCreated4")
        viewModel = ViewModelProvider(this, EmpresaViewModel.Factory(activity.application)).get(
            EmpresaViewModel::class.java)
        viewModel.refreshDataFromNetwork(id!!, tokenUser!!)
        Log.d("testing Empresafragment", "Inicio onActivityCreated5")
        viewModel.empresa.observe(viewLifecycleOwner, Observer<Empresa> {
            it.apply {
                binding.empresa=viewModel.empresa.value
            }
        })
        Log.d("testing Empresafragment", "Inicio onActivityCreated6")
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
        Log.d("testing Empresafragment", "Inicio onActivityCreated7")

        viewModel.errorText.observe(viewLifecycleOwner, Observer<String> {errorText ->
            onNetworkErrorMsg(errorText.toString())
        })

        binding.btnCalificaciones.setOnClickListener() {
            Log.d("testing Btn Calificaciones", "Inicio")
            val action = EmpresaFragmentDirections.actionEmpresaFragmentToQualyFragment(
                viewModel.empresa.value!!.id,
                viewModel.empresa.value!!.id_usuario,
                tokenUser!!
            )
            Log.d("testing Empresa", "Despues Action Dentro del If")
            it.findNavController().navigate(action)
            Log.d("testing Empresa", "Despues Navigate")
        }

        binding.btnAsignacion.setOnClickListener() {
            Log.d("testing Btn Asignacion", "Inicio")
            val action = EmpresaFragmentDirections.actionEmpresaFragmentToAsignaFragment(
                viewModel.empresa.value!!.id,
                viewModel.empresa.value!!.id_usuario,
                tokenUser!!,
                true
            )
            Log.d("testing Empresa", "Despues Action Dentro del If")
            it.findNavController().navigate(action)
            Log.d("testing Empresa", "Despues Navigate")
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
                        mensaje=getString(R.string.company_not_recovered)+": "+msgBackend  //"User already exists"  //Unauthorized
                    }
                    else{
                        mensaje= getString(R.string.company_not_recovered)
                    }
                }
                else{
                    val delimiter = "."
                    val values=msg1.split(delimiter)
                    val lenguaje=lenguajeActivo()
                    if (lenguaje=="español"){
                        mensaje=getString(R.string.company_not_recovered)
                    }
                    else{
                        mensaje=getString(R.string.company_not_recovered)+": "+values[values.size-1]  //"Login Unsuccessful: Network Error"
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
         * @return A new instance of fragment CandidatoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EmpresaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}



