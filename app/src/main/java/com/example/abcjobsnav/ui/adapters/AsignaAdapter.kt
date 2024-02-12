package com.example.abcjobsnav.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.abcjobsnav.R
import com.example.abcjobsnav.databinding.AsignaItemBinding
import com.example.abcjobsnav.models.Puesto
import com.example.abcjobsnav.ui.AsignaFragmentDirections


//import com.example.abcjobsnav.ui.AlbumFragmentDirections

class AsignaAdapter(val idEmp:Int?, val idUser: Int?, val tokenUser: String?) : RecyclerView.Adapter<AsignaAdapter.AsignaViewHolder>(){

    var puestosSinAsig :List<Puesto> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsignaViewHolder {
        val withDataBinding: AsignaItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AsignaViewHolder.LAYOUT,
            parent,
            false)
        return AsignaViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: AsignaViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.puesto = puestosSinAsig[position]
        }
        holder.viewDataBinding.root.setOnClickListener {
            Log.d("testing adapter fecha_ini", puestosSinAsig[position].fecha_inicio)
            val action = AsignaFragmentDirections.actionAsignaFragmentToCumplenFragment(
                idEmp!!,
                idUser!!,
                tokenUser!!,
                puestosSinAsig[position].id_perfil,
                puestosSinAsig[position].id,
                puestosSinAsig[position].fecha_inicio,
                puestosSinAsig[position].nom_perfil,
                puestosSinAsig[position].nom_proyecto)
            holder.viewDataBinding.root.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return puestosSinAsig.size
    }


    class AsignaViewHolder(val viewDataBinding: AsignaItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.asigna_item
        }
    }


}