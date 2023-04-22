package com.jahir.pia_appsmov;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.core.View;

public class MiCuenta extends AppCompatActivity {

    Button BtnCS;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_cuenta);

        BtnCS = findViewById(R.id.BtnCS);
        mAuth = FirebaseAuth.getInstance();
        BtnCS.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(MiCuenta.this, MainActivity.class));
            }
        });
    }
}