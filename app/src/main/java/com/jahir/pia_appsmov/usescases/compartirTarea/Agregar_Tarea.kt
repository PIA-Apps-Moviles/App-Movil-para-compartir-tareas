package com.jahir.pia_appsmov.usescases.compartirTarea

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.jahir.pia_appsmov.usescases.menu.Menu
import com.jahir.pia_appsmov.R
import com.jahir.pia_appsmov.usescases.registro.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Agregar_Tarea : AppCompatActivity() {

    lateinit var preferences: SharedPreferences
    lateinit var URL : SharedPreferences
    lateinit var progressDialog: ProgressDialog
    lateinit var storageReference: StorageReference
    lateinit private var pdf_url: Uri
    lateinit var Subir: Button
    lateinit var ElegirT: Button
    lateinit var Nombre: TextInputEditText
    lateinit var Materia: TextInputEditText
    lateinit var Descripcion: TextInputEditText

    var miTarea = Tarea(-1, "", "", "", "", "")

    var siempre = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_tarea)

        Subir = findViewById(R.id.AddHome)
        preferences = getSharedPreferences("Preferences", MODE_PRIVATE)
        URL = getSharedPreferences(urlT.DATOS, Context.MODE_PRIVATE)
        Nombre = findViewById(R.id.Name_Tarea)
        Materia = findViewById(R.id.Materia_Tarea)
        Descripcion = findViewById(R.id.Descripcion_Tarea)

        storageReference = FirebaseStorage.getInstance().getReference()

        ElegirT = findViewById(R.id.ElegirTarea)

        ElegirT.setOnClickListener(View.OnClickListener {
            openFile()
        })

        Subir.setOnClickListener(View.OnClickListener {
            val U = preferences.getString("user", null)
            val Nom = Nombre.getText().toString().trim { it <= ' ' }
            val Mat = Materia.getText().toString().trim { it <= ' ' }
            val Des = Descripcion.getText().toString().trim { it <= ' ' }

            if (Nom.isEmpty() && Mat.isEmpty() && Des.isEmpty()) {
                Toast.makeText(this@Agregar_Tarea, "Complete los campos", Toast.LENGTH_SHORT).show()
            } else {
                CompartirTarea(Nom, Mat, Des, U)
                progressDialog = ProgressDialog(this@Agregar_Tarea)
                progressDialog!!.show()
                progressDialog!!.setContentView(R.layout.pantalla_de_carga)
                progressDialog!!.window!!.setBackgroundDrawableResource(
                    android.R.color.transparent
                )
            }
        })
    }


    private fun openFile() {
        val i = Intent()
        i.type = "application/pdf"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i, "Seleccione su tarea.."), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == 1 && data != null && data.data != null) {
            pdf_url = data.data!!
            subirArchivo(pdf_url)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun subirArchivo(pdf_url: Uri?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Cargando archivo seleccionado")
        progressDialog.show()
        val reference = storageReference.child("Tareas/*" + Nombre!!.text + ".pdf")
        reference.putFile(pdf_url!!).addOnSuccessListener (OnSuccessListener{
                try{
                    reference.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri> { uri ->
                    Toast.makeText(this@Agregar_Tarea, "Selección exitosa", Toast.LENGTH_SHORT)
                    .show()
                    URL.edit().putString(urlT.URLT, uri.toString()).commit()
                    progressDialog!!.dismiss()
                    })
                }catch (e: Exception) {
                    Toast.makeText(this@Agregar_Tarea, "No se pudo seleccionar ese archivo", Toast.LENGTH_SHORT)
                        .show()
                    progressDialog!!.dismiss()
                }
        }).addOnFailureListener {
            Toast.makeText(
                this@Agregar_Tarea,
                "Error al cargar",
                Toast.LENGTH_SHORT
            ).show()
        }.addOnProgressListener { snapshot ->
            val progress = 100.0 * snapshot.bytesTransferred / snapshot.totalByteCount
            progressDialog.setMessage("Cargando" + " " + progress.toInt() + "%")
        }
    }

    private fun CompartirTarea(nom: String, mat: String, des: String, u: String?) {

        val URLH = URL.getString(urlT.URLT, "Algo salio mal")
        this.miTarea.userName = u.toString()
        this.miTarea.nameT = nom
        this.miTarea.materiaT = mat
        this.miTarea.DesH = des
        this.miTarea.urlT = URLH.toString()

        if(siempre) {
            CoroutineScope(Dispatchers.IO).launch {
                val call = RetrofitClient.webService.guardarTarea(miTarea)
                runOnUiThread {
                    if (call.isSuccessful) {
                        Toast.makeText(
                            this@Agregar_Tarea,
                            call.body().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        limpiarObjeto()
                        val intent = Intent(this@Agregar_Tarea, Menu::class.java)
                        startActivity(intent)
                        limpiarcampos()
                        finish()
                        progressDialog!!.dismiss()
                    } else {
                        Toast.makeText(
                            this@Agregar_Tarea,
                            call.body().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun limpiarcampos() {
        Nombre.setText("")
        Materia.setText("")
        Descripcion.setText("")
    }

    private fun limpiarObjeto() {
        this.miTarea.idUsuario = -1
        this.miTarea.nameT = ""
        this.miTarea.materiaT = ""
        this.miTarea.DesH = ""
        this.miTarea.urlT = ""
        this.miTarea.userName = ""
    }
}


