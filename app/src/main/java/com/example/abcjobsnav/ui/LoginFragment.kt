package com.example.abcjobsnav.ui

import android.os.Bundle
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
        activity.actionBar?.title = getString(R.string.Titulo_entrevistas)
        viewModel = ViewModelProvider(this, LoginViewModel.Factory(activity.application)).get(
            LoginViewModel::class.java)
        viewModel.login.observe(viewLifecycleOwner, Observer<Login> {
            it.apply {
                Log.d("testing Observe Login", viewModel.login.value.toString())
                //viewModelAdapter!!.entrevistas = this
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

                }
                else{

                }
            }
        })
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        binding.textButtonSignup.setOnClickListener(){
            Log.d("testing Signup", "Inicio")
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            it.findNavController().navigate(action)
        }

        binding.btnLogin.setOnClickListener(){
            myView=it
            try{
                viewModel.refreshDataFromNetwork(binding.txtUserName.text.toString(), binding.txtPassword.text.toString())
                Log.d("testing Navegar", "Inicio")
            }
            catch (e:Exception){
                Log.d("Testing Error LF", e.toString())
                Toast.makeText(activity, "Login Error", Toast.LENGTH_LONG).show()
            }
            //viewModel.refreshDataFromNetwork(binding.txtUserName.text.toString(), binding.txtPassword.text.toString())
            //Log.d("testing OnClickListener", "LoginFragment after refresh")
            //Log.d("testing OnClickListener", viewModel.login.toString())
        }

        binding.btnNavegar.setOnClickListener(){
            Log.d("testing Navegar", "Inicio")
            if (viewModel.login.value?.tipo =="CANDIDATO"){
                Log.d("testing Navegar", "Dentro del If")
                if (viewModel.login.value?.idTipo!=0){
                    val action = LoginFragmentDirections.actionLoginFragmentToCandidatoFragment(
                        viewModel.login.value!!.token,
                        viewModel.login.value!!.tipo,
                        viewModel.login.value!!.id,
                        viewModel.login.value!!.idTipo)
                    it.findNavController().navigate(action)
                }
                else{
                    val action = LoginFragmentDirections.actionLoginFragmentToCrearCandidatoFragment(
                        viewModel.login.value!!.id,
                        viewModel.login.value!!.token)
                    it.findNavController().navigate(action)
                }
                Log.d("testing Navegar", "Despues Navigate")
            }
            else if (viewModel.login.value?.tipo=="EMPRESA"){

            }
            else{

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

    /*private fun onNetworkError(msg: String) {
        var mensaje:String=""
        if(!viewModel.isNetworkErrorShown.value!!) {
            if (msg.contains("AuthFailureError")){
                mensaje="Incorrect Password"
            }
            else if(msg.contains("AuthFailureError")){
                mensaje="Wrong user name"
            }
            else{
                mensaje="Network Error"
            }
            Toast.makeText(activity, mensaje, Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }*/

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