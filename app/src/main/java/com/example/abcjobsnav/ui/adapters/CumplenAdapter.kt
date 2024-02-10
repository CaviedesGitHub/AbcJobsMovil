package com.example.abcjobsnav.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.abcjobsnav.R
import com.example.abcjobsnav.databinding.CumplenItemBinding
import com.example.abcjobsnav.models.CandidatoSel

class CumplenAdapter : RecyclerView.Adapter<CumplenAdapter.CumplenViewHolder>(){

    var lstCand :List<CandidatoSel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CumplenViewHolder {
        val withDataBinding: CumplenItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CumplenViewHolder.LAYOUT,
            parent,
            false)
        return CumplenViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CumplenViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.candSel = lstCand[position]
        }
        holder.bind(lstCand[position])
        holder.viewDataBinding.root.setOnClickListener {
            //val action = EntrevistasFragmentDirections.actionEntrevistasFragmentToResultadoEntrevistaFragment(entrevistas[position].id, "")
            //holder.viewDataBinding.root.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return lstCand.size
    }


    class CumplenViewHolder(val viewDataBinding: CumplenItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.cumplen_item
        }

        fun bind(cand: CandidatoSel) {
            Glide.with(itemView)
                .load(cand.imagen.toUri().buildUpon().scheme("https").build())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ic_broken_image))
                .into(viewDataBinding.imgPhotoCumple)
        }
    }

}