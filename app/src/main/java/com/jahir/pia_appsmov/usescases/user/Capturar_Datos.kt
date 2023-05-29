package com.jahir.pia_appsmov.usescases.user

import com.google.gson.annotations.SerializedName

data class Capturar_Datos(
    @SerializedName("misT") var misT: ArrayList<MisDatos>
)
