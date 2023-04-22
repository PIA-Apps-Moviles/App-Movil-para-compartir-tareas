package com.jahir.pia_appsmov;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jahir.pia_appsmov.Model.Datos_PDF;
import com.jahir.pia_appsmov.Model.Datos_PDF_2;
import com.jahir.pia_appsmov.Menu.Menu;

import java.util.HashMap;
import java.util.Map;

public class Agregar_Tarea extends AppCompatActivity {

    ProgressDialog processDialog;
    StorageReference storageReference;
    DatabaseReference databaseReference, databaseReference2;
    String storage_path;
    private Uri pdf_url;
    Button Subir;
    EditText Usuario, Nombre, Materia, Descripcion, Carrera;
    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);

        //Agregar = findViewById(R.id.AddTarea);
        Subir = findViewById(R.id.AddHome);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Tareas");
        Usuario = findViewById(R.id.Usuario);
        Nombre = findViewById(R.id.Name_Tarea);
        Materia = findViewById(R.id.Materia_Tarea);
        Descripcion = findViewById(R.id.Descripcion_Tarea);
        Carrera = findViewById(R.id.Carrera_Tarea);
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Usu = Usuario.getText().toString().trim();
                String Nom = Nombre.getText().toString().trim();
                storage_path = Nom + "/*";
                databaseReference2 = FirebaseDatabase.getInstance().getReference(Usu);
                String Mat = Materia.getText().toString().trim();
                String Des = Descripcion.getText().toString().trim();
                String Car = Carrera.getText().toString().trim();
                openFile();

                if(Nom.isEmpty() && Mat.isEmpty() && Des.isEmpty() && Car.isEmpty()){
                    Toast.makeText(Agregar_Tarea.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                }else{
                    postDatosHomework(Usu, Nom, Mat, Des, Car);
                }
            }
        });

    }

    private void postDatosHomework(String usu, String nom, String mat, String des, String car) {
        Map<String,Object> map = new HashMap<>();
        map.put("Usuario", usu);
        map.put("Nombre", nom);
        map.put("Materia", mat);
        map.put("Descripcion", des);
        map.put("Carrera",  car);

        mfirestore.collection("Tareas").document(nom).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Agregar_Tarea.this, "Datos guardados", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Agregar_Tarea.this, "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFile() {
        Intent i = new Intent();
        i.setType("application/pdf");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Seleccione su tarea.."),1);

        /*Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_FILE);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == 1 && data != null && data.getData()!=null){
            pdf_url = data.getData();
            subirArchivo(pdf_url);

            /*if(requestCode == PICK_PDF_FILE){
                pdf_url = data.getData();
                subirArchivo(pdf_url);
            }*/
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*private void SubirHome(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Subiendo archivo");
        progressDialog.show();

        StorageReference reference = storageReference.child(storage_path+System.currentTimeMillis()+".pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                if (uriTask.isSuccessful()) {
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String download_pdf = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("tarea", download_pdf);
                            mfirestore.collection("Tarea").document(idd).update(map);
                            Toast.makeText(Agregar_Tarea.this, "Tarea subida exitosamente", Toast.LENGTH_SHORT).show();
                            processDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Agregar_Tarea.this, "Error al subir", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }*/
    private void subirArchivo(Uri pdf_url) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Subiendo archivo");
        progressDialog.show();

        StorageReference reference = storageReference.child(storage_path + System.currentTimeMillis() + ".pdf");
        reference.putFile(pdf_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri url = uriTask.getResult();

                Datos_PDF datos = new Datos_PDF(Nombre.getText().toString(), Materia.getText().toString(), Descripcion.getText().toString(), Carrera.getText().toString(), url.toString());
                Datos_PDF_2 datos2 = new Datos_PDF_2(Nombre.getText().toString(), Materia.getText().toString(), Descripcion.getText().toString(), Carrera.getText().toString(), url.toString());

                databaseReference.child(databaseReference.push().getKey()).setValue(datos);
                databaseReference2.child(databaseReference2.push().getKey()).setValue(datos2);

                Toast.makeText(Agregar_Tarea.this, "Tarea subida exitosamente", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                startActivity(new Intent(Agregar_Tarea.this, Menu.class));

                /*while(!uriTask.isSuccessful());
                if(uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String download_uri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("Tarea", download_uri);
                            //mfirestore.collection("Tarea").add(map);
                            startActivity(new Intent(Agregar_Tarea.this, Menu.class));
                            progressDialog.dismiss();
                        }
                    });
                }*/
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Agregar_Tarea.this, "Error al subir", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress=(100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                progressDialog.setMessage("Cargando"+  " " + (int)progress+"%");
            }
        });
    }
}