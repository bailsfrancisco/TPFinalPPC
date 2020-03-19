package ar.edu.unnoba.tpfinalppc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.tpfinalppc.R;

import ar.edu.unnoba.tpfinalppc.Utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Session session;

    private RequestQueue requestQueue;
    //private TextView tvContent;
    private TextView textView;
    private ListView listView;
    private String clientes [] = {};

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

        //LLENAR LIST VIEW

        textView = (TextView) findViewById(R.id.textView);
        listView = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_view_item_content_main, clientes);
        listView.setAdapter(adapter);

        //PARA CUANDO SE SELECCIONA UN CLIENTE
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText("Se selecciono " + listView.getItemAtPosition(position));
            }
        });*/

        //tvContent = (TextView) findViewById(R.id.textViewContent);
        //Button btnGet = (Button) findViewById(R.id.buttonGet);

        /*btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });*/
        requestQueue = Volley.newRequestQueue(this);
    }


    //Trae los datos del WebService
    private void jsonParse () {

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

                                //tvContent.append(descripcion + ", " + String.valueOf(latitud) + ", " + String.valueOf(longitud) + ", " + domicilio +"\n\n");
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

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_salir) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
