package com.jahir.pia_appsmov.usescases.MenuFragments.TodasTareas

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jahir.pia_appsmov.R
import com.jahir.pia_appsmov.usescases.VerInfoTarea.VerInfoT
import com.jahir.pia_appsmov.usescases.compartirTarea.Tarea
import com.jahir.pia_appsmov.usescases.registro.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [Inicio.newInstance] factory method to
 * create an instance of this fragment.
 */
class Inicio : Fragment(), TGAdapter.OnItemClicked {

    lateinit var TareasAdapter: TGAdapter
    var TG = arrayListOf<Tarea>()
    var mT = Tarea(-1,"","","","","")
    var siempreT = true

    lateinit var TareasG: RecyclerView

    lateinit var progressDialog: ProgressDialog

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

    //private FloatingActionButton Add;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_inicio, container, false)
        TareasG = root.findViewById(R.id.TareasGlobales)

        TareasG.layoutManager = LinearLayoutManager(activity)
        setupRecyclerView()
        verTareas()
        return root
    }

    fun verTareas(){
        try{
            if(siempreT){
            CoroutineScope(Dispatchers.IO).launch {
                val call = RetrofitClient.webService.tareasGlobales()
                activity?.runOnUiThread {
                    if (call.isSuccessful) {
                        TG = call.body()!!.misT
                        setupRecyclerView()
                    } else {
                        Toast.makeText(activity, call.body().toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            }}catch(e: Exception){
            Toast.makeText(activity, "Algo salio mal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        TareasAdapter =  TGAdapter(this, TG)
        TareasAdapter.setOnClick(this@Inicio)
        TareasG.adapter = TareasAdapter
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
         * @return A new instance of fragment Inicio.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): Inicio {
            val fragment = Inicio()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
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