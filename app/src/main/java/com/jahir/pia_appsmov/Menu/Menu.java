package com.jahir.pia_appsmov.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.jahir.pia_appsmov.IS_R.InicioSesion;
import com.jahir.pia_appsmov.MiCuenta;
import com.jahir.pia_appsmov.R;

public class Menu extends AppCompatActivity {

    private Toolbar Toolbar1;
    private BottomNavigationView Navegacion;

    //Button BtnCS;
    FirebaseAuth mAuth;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar1 = findViewById(R.id.Toolbar1);
        Navegacion = findViewById(R.id.Navegacion);
        Toolbar1.setTitle("Mis Tareas");

        setSupportActionBar(Toolbar1);

        getSupportFragmentManager().beginTransaction().add(R.id.Frame1, new MisTareas()).commit();

        Navegacion.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){

                    case R.id.menuUsuario:
                        getSupportFragmentManager().beginTransaction().replace(R.id.Frame1, new MisTareas()).commit();
                        Toolbar1.setTitle("Comparte tu tarea");
                        return true;
                    case R.id.menuUsuarios:
                        getSupportFragmentManager().beginTransaction().replace(R.id.Frame1, new Buscar()).commit();
                        Toolbar1.setTitle("Buscar Tareas");
                        return true;
                    case R.id.menuTareas:
                        getSupportFragmentManager().beginTransaction().replace(R.id.Frame1, new Inicio()).commit();
                        Toolbar1.setTitle("Tareas subidas por otros usuarios");
                        return true;
                }
                return false;

            }
        });

        /*BtnCS = findViewById(R.id.BtnCS);
        mAuth = FirebaseAuth.getInstance();
        BtnCS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(Menu.this, MainActivity.class));
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menuopciones,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int nro=item.getItemId();

        switch(nro){
            case R.id.Cuenta:
                startActivity(new Intent(Menu.this, MiCuenta.class));
                return true;
            case R.id.CerrarSesion:
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                finish();
                startActivity(new Intent(Menu.this, InicioSesion.class));
                return true;
            /*case R.id.Salir:
                System.exit(0);
                return true;*/
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}