package com.jahir.pia_appsmov.usescases.MenuFragments

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jahir.pia_appsmov.Model.datosTareaGlobal.Datos_PDF
import com.jahir.pia_appsmov.R

/**
 * A simple [Fragment] subclass.
 * Use the [Inicio.newInstance] factory method to
 * create an instance of this fragment.
 */
class Inicio : Fragment() {
    //RecyclerView mRecycler;
    //TareaAdapter mAdapter;
    //FirebaseFirestore mFirestore;
    lateinit var TareasSubidas: ListView
    lateinit var databaseReference: DatabaseReference
    lateinit var datos: MutableList<Datos_PDF?>

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

        /*mFirestore = FirebaseFirestore.getInstance();
        mRecycler = root.findViewById(R.id.TareasSubidas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(linearLayoutManager);
        Query query = mFirestore.collection("Tarea");
        FirestoreRecyclerOptions<Tareas> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Tareas>().setQuery(query, Tareas.class).build();
        mAdapter = new TareaAdapter(firestoreRecyclerOptions, Inicio.this);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);*/

        //Add = root.findViewById(R.id.Add);
        TareasSubidas = root.findViewById(R.id.TareasSubidas)
        datos = ArrayList()
        VerArchivos()
        TareasSubidas.setOnItemClickListener(OnItemClickListener { adapterView, view, i, l ->
            val pdfTarea = datos.get(i)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.type = "application/pdf"
            intent.data = Uri.parse(pdfTarea!!.url)
            startActivity(intent)
        })


        /*Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), Agregar_Tarea.class);
                startActivity(intent);
            }
        });*/return root
    }

    private fun VerArchivos() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Tareas")
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postsnapshot in snapshot.children) {
                    val homework = postsnapshot.getValue(Datos_PDF::class.java)
                    datos!!.add(homework)
                }
                val Datos = arrayOfNulls<String>(datos!!.size)
                for (i in Datos.indices) {
                    Datos[i] = "Nombre:  " + datos!![i]!!
                        .nombre + "\n" + "Materia:  " + datos!![i]!!
                        .materia + "\n" + "Descripción:  " + datos!![i]!!
                        .descripcion + "\n" + "Carrera:  " + datos!![i]!!.carrera + "\n" + "\t"
                }
                try {
                    val adapter: ArrayAdapter<String?> = object : ArrayAdapter<String?>(
                        activity!!,
                        android.R.layout.simple_list_item_activated_1, Datos
                    ) {
                        override fun getView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val view = super.getView(position, convertView, parent)
                            val texto = view.findViewById<View>(android.R.id.text1) as TextView
                            texto.setTextColor(Color.BLACK)
                            texto.textSize = 22f
                            texto.setTypeface(null, Typeface.BOLD_ITALIC)
                            return view
                        }
                    }
                    TareasSubidas!!.adapter = adapter
                } catch (e: Exception) {
                    //Toast.makeText(getActivity(), "Cargando ...", Toast.LENGTH_SHORT).show();
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    } /*@Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }*/

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
}