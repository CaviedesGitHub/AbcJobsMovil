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
import com.example.abcjobsnav.databinding.FragmentSignupBinding
import com.example.abcjobsnav.models.Signup
import com.example.abcjobsnav.viewmodels.SignupViewModel
import androidx.lifecycle.Observer
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.navigation.Navigation
import com.example.abcjobsnav.ui.FieldValidators.isStringContainNumber
import com.example.abcjobsnav.ui.FieldValidators.isStringContainSpecialCharacter
import com.example.abcjobsnav.ui.FieldValidators.isStringLowerAndUpperCase
import com.example.abcjobsnav.ui.FieldValidators.isValidEmail
import java.util.Locale


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SignupViewModel

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
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        val view = binding.root
        setupListeners()
        binding.btnSignUp.setOnClickListener {
            myView=it
            Log.d("testing", "inicio Singup onClickListener")
            if (isValidate()) {
                origenBtn=true
                //Toast.makeText(view.context, "validated", Toast.LENGTH_SHORT).show()
                viewModel.refreshDataFromNetwork(binding.userName.text.toString(),
                    binding.password.text.toString(),
                    binding.confirmPassword.text.toString(),
                    "CANDIDATO")
                Log.d("testing OnClickListener", "LoginFragment after refresh")
                Log.d("testing OnClickListener", viewModel.signup.toString())
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
        viewModel = ViewModelProvider(this, SignupViewModel.Factory(activity.application)).get(
            SignupViewModel::class.java)
        viewModel.signup.observe(viewLifecycleOwner, Observer<Signup> {
            it.apply {
                if (origenBtn) {
                    origenBtn = false
                    val action = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
                    if (myView!=null){
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

        binding.textButtonLogin.setOnClickListener(){
            Log.d("testing Signup", "Inicio")
            val action = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
            it.findNavController().navigate(action)
        }

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
                        mensaje=getString(R.string.signup_unsuccessful)+": "+msgBackend  //"User already exists"  //Unauthorized
                    }
                    else {
                        mensaje=getString(R.string.signup_unsuccessful)
                    }
                }
                else{
                    val delimiter = "."
                    val values=msg1.split(delimiter)
                    val lenguaje=lenguajeActivo()
                    if (lenguaje=="español"){
                        mensaje=getString(R.string.signup_unsuccessful)
                    }
                    else{
                        mensaje=getString(R.string.signup_unsuccessful)+": "+values[values.size-1]  //"Login Unsuccessful: Network Error"
                    }

                }
                Toast.makeText(activity, mensaje, Toast.LENGTH_LONG).show()
                viewModel.onNetworkErrorShown()
            }
        }
    }

    private fun isValidate(): Boolean =
        validateUserName() && validateEmail() && validatePassword() && validateConfirmPassword()

    private fun setupListeners() {
        Log.d("testing", "inicio setupListeners")
        binding.userName.addTextChangedListener(TextFieldValidation(binding.userName))
        Log.d("testing", "inicio setupListeners2")
        //binding.email.addTextChangedListener(TextFieldValidation(binding.email))
        binding.password.addTextChangedListener(TextFieldValidation(binding.password))
        Log.d("testing", "inicio setupListeners3")
        binding.confirmPassword.addTextChangedListener(TextFieldValidation(binding.confirmPassword))
        Log.d("testing", "inicio setupListeners4")
    }

    /**
     * field must not be empy
     */
    private fun validateUserName(): Boolean {
        Log.d("testing", "inicio validateUserName")
        if (binding.userName.text.toString().trim().isEmpty()) {
            binding.textMatNameUser.error = getString(R.string.required_field)
            binding.userName.requestFocus()
            return false
        } else {
            binding.textMatNameUser.isErrorEnabled = false
        }
        return true
    }

    /**
     * 1) field must not be empty
     * 2) text should matches email address format
     */
    private fun validateEmail(): Boolean {
        /*if (binding.email.text.toString().trim().isEmpty()) {
            binding.emailTextInputLayout.error = "Required Field!"
            binding.email.requestFocus()
            return false
        } else if (!isValidEmail(binding.email.text.toString())) {
            binding.emailTextInputLayout.error = "Invalid Email!"
            binding.email.requestFocus()
            return false
        } else {
            binding.emailTextInputLayout.isErrorEnabled = false
        }*/
        return true
    }

    /**
     * 1) field must not be empty
     * 2) password lenght must not be less than 6
     * 3) password must contain at least one digit
     * 4) password must contain atleast one upper and one lower case letter
     * 5) password must contain atleast one special character.
     */
    private fun validatePassword(): Boolean {
        if (binding.password.text.toString().trim().isEmpty()) {
            binding.txtMatPassword.error = getString(R.string.required_field)
            binding.password.requestFocus()
            return false
        } else if (binding.password.text.toString().length < 6) {
            binding.txtMatPassword.error = getString(R.string.password_can_t_be_less_than_6)
            binding.password.requestFocus()
            return false
        } else if (!isStringContainNumber(binding.password.text.toString())) {
            binding.txtMatPassword.error = getString(R.string.required_at_least_1_digit)
            binding.password.requestFocus()
            return false
        } else if (!isStringLowerAndUpperCase(binding.password.text.toString())) {
            binding.txtMatPassword.error =
                getString(R.string.password_must_contain_upper_and_lower_case_letters)
            binding.password.requestFocus()
            return false
        } else if (!isStringContainSpecialCharacter(binding.password.text.toString())) {
            binding.txtMatPassword.error = getString(R.string._1_special_character_required)
            binding.password.requestFocus()
            return false
        } else {
            binding.txtMatPassword.isErrorEnabled = false
        }
        return true
    }

    /**
     * 1) field must not be empty
     * 2) password and confirm password should be same
     */
    private fun validateConfirmPassword(): Boolean {
        when {
            binding.confirmPassword.text.toString().trim().isEmpty() -> {
                binding.txtMatPasswordAgain.error = getString(R.string.required_field)
                binding.confirmPassword.requestFocus()
                return false
            }
            binding.confirmPassword.text.toString() != binding.password.text.toString() -> {
                binding.txtMatPasswordAgain.error = getString(R.string.passwords_don_t_match)
                binding.confirmPassword.requestFocus()
                return false
            }
            else -> {
                binding.txtMatPasswordAgain.isErrorEnabled = false
            }
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
                binding.userName.id -> {
                    validateUserName()
                }
                binding.password.id -> {
                    validatePassword()
                }
                binding.confirmPassword.id -> {
                    validateConfirmPassword()
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