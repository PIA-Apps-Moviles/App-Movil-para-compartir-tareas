package com.jahir.pia_appsmov.usescases.MenuFragments.misTareas

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jahir.pia_appsmov.usescases.compartirTarea.Agregar_Tarea
import com.jahir.pia_appsmov.Model.datosTareaLocal.Datos_PDF_2
import com.jahir.pia_appsmov.R
import com.jahir.pia_appsmov.usescases.MenuFragments.misTareas.misTareasAdapter.HAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [MisTareas.newInstance] factory method to
 * create an instance of this fragment.
 */
class MisTareas : Fragment() {
    lateinit var MisTareas: ListView

    //EditText Apodo_User;
    //Button Btn_Apodo;
    lateinit var preferences: SharedPreferences
    lateinit var databaseReference: DatabaseReference
    lateinit var datosMisTAREAS: MutableList<Datos_PDF_2?>

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
        MisTareas = root.findViewById(R.id.MisTareas)
        datosMisTAREAS = ArrayList()
        User?.let { VerMisArchivos(it) }
        MisTareas.setOnItemClickListener(OnItemClickListener { adapterView, view, i, l ->
            val pdfTarea = datosMisTAREAS.get(i)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(pdfTarea!!.url), "application/pdf")
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
        })
        Add.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, Agregar_Tarea::class.java)
            startActivity(intent)
        })
        return root
    }

    private fun VerMisArchivos(user: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference(user)
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postsnapshot in snapshot.children) {
                    val homework = postsnapshot.getValue(Datos_PDF_2::class.java)
                    datosMisTAREAS!!.add(homework)
                }
                val NT = arrayOfNulls<String>(datosMisTAREAS!!.size)
                val MT = arrayOfNulls<String>(datosMisTAREAS!!.size)
                val DT = arrayOfNulls<String>(datosMisTAREAS!!.size)
                val CT = arrayOfNulls<String>(datosMisTAREAS!!.size)
                for (i in NT.indices) {
                    NT[i] = "Nombre:  " + datosMisTAREAS!![i]!!.nombre
                }
                for (i in MT.indices) {
                    MT[i] = "Materia:  " + datosMisTAREAS!![i]!!.materia
                }
                for (i in DT.indices) {
                    DT[i] = "Descripción:  " + datosMisTAREAS!![i]!!.descripcion
                }
                for (i in CT.indices) {
                    CT[i] = "Carrera:  " + datosMisTAREAS!![i]!!.carrera
                }
                val adapter =
                    HAdapter(
                        context,
                        NT,
                        MT,
                        DT,
                        CT
                    )
                MisTareas!!.adapter = adapter

                /*
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, Datos2) {
                            @NonNull
                            @Override
                            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);

                                TextView texto = (TextView) view.findViewById(android.R.id.text1);
                                texto.setTextColor(Color.BLACK);
                                texto.setTextSize(22);
                                texto.setTypeface(null, Typeface.BOLD_ITALIC);
                                return view;
                            }
                        };
                        MisTareas.setAdapter(adapter);*/
            }

            override fun onCancelled(error: DatabaseError) {}
        })
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
}