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
import com.example.abcjobsnav.databinding.FragmentCrearCandidatoBinding
import com.example.abcjobsnav.models.Candidato
import com.example.abcjobsnav.viewmodels.CreateCandidatoViewModel
import androidx.lifecycle.Observer
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import com.example.abcjobsnav.ui.FieldValidators.isStringContainNumber
import com.example.abcjobsnav.ui.FieldValidators.isStringContainSpecialCharacter
import com.example.abcjobsnav.ui.FieldValidators.isStringLowerAndUpperCase
import com.example.abcjobsnav.ui.FieldValidators.isValidEmail
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ID_PARAM = "idUser"
private const val TOKEN_PARAM = "token"

/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CrearCandidatoFragment : Fragment() {

    private var _binding: FragmentCrearCandidatoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CreateCandidatoViewModel

    private var id: Int? = null
    private var tokenUser: String? = null
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
            Log.d("testing id param crearCandidato", id.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrearCandidatoBinding.inflate(inflater, container, false)
        val view = binding.root
        setupListeners()
        binding.btnCreate.setOnClickListener {
            myView=it
            Log.d("testing", "inicio Create Candidato onClickListener")
            if (isValidate()) {
                origenBtn=true
                Toast.makeText(view.context, "validated", Toast.LENGTH_SHORT).show()
                viewModel.refreshDataFromNetwork(
                    binding.name.text.toString(),
                    binding.lastname.text.toString(),
                    binding.document.text.toString(),
                    binding.fechanac.text.toString(),
                    binding.email.text.toString(),
                    binding.phone.text.toString(),
                    binding.city.text.toString(),
                    binding.address.text.toString(),
                    "",
                    id!!,
                    0,
                    tokenUser!!)
                Log.d("testing OnClickListener", "LoginFragment after refresh")
                Log.d("testing OnClickListener", viewModel.candidato.toString())
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.abc_jobs)
        viewModel = ViewModelProvider(this, CreateCandidatoViewModel.Factory(activity.application)).get(
            CreateCandidatoViewModel::class.java)
        viewModel.candidato.observe(viewLifecycleOwner, Observer<Candidato> {
            it.apply {
                Log.d("testing observe", viewModel.candidato.toString())
                if (origenBtn) {
                    origenBtn = false
                    //val action = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
                    if (myView!=null){
                        Log.d("testing id CrearCandidatofragment", id.toString())
                        Log.d("testing idTipo CreaCandidatofragment", viewModel.candidato.value!!.id.toString())
                        val action = CrearCandidatoFragmentDirections.actionCrearCandidatoFragmentToCandidatoFragment(
                                                        tokenUser!!,
                                                        "CANDIDATO",
                                                        viewModel.candidato.value!!.id_usuario,
                                                        viewModel.candidato.value!!.id)
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

        //binding.btnNavCand.setOnClickListener(){
        //    Log.d("testing Create Candidato", "Inicio")
        //    val action = CrearCandidatoFragmentDirections.actionCrearCandidatoFragmentToCandidatoFragment(
        //                            tokenUser!!,
        //                            "",
        //                            id!!,
        //                            viewModel.candidato.value!!.id)
        //    it.findNavController().navigate(action)
        //}

        //binding.btnSignup.setOnClickListener(){
        //    viewModel.refreshDataFromNetwork(binding.txtUserName.text.toString(), binding.txtPassword.text.toString())
        //    Log.d("testing OnClickListener", "LoginFragment after refresh")
        //    Log.d("testing OnClickListener", viewModel.login.toString())
        //}
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
                        mensaje=getString(R.string.successfully_created_candidate)+" :"+msgBackend  //"User already exists"  //Unauthorized
                    }
                    else {
                        mensaje=getString(R.string.successfully_created_candidate)
                    }
                }
                else{
                    val delimiter = "."
                    val values=msg1.split(delimiter)
                    val lenguaje=lenguajeActivo()
                    if (lenguaje=="español"){
                        mensaje=getString(R.string.successfully_created_candidate)
                    }
                    else{
                        mensaje=getString(R.string.successfully_created_candidate)+" :"+values[values.size-1]  //"Login Unsuccessful: Network Error"
                    }
                }
                Toast.makeText(activity, mensaje, Toast.LENGTH_LONG).show()
                viewModel.onNetworkErrorShown()
            }
        }
    }

    private fun isValidate(): Boolean =
        validateName() && validateLastName() && validateDocument() && validateEmail() &&validateFecha()

    private fun setupListeners() {
        binding.name.addTextChangedListener(TextFieldValidation(binding.name))
        binding.lastname.addTextChangedListener(TextFieldValidation(binding.lastname))
        binding.document.addTextChangedListener(TextFieldValidation(binding.document))
        binding.email.addTextChangedListener(TextFieldValidation(binding.email))
        binding.fechanac.addTextChangedListener(TextFieldValidation(binding.fechanac))
    }

    private fun validateName(): Boolean {
        if (binding.name.text.toString().trim().isEmpty()) {
            binding.txtMatName.error = getString(R.string.required_name)
            binding.name.requestFocus()
            return false
        } else {
            binding.txtMatName.isErrorEnabled = false
        }
        return true
    }

    private fun validateLastName(): Boolean {
        if (binding.lastname.text.toString().trim().isEmpty()) {
            binding.txtMatLastName.error = getString(R.string.required_lastname)
            binding.lastname.requestFocus()
            return false
        } else {
            binding.txtMatLastName.isErrorEnabled = false
        }
        return true
    }

    private fun validateDocument(): Boolean {
        if (binding.document.text.toString().trim().isEmpty()) {
            binding.txtMatDocument.error = getString(R.string.required_document)
            binding.document.requestFocus()
            return false
        } else {
            binding.txtMatDocument.isErrorEnabled = false
        }
        return true
    }

    private fun validateEmail(): Boolean {
        if (binding.email.text.toString().trim().isEmpty()) {
            binding.txtMatEmail.error = getString(R.string.required_email)
            binding.email.requestFocus()
            return false
        } else if (!isValidEmail(binding.email.text.toString())) {
            binding.txtMatEmail.error = getString(R.string.invalid_email)
            binding.email.requestFocus()
            return false
        } else {
            binding.txtMatEmail.isErrorEnabled = false
        }
        return true
    }

    private fun validateFecha(): Boolean {
        if (binding.fechanac.text.toString().trim().isEmpty()) {
            binding.txtMatFechaNac.isErrorEnabled = false
            return true
            //binding.txtMatFechaNac.error = "Required Field!"
            //binding.fechanac.requestFocus()
            //return false
        } else if (!isValidFecha(binding.fechanac.text.toString())) {
            binding.txtMatFechaNac.error = getString(R.string.invalid_date_yyyy_mm_dd)
            binding.fechanac.requestFocus()
            return false
        } else if (!isValidFecha18(binding.fechanac.text.toString())) {
            binding.txtMatFechaNac.error = getString(R.string.invalid_date_over_18)
            binding.fechanac.requestFocus()
            return false
        }
        else {
            binding.txtMatFechaNac.isErrorEnabled = false
        }
        return true
    }

    //@RequiresApi(Build.VERSION_CODES.O)
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
                binding.name.id -> {
                    validateName()
                }
                binding.lastname.id -> {
                    validateLastName()
                }
                binding.document.id -> {
                    validateDocument()
                }
                binding.email.id -> {
                    validateEmail()
                }
                binding.fechanac.id -> {
                    validateFecha()
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
            SignupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}