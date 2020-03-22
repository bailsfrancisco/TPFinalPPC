package ar.edu.unnoba.tpfinalppc;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Double lat_c1;
    Double long_c1;
    String descripcion_c1;
    Double lat_c2;
    Double long_c2;
    String descripcion_c2;
    Double lat_c3;
    Double long_c3;
    String descripcion_c3;

    Double lat;
    Double longi;
    String desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        lat_c1 = extras.getDouble("latitud_c1");
        long_c1 = extras.getDouble("longitud_c1");
        descripcion_c1 = extras.getString("desc_c1");
        lat_c2 = extras.getDouble("latitud_c2");
        long_c2 = extras.getDouble("longitud_c2");
        descripcion_c2 = extras.getString("desc_c2");
        lat_c3 = extras.getDouble("latitud_c3");
        long_c3 = extras.getDouble("longitud_c3");
        descripcion_c3 = extras.getString("desc_c3");

        lat = extras.getDouble("lat");
        longi = extras.getDouble("long");
        desc = extras.getString("desc");

    }

    //Para visualizar el mapa por cliente
    public void clienteDetailMap(){
        LatLng c1 = new LatLng(lat, longi);
        mMap.addMarker(new MarkerOptions().position(c1).title(desc)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_map));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(c1, 13));
    }

    //Para ver mapa con todos los clientes
    public void clientesMap(){
        //Republica 221 Junin
        LatLng cliente1 = new LatLng(lat_c1, long_c1);
        mMap.addMarker(new MarkerOptions().position(cliente1).title(descripcion_c1)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_map));

        //Rivadavia 1470 Junin
        LatLng cliente2 = new LatLng(lat_c2, long_c2);
        mMap.addMarker(new MarkerOptions().position(cliente2).title(descripcion_c2)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_map));

        //Parque Industrial Junin
        LatLng cliente3 = new LatLng(lat_c3, long_c3);
        mMap.addMarker(new MarkerOptions().position(cliente3).title(descripcion_c3)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_map));

        //que se mueva por defecto a tal direccion y agregue zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cliente1, 13));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        clientesMap();
        clienteDetailMap();

    }
}
