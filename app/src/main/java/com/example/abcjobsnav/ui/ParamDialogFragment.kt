package com.example.abcjobsnav.ui

//import android.R
//import android.R
//import android.R
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.abcjobsnav.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class ParamDialogFragment : DialogFragment(), View.OnClickListener {

    /** The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */

    private var paramPerfil :String? = null
    private var paramProyecto :String? = null
    private var paramEmpresa :String? = null
    private var paramCandidato :String? = null

    val TAG: String? = "paramsEVsEmp"
    private val toolbar: Toolbar? = null

    lateinit var perfilTxt:TextInputEditText
    lateinit var proyTxt:TextInputEditText
    lateinit var empTxt:TextInputEditText
    lateinit var candTxt:TextInputEditText

    private var callback: Callback? = null
    fun setCallback(callback: Callback?) {
        this.callback = callback
    }
    companion object {
        private const val ARG_PERFIL = "argPerfil"
        private const val ARG_PROYECTO = "argProyecto"
        private const val ARG_EMPRESA = "argEmpresa"
        private const val ARG_CANDIDATO = "argCandidato"
        fun display(fragmentManager: FragmentManager?): ParamDialogFragment? {
            val exampleDialog = ParamDialogFragment()
            if (fragmentManager != null) {
                exampleDialog.show(fragmentManager, "paramsEVsEmp")
            }
            return exampleDialog
        }

        fun newInstance(): ParamDialogFragment? {
            return ParamDialogFragment()
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        when (id) {
            R.id.btnCancelDialogAbc -> dismiss()
            R.id.btnApplyDialogAbc -> {
                Log.d("Testing CallBack", "Inicio Apply")
                callback!!.onActionClick(perfilTxt.text?.toString(), proyTxt.text?.toString(),
                    empTxt.text?.toString(), candTxt.text?.toString())
                dismiss()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setStyle(STYLE_NORMAL, com.example.abcjobsnav.R.style.AppTheme_FullScreenDialog)
        arguments?.let {
            paramPerfil = it.getString(ARG_PERFIL)
            paramProyecto = it.getString(ARG_PROYECTO)
            paramEmpresa = it.getString(ARG_EMPRESA)
            paramCandidato = it.getString(ARG_CANDIDATO)
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
            //dialog.window!!.setWindowAnimations(com.google.android.material.R.style.AlertDialog_AppCompat_Light)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout to use as dialog or embedded fragment
        Log.d("testing oncreateView Param", "oncreateView Param")
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_param_dialog, container, false)
        //toolbar = view.findViewById<View>(R.id.toolbar)
        val close = view.findViewById<Button>(R.id.btnCancelDialogAbc)
        val action = view.findViewById<Button>(R.id.btnApplyDialogAbc)
        close.setOnClickListener(this)
        action.setOnClickListener(this)
        perfilTxt = view.findViewById<TextInputEditText>(R.id.profileParam)
        proyTxt = view.findViewById<TextInputEditText>(R.id.projectParam)
        empTxt = view.findViewById<TextInputEditText>(R.id.companyParam)
        candTxt = view.findViewById<TextInputEditText>(R.id.candidateParam)
        perfilTxt.setText(paramPerfil)
        proyTxt.setText(paramProyecto)
        empTxt.setText(paramEmpresa)
        candTxt.setText(paramCandidato)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(requireView(), savedInstanceState)
        if (view != null) {
            super.onViewCreated(view, savedInstanceState)
        }
        //toolbar.setNavigationOnClickListener { v -> dismiss() }
        //toolbar.setTitle("FullScreen Dialog")
    }

    /** The system calls this only when creating the layout in a dialog. */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Log.d("testing onCreateDialog Param", "onCreateDialog Param")
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }


    interface Callback {
        fun onActionClick(nom_perfil: String?, nom_proy: String?, nom_emp: String?, nom_cand: String?,)
    }

}