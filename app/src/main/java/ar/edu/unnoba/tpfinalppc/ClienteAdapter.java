package ar.edu.unnoba.tpfinalppc;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;

import java.util.List;

import ar.edu.unnoba.tpfinalppc.Model.Cliente;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    private List<Cliente> clientes;

    public ClienteAdapter(List<Cliente> clientes){
        this.clientes = clientes;
    }

    public ClienteAdapter(){}

    public class ClienteViewHolder extends RecyclerView.ViewHolder {

        TextView descripcion, tipo;
        CardView cardView;
        ImageView img_view;
        Cliente cliente_mostrar;

        public ClienteViewHolder(View view){
            super(view);
            cardView = view.findViewById(R.id.card_view);
            img_view = view.findViewById(R.id.imageAnonima);
            descripcion = view.findViewById(R.id.descripcionWebService);
            tipo = view.findViewById(R.id.tipoWebService);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(img_view.getContext(), ClienteDetail.class);
                    Gson gson = new Gson();
                    String cliente_json = gson.toJson(cliente_mostrar);
                    intent.putExtra("myjson", cliente_json);
                    img_view.getContext().startActivity(intent);
                }
            });
        }

        public void setClienteDTO(Cliente cliente){
            this.cliente_mostrar = cliente;
        }

        public void updateUI(){
            descripcion.setText(cliente_mostrar.getDescripcion());
            tipo.setText(cliente_mostrar.getTipo());
            img_view.setImageResource(cliente_mostrar.getReferenceImage());
        }

    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_clientes, parent, false);
        return new ClienteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        holder.setClienteDTO(clientes.get(position));
        holder.updateUI();
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

}
