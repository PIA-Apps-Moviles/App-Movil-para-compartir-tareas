package com.jahir.pia_appsmov.usescases.user

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.jahir.pia_appsmov.R

class MisDatosAd(
    var context: Context,
    var misT: ArrayList<MisDatos>
): RecyclerView.Adapter<MisDatosAd.MDViewHolder>() {

    private var onClick: OnItemClicked? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MDViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.mic_item, parent, false)
        return MDViewHolder(vista)
    }

    override fun getItemCount(): Int {
        return misT.size
    }

    override fun onBindViewHolder(holder: MDViewHolder, position: Int) {
        val datos = misT.get(position)

        holder.nombre_U.text = "Nombre de Usuario:   "+ datos.usuario
        holder.correo_U.text = "Correo Electronico:  " + datos.correo

        Glide.with(context).load(datos.FP).into(holder.Fp)

        holder.btnUpd.setOnClickListener {
            onClick?.FP()
        }

        holder.btnConF.setOnClickListener {
            onClick?.CONf()
        }


    }

    inner class MDViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nombre_U = itemView.findViewById(R.id.tvUsuario) as TextView
        val correo_U = itemView.findViewById(R.id.tvCorreo) as TextView
        val btnUpd = itemView.findViewById(R.id.btn_FP) as Button
        val btnConF = itemView.findViewById(R.id.btn_Conf) as Button
        val Fp = itemView.findViewById(R.id.miFoto) as ImageView
    }

    interface OnItemClicked {
        fun FP()
        fun CONf()
    }

    fun setOnClick(onClick: MiCuenta) {
        this.onClick = onClick
    }
}