package com.jahir.pia_appsmov.usescases.MenuFragments.TodasTareas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jahir.pia_appsmov.R
import com.jahir.pia_appsmov.usescases.compartirTarea.Tarea

class TGAdapter(
    var context: Inicio,
    var TG: ArrayList<Tarea>
    ): RecyclerView.Adapter<TGAdapter.TGViewHolder>(){

    private var onClick: OnItemClicked? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TGViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.tareas_item, parent, false)
        return TGViewHolder(vista)
    }

    override fun getItemCount(): Int {
        return TG.size
    }

    override fun onBindViewHolder(holder: TGViewHolder, position: Int) {
        val tarea = TG.get(position)

        holder.tvIdUsuario.text = tarea.userName
        holder.tvNombre.text = tarea.nameT
        holder.tvMateria.text = tarea.materiaT

        holder.btnVerPDF.setOnClickListener {
            onClick?.verPDF(tarea.urlT)
        }

        holder.btnVerInfo.setOnClickListener {
            onClick?.verInfo(tarea)
        }
    }

        inner class TGViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvIdUsuario = itemView.findViewById(R.id.tvIdUsuario) as TextView
            val tvNombre = itemView.findViewById(R.id.tvNombre) as TextView
            val tvMateria = itemView.findViewById(R.id.tvMateria) as TextView
            val btnVerPDF = itemView.findViewById(R.id.btnVerPDF) as Button
            val btnVerInfo = itemView.findViewById(R.id.btnVerInfo) as Button
        }

    interface OnItemClicked {
        fun verPDF(urlT: String)
        fun verInfo(tarea: Tarea)
    }

    fun setOnClick(onClick: Inicio) {
        this.onClick = onClick
    }
}