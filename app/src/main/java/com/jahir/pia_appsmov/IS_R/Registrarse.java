package com.jahir.pia_appsmov.IS_R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.jahir.pia_appsmov.MainActivity;
import com.jahir.pia_appsmov.Model.Usuario;
import com.jahir.pia_appsmov.R;

import java.util.HashMap;
import java.util.Map;

public class Registrarse extends AppCompatActivity {
    Button RBtn, BtnInS;
    ProgressDialog progressDialog;
    TextInputEditText NU, CN, PassN;
    //EditText email, password, userA;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        NU = findViewById(R.id.NU);
        CN = findViewById(R.id.CN);
        PassN = findViewById(R.id.PassN);
        RBtn = findViewById(R.id.RBtn);
        BtnInS = findViewById(R.id.Btn_INS);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        BtnInS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registrarse.this, InicioSesion.class));
            }
        });

        RBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Correo = CN.getText().toString().trim();
                String Contrasena = PassN.getText().toString().trim();
                String UserA = NU.getText().toString().trim();

                if(Correo.isEmpty() && Contrasena.isEmpty() && UserA.isEmpty()){
                    Toast.makeText(Registrarse.this, "Complete los campos correspondientes", Toast.LENGTH_SHORT).show();
                }else{
                    //Obtener(UserA, Correo, Contrasena);
                    RegistroU(UserA,Correo, Contrasena);
                    progressDialog = new ProgressDialog(Registrarse.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.pantalla_de_carga);
                    progressDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                }
            }
        });
    }

    /*private void Obtener(String userA, String correo, String contrasena) {
        try{
            mFirestore.collection("Usuario").document(userA).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    if(documentSnapshot.exists()){
                        String Usuario = "";
                        String Correo = "";

                        if(documentSnapshot.contains("Usuario")){
                            Usuario = documentSnapshot.getString("Usuario");
                        }
                        if(documentSnapshot.contains("Correo")){
                            Correo = documentSnapshot.getString("Correo");
                        }

                        if ((userA.equals(Usuario)) || (correo.equals(Correo))) {
                           Toast.makeText(Registrarse.this, "El nombre de usuario o correo ya estan en uso, intentelo de nuevo", Toast.LENGTH_SHORT).show();
                           finish();
                        }else {
                            RegistroU(userA, correo, contrasena);
                        }
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Algo salio mal", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void RegistroU(String userA ,String correo, String contrasena) {

        mAuth.createUserWithEmailAndPassword(correo, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //String id = mAuth.getCurrentUser().getUid();
                Map<String, Object> map = new HashMap<>();
                //map.put("ID", id);
                map.put("Usuario", userA);
                map.put("Correo", correo);
                map.put("Contrasena", contrasena);

                mFirestore.collection("Usuario").document(userA).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                        if(task.isSuccessful()){
                            startActivity(new Intent(Registrarse.this, InicioSesion.class));
                            progressDialog.dismiss();
                            Toast.makeText(Registrarse.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Registrarse.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registrarse.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registrarse.this, "Error al registarse", Toast.LENGTH_SHORT).show();
            }
        });
    }

}