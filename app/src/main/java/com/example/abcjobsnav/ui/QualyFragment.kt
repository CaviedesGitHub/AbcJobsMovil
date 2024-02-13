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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.abcjobsnav.R
import com.example.abcjobsnav.ui.adapters.QualyAdapter
import androidx.navigation.fragment.findNavController
import com.example.abcjobsnav.databinding.FragmentQualyBinding
import com.example.abcjobsnav.models.Puesto
import com.example.abcjobsnav.viewmodels.QualyViewModel
import java.util.Locale


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ID_PARAM_EMP = "id_emp"
private const val ID_PARAM_USER = "id_user"
private const val TOKEN_PARAM = "token_user"

/**
 * A simple [Fragment] subclass.
 * Use the [EntrevistasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QualyFragment : Fragment() {
    private var _binding: FragmentQualyBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: QualyViewModel
    private var viewModelAdapter: QualyAdapter? = null

    private var id: Int? = null
    private var id_user: Int? = null
    private var tokenUser: String? = null
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            id = it.getInt(ID_PARAM_EMP)
            id_user = it.getInt(ID_PARAM_USER)
            tokenUser = it.getString(TOKEN_PARAM)
            Log.d("testing id=id_emp param", id.toString())
            Log.d("testing id_user param", id_user.toString())
            Log.d("testing tokenUser param", tokenUser.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("testing onCreateView", "Inicio")
        _binding = FragmentQualyBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = QualyAdapter(id_user, tokenUser, id)
        Log.d("testing onCreateView", "Fin")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("testing onViewCreated", "Inicio")
        recyclerView = binding.puestosAsigRv
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
        viewModel = ViewModelProvider(this, QualyViewModel.Factory(activity.application)).get(QualyViewModel::class.java)
        viewModel.refreshDataFromNetwork(id!!, tokenUser!!)
        viewModel.puestosEmpAsig.observe(viewLifecycleOwner, Observer<List<Puesto>> {
            it.apply {
                viewModelAdapter!!.puestosAsig = this
                binding.progressBar.visibility=View.GONE
                if (viewModel.puestosEmpAsig.value.isNullOrEmpty()){
                    binding.txtMsgVacio.visibility=View.VISIBLE
                    binding.puestosAsigRv.visibility=View.INVISIBLE
                }
                else{
                    binding.txtMsgVacio.visibility=View.GONE
                    binding.puestosAsigRv.visibility=View.VISIBLE
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

        binding.btnQualyBackCompany.setOnClickListener(){
            val action = QualyFragmentDirections.actionQualyFragmentToEmpresaFragment(
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
                        mensaje=getString(R.string.jobs_list_not_retrieved)+": "+msgBackend  //"User already exists"  //Unauthorized
                    }
                    else{
                        mensaje= getString(R.string.jobs_list_not_retrieved)
                    }
                }
                else{
                    val delimiter = "."
                    val values=msg1.split(delimiter)
                    val lenguaje=lenguajeActivo()
                    if (lenguaje=="español"){
                        mensaje=getString(R.string.jobs_list_not_retrieved)
                    }
                    else{
                        mensaje=getString(R.string.jobs_list_not_retrieved)+": "+values[values.size-1]  //"Login Unsuccessful: Network Error"
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
            QualyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
