package ar.edu.unnoba.tpfinalppc;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ar.edu.unnoba.tpfinalppc.Model.Cliente;

public class ClienteDetail extends AppCompatActivity  implements OnMapReadyCallback {

    private static final String TAG = ClienteDetail.class.getSimpleName();

    TextView descripcion,detalle,distancia,domicilio, telefono,valor, tipo;
    ImageView img_cliente;
    Cliente cliente;

    private GoogleMap googleMap;
    private MapView mMapView;

    private LatLng location_cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_details);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }

        descripcion = findViewById(R.id.descripcionWebService);
        detalle = findViewById(R.id.detalleWebService);
        distancia = findViewById(R.id.distanciaWebService);
        domicilio = findViewById(R.id.domicilioWebService);
        telefono = findViewById(R.id.telefonoWebService);
        valor = findViewById(R.id.valorWebService);
        tipo = findViewById(R.id.tipoWebService);
        img_cliente = findViewById(R.id.imageCliente);

        Gson gson = new Gson();
        cliente = gson.fromJson(getIntent().getStringExtra("myjson"), Cliente.class);

        location_cliente = new LatLng(cliente.getLatitud(), cliente.getLongitud());

        img_cliente.setImageResource(cliente.getImage());
        descripcion.setText(cliente.getDescripcion());
        detalle.setText(cliente.getDetalle());

        domicilio.setText(cliente.getDomicilio());
        telefono.setText("+"+cliente.getTelefono());
        valor.setText(String.valueOf(cliente.getValor()));
        tipo.setText(cliente.getTipo());

        mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
    }

    private float getDistance(LatLng usuario,LatLng cliente){
        Location l1=new Location("One");
        l1.setLatitude(usuario.latitude);
        l1.setLongitude(usuario.longitude);

        Location l2=new Location("Two");
        l2.setLatitude(cliente.latitude);
        l2.setLongitude(cliente.longitude);

        float distance=l1.distanceTo(l2);

        return distance;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_cliente_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuDirecciones:
                //Obtener direcciones
                String uri = "http://maps.google.com/maps?daddr=" + cliente.getLatitud() + "," + cliente.getLongitud() + " (" + cliente.getDescripcion() + ")";
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                i.setPackage("com.google.android.apps.maps");
                startActivity(i);
                return true;
                //Ver mapa una vez seleccionado el cliente
            case R.id.menuMapa:
                viewMapOnClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void viewMapOnClick(){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("lat", cliente.getLatitud());
        intent.putExtra("long", cliente.getLongitud());
        intent.putExtra("desc", cliente.getDescripcion());
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng c = new LatLng(cliente.getLatitud(), cliente.getLongitud());
        googleMap.addMarker(new MarkerOptions().position(c).title(cliente.getDescripcion())).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_map));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(c, 15));
        //bloquear movimiento de map view
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        //abrir mapa al tocar el map view
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                viewMapOnClick();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setClienteDetail(this);
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

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    public void setLocation(Location loc) {
        LatLng location_usuario = null;
        float dist;
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Double lat = loc.getLatitude();
                    Double lon = loc.getLongitude();

                    location_usuario = new LatLng(lat, lon);

                    dist = getDistance(location_usuario, location_cliente);
                    if(dist>1000){
                        distancia.setText(String.format("%.1f",dist/1000)+" Km");
                    }else {
                        distancia.setText(String.format("%.1f",dist) + " m");
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class Localizacion implements LocationListener {

        ClienteDetail clienteDetail;

        public ClienteDetail getClienteDetail() {
            return clienteDetail;
        }

        public void setClienteDetail(ClienteDetail clienteDetail) {
            this.clienteDetail = clienteDetail;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas debido a la deteccion de un cambio de ubicacion del dispositivo
            loc.getLatitude();
            loc.getLongitude();
            this.clienteDetail.setLocation(loc);
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
