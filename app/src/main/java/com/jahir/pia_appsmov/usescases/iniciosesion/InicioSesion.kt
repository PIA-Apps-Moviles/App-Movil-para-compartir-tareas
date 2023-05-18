package com.jahir.pia_appsmov.usescases.iniciosesion

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.jahir.pia_appsmov.usescases.menu.Menu
import com.jahir.pia_appsmov.R
import com.jahir.pia_appsmov.usescases.registro.Registrarse
import com.jahir.pia_appsmov.usescases.registro.RetrofitClient
import com.jahir.pia_appsmov.usescases.registro.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InicioSesion : AppCompatActivity() {

    lateinit var ISBtn: Button
    lateinit var RBtn: Button
    lateinit var User: TextInputEditText
    lateinit var Correo: TextInputEditText
    lateinit var Contrasena: TextInputEditText
    lateinit var progressDialog: ProgressDialog
    lateinit var preferences: SharedPreferences
    var INS = NomU("","","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)

        Correo = findViewById(R.id.Correo)
        User = findViewById(R.id.User)
        Contrasena = findViewById(R.id.Contrasena)
        ISBtn = findViewById(R.id.ISBtn)
        RBtn = findViewById(R.id.RBtn)

        preferences = getSharedPreferences("Preferences", MODE_PRIVATE)

        ISBtn.setOnClickListener(View.OnClickListener {
            val correo = Correo.getText().toString().trim { it <= ' ' }
            val password = Contrasena.getText().toString().trim { it <= ' ' }
            val user = User.getText().toString().trim { it <= ' ' }
            if (correo.isEmpty() && password.isEmpty() && user.isEmpty()) {
                Toast.makeText(
                    this@InicioSesion,
                    "Complete los campos correspondientes",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                InicioSesionU(user, correo, password)
                progressDialog = ProgressDialog(this@InicioSesion)
                progressDialog!!.show()
                progressDialog!!.setContentView(R.layout.pantalla_de_carga)
                progressDialog!!.window!!.setBackgroundDrawableResource(
                    android.R.color.transparent
                )
            }
        })
        RBtn.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@InicioSesion,
                    Registrarse::class.java
                )
            )
        })
    }

    private fun InicioSesionU(user: String, correo: String, password: String) {
        this.INS.user = user
        this.INS.email = correo
        this.INS.passW = password

        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.INUser(INS)
            runOnUiThread{
                if(call.body().toString().equals("SI")){

                    val editor = preferences!!.edit()
                    editor.putString("user", user)
                    editor.putString("correo", correo)
                    editor.commit()

                    Toast.makeText(this@InicioSesion, "Bienvenido",Toast.LENGTH_SHORT).show()
                    limpiarObjeto()
                    val intent = Intent(this@InicioSesion, Menu::class.java)
                    startActivity(intent)
                    limpiarcampos()
                    finish()
                    progressDialog!!.dismiss()
                }else{
                    Toast.makeText(this@InicioSesion, call.body().toString(),Toast.LENGTH_SHORT).show()
                    progressDialog!!.dismiss()
                }
            }
        }
    }
    private fun limpiarcampos() {
        Correo.setText("")
        User.setText("")
        Contrasena.setText("")
    }

    private fun limpiarObjeto() {
        this.INS.user = ""
        this.INS.email = ""
        this.INS.passW = ""
    }


}