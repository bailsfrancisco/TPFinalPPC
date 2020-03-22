package ar.edu.unnoba.tpfinalppc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import ar.edu.unnoba.tpfinalppc.Model.Cliente;

public class ClienteDetail extends AppCompatActivity {

    TextView descripcion,detalle,distancia,domicilio, latitud, longitud,telefono,valor, tipo;
    ImageView img_anonima;
    Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_details);

        descripcion = findViewById(R.id.descripcionWebService);
        detalle = findViewById(R.id.detalleWebService);
        distancia = findViewById(R.id.distanciaWebService);
        domicilio = findViewById(R.id.domicilioWebService);
        //latitud = findViewById(R.id.latitudWebService);
        //longitud = findViewById(R.id.longitudWebService);
        telefono = findViewById(R.id.telefonoWebService);
        valor = findViewById(R.id.valorWebService);
        tipo = findViewById(R.id.tipoWebService);
        img_anonima = findViewById(R.id.imageAnonima);

        Gson gson = new Gson();
        cliente = gson.fromJson(getIntent().getStringExtra("myjson"), Cliente.class);

        img_anonima.setImageResource(cliente.getImage());
        descripcion.setText(cliente.getDescripcion());
        detalle.setText(cliente.getDetalle());
        if(cliente.getDistancia()>1000){
            distancia.setText(String.format("%.1f",cliente.getDistancia()/1000)+" Km");
        }else {
            distancia.setText(String.format("%.1f",cliente.getDistancia()) + " m");
        }
        domicilio.setText(cliente.getDomicilio());
        //latitud.setText(String.valueOf(cliente.getLatitud()));
        //longitud.setText(String.valueOf(cliente.getLongitud()));
        telefono.setText("+"+cliente.getTelefono());
        valor.setText(String.valueOf(cliente.getValor()));
        tipo.setText(cliente.getTipo());

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
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("lat", cliente.getLatitud());
                intent.putExtra("long", cliente.getLongitud());
                intent.putExtra("desc", cliente.getDescripcion());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
