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
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.abcjobsnav.R
import com.example.abcjobsnav.databinding.FragmentLoginBinding
import com.example.abcjobsnav.models.Login
import com.example.abcjobsnav.viewmodels.LoginViewModel
import java.util.Locale

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
                        val action = LoginFragmentDirections.actionLoginFragmentToJobsFragment(
                            viewModel.login.value!!.id,
                            viewModel.login.value!!.token)
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
            Log.d("Testing Login errorNet", "login")
            Toast.makeText(activity, "Login Unsuccessful", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    private fun onNetworkErrorMsg(msg: String) {
        Log.d("Testing Login 000 funMensaje", msg)
        if (!msg.isNullOrEmpty()){
            val delimiter = "$"
            val values=msg.split(delimiter)
            val msg1:String=values[0]
            val msgBackend:String=values[1]
            Log.d("Testing Login msg1", msg1)
            Log.d("Testing Login msgBackend", msgBackend)
            var mensaje:String=""
            if(!viewModel.isNetworkErrorShown.value!!) {
                if (!msg.contains("nullllll")){
                    // mensaje="Signup Unsuccessful: "+msgBackend
                    if (msg.contains("AuthFailureError")){
                        mensaje= getString(R.string.login_unsuccessful_incorrect_password)
                    }
                    else if(msg.contains("ClientError")){
                        mensaje= getString(R.string.login_unsuccessful_wrong_user_name)
                    }
                    else{
                        val delimiter = "."
                        val values2=msg1.split(delimiter)
                        val lenguaje=lenguajeActivo()
                        if (lenguaje=="español"){
                            mensaje=getString(R.string.login_unsuccessful)+": "+msgBackend //values2[values2.size-1]
                        }
                        else{
                            mensaje=getString(R.string.login_unsuccessful)
                        }
                    }
                }
                else{
                    val delimiter = "."
                    val values3=msg1.split(delimiter)
                    mensaje=getString(R.string.login_unsuccessful)+"=>"+values3[values3.size-1]
                }
                Toast.makeText(activity, mensaje, Toast.LENGTH_LONG).show()
                viewModel.onNetworkErrorShown()
            }
        }
    }
    private fun onNetworkErrorMsgIni(msg: String) {
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
                    // mensaje="Signup Unsuccessful: "+msgBackend
                    if (msg.contains("AuthFailureError")){
                        mensaje= getString(R.string.login_unsuccessful_incorrect_password)
                        Log.d("Testing mensaje", mensaje)
                    }
                    else if(msg.contains("ClientError")){
                        mensaje= getString(R.string.login_unsuccessful_wrong_user_name)
                        Log.d("Testing mensaje", mensaje)
                    }
                    else{
                        val delimiter = "."
                        val values2=msg1.split(delimiter)
                        val lenguaje=lenguajeActivo()
                        Log.d("Testing lenguaje login", lenguaje)
                        if (lenguaje=="español"){
                            mensaje=getString(R.string.login_unsuccessful)+": "+values2[values2.size-1]
                            Log.d("Testing mensaje", mensaje)
                        }
                        else{
                            mensaje=getString(R.string.login_unsuccessful)
                            Log.d("Testing mensaje", mensaje)
                        }
                    }
                }
                else{
                    val delimiter = "."
                    val values=msg1.split(delimiter)
                    mensaje=getString(R.string.login_unsuccessful)  //"Signup Unsuccessful: "+values[values.size-1]
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
            binding.txtFieldUsuario.error = getString(R.string.required_username)
            binding.txtUserName.requestFocus()
            return false
        } else {
            binding.txtFieldUsuario.isErrorEnabled = false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        if (binding.txtPassword.text.toString().trim().isEmpty()) {
            binding.txtFieldClave.error = getString(R.string.required_password)
            binding.txtPassword.requestFocus()
            return false
        }
        else {
            binding.txtFieldClave.isErrorEnabled = false
        }
        return true
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