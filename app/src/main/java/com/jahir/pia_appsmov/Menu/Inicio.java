package com.jahir.pia_appsmov.Menu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jahir.pia_appsmov.Model.Datos_PDF;
import com.jahir.pia_appsmov.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Inicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Inicio extends Fragment {

    //RecyclerView mRecycler;
    //TareaAdapter mAdapter;
    //FirebaseFirestore mFirestore;
    ListView TareasSubidas;
    DatabaseReference databaseReference;
    List<Datos_PDF> datos;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Inicio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Inicio.
     */
    // TODO: Rename and change types and number of parameters
    public static Inicio newInstance(String param1, String param2) {
        Inicio fragment = new Inicio();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //private FloatingActionButton Add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);

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
        TareasSubidas = root.findViewById(R.id.TareasSubidas);
        datos= new ArrayList<>();

        VerArchivos();

        TareasSubidas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Datos_PDF pdfTarea = datos.get(i);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("application/pdf");
                intent.setData(Uri.parse(pdfTarea.getUrl()));
                startActivity(intent);

            }
        });


        /*Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), Agregar_Tarea.class);
                startActivity(intent);
            }
        });*/
        return root;
    }

    private void VerArchivos() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Tareas");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postsnapshot: snapshot.getChildren()){
                    Datos_PDF homework = postsnapshot.getValue(Datos_PDF.class);
                    datos.add(homework);
                }

                String[] Datos = new String[datos.size()];
                for(int i=0; i<Datos.length; i++){
                    Datos[i] = "Nombre:  " + datos.get(i).getNombre()  + "\n" + "Materia:  " + datos.get(i).getMateria() + "\n" + "Descripción:  " + datos.get(i).getDescripcion() + "\n"  + "Carrera:  " + datos.get(i).getCarrera() + "\n" + "\t";
                }
                try{
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_activated_1, Datos){
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view = super.getView(position, convertView,parent);

                            TextView texto = (TextView) view.findViewById(android.R.id.text1);
                            texto.setTextColor(Color.BLACK);
                            texto.setTextSize(22);
                            texto.setTypeface(null, Typeface.BOLD_ITALIC);

                            return view;
                        }
                    };
                    TareasSubidas.setAdapter(adapter);
                }catch (Exception e){
                    //Toast.makeText(getActivity(), "Cargando ...", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*@Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }*/
}