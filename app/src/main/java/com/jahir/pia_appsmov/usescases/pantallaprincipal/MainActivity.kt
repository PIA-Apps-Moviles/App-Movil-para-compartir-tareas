package com.jahir.pia_appsmov.usescases.pantallaprincipal

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.jahir.pia_appsmov.usescases.menu.Menu
import com.jahir.pia_appsmov.R
import com.jahir.pia_appsmov.usescases.iniciosesion.InicioSesion
import com.jahir.pia_appsmov.usescases.registro.Registrarse

class MainActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
    }

    fun PInicioS(view: View?) {
        val i = Intent(this, InicioSesion::class.java)
        startActivity(i)
    }

    fun PRegistrarse(view: View?) {
        val i = Intent(this, Registrarse::class.java)
        startActivity(i)
    }

    override fun onStart() {
        super.onStart()
        val usuario = mAuth!!.currentUser
        if (usuario != null) {
            startActivity(Intent(this@MainActivity, Menu::class.java))
            finish()
        }
    }
}