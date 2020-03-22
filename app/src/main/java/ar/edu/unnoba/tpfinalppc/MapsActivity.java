package ar.edu.unnoba.tpfinalppc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

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

    Double lat_detail;
    Double long_detail;
    String desc_detail;

    private GoogleApiClient googleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(1000);

        Bundle extras = getIntent().getExtras();

        //todos los detalles
        lat_c1 = extras.getDouble("latitud_c1");
        long_c1 = extras.getDouble("longitud_c1");
        descripcion_c1 = extras.getString("desc_c1");
        lat_c2 = extras.getDouble("latitud_c2");
        long_c2 = extras.getDouble("longitud_c2");
        descripcion_c2 = extras.getString("desc_c2");
        lat_c3 = extras.getDouble("latitud_c3");
        long_c3 = extras.getDouble("longitud_c3");
        descripcion_c3 = extras.getString("desc_c3");

        //Detalle del cliente seleccionado
        lat_detail = extras.getDouble("lat");
        long_detail = extras.getDouble("long");
        desc_detail = extras.getString("desc");

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

        if(lat_c1 != 0.0 && lat_c2 != 0.0 && lat_c3 != 0.0){
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
        }else{
            LatLng c = new LatLng(lat_detail, long_detail);
            mMap.addMarker(new MarkerOptions().position(c).title(desc_detail)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_map));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(c, 15));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1800: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                }
                break;
            }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1800);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1);
            return;
        }
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        googleApiClient.disconnect();
    }
}
