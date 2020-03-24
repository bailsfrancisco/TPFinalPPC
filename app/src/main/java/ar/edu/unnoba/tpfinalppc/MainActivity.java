package ar.edu.unnoba.tpfinalppc;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import com.android.volley.ClientError;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import ar.edu.unnoba.tpfinalppc.Model.Cliente;
import ar.edu.unnoba.tpfinalppc.Utils.Session;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Session session;
    private RequestQueue requestQueue;
    private static final String URL = "http://ppc.edit.com.ar:8080/resources/datos/deudas/-34.581727/-60.931513";

    Gson gson;
    List<Cliente> clientes;
    RecyclerView listado_clientesRecycler;
    ClienteAdapter clienteAdapter;
    ProgressBar progressBar;

    private  float dist1;
    private float dist2;
    private  float dist3;

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

        clientes = new ArrayList<Cliente>();

        listado_clientesRecycler = findViewById(R.id.recycler); //RECYCLER
        requestQueue = Volley.newRequestQueue(this);
        gson = new Gson();

        jsonParse();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }

    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
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

        if (id == R.id.reconectar){
            recreate();
        }

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

        if (id == R.id.menuMapa) {
            //DATOS TRAIDOS DEL WS A TRAVES DE UN BUNDLE AL MAPA
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            intent.putExtra("latitud_c1",clientes.get(0).getLatitud());
            intent.putExtra("longitud_c1",clientes.get(0).getLongitud());
            intent.putExtra("desc_c1", clientes.get(0).getDescripcion());
            intent.putExtra("latitud_c2",clientes.get(1).getLatitud());
            intent.putExtra("longitud_c2",clientes.get(1).getLongitud());
            intent.putExtra("desc_c2", clientes.get(1).getDescripcion());
            intent.putExtra("latitud_c3",clientes.get(2).getLatitud());
            intent.putExtra("longitud_c3",clientes.get(2).getLongitud());
            intent.putExtra("desc_c3", clientes.get(2).getDescripcion());
            startActivity(intent);
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
            Log.e(TAG, "ERROR DE CONEXION");
        }
        if (clientes != null && !clientes.isEmpty()) {
            clientes.get(0).setImage(R.drawable.cliente_la_anonima);
            clientes.get(1).setImage(R.drawable.cliente_garbarino);
            clientes.get(2).setImage(R.drawable.cliente_sistema_riego);



            Cliente cl = clientes.get(0);
            Cliente cl1 = clientes.get(1);
            Cliente cl2 = clientes.get(2);
            clientes.get(0).setDistancia(dist1);
            clientes.get(1).setDistancia(dist2);
            clientes.get(2).setDistancia(dist3);

            List<Cliente> aux = clientes;

            Collections.sort(aux);

            Cliente c = aux.get(0);
            Cliente c1 = aux.get(1);
            Cliente c2 = aux.get(2);
            float x = aux.get(0).getDistancia();
            float y = aux.get(1).getDistancia();
            float z = aux.get(2).getDistancia();


            listado_clientesRecycler.setLayoutManager(new LinearLayoutManager(this));

            clienteAdapter = new ClienteAdapter(aux);

            //clienteAdapter.notifyDataSetChanged();

            listado_clientesRecycler.setAdapter(clienteAdapter);
        }
    }

    public void setLocation(Location loc) {
        LatLng location_usuario = null;
        LatLng location_cliente1 = null;
        LatLng location_cliente2 = null;
        LatLng location_cliente3 = null;
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Double lat = loc.getLatitude();
                    Double lon = loc.getLongitude();
                    location_usuario = new LatLng(lat, lon);

                    location_cliente1 = new LatLng(-34.580522,-60.930744);
                    location_cliente2 = new LatLng(-34.575407,-60.962179);
                    location_cliente3 = new LatLng(-34.556917,-60.919601);

                    dist1 = getDistance(location_usuario, location_cliente1);
                    dist2 = getDistance(location_usuario, location_cliente2);
                    dist3 = getDistance(location_usuario, location_cliente3);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private float getDistance(LatLng usuario, LatLng cliente){
        Location l1=new Location("One");
        l1.setLatitude(usuario.latitude);
        l1.setLongitude(usuario.longitude);

        Location l2=new Location("Two");
        l2.setLatitude(cliente.latitude);
        l2.setLongitude(cliente.longitude);

        float distance=l1.distanceTo(l2);

        return distance;
    }

    public class Localizacion implements LocationListener {

        MainActivity mainActivity;

        public MainActivity getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas debido a la deteccion de un cambio de ubicacion del dispositivo
            loc.getLatitude();
            loc.getLongitude();
            this.mainActivity.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "GPS Desactivado.");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "GPS Activado.");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

}