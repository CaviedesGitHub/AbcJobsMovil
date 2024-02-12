package com.example.abcjobsnav.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.abcjobsnav.R
import com.example.abcjobsnav.databinding.QualyItemBinding
import com.example.abcjobsnav.models.Puesto
import com.example.abcjobsnav.ui.QualyFragmentDirections


//import com.example.abcjobsnav.ui.AlbumFragmentDirections

class QualyAdapter(val idUser: Int?, val tokenUser: String?, val idEmp: Int?) : RecyclerView.Adapter<QualyAdapter.QualyViewHolder>(){

    var puestosAsig :List<Puesto> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QualyViewHolder {
        val withDataBinding: QualyItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            QualyViewHolder.LAYOUT,
            parent,
            false)
        return QualyViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: QualyViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.puesto = puestosAsig[position]
        }
        holder.viewDataBinding.root.setOnClickListener {
            Log.d("Testing puestosAsig[position].id", puestosAsig[position].id.toString())
            val action = QualyFragmentDirections.actionQualyFragmentToEvalsFragment(
                puestosAsig[position].id,
                idUser!!,
                tokenUser!!,
                puestosAsig[position].candidato,
                puestosAsig[position].nom_proyecto,
                puestosAsig[position].nom_perfil,
                puestosAsig[position].id_cand,
                puestosAsig[position].fecha_inicio,
                puestosAsig[position].fecha_asig,
                idEmp!!,
                true)
            holder.viewDataBinding.root.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return puestosAsig.size
    }


    class QualyViewHolder(val viewDataBinding: QualyItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.qualy_item
        }
    }


}