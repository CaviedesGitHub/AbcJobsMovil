package com.example.abcjobsnav.ui

import android.os.Build
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
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.text.set
import androidx.navigation.Navigation
import com.example.abcjobsnav.databinding.FragmentCreaEvalBinding
import com.example.abcjobsnav.models.Evaluacion
import com.example.abcjobsnav.viewmodels.CreateEvalViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ID_PARAM = "id_perfilProy"
private const val ID_PARAM_CAND = "id_cand"
private const val TOKEN_PARAM = "token_user"
private const val PARAM_CANDIDATO = "nom_cand"
private const val PARAM_PROYECTO = "nom_proy"
private const val PARAM_PERFIL = "nom_perfil"
private const val PARAM_FECHAINI = "fecha_inicio"
private const val PARAM_FECHAASIG = "fecha_asig"
private const val PARAM_ANNO = "anno_ult"
private const val PARAM_MES = "mes_ult"
private const val PARAM_IDEMP = "id_emp"

/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreaEvalFragment : Fragment() {

    private var _binding: FragmentCreaEvalBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CreateEvalViewModel

    private var id: Int? = null
    private var id_user: Int? = null
    private var tokenUser: String? = null
    private var id_cand: Int? = null
    private var candidato: String? = null
    private var proyecto: String? = null
    private var perfil: String? = null
    private var fecha_ini: String? = null
    private var fecha_asig: String? = null
    private var anno_ult: Int? = null
    private var mes_ult: Int? = null
    private var idEmp: Int? = null

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
            tokenUser = it.getString(TOKEN_PARAM)
            id_cand = it.getInt(ID_PARAM_CAND)
            candidato = it.getString(PARAM_CANDIDATO)
            proyecto = it.getString(PARAM_PROYECTO)
            perfil = it.getString(PARAM_PERFIL)
            fecha_ini = it.getString(PARAM_FECHAINI)
            fecha_asig = it.getString(PARAM_FECHAASIG)
            anno_ult = it.getInt(PARAM_ANNO)
            mes_ult = it.getInt(PARAM_MES)
            idEmp = it.getInt(PARAM_IDEMP)
            Log.d("testing id param crearEvaluacion", id.toString())
            Log.d("testing id param crearEvaluacion", candidato!!)
            Log.d("testing idEmp param crearEvaluacion", idEmp.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("testing onCreateView crearEvaluacion", "onCreateView")
        _binding = FragmentCreaEvalBinding.inflate(inflater, container, false)
        val view = binding.root
        setupListeners()
        binding.btnCreateEval.setOnClickListener {
            myView=it
            Log.d("testing", "inicio Create Eval onClickListener")
            Log.d("testing AutoComplete1.listSelection", binding.AutoComplete1.listSelection.toString())
            Log.d("testing AutoComplete1.0getItem", binding.AutoComplete1.adapter.getItem(0).toString())
            Log.d("testing AutoComplete1.1getItem", binding.AutoComplete1.adapter.getItem(1).toString())
            Log.d("testing AutoComplete1.2getItem", binding.AutoComplete1.adapter.getItem(2).toString())
            Log.d("testing AutoComplete1.3getItem", binding.AutoComplete1.adapter.getItem(3).toString())
            Log.d("testing AutoComplete1.count", binding.AutoComplete1.adapter.count.toString())
            Log.d("testing AutoComplete1.0getItemId", binding.AutoComplete1.adapter.getItemId(0).toString())
            Log.d("testing AutoComplete1.ID", binding.AutoComplete1.id.toString())

            if (isValidate()) {
                origenBtn=true
                Toast.makeText(view.context, "validated", Toast.LENGTH_SHORT).show()
                var valQualy: String =""
                valQualy=backQualy()
                Log.d("testing valQualy", valQualy)
                viewModel.refreshDataFromNetwork(
                    viewModel.idPerfilProy,
                    viewModel.idCand,
                    anno_ult!!,
                    mes_ult!!,
                    valQualy,
                    binding.txtNota.text.toString(),
                    viewModel.tokenUser
                )
                Log.d("testing OnClickListener", "LoginFragment after refresh")
                Log.d("testing OnClickListener", viewModel.evaluacion.toString())
            }
        }
        binding.btnCancelCreateEval.setOnClickListener {
            myView=it
            Log.d("testing", "inicio Create Eval onClickListener")
            origenBtn=true
            val action = CreaEvalFragmentDirections.actionCreaEvalFragmentToEvalsFragment(
                id!!,
                0,
                tokenUser!!,
                candidato!!,
                proyecto!!,
                perfil!!,
                id_cand!!,
                fecha_ini!!,
                fecha_asig!!,
                viewModel.
                idEmp!!,
                true)
            Navigation.findNavController(it).navigate(action)
        }
        val qualys=resources.getStringArray(R.array.qualys4)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, qualys)
        binding.AutoComplete1.setAdapter(arrayAdapter)
        return view
    }

    fun backQualy(): String{
        val strQ=binding.AutoComplete1.text.toString()
        var strBackQualy: String = ""
        var i: Int = 0
        for (i in 0..3){
            if (binding.AutoComplete1.adapter.getItem(i).toString()==strQ){
                if (i==0){
                    strBackQualy="EXCELENTE"
                }
                else if (i==1){
                    strBackQualy="BUENA"
                }
                else if (i==2){
                    strBackQualy="REGULAR"
                }
                else if (i==3){
                    strBackQualy="MALA"
                }
                break
            }
        }
        return strBackQualy
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        Log.d("testing onActivityCreated crearEvaluacion", "onActivityCreated")
        activity.actionBar?.title = getString(R.string.abc_jobs)
        viewModel = ViewModelProvider(this, CreateEvalViewModel.Factory(activity.application)).get(
            CreateEvalViewModel::class.java)
        viewModel.evaluacion.observe(viewLifecycleOwner, Observer<Evaluacion> {
            it.apply {
                Log.d("testing observe", viewModel.evaluacion.toString())
                if (origenBtn) {
                    origenBtn = false
                    //val action = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
                    if (myView!=null){
                        Log.d("testing id CrearEvalfragment", id.toString())
                        Log.d("testing idTipo CreaEvalfragment", viewModel.evaluacion.value!!.id.toString())
                        val action = CreaEvalFragmentDirections.actionCreaEvalFragmentToEvalsFragment(
                            viewModel.evaluacion.value!!.idPerfilProy,
                            0,
                            tokenUser!!,
                            viewModel.candidato,
                            proyecto!!,
                            perfil!!,
                            viewModel.evaluacion.value!!.id_cand,
                            fecha_ini!!,
                            fecha_asig!!,
                            viewModel.idEmp,
                            false,)
                        Navigation.findNavController(myView!!).navigate(action)
                    }
                }
            }
        })
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
        viewModel.errorText.observe(viewLifecycleOwner, Observer<String> {errorText ->
            onNetworkErrorMsg(errorText.toString())
        })

        viewModel.idPerfilProy=id!!
        viewModel.idCand=id_cand!!
        viewModel.tokenUser=tokenUser!!
        viewModel.candidato=candidato!!
        viewModel.idEmp=idEmp!!

        binding.txtCandidato.text=candidato
        binding.txtProyecto.text=proyecto
        binding.txtPerfil.text=perfil

        if (anno_ult==-1){
            anno_ult=Integer.valueOf(fecha_asig?.substring(0, 4))
            mes_ult=Integer.valueOf(fecha_asig?.substring(5, 6))-1
        }
        else{
            mes_ult= mes_ult?.plus(1)
            if (mes_ult==12){
                mes_ult=0
                anno_ult= anno_ult?.plus(1)
            }
            val anno_aux = Integer.valueOf(fecha_asig?.substring(0, 4))
            val mes_aux = Integer.valueOf(fecha_asig?.substring(5, 6))-1
            if((anno_aux > anno_ult!!) or ((anno_aux == anno_ult!!) and (mes_aux>=mes_ult!!))){
                anno_ult=anno_aux
                mes_ult=mes_aux
            }
        }
        binding.txtAnno.setText(Integer.toString(anno_ult!!))
        binding.txtMonth.setText(setStrMonth(mes_ult!!))
    }

    fun setStrMonth(mes: Int): String{
        val mesesIngles: List<String> = listOf("January","February","March","April","May","June","July","August","September","October","November","December");
        val mesesSpanish: List<String> = listOf("Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre");
        return  mesesIngles[mes]
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
                        mensaje=getString(R.string.evaluation_could_not_be_created)+" :"+msgBackend  //"User already exists"  //Unauthorized
                    }
                    else {
                        mensaje= getString(R.string.evaluation_could_not_be_created)
                    }
                }
                else{
                    val delimiter = "."
                    val values=msg1.split(delimiter)
                    val lenguaje=lenguajeActivo()
                    if (lenguaje=="español"){
                        mensaje=getString(R.string.evaluation_could_not_be_created)
                    }
                    else{
                        mensaje=getString(R.string.evaluation_could_not_be_created)+" :"+values[values.size-1]  //"Login Unsuccessful: Network Error"
                    }
                }
                Toast.makeText(activity, mensaje, Toast.LENGTH_LONG).show()
                viewModel.onNetworkErrorShown()
            }
        }
    }

    private fun isValidate(): Boolean =
        validateValuation() && validateNota()

    private fun setupListeners() {
        binding.AutoComplete1.addTextChangedListener(TextFieldValidation(binding.AutoComplete1))
        binding.txtNota.addTextChangedListener(TextFieldValidation(binding.txtNota))
    }

    private fun validateValuation(): Boolean {
        if (binding.AutoComplete1.text.toString().trim().isEmpty()) {
            binding.layoutQualy1.error = getString(R.string.valuation_required)
            binding.AutoComplete1.requestFocus()
            return false
        } else {
            binding.layoutQualy1.isErrorEnabled = false
        }
        return true
    }

    private fun validateNota(): Boolean {
        if (binding.txtNota.text.toString().trim().isEmpty()) {
            binding.layoutMatNota.error = getString(R.string.createeval_required_notes)
            binding.txtNota.requestFocus()
            return false
        } else {
            binding.layoutMatNota.isErrorEnabled = false
        }
        return true
    }

    private fun isValidFecha(strFecha: String): Boolean{
        //val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        //val date = LocalDate.parse(strFecha, formatter)
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        try {
            val date = formatter.parse(strFecha)
            Log.d("Testing fecha",date.toString())
            return true
        }
        catch(e: Exception){
            return false
        }
    }

    private fun isValidFecha18(strFecha: String): Boolean{
        //val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        //val date = LocalDate.parse(strFecha, formatter)
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        try {
            val ahora= Date()
            val fecha_nac = formatter.parse(strFecha)
            val dif=ahora.getTime()-fecha_nac.getTime()
            val year = dif/(1000*60*60*24*365)
            if (year>=18){
                return true
            }
            else {
                return false
            }
        }
        catch(e: Exception){
            return false
        }
    }

    private fun lenguajeActivo(): String {
        return Locale.getDefault().getDisplayLanguage()
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            Log.d("testing", "inicio TextFieldValidation2")
            // checking ids of each text field and applying functions accordingly.
            when (view.id) {
                binding.AutoComplete1.id -> {
                    validateValuation()
                }
                binding.txtNota.id -> {
                    validateNota()
                }
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
         * @return A new instance of fragment SignupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreaEvalFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}