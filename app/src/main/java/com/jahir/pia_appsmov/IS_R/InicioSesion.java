package com.jahir.pia_appsmov.IS_R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.jahir.pia_appsmov.Menu.Menu;
import com.jahir.pia_appsmov.Model.Usuario;
import com.jahir.pia_appsmov.R;

public class InicioSesion extends AppCompatActivity {

    Button ISBtn, RBtn;
    EditText Correo, Contrasena;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        Correo = findViewById(R.id.Correo);
        Contrasena = findViewById(R.id.Contrasena);
        ISBtn = findViewById(R.id.ISBtn);
        RBtn = findViewById(R.id.RBtn);
        mAuth = FirebaseAuth.getInstance();

        ISBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = Correo.getText().toString().trim();
                String password = Contrasena.getText().toString().trim();

                if(correo.isEmpty() && password.isEmpty()){
                    Toast.makeText(InicioSesion.this, "Complete los campos correspondientes", Toast.LENGTH_SHORT).show();
                }else{
                    InicioSesionU(correo, password);
                    progressDialog = new ProgressDialog(InicioSesion.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.pantalla_de_carga);
                    progressDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                }
            }
        });

        RBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InicioSesion.this, Registrarse.class));
            }
        });

    }

    private void InicioSesionU(String correo, String password) {
        mAuth.signInWithEmailAndPassword(correo, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(InicioSesion.this, Menu.class));
                    progressDialog.dismiss();
                    Toast.makeText(InicioSesion.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(InicioSesion.this, "Error", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InicioSesion.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /*@Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuario = mAuth.getCurrentUser();
        if(usuario != null){
            startActivity(new Intent(InicioSesion.this, Registrarse.class));
            finish();
        }
    }*/

}