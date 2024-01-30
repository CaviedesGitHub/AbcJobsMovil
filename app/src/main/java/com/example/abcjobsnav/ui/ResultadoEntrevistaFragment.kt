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
import com.example.abcjobsnav.models.Entrevista
import androidx.lifecycle.Observer
import com.example.abcjobsnav.databinding.FragmentResultadoEntrevistaBinding
import com.example.abcjobsnav.viewmodels.ResultEvViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ID_PARAM = "evId"
private const val TOKEN_PARAM = "token"

class ResultadoEntrevistaFragment : Fragment() {
    private var _binding: FragmentResultadoEntrevistaBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ResultEvViewModel

    private var id: Int? = null
    private var tokenUser: String? = null
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("testing Entrevistafragment", "Inicio onCreate")
        super.onCreate(savedInstanceState)
        Log.d("testing Entrevistafragment", "Inicio onCreate2")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            id = it.getInt(ID_PARAM)
            tokenUser = it.getString(TOKEN_PARAM)
        }
        Log.d("testing ResultEvfragment", "Inicio onCreate3")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("testing Entrevistafragment", "Inicio onCreateView")
        _binding = FragmentResultadoEntrevistaBinding.inflate(inflater, container, false)
        Log.d("testing Entrevistafragment", "Inicio onCreateView2")
        val view = binding.root
        Log.d("testing Entrevistafragment", "Inicio onCreateView3")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("testing Entrevistafragment", "Inicio onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        Log.d("testing Entrevistafragment", "Inicio onActivityCreated2")
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        Log.d("testing Entrevistafragment", "Inicio onActivityCreated3")
        activity.actionBar?.title = "Interview Result"
        Log.d("testing Entrevistafragment", "Inicio onActivityCreated4")
        viewModel = ViewModelProvider(this, ResultEvViewModel.Factory(activity.application)).get(
            ResultEvViewModel::class.java)
        viewModel.refreshDataFromNetwork(id!!, tokenUser!!)
        Log.d("testing Entrevistafragment", "Inicio onActivityCreated5")
        viewModel.entrevista.observe(viewLifecycleOwner, Observer<Entrevista> {
            it.apply {
                binding.entrevista=viewModel.entrevista.value
                //viewModelAdapter!!.entrevistas = this
            }
        })
        Log.d("testing Entrevistafragment", "Inicio onActivityCreated6")
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
        Log.d("testing Entrevistafragment", "Inicio onActivityCreated7")

        viewModel.errorText.observe(viewLifecycleOwner, Observer<String> {errorText ->
            onNetworkErrorMsg(errorText.toString())
        })
        /*binding.btnEntrevistasCand.setOnClickListener() {
            Log.d("testing Entrevistas", "Inicio")
            val action = CandidatoFragmentDirections.actionCandidatoFragmentToEntrevistasFragment(
                viewModel.candidato.value!!.id,
                tokenUser!!
            )
            Log.d("testing Entrevistas", "Despues Action Dentro del If")
            it.findNavController().navigate(action)
            Log.d("testing Entrevistas", "Despues Navigate")
        }*/
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
                    mensaje="Signup Unsuccessful: "+msgBackend  //"User already exists"  //Unauthorized
                }
                else{
                    val delimiter = "."
                    val values=msg1.split(delimiter)
                    mensaje="Signup Unsuccessful: "+values[values.size-1]  //"Login Unsuccessful: Network Error"
                }
                Toast.makeText(activity, mensaje, Toast.LENGTH_LONG).show()
                viewModel.onNetworkErrorShown()
            }
        }
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
            CandidatoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}