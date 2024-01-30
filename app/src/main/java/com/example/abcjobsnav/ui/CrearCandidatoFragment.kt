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
import com.example.abcjobsnav.databinding.FragmentCrearCandidatoBinding
import com.example.abcjobsnav.models.Candidato
import com.example.abcjobsnav.viewmodels.CreateCandidatoViewModel
import androidx.lifecycle.Observer
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.navigation.Navigation
import com.example.abcjobsnav.ui.FieldValidators.isStringContainNumber
import com.example.abcjobsnav.ui.FieldValidators.isStringContainSpecialCharacter
import com.example.abcjobsnav.ui.FieldValidators.isStringLowerAndUpperCase
import com.example.abcjobsnav.ui.FieldValidators.isValidEmail



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
        activity.actionBar?.title = "Signup"
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

    private fun isValidate(): Boolean =
        validateName() && validateLastName() && validateDocument() && validateEmail()

    private fun setupListeners() {
        binding.name.addTextChangedListener(TextFieldValidation(binding.name))
        binding.lastname.addTextChangedListener(TextFieldValidation(binding.lastname))
        binding.document.addTextChangedListener(TextFieldValidation(binding.document))
        binding.email.addTextChangedListener(TextFieldValidation(binding.email))
    }

    private fun validateName(): Boolean {
        if (binding.name.text.toString().trim().isEmpty()) {
            binding.txtMatName.error = "Required Field!"
            binding.name.requestFocus()
            return false
        } else {
            binding.txtMatName.isErrorEnabled = false
        }
        return true
    }

    private fun validateLastName(): Boolean {
        if (binding.lastname.text.toString().trim().isEmpty()) {
            binding.txtMatLastName.error = "Required Field!"
            binding.lastname.requestFocus()
            return false
        } else {
            binding.txtMatLastName.isErrorEnabled = false
        }
        return true
    }

    private fun validateDocument(): Boolean {
        if (binding.document.text.toString().trim().isEmpty()) {
            binding.txtMatDocument.error = "Required Field!"
            binding.document.requestFocus()
            return false
        } else {
            binding.txtMatDocument.isErrorEnabled = false
        }
        return true
    }

    private fun validateEmail(): Boolean {
        if (binding.email.text.toString().trim().isEmpty()) {
            binding.txtMatEmail.error = "Required Field!"
            binding.email.requestFocus()
            return false
        } else if (!isValidEmail(binding.email.text.toString())) {
            binding.txtMatEmail.error = "Invalid Email!"
            binding.email.requestFocus()
            return false
        } else {
            binding.txtMatEmail.isErrorEnabled = false
        }
        return true
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