package ar.edu.unnoba.tpfinalppc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

import ar.edu.unnoba.tpfinalppc.Model.Cliente;
import ar.edu.unnoba.tpfinalppc.Utils.Session;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Session session;
    private RequestQueue requestQueue;
    private static final String URL = "http://ppc.edit.com.ar:8080/resources/datos/clientes/-34.581727/-60.931513";

    Gson gson;
    List<Cliente> clientes; //DATASET FOR RECYCLER
    RecyclerView listado_clientesRecycler; //RECYCLER
    ClienteAdapter clienteAdapter;
    ProgressBar progressBar;
    //RecyclerView.LayoutManager layoutManager;

    boolean local = false;
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        session = new Session(this);
        if(!session.loggedin()){
            logout();
        }
        View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);
        ((TextView) header.findViewById(R.id.textViewUserLog)).setText(session.mostrarDatos());




        progressBar = findViewById(R.id.loading);

        listado_clientesRecycler = findViewById(R.id.recycler); //RECYCLER
        requestQueue = Volley.newRequestQueue(this);
        gson = new Gson();

        jsonParse();

    }

    private void logout(){
        session.setLoggedin(false);
        //borra la shared preferences al salir de la app
        session.borrarSP();
        finish();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.about) {
            Intent i = new Intent(this, InfoActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menuReconectar) {
            recreate();
        }

        if (id == R.id.menuDirecciones) {

        }

        if (id == R.id.menuMapa) {

        }

        if (id == R.id.menuSalir) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void jsonParse () {

        requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest json_request = new JsonArrayRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG,"CONEXION EXITOSA: "+URL);
                        local=false;
                        llenar_lista(response);
                        progressBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (!local) {
                    Log.e(TAG, "ERROR DE CONEXION");
                }
            }
        });

        requestQueue.add(json_request);
    }

    private void llenar_lista(JSONArray source){
        if (source!=null) {
            clientes = Arrays.asList(gson.fromJson(source.toString(), Cliente[].class));
        }else {
            Log.e(TAG, "Error de conexion - USANDO LA BASE DE DATOS LOCAL");
        }
        if (clientes != null && !clientes.isEmpty()) {
            //el json que provee el web service deberia contener un campo con un enlace a un recurso web que ilustre la obra
            clientes.get(0).setImage(R.drawable.cliente_la_anonima);
            clientes.get(1).setImage(R.drawable.cliente_garbarino);
            clientes.get(2).setImage(R.drawable.cliente_sistema_riego);
            clienteAdapter = new ClienteAdapter(clientes);
            listado_clientesRecycler.setAdapter(clienteAdapter);
        }
    }

}