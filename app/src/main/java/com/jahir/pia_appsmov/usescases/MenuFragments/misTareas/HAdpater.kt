package com.jahir.pia_appsmov.usescases.MenuFragments.misTareas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jahir.pia_appsmov.R
import com.jahir.pia_appsmov.usescases.compartirTarea.Tarea

class HAdpater(
    var context: MisTareas,
    var misT: ArrayList<Tarea>,
    var User : String?
    ): RecyclerView.Adapter<HAdpater.MTViewHolder>(){

    private var onClick: OnItemClicked? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MTViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.tarea_item, parent, false)
        return MTViewHolder(vista)
    }

    override fun onBindViewHolder(holder: MTViewHolder, position: Int) {

        val tarea = misT.get(position)
        val miUser = User.toString()

        if (miUser.equals(tarea.userName)){
            holder.tvIdUsuario.text = tarea.userName
            holder.tvNombre.text = tarea.nameT
            holder.tvMateria.text = tarea.materiaT
        }

        holder.btnEditar.setOnClickListener {
            onClick?.editarTarea(tarea)
        }

        holder.btnBorrar.setOnClickListener {
            onClick?.borrarTarea(tarea.idUsuario, User)
        }

        holder.btnVerPDF.setOnClickListener{
            onClick?.verPDF(tarea.urlT)
        }

        holder.btnVerInfo.setOnClickListener{
            onClick?.verInfo(tarea)
        }

    }

    override fun getItemCount(): Int {
        return misT.size
    }

    inner class MTViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvIdUsuario = itemView.findViewById(R.id.tvIdUsuario) as TextView
        val tvNombre = itemView.findViewById(R.id.tvNombre) as TextView
        val tvMateria = itemView.findViewById(R.id.tvMateria) as TextView
        val btnVerPDF = itemView.findViewById(R.id.btnVerPDF) as Button
        val btnVerInfo = itemView.findViewById(R.id.btnVerInfo) as Button
        val btnEditar = itemView.findViewById(R.id.btnEditar) as Button
        val btnBorrar = itemView.findViewById(R.id.btnBorrar) as Button
    }

    interface OnItemClicked {
        fun editarTarea(tarea: Tarea)
        fun borrarTarea(idUsuario: Int, User: String?)
        fun verPDF(urlT: String)
        fun verInfo(tarea: Tarea)
    }

    fun setOnClick(onClick: MisTareas) {
        this.onClick = onClick
    }
}