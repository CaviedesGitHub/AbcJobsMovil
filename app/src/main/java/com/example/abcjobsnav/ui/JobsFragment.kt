package com.example.abcjobsnav.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.abcjobsnav.R
import com.example.abcjobsnav.databinding.FragmentAsignaBinding
import com.example.abcjobsnav.databinding.FragmentJobsBinding
import com.example.abcjobsnav.ui.adapters.AsignaAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ID_PARAM_USER = "idUser"
private const val TOKEN_PARAM = "tokenUser"
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [JobsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class JobsFragment : Fragment() {
    private var _binding: FragmentJobsBinding? = null
    private val binding get() = _binding!!
    // TODO: Rename and change types of parameters
    private var idUser: Int? = null
    private var tokenUser: String? = null
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            idUser = it.getInt(ID_PARAM_USER)
            tokenUser = it.getString(TOKEN_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("testing onCreateView", "Inicio")
        _binding = FragmentJobsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("testing onActivityCreated", "Inicio")
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.abc_jobs)

        binding.btnAssignABCJobs.setOnClickListener(){
            Log.d("testing idUser",idUser.toString())
            Log.d("testing tokenUser",tokenUser.toString())
            val action = JobsFragmentDirections.actionJobsFragmentToAsignaAbcFragment(
                idUser!!,
                tokenUser!!,
                true
            )
            Navigation.findNavController(it).navigate(action)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment JobsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            JobsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}