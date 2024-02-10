package com.example.abcjobsnav.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.abcjobsnav.R
import com.example.abcjobsnav.models.Evaluacion
import com.example.abcjobsnav.databinding.EvalsItemBinding
//import com.example.abcjobsnav.ui.AlbumFragmentDirections

class EvalsAdapter : RecyclerView.Adapter<EvalsAdapter.EvalsViewHolder>(){

    var lstEvalsPuestoAsig :List<Evaluacion> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EvalsViewHolder {
        val withDataBinding: EvalsItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            EvalsViewHolder.LAYOUT,
            parent,
            false)
        return EvalsViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: EvalsViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.evaluacion = lstEvalsPuestoAsig[position]
        }
        holder.viewDataBinding.root.setOnClickListener {
            //val action = EntrevistasFragmentDirections.actionEntrevistasFragmentToResultadoEntrevistaFragment(entrevistas[position].id, "")
            //holder.viewDataBinding.root.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return lstEvalsPuestoAsig.size
    }


    class EvalsViewHolder(val viewDataBinding: EvalsItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.evals_item
        }
    }


}