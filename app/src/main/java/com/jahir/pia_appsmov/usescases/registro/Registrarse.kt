package com.jahir.pia_appsmov.usescases.registro

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
import com.google.firebase.firestore.FirebaseFirestore
import com.jahir.pia_appsmov.R
import com.jahir.pia_appsmov.usescases.iniciosesion.InicioSesion
import com.jahir.pia_appsmov.usescases.menu.Menu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Registrarse : AppCompatActivity() {

    lateinit var RBtn: Button
    lateinit var BtnInS: Button
    lateinit var progressDialog: ProgressDialog
    lateinit var NU: TextInputEditText
    lateinit var CN: TextInputEditText
    lateinit var PassN: TextInputEditText

    var USER = Usuario("","","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)

        NU = findViewById(R.id.NU)
        CN = findViewById(R.id.CN)
        PassN = findViewById(R.id.PassN)
        RBtn = findViewById(R.id.RBtn)
        BtnInS = findViewById(R.id.Btn_INS)
        BtnInS.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@Registrarse,
                    InicioSesion::class.java
                )
            )
        })
        RBtn.setOnClickListener(View.OnClickListener {
            val Correo = CN.getText().toString().trim { it <= ' ' }
            val Contrasena = PassN.getText().toString().trim { it <= ' ' }
            val UserA = NU.getText().toString().trim { it <= ' ' }
            agregarUsuario(Correo, Contrasena, UserA)
            progressDialog = ProgressDialog(this@Registrarse)
            progressDialog!!.show()
            progressDialog!!.setContentView(R.layout.pantalla_de_carga)
            progressDialog!!.window!!.setBackgroundDrawableResource(
                android.R.color.transparent
            )

            /*if (Correo.isEmpty() && Contrasena.isEmpty() && UserA.isEmpty()) {
                Toast.makeText(
                    this@Registrarse,
                    "Complete los campos correspondientes",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

            }*/
        })
    }

    private fun agregarUsuario(correo: String, contrasena: String, userA: String) {
        this.USER.usuario = userA
        this.USER.correo = correo
        this.USER.password = contrasena
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.registrarUser(USER)
            runOnUiThread{
                if(call.isSuccessful){
                    Toast.makeText(this@Registrarse, call.body().toString(),Toast.LENGTH_SHORT).show()
                    limpiarObjeto()
                    val intent = Intent(this@Registrarse, InicioSesion::class.java)
                    startActivity(intent)
                    limpiarcampos()
                    finish()
                    progressDialog!!.dismiss()
                }else{
                    Toast.makeText(this@Registrarse, call.body().toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun limpiarcampos() {
        CN.setText("")
        NU.setText("")
        PassN.setText("")
    }

    private fun limpiarObjeto() {
        this.USER.usuario = ""
        this.USER.correo = ""
        this.USER.password = ""
    }
}













