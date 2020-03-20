package ar.edu.unnoba.tpfinalppc;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.List;

import ar.edu.unnoba.tpfinalppc.Model.Cliente;
import ar.edu.unnoba.tpfinalppc.Utils.Session;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Session session;
    private RequestQueue requestQueue;

    //NUEVO
    Gson gson;
    String url = "http://ppc.edit.com.ar:8080/resources/datos/deudas/-34.581727/-60.931513";
    List<Cliente> clientes;
    RecyclerView listado_clientesRecycler;
    ClienteAdapter clienteAdapter;
    ProgressBar progressBar;
    boolean local = false;
    public static final String TAG = MainActivity.class.getSimpleName();

    //PRUEBA
    private TextView textView;

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

        //requestQueue = Volley.newRequestQueue(this);

        //NUEVO
        progressBar = findViewById(R.id.loading);
        listado_clientesRecycler = findViewById(R.id.recycler);
        requestQueue = Volley.newRequestQueue(this);
        gson = new Gson();

        //PRUEBA
        /*textView = findViewById(R.id.textView);
        Button buttonGET = (Button) findViewById(R.id.buttonGET);

        buttonGET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });*/

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

    private void cargar_clientesWS(){
        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest json_request = new JsonArrayRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG,"Se obtuvo una conexion exitosa con el webService en "+url);
                        local=false;
                        llenar_lista(response);
                        progressBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (!local) {
                    Log.e(TAG, "Error al conectar con el webService, revertiendo a datos locales");
                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(android.R.drawable.stat_notify_error)
                            .setTitle("Error")
                            .setMessage("No es posible conectar con el web service para actualizar, se utilizaran datos locales (podrian no estar actualizados) \n La distancia NO puede calcularse en base a datos locales")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    local=true;
                                    llenar_lista(null);
                                    progressBar.setVisibility(View.GONE);
                                }

                            })
                            .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    session.borrarSP();
                                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(i);
                                }

                            })
                            .show();
                }
            }
        });

        requestQueue.add(json_request);
    }

    private void llenar_lista(JSONArray source){
        if(source == null){
            Toast.makeText(this,"Error de conexion.", Toast.LENGTH_SHORT);
        }
        else{
            Toast.makeText(this,"Conexion exitosa.", Toast.LENGTH_SHORT);
        }
        if (clientes != null && !clientes.isEmpty()) {

            clientes.get(0).setReferenceImage(R.drawable.cliente_la_anonima);
            clientes.get(1).setReferenceImage(R.drawable.cliente_garbarino);
            clientes.get(2).setReferenceImage(R.drawable.cliente_sistema_riego);
            clienteAdapter = new ClienteAdapter(clientes);
            listado_clientesRecycler.setAdapter(clienteAdapter);
        }
    }


    /*private void jsonParse () {

        String URL = "http://ppc.edit.com.ar:8080/resources/datos/clientes/-34.581727/-60.931513";


        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("REST response", response.toString());
                        try {
                            //JSONObject jsonArray = response.getJSONObject(0);

                            String json = "";
                            json = response.toString();
                            JSONArray jsonArray = new JSONArray(json);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String descripcion = jsonObject.getString("descripcion");
                                Double latitud = jsonObject.getDouble("latitud");
                                Double longitud = jsonObject.getDouble("longitud");
                                String domicilio = jsonObject.getString("domicilio");
                                String telefono = jsonObject.getString("telefono");
                                int valor = jsonObject.getInt("valor");
                                String detalle = jsonObject.getString("detalle");
                                String tipo = jsonObject.getString("tipo");
                                Double distancia = jsonObject.getDouble("distancia");

                                textView.append(descripcion + ", " + String.valueOf(latitud) + ", " + String.valueOf(longitud) + ", " + domicilio +"\n\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("REST response", error.toString());
                        error.printStackTrace();
                    }
                });

        requestQueue.add(objectRequest);

    }*/

}
