package com.example.abcjobsnav.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.app.LoaderManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.abcjobsnav.R
import com.example.abcjobsnav.databinding.FragmentEntrevistasEmpresaBinding
import com.example.abcjobsnav.models.Entrevista
import com.example.abcjobsnav.ui.adapters.EntrevistasEmpresaAdapter
import com.example.abcjobsnav.viewmodels.EntrevistaViewModel
import androidx.navigation.fragment.findNavController
import com.example.abcjobsnav.models.ListaEntrevista
import com.example.abcjobsnav.viewmodels.EntrevistasEmpresaViewModel
import java.util.Locale


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ID_PARAM = "id_emp"
private const val ID_PARAM_USER = "id_user"
private const val TOKEN_PARAM = "token"

/**
 * A simple [Fragment] subclass.
 * Use the [EntrevistasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EntrevistasEmpresaFragment : Fragment() {
    private var _binding: FragmentEntrevistasEmpresaBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: EntrevistasEmpresaViewModel
    private var viewModelAdapter: EntrevistasEmpresaAdapter? = null

    private var id: Int? = null
    private var id_user: Int? = null
    private var tokenUser: String? = null
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var isLargeLayout: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("testing EntrevistasEmpresafragment", "Inicio")
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            id = it.getInt(ID_PARAM)
            id_user = it.getInt(ID_PARAM_USER)
            tokenUser = it.getString(TOKEN_PARAM)
            Log.d("testing EntrevistasEmpresafragment", "Params")
            Log.d("testing EntrevistasEmpresafragment", id.toString())
            Log.d("testing EntrevistasEmpresafragment", tokenUser!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("testing onCreateView", "Inicio")
        _binding = FragmentEntrevistasEmpresaBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = EntrevistasEmpresaAdapter(id, tokenUser, id_user)
        Log.d("testing onCreateView", "Fin")
        return view
        // Inflate the layout for this fragment   return null;
        //return inflater.inflate(R.layout.fragment_entrevistas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("testing onViewCreated", "Inicio")
        recyclerView = binding.entrevistasRvEVC
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
        isLargeLayout = resources.getBoolean(R.bool.large_layout)
        Log.d("testing onViewCreated", "Fin")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("testing onActivityCreated", "Inicio")
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.abc_jobs)
        viewModel = ViewModelProvider(this, EntrevistasEmpresaViewModel.Factory(activity.application)).get(EntrevistasEmpresaViewModel::class.java)
        viewModel.refreshDataFromNetwork(id!!, tokenUser!!,20, 1, "", "", "", "")
        viewModel.entrevistas.observe(viewLifecycleOwner, Observer<ListaEntrevista> {
            Log.d("testing Observe", "Inicio Observe")
            it.apply {
                viewModelAdapter!!.entrevistas = this.lista
                binding.progressBarEVC.visibility=View.GONE
                if (viewModel.entrevistas.value!!.lista.isNullOrEmpty()){
                    binding.txtMsgVacioEVC.visibility=View.VISIBLE
                    binding.entrevistasRvEVC.visibility=View.INVISIBLE
                    binding.pagAnt.isEnabled=false
                    binding.pagAnt2.isEnabled=false
                    binding.pagSig.isEnabled=false
                    binding.pagSig2.isEnabled=false
                    var strRegs:String ="0-0 of 0"
                    binding.editTextText.setText(strRegs)
                    binding.editTextText2.setText(strRegs)
                }
                else{
                    if (viewModel.entrevistas.value!!.total_reg < 10){
                        binding.paginaJobsABC.visibility=View.INVISIBLE
                    }
                    else{
                        binding.paginaJobsABC.visibility=View.VISIBLE
                    }
                    binding.txtMsgVacioEVC.visibility=View.GONE
                    binding.entrevistasRvEVC.visibility=View.VISIBLE

                    viewModel.total_pags=(viewModel.entrevistas.value!!.total_reg/viewModel.max_items)
                    if ((viewModel.entrevistas.value!!.total_reg%viewModel.max_items)!=0){
                        viewModel.total_pags=viewModel.total_pags+1
                    }
                    //if (viewModel.num_pag>viewModel.total_pags){
                    //    viewModel.num_pag=viewModel.total_pags
                    //}
                    var num_ini: Int =(viewModel.num_pag-1)*viewModel.max_items + 1
                    var num_fin: Int = 0
                    if (viewModel.num_pag!=viewModel.total_pags){
                        num_fin=(viewModel.num_pag)*viewModel.max_items
                    }
                    else{
                        if (viewModel.entrevistas.value!!.total_reg % viewModel.max_items==0){
                            num_fin=num_ini+viewModel.max_items-1
                        }
                        else{
                            num_fin=num_ini+viewModel.entrevistas.value!!.total_reg % viewModel.max_items-1
                        }
                    }
                    var strRegs:String =""
                    strRegs=num_ini.toString()+"-"+num_fin.toString()+" of "+viewModel.entrevistas.value!!.total_reg.toString()
                    binding.editTextText.setText(strRegs)
                    binding.editTextText2.setText(strRegs)
                    if (viewModel.num_pag==1){
                        binding.pagAnt.isEnabled=false
                        binding.pagAnt2.isEnabled=false
                    }
                    else{
                        binding.pagAnt.isEnabled=true
                        binding.pagAnt2.isEnabled=true
                    }
                    if (viewModel.num_pag<viewModel.total_pags){
                        binding.pagSig.isEnabled=true
                        binding.pagSig2.isEnabled=true
                    }
                    else{
                        binding.pagSig.isEnabled=false
                        binding.pagSig2.isEnabled=false
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
        binding.btnEvBackCompany.setOnClickListener(){
            val action = EntrevistasEmpresaFragmentDirections.actionEntrevistasEmpresaFragmentToEmpresaFragment(
                id_user!!,
                tokenUser!!
            )
            Navigation.findNavController(it).navigate(action)
        }

        binding.pagAnt.setOnClickListener(){
            prevItem()
        }
        binding.pagSig.setOnClickListener(){
            nextItem()
        }
        binding.btnFiltersJobsABC.setOnClickListener() {
            despliegaDialogo()
        }
        binding.pagAnt2.setOnClickListener(){
            prevItem()
        }
        binding.pagSig2.setOnClickListener(){
            nextItem()
        }
        binding.btnFiltersJobsABC2.setOnClickListener() {
            despliegaDialogo()
        }
    }

    fun prevItem(){
        binding.progressBarEVC.visibility=View.VISIBLE
        binding.entrevistasRvEVC.visibility=View.INVISIBLE
        binding.txtMsgVacioEVC.visibility=View.INVISIBLE
        viewModel.num_pag=viewModel.num_pag-1
        viewModel.refreshDataFromNetwork(id!!, tokenUser!!, viewModel.max_items, viewModel.num_pag,
            viewModel.nom_proy, viewModel.nom_perfil, viewModel.nom_cand, viewModel.nom_cand)
    }

    fun nextItem(){
        binding.progressBarEVC.visibility=View.VISIBLE
        binding.entrevistasRvEVC.visibility=View.INVISIBLE
        binding.txtMsgVacioEVC.visibility=View.INVISIBLE
        viewModel.num_pag=viewModel.num_pag+1
        viewModel.refreshDataFromNetwork(id!!, tokenUser!!, viewModel.max_items, viewModel.num_pag,
            viewModel.nom_proy, viewModel.nom_perfil, viewModel.nom_cand, viewModel.nom_cand)
    }
    fun despliegaDialogo(){
        Log.d("testing Btn Params Entrevistas", "Inicio")
        //val newFragment = ParamDialogFragment()
        //newFragment.show(requireFragmentManager(), "paramsEVsEmp")

        val fragmentManager = requireFragmentManager()
        val newFragment = ParamEntrevistaDialogFragment()
        val args = Bundle()
        args.putString("argPerfil", viewModel.nom_perfil)
        args.putString("argProyecto", viewModel.nom_proy)
        args.putString("argEmpresa", viewModel.nom_emp)
        args.putString("argCandidato", viewModel.nom_cand)
        newFragment.setArguments(args)
        if (true) {
            // The device is using a large layout, so show the fragment as a dialog
            newFragment.setCallback(object : ParamEntrevistaDialogFragment.Callback {
                override fun onActionClick(
                    nom_perfil: String?,
                    nom_proy: String?,
                    nom_emp: String?,
                    nom_cand: String?
                ) {
                    //Toast.makeText(this@MainActivity, name, Toast.LENGTH_SHORT).show()
                    Log.d("testing param", nom_perfil.toString())
                    binding.progressBarEVC.visibility=View.VISIBLE
                    binding.entrevistasRvEVC.visibility=View.INVISIBLE
                    binding.txtMsgVacioEVC.visibility=View.INVISIBLE
                    viewModel.nom_perfil=nom_perfil!!
                    viewModel.nom_proy=nom_proy!!
                    viewModel.nom_emp=nom_emp!!
                    viewModel.nom_cand=nom_cand!!
                    viewModel.num_pag=1
                    viewModel.refreshDataFromNetwork(id!!, tokenUser!!, viewModel.max_items, viewModel.num_pag,
                        viewModel.nom_proy, viewModel.nom_perfil, viewModel.nom_cand, viewModel.nom_emp )
                }
            })
            newFragment.show(fragmentManager, "paramsEVsEmp")
            Log.d("testing como dialog","despues show")
        } else {
            // The device is smaller, so show the fragment fullscreen
            Log.d("testing max screen","inicio else")
            val transaction = fragmentManager.beginTransaction()
            Log.d("testing max screen","begin transaction")
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            Log.d("testing max screen","set Transition")
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            Log.d("testing max screen","antes add")
            transaction
                .add(R.id.entrevistasEmpresaFragment, newFragment)
                .addToBackStack(null)
                .commit()
            // R.id.entrevistasEmpresaFragment  android.R.id.content
            Log.d("testing max screen","despues add")
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
                        mensaje=getString(R.string.interview_list_not_retrieved)+": "+msgBackend  //"User already exists"  //Unauthorized
                    }
                    else{
                        mensaje=getString(R.string.interview_list_not_retrieved)
                    }
                }
                else{
                    val delimiter = "."
                    val values=msg1.split(delimiter)
                    val lenguaje=lenguajeActivo()
                    if (lenguaje=="español"){
                        mensaje=getString(R.string.interview_list_not_retrieved)
                    }
                    else{
                        mensaje=getString(R.string.interview_list_not_retrieved)+": "+values[values.size-1]  //"Login Unsuccessful: Network Error"
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
            EntrevistasEmpresaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}