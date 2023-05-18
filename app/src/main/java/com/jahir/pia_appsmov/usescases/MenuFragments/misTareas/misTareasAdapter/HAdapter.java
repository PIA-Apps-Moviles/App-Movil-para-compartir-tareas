package com.jahir.pia_appsmov.usescases.MenuFragments.misTareas.misTareasAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jahir.pia_appsmov.R;

public class HAdapter extends BaseAdapter  {

    private Context context;
    private String [] NT;
    private String [] MT;
    private String [] DT;
    private String [] CT;
    private static LayoutInflater inflater = null;

    public HAdapter() {
    }

    public HAdapter(Context context, String[] NT, String[] MT, String[] DT, String[] CT) {
        this.context = context;
        this.NT = NT;
        this.MT = MT;
        this.DT = DT;
        this.CT = CT;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return NT.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista = inflater.inflate(R.layout.tarea_item,null);

        TextView Nombre_T = (TextView) vista.findViewById(R.id.N);
        TextView Materia_T = (TextView) vista.findViewById(R.id.M);
        TextView Des_T = (TextView) vista.findViewById(R.id.D);
        TextView Carrera_T = (TextView) vista.findViewById(R.id.C);
        ImageView imagen_C = (ImageView) vista.findViewById(R.id.img_C);

        Nombre_T.setText(NT[i]);
        Materia_T.setText(MT[i]);
        Des_T.setText(DT[i]);
        Carrera_T.setText(CT[i]);

        return vista;
    }
}
