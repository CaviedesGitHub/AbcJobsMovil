package com.example.abcjobsnav.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.abcjobsnav.R
import com.example.abcjobsnav.databinding.AsignaabcItemBinding
import com.example.abcjobsnav.models.Puesto
import com.example.abcjobsnav.ui.AsignaFragmentDirections


//import com.example.abcjobsnav.ui.AlbumFragmentDirections

class AsignaAbcAdapter(val idEmp:Int?, val idUser: Int?, val tokenUser: String?) : RecyclerView.Adapter<AsignaAbcAdapter.AsignaAbcViewHolder>(){

    var puestosSinAsig :List<Puesto> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsignaAbcViewHolder {
        val withDataBinding: AsignaabcItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AsignaAbcViewHolder.LAYOUT,
            parent,
            false)
        return AsignaAbcViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: AsignaAbcViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.puesto = puestosSinAsig[position]
        }
        holder.viewDataBinding.root.setOnClickListener {
            Log.d("testing adapter fecha_ini", puestosSinAsig[position].fecha_inicio)
            //val action = AsignaAbcFragmentDirections.actionAsignaFragmentToCumplenFragment(
            //    idEmp!!,
            //    idUser!!,
            //    tokenUser!!,
            //    puestosSinAsig[position].id_perfil,
            //    puestosSinAsig[position].id,
            //    puestosSinAsig[position].fecha_inicio,
            //    puestosSinAsig[position].nom_perfil,
            //    puestosSinAsig[position].nom_proyecto)
            //holder.viewDataBinding.root.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return puestosSinAsig.size
    }


    class AsignaAbcViewHolder(val viewDataBinding: AsignaabcItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.asignaabc_item
        }
    }


}