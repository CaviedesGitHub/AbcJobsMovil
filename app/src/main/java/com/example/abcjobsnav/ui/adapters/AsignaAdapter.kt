package com.example.abcjobsnav.ui.adapters

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

class AsignaAdapter : RecyclerView.Adapter<AsignaAdapter.AsignaViewHolder>(){

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
            val action = AsignaFragmentDirections.actionAsignaFragmentToCumplenFragment(
                0,
                0,
                "",
                puestosSinAsig[position].id_perfil,
                puestosSinAsig[position].id)
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