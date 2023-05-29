package com.jahir.pia_appsmov.usescases.VerInfoTarea

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.jahir.pia_appsmov.R

class VerInfoT : AppCompatActivity() {

    lateinit var miUsuarioH: TextView
    lateinit var nombreH: TextView
    lateinit var materiaH: TextView
    lateinit var desH: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_info_t)

        val bundle = intent.extras
        val miUser = bundle?.getString("Usuario")
        val nombreHome = bundle?.getString("NombreT")
        val materiaHome = bundle?.getString("MateriaT")
        val desHome = bundle?.getString("DesT")

        miUsuarioH = findViewById(R.id.miusuarioH)
        nombreH = findViewById(R.id.nombreHomeW)
        materiaH = findViewById(R.id.materiaHomeW)
        desH = findViewById(R.id.desHomeW)

        miUsuarioH.text = "Subido por:  " + miUser
        nombreH.text = "Nombre de la tarea:  " + nombreHome
        materiaH.text = "Materia:  " + materiaHome
        desH.text = "Descripción de la tarea:  "+ desHome
    }
}