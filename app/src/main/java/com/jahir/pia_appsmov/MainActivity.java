package com.jahir.pia_appsmov;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jahir.pia_appsmov.IS_R.InicioSesion;
import com.jahir.pia_appsmov.IS_R.Registrarse;
import com.jahir.pia_appsmov.Menu.Menu;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

    public void PInicioS(View view){
        Intent i = new Intent(this, InicioSesion.class);
        startActivity(i);
    }

    public void PRegistrarse(View view){
        Intent i = new Intent(this, Registrarse.class);
        startActivity(i);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuario = mAuth.getCurrentUser();
        if(usuario != null){
            startActivity(new Intent(MainActivity.this, Menu.class));
            finish();
        }
    }
}