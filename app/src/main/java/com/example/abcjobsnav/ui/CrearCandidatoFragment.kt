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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            id = it.getInt(ID_PARAM)
            tokenUser = it.getString(TOKEN_PARAM)
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
            Log.d("testing", "inicio Create Candidato onClickListener")
            if (isValidate()) {
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
                //viewModelAdapter!!.entrevistas = this
            }
        })
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        binding.btnNavCand.setOnClickListener(){
            Log.d("testing Create Candidato", "Inicio")
            val action = CrearCandidatoFragmentDirections.actionCrearCandidatoFragmentToCandidatoFragment(
                                    tokenUser!!,
                                    "",
                                    id!!,
                                    viewModel.candidato.value!!.id)
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

    private fun isValidate(): Boolean =
        validateUserName() && validateEmail() && validatePassword() && validateConfirmPassword()

    private fun setupListeners() {
        Log.d("testing", "inicio setupListeners")
        //binding.userName.addTextChangedListener(TextFieldValidation(binding.userName))
        Log.d("testing", "inicio setupListeners2")
        //binding.email.addTextChangedListener(TextFieldValidation(binding.email))
        //binding.password.addTextChangedListener(TextFieldValidation(binding.password))
        Log.d("testing", "inicio setupListeners3")
        //binding.confirmPassword.addTextChangedListener(TextFieldValidation(binding.confirmPassword))
        Log.d("testing", "inicio setupListeners4")
    }

    /**
     * field must not be empy
     */
    private fun validateUserName(): Boolean {
        Log.d("testing", "inicio validateUserName")
        /*if (binding.userName.text.toString().trim().isEmpty()) {
            binding.textMatNameUser.error = "Required Field!"
            binding.userName.requestFocus()
            return false
        } else {
            binding.textMatNameUser.isErrorEnabled = false
        }*/
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
        /*if (binding.password.text.toString().trim().isEmpty()) {
            binding.txtMatPassword.error = "Required Field!"
            binding.password.requestFocus()
            return false
        } else if (binding.password.text.toString().length < 6) {
            binding.txtMatPassword.error = "password can't be less than 6"
            binding.password.requestFocus()
            return false
        } else if (!isStringContainNumber(binding.password.text.toString())) {
            binding.txtMatPassword.error = "Required at least 1 digit"
            binding.password.requestFocus()
            return false
        } else if (!isStringLowerAndUpperCase(binding.password.text.toString())) {
            binding.txtMatPassword.error =
                "Password must contain upper and lower case letters"
            binding.password.requestFocus()
            return false
        } else if (!isStringContainSpecialCharacter(binding.password.text.toString())) {
            binding.txtMatPassword.error = "1 special character required"
            binding.password.requestFocus()
            return false
        } else {
            binding.txtMatPassword.isErrorEnabled = false
        }*/
        return true
    }

    /**
     * 1) field must not be empty
     * 2) password and confirm password should be same
     */
    private fun validateConfirmPassword(): Boolean {
        /*when {
            binding.confirmPassword.text.toString().trim().isEmpty() -> {
                binding.txtMatPasswordAgain.error = "Required Field!"
                binding.confirmPassword.requestFocus()
                return false
            }
            binding.confirmPassword.text.toString() != binding.password.text.toString() -> {
                binding.txtMatPasswordAgain.error = "Passwords don't match"
                binding.confirmPassword.requestFocus()
                return false
            }
            else -> {
                binding.txtMatPasswordAgain.isErrorEnabled = false
            }
        }*/
        return true
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            Log.d("testing", "inicio TextFieldValidation2")
            // checking ids of each text field and applying functions accordingly.
            /*when (view.id) {
                binding.userName.id -> {
                    validateUserName()
                }
                binding.password.id -> {
                    validatePassword()
                }
                binding.confirmPassword.id -> {
                    validateConfirmPassword()
                }
            }*/
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