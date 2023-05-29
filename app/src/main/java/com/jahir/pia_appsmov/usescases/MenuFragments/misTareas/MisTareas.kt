package com.jahir.pia_appsmov.usescases.MenuFragments.misTareas

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jahir.pia_appsmov.usescases.compartirTarea.Agregar_Tarea
import com.jahir.pia_appsmov.R
import com.jahir.pia_appsmov.usescases.VerInfoTarea.VerInfoT
import com.jahir.pia_appsmov.usescases.actualizarT.ActualizarT
import com.jahir.pia_appsmov.usescases.compartirTarea.Tarea
import com.jahir.pia_appsmov.usescases.registro.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [MisTareas.newInstance] factory method to
 * create an instance of this fragment.
 */
class MisTareas : Fragment(), HAdpater.OnItemClicked {

    lateinit var HAdpater: HAdpater
    var misT = arrayListOf<Tarea>()
    var mT = Tarea(-1,"","","","","")
    var siempreT = true

    lateinit var MisTareas: RecyclerView

    lateinit var progressDialog: ProgressDialog

    lateinit var preferences: SharedPreferences


    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    lateinit private var Add: FloatingActionButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val root = inflater.inflate(R.layout.fragment_mistareas, container, false)
        Add = root.findViewById(R.id.Add)
        preferences = this.requireActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val User = preferences.getString("user", null)
        MisTareas = root.findViewById(R.id.misTareas)

        MisTareas.layoutManager = LinearLayoutManager(activity)
        setupRecyclerView(User)
        vermisTareas(User)

        Add.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, Agregar_Tarea::class.java)
            startActivity(intent)
        })
        return root
    }

    fun vermisTareas(user: String?){
        try{
            if(siempreT){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.misTareas(user.toString())
            activity?.runOnUiThread {
                if (call.isSuccessful) {
                    misT = call.body()!!.misT
                    setupRecyclerView(user)
                } else {
                    Toast.makeText(activity, call.body().toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
        }}catch(e: Exception){
            Toast.makeText(activity, "Algo salio mal",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView(user: String?) {
        HAdpater =  HAdpater(this, misT, user)
        HAdpater.setOnClick(this@MisTareas)
        MisTareas.adapter = HAdpater
    }


    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MisTareas.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): MisTareas {
            val fragment = MisTareas()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun editarTarea(tarea: Tarea) {
        val intent = Intent(context, ActualizarT::class.java)
        intent.putExtra("idTarea", tarea.idUsuario)
        startActivity(intent)
    }

    override fun borrarTarea(idUsuario: Int, miUser: String?) {
        CoroutineScope(Dispatchers.IO).launch {
                val call = RetrofitClient.webService.eliminartarea(idUsuario)
                activity?.runOnUiThread{
                    if(call.isSuccessful){
                        Toast.makeText(activity, call.body().toString(),Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                        vermisTareas(miUser)
                    }else{
                        Toast.makeText(activity, call.body().toString(),Toast.LENGTH_SHORT).show()
                    }
                }
         }
        progressDialog = ProgressDialog(activity)
        progressDialog!!.show()
        progressDialog!!.setContentView(R.layout.pantalla_de_carga)
        progressDialog!!.window!!.setBackgroundDrawableResource(
            android.R.color.transparent)
    }

    override fun verPDF(urlT: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(urlT), "application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            requireActivity().startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                activity,
                "No existe una aplicación para abrir el PDF",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun verInfo(tarea: Tarea) {
        val intent = Intent(context, VerInfoT::class.java)
        intent.putExtra("Usuario", tarea.userName)
        intent.putExtra("NombreT", tarea.nameT)
        intent.putExtra("MateriaT",tarea.materiaT)
        intent.putExtra("DesT", tarea.DesH)
        startActivity(intent)
    }
}