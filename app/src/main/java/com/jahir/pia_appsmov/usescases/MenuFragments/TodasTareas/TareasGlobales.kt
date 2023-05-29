package com.jahir.pia_appsmov.usescases.MenuFragments.TodasTareas

import com.google.gson.annotations.SerializedName
import com.jahir.pia_appsmov.usescases.compartirTarea.Tarea

data class TareasGlobales(
    @SerializedName("misT") var misT: ArrayList<Tarea>
)
