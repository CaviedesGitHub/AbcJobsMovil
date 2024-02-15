package com.example.abcjobsnav.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.abcjobsnav.R
import com.example.abcjobsnav.databinding.EntrevistasempresaItemBinding
import com.example.abcjobsnav.models.Entrevista
import com.example.abcjobsnav.ui.EntrevistasEmpresaFragmentDirections
import com.example.abcjobsnav.ui.EntrevistasFragmentDirections

//import com.example.abcjobsnav.ui.AlbumFragmentDirections

class EntrevistasEmpresaAdapter(val idEmp: Int?, val tokenUser: String?, val idUser: Int?) : RecyclerView.Adapter<EntrevistasEmpresaAdapter.EntrevistasEmpresaViewHolder>(){

    var entrevistas :List<Entrevista> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntrevistasEmpresaViewHolder {
        val withDataBinding: EntrevistasempresaItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            EntrevistasEmpresaViewHolder.LAYOUT,
            parent,
            false)
        return EntrevistasEmpresaViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: EntrevistasEmpresaViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.entrevista = entrevistas[position]
        }
        holder.viewDataBinding.root.setOnClickListener {
            val action = EntrevistasEmpresaFragmentDirections.actionEntrevistasEmpresaFragmentToResultadoEntrevistaFragment(
                entrevistas[position].id,
                tokenUser!!,
                idEmp!!,
                0,
                1,
                idUser!!)
            holder.viewDataBinding.root.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return entrevistas.size
    }

    class EntrevistasEmpresaViewHolder(val viewDataBinding: EntrevistasempresaItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.entrevistasempresa_item
        }
    }
}