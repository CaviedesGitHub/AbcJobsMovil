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
import android.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.abcjobsnav.R


class ParamDialogFragment : DialogFragment() {

    /** The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */

    val TAG: String? = "paramsEVsEmp"
    private val toolbar: Toolbar? = null

    companion object {
        fun display(fragmentManager: FragmentManager?): ParamDialogFragment? {
            val exampleDialog = ParamDialogFragment()
            if (fragmentManager != null) {
                exampleDialog.show(fragmentManager, "paramsEVsEmp")
            }
            return exampleDialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setStyle(STYLE_NORMAL, com.example.abcjobsnav.R.style.AppTheme_FullScreenDialog)
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
}