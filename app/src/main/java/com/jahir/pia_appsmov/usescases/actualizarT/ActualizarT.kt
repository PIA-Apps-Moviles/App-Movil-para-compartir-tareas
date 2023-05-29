package com.jahir.pia_appsmov.usescases.actualizarT

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jahir.pia_appsmov.R
import com.jahir.pia_appsmov.usescases.compartirTarea.urlT
import com.jahir.pia_appsmov.usescases.menu.Menu
import com.jahir.pia_appsmov.usescases.registro.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActualizarT : AppCompatActivity() {

    lateinit var progressDialog: ProgressDialog
    lateinit var storageReference: StorageReference
    lateinit private var pdf_url: Uri
    lateinit var SubirNew: Button
    lateinit var ElegirTNew: Button
    lateinit var NombreNew: TextInputEditText
    lateinit var MateriaNew: TextInputEditText
    lateinit var DescripcionNew: TextInputEditText
    lateinit var URLNew : SharedPreferences

    var minuevaT = UpdateTarea("", "", "", "")

    var siempre = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_t)

        val bundle = intent.extras
        val dato = bundle?.getInt("idTarea")


        SubirNew = findViewById(R.id.AddHomeNew)
        NombreNew = findViewById(R.id.nombreNew)
        MateriaNew = findViewById(R.id.materiaNew)
        DescripcionNew = findViewById(R.id.desNew)
        URLNew = getSharedPreferences(urlT.DATOS, Context.MODE_PRIVATE)

        storageReference = FirebaseStorage.getInstance().getReference()

        ElegirTNew = findViewById(R.id.ElegirTareaNew)

        ElegirTNew.setOnClickListener(View.OnClickListener {
            openFile()
        })

        SubirNew.setOnClickListener(View.OnClickListener {
            val Nom = NombreNew.getText().toString().trim { it <= ' ' }
            val Mat = MateriaNew.getText().toString().trim { it <= ' ' }
            val Des = DescripcionNew.getText().toString().trim { it <= ' ' }

            if (Nom.isEmpty() && Mat.isEmpty() && Des.isEmpty()) {
                Toast.makeText(this@ActualizarT, "Complete los campos", Toast.LENGTH_SHORT).show()
            } else {
                ActualizarTarea(dato, Nom, Mat, Des)
                progressDialog = ProgressDialog(this@ActualizarT)
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
        val reference = storageReference.child("Tareas/*" + NombreNew!!.text + ".pdf")
        reference.putFile(pdf_url!!).addOnSuccessListener (OnSuccessListener{
            try{
                reference.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri> { uri ->
                    Toast.makeText(this@ActualizarT, "Selección exitosa", Toast.LENGTH_SHORT)
                        .show()
                    URLNew.edit().putString(urlT.URLT, uri.toString()).commit()
                    progressDialog!!.dismiss()
                })
            }catch (e: Exception) {
                Toast.makeText(this@ActualizarT, "No se pudo seleccionar ese archivo", Toast.LENGTH_SHORT)
                    .show()
                progressDialog!!.dismiss()
            }
        }).addOnFailureListener {
            Toast.makeText(
                this@ActualizarT,
                "Error al cargar",
                Toast.LENGTH_SHORT
            ).show()
        }.addOnProgressListener { snapshot ->
            val progress = 100.0 * snapshot.bytesTransferred / snapshot.totalByteCount
            progressDialog.setMessage("Cargando" + " " + progress.toInt() + "%")
        }
    }


   private fun ActualizarTarea(dato: Int?, nom: String, mat: String, des: String) {

       val miId = dato!!.toInt()
       val URLHNew = URLNew.getString(urlT.URLT, "Algo salio mal")
       this.minuevaT.newN = nom
       this.minuevaT.newM = mat
       this.minuevaT.newD = des
       this.minuevaT.newUrl = URLHNew.toString()

       if(siempre) {
           CoroutineScope(Dispatchers.IO).launch {
               val call = RetrofitClient.webService.actualizarTarea(miId, minuevaT)
               runOnUiThread {
                   if (call.isSuccessful) {
                       Toast.makeText(this@ActualizarT, call.body().toString(), Toast.LENGTH_SHORT)
                           .show()
                       limpiarObjeto()
                       val intent = Intent(this@ActualizarT, Menu::class.java)
                       startActivity(intent)
                       limpiarcampos()
                       finish()
                       progressDialog!!.dismiss()
                   } else {
                       Toast.makeText(this@ActualizarT, call.body().toString(), Toast.LENGTH_SHORT)
                           .show()
                   }
               }
           }
       }
   }

    private fun limpiarcampos() {
        NombreNew.setText("")
        MateriaNew.setText("")
        DescripcionNew.setText("")
    }

    private fun limpiarObjeto() {
        this.minuevaT.newN = ""
        this.minuevaT.newM = ""
        this.minuevaT.newD = ""
        this.minuevaT.newUrl = ""
    }
}
