package com.jahir.pia_appsmov.usescases.compartirTarea

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jahir.pia_appsmov.usescases.menu.Menu
import com.jahir.pia_appsmov.Model.datosTareaGlobal.Datos_PDF
import com.jahir.pia_appsmov.Model.datosTareaLocal.Datos_PDF_2
import com.jahir.pia_appsmov.R

class Agregar_Tarea : AppCompatActivity() {

    lateinit var preferences: SharedPreferences
    lateinit var processDialog: ProgressDialog
    lateinit var storageReference: StorageReference
    lateinit var databaseReference: DatabaseReference
    lateinit var databaseReference2: DatabaseReference
    lateinit var storage_path: String
    lateinit private var pdf_url: Uri
    lateinit var Subir: Button
    lateinit var Nombre: TextInputEditText
    lateinit var Materia: TextInputEditText
    lateinit var Descripcion: TextInputEditText
    lateinit var Carrera: TextInputEditText
    lateinit private var mfirestore: FirebaseFirestore
    lateinit private var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_tarea)
        Subir = findViewById(R.id.AddHome)
        databaseReference = FirebaseDatabase.getInstance().getReference("Tareas")
        preferences = getSharedPreferences("Preferences", MODE_PRIVATE)
        Nombre = findViewById(R.id.Name_Tarea)
        Materia = findViewById(R.id.Materia_Tarea)
        Descripcion = findViewById(R.id.Descripcion_Tarea)
        Carrera = findViewById(R.id.Carrera_Tarea)
        mfirestore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val U = preferences.getString("user", null)
        storageReference = FirebaseStorage.getInstance().reference
        Subir.setOnClickListener(View.OnClickListener {
            val Nom = Nombre.getText().toString().trim { it <= ' ' }
            storage_path = "$U/*"
            databaseReference2 = FirebaseDatabase.getInstance().getReference(U!!)
            val Mat = Materia.getText().toString().trim { it <= ' ' }
            val Des = Descripcion.getText().toString().trim { it <= ' ' }
            val Car = Carrera.getText().toString().trim { it <= ' ' }
            openFile()
            if (Nom.isEmpty() && Mat.isEmpty() && Des.isEmpty() && Car.isEmpty()) {
                Toast.makeText(this@Agregar_Tarea, "Complete los campos", Toast.LENGTH_SHORT).show()
            } else {
                postDatosHomework(U, Nom, Mat, Des, Car)
            }
        })
    }

    private fun postDatosHomework(
        usu: String?,
        nom: String,
        mat: String,
        des: String,
        car: String
    ) {
        val map: MutableMap<String, Any?> = HashMap()
        map["Usuario"] = usu
        map["Nombre"] = nom
        map["Materia"] = mat
        map["Descripcion"] = des
        map["Carrera"] = car
        mfirestore!!.collection("Tareas").document(nom).set(map).addOnSuccessListener {
            Toast.makeText(
                this@Agregar_Tarea,
                "Datos guardados",
                Toast.LENGTH_SHORT
            ).show()
        }
            .addOnFailureListener {
                Toast.makeText(
                    this@Agregar_Tarea,
                    "Error al ingresar",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun openFile() {
        val i = Intent()
        i.type = "application/pdf"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i, "Seleccione su tarea.."), 1)

        /*Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_FILE);*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == 1 && data != null && data.data != null) {
            pdf_url = data.data!!
            subirArchivo(pdf_url)

            /*if(requestCode == PICK_PDF_FILE){
                pdf_url = data.getData();
                subirArchivo(pdf_url);
            }*/
        }
        super.onActivityResult(requestCode, resultCode, data)
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
    private fun subirArchivo(pdf_url: Uri?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Subiendo archivo")
        progressDialog.show()
        val reference = storageReference!!.child(storage_path + Nombre!!.text + ".pdf")
        reference.putFile(pdf_url!!).addOnSuccessListener { taskSnapshot ->
            try {
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isComplete);
                val url = uriTask.result
                val datos =
                    Datos_PDF(
                        Nombre!!.text.toString(),
                        Materia!!.text.toString(),
                        Descripcion!!.text.toString(),
                        Carrera!!.text.toString(),
                        url.toString()
                    )
                val datos2 =
                    Datos_PDF_2(
                        Nombre!!.text.toString(),
                        Materia!!.text.toString(),
                        Descripcion!!.text.toString(),
                        Carrera!!.text.toString(),
                        url.toString()
                    )
                databaseReference!!.child(databaseReference!!.push().key!!).setValue(datos)
                databaseReference2!!.child(databaseReference2!!.push().key!!).setValue(datos2)
                Toast.makeText(this@Agregar_Tarea, "Tarea subida exitosamente", Toast.LENGTH_SHORT)
                    .show()
                progressDialog.dismiss()
                startActivity(Intent(this@Agregar_Tarea, Menu::class.java))
            } catch (e: Exception) {
                Toast.makeText(this@Agregar_Tarea, "Tarea subida exitosamente", Toast.LENGTH_SHORT)
                    .show()
            }

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
        }.addOnFailureListener {
            Toast.makeText(
                this@Agregar_Tarea,
                "Error al subir",
                Toast.LENGTH_SHORT
            ).show()
        }
            .addOnProgressListener { snapshot ->
                val progress = 100.0 * snapshot.bytesTransferred / snapshot.totalByteCount
                progressDialog.setMessage("Cargando" + " " + progress.toInt() + "%")
            }
    }
}