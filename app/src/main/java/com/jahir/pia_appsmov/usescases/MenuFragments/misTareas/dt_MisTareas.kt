package com.jahir.pia_appsmov.usescases.MenuFragments.misTareas

import com.google.gson.annotations.SerializedName
import com.jahir.pia_appsmov.usescases.compartirTarea.Tarea

data class  dt_MisTareas(
    @SerializedName("misT") var misT: ArrayList<Tarea>
)
