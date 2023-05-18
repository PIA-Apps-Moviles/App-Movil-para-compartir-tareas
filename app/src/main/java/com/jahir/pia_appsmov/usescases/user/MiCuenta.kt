package com.jahir.pia_appsmov.usescases.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.jahir.pia_appsmov.R
import com.jahir.pia_appsmov.usescases.pantallaprincipal.MainActivity

class MiCuenta : AppCompatActivity() {
    lateinit var BtnCS: Button
    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mi_cuenta)
        BtnCS = findViewById(R.id.BtnCS)
        mAuth = FirebaseAuth.getInstance()
        BtnCS.setOnClickListener(View.OnClickListener {
            mAuth!!.signOut()
            finish()
            startActivity(Intent(this@MiCuenta, MainActivity::class.java))
        })
    }
}