package com.example.abcjobsnav.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.abcjobsnav.R
import com.example.abcjobsnav.databinding.FragmentLoginBinding
import com.example.abcjobsnav.models.Login
import com.example.abcjobsnav.viewmodels.LoginViewModel
import kotlinx.coroutines.launch
import android.util.Patterns
import com.example.abcjobsnav.ui.FieldValidators.isStringContainNumber
import com.example.abcjobsnav.ui.FieldValidators.isStringContainSpecialCharacter
import com.example.abcjobsnav.ui.FieldValidators.isStringLowerAndUpperCase
import com.example.abcjobsnav.ui.FieldValidators.isValidEmail

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel

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
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        setupListeners()
        return view
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.abc_jobs)
        viewModel = ViewModelProvider(this, LoginViewModel.Factory(activity.application)).get(
            LoginViewModel::class.java)
        viewModel.login.observe(viewLifecycleOwner, Observer<Login> {
            it.apply {
                Log.d("testing Observe Login", viewModel.login.value.toString())
                //viewModelAdapter!!.entrevistas = this
                if (origenBtn){
                    origenBtn=false
                    if (viewModel.login.value?.tipo =="CANDIDATO"){
                        Log.d("testing Navegar", "Dentro del If")
                        if (viewModel.login.value?.idTipo!=0){
                            Log.d("testing Navegar", "Tipo distinto de cero")
                            val action = LoginFragmentDirections.actionLoginFragmentToCandidatoFragment(
                                viewModel.login.value!!.token,
                                viewModel.login.value!!.tipo,
                                viewModel.login.value!!.id,
                                viewModel.login.value!!.idTipo)
                            //it.findNavController().navigate(action)
                            Log.d("testing Navegar", "Despues de Action")
                            //Navigation.findNavController(activity.parent!!, R.id.loginFragment).navigate(action)
                            if (myView!=null){
                                Navigation.findNavController(myView!!).navigate(action)
                            }
                            Log.d("testing Navegar", "NavController")
                        }
                        else{
                            Log.d("testing id Loginfragment", viewModel.login.value!!.id.toString())
                            val action = LoginFragmentDirections.actionLoginFragmentToCrearCandidatoFragment(
                                viewModel.login.value!!.id,
                                viewModel.login.value!!.token)
                            //it.findNavController().navigate(action)
                            if (myView!=null){
                                Navigation.findNavController(myView!!).navigate(action)
                            }
                        }
                        Log.d("testing Navegar", "Despues Navigate")
                    }
                    else if (viewModel.login.value?.tipo=="EMPRESA"){
                        if (viewModel.login.value?.idTipo==0){
                            Toast.makeText(activity, "Complete the registration on the web platform", Toast.LENGTH_LONG).show()
                        }
                        else{
                            val action = LoginFragmentDirections.actionLoginFragmentToEmpresaFragment(
                                viewModel.login.value!!.id,
                                viewModel.login.value!!.token)
                            if (myView!=null){
                                Navigation.findNavController(myView!!).navigate(action)
                            }
                        }
                    }
                    else{
                        val action = LoginFragmentDirections.actionLoginFragmentToJobsFragment()
                        if (myView!=null){
                            Navigation.findNavController(myView!!).navigate(action)
                        }
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

        binding.textButtonSignup.setOnClickListener(){
            Log.d("testing Signup", "Inicio")
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            it.findNavController().navigate(action)
        }

        binding.btnLogin.setOnClickListener(){
            myView=it
            if (isValidate()) {
                origenBtn=true
                try{
                    viewModel.refreshDataFromNetwork(binding.txtUserName.text.toString(), binding.txtPassword.text.toString())
                    Log.d("testing Navegar", "Inicio")
                }
                catch (e:Exception){
                    Log.d("Testing Error LF", e.toString())
                    Toast.makeText(activity, "Login Error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Login Unsuccessful", Toast.LENGTH_LONG).show()
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
                    mensaje="Login Unsuccessful: "+msgBackend  //"User already exists"  //Unauthorized
                }
                else{
                    val delimiter = "."
                    val values=msg1.split(delimiter)
                    mensaje="Login Unsuccessful: "+values[values.size-1]  //"Login Unsuccessful: Network Error"
                }
                Toast.makeText(activity, mensaje, Toast.LENGTH_LONG).show()
                viewModel.onNetworkErrorShown()
            }
        }
    }
    private fun onNetworkErrorMsgIni(msg: String) {
        Log.d("Testing funMensaje", msg)
        var mensaje:String=""
        if (!msg.isNullOrEmpty()){
            if(!viewModel.isNetworkErrorShown.value!!) {
                if (msg.contains("AuthFailureError")){
                    mensaje="Login Unsuccessful: Incorrect Password"
                }
                else if(msg.contains("ClientError")){
                    mensaje="Login Unsuccessful: Wrong user name"
                }
                else{
                    val delimiter = "."
                    val values=msg.split(delimiter)
                    mensaje="Login Unsuccessful: "+values[values.size-1]  //"Login Unsuccessful: Network Error"
                }
                Toast.makeText(activity, mensaje, Toast.LENGTH_LONG).show()
                viewModel.onNetworkErrorShown()
            }
        }
    }

    private fun isValidate(): Boolean =
        validateUserName() && validatePassword()

    private fun setupListeners() {
        binding.txtUserName.addTextChangedListener(TextFieldValidation(binding.txtUserName))
        binding.txtPassword.addTextChangedListener(TextFieldValidation(binding.txtPassword))
    }

    /**
     * field must not be empy
     */
    private fun validateUserName(): Boolean {
        Log.d("testing", "inicio validateUserName")
        if (binding.txtUserName.text.toString().trim().isEmpty()) {
            binding.txtFieldUsuario.error = "Required Field!"
            binding.txtUserName.requestFocus()
            return false
        } else {
            binding.txtFieldUsuario.isErrorEnabled = false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        if (binding.txtPassword.text.toString().trim().isEmpty()) {
            binding.txtFieldClave.error = "Required Field!"
            binding.txtPassword.requestFocus()
            return false
        }
        else {
            binding.txtFieldClave.isErrorEnabled = false
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
                binding.txtUserName.id -> {
                    validateUserName()
                }
                binding.txtPassword.id -> {
                    validatePassword()
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
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}