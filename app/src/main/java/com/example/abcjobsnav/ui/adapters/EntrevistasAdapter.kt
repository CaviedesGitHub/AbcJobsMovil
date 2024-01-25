package com.example.abcjobsnav.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.abcjobsnav.R
import com.example.abcjobsnav.databinding.EntrevistaItemBinding
import com.example.abcjobsnav.models.Entrevista
import com.example.abcjobsnav.ui.EntrevistasFragmentDirections

//import com.example.abcjobsnav.ui.AlbumFragmentDirections

class EntrevistasAdapter : RecyclerView.Adapter<EntrevistasAdapter.EntrevistaViewHolder>(){

    var entrevistas :List<Entrevista> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntrevistaViewHolder {
        val withDataBinding: EntrevistaItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            EntrevistaViewHolder.LAYOUT,
            parent,
            false)
        return EntrevistaViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: EntrevistaViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.entrevista = entrevistas[position]
        }
        holder.viewDataBinding.root.setOnClickListener {
            val action = EntrevistasFragmentDirections.actionEntrevistasFragmentToResultadoEntrevistaFragment(entrevistas[position].id, "")
            holder.viewDataBinding.root.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return entrevistas.size
    }


    class EntrevistaViewHolder(val viewDataBinding: EntrevistaItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.entrevista_item
        }
    }


}