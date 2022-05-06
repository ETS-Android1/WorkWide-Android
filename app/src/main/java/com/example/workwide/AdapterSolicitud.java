package com.example.workwide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workwide.modelo.Solicitud;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class AdapterSolicitud extends RecyclerView.Adapter<AdapterSolicitud.ViewHolder> {
    ArrayList<Solicitud> lista;
    int tipo;

    public AdapterSolicitud(ArrayList<Solicitud> lista, int tipo){
        this.lista = lista;
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_requests, null, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nombre.setText(tipo == 1 ? lista.get(position).getNombreReceptor() : lista.get(position).getNombreEmisor());
        holder.titulo.setText(lista.get(position).getTitulo());
        holder.estado.setText(lista.get(position).getEstado());
        holder.descripcion.setText(lista.get(position).getDescripcion().replace("<br>", "\n"));
        holder.inicio.setText(lista.get(position).getInicio().toString());
        holder.fin.setText(lista.get(position).getFin().toString());

        int pos = position;

        if(tipo == 1){
            holder.aceptar.setEnabled(false);
            holder.rechazar.setEnabled(false);
            holder.aceptar.setVisibility(View.INVISIBLE);
            holder.rechazar.setVisibility(View.INVISIBLE);
        }

        holder.aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == holder.aceptar.getId()){
                    holder.aceptar.setEnabled(false);
                    holder.rechazar.setEnabled(false);
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();

                    params.put("accion", "2");
                    params.put("id", lista.get(pos).getIdSoli());

                    client.post("http://192.168.0.11:8080/WorkWide/evaluarSolicitudesAndroid", params, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);

                            holder.aceptar.setEnabled(false);
                            holder.rechazar.setEnabled(false);
                            holder.aceptar.setVisibility(View.INVISIBLE);
                            holder.rechazar.setVisibility(View.INVISIBLE);

                            holder.estado.setText("Aceptada");
                        }
                    });
                }
            }
        });

        holder.rechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == holder.rechazar.getId()){
                    holder.aceptar.setEnabled(false);
                    holder.rechazar.setEnabled(false);
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();

                    params.put("accion", "3");
                    params.put("id", lista.get(pos).getIdSoli());

                    client.post("http://192.168.0.11:8080/WorkWide/evaluarSolicitudesAndroid", params, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);

                            holder.aceptar.setEnabled(false);
                            holder.rechazar.setEnabled(false);
                            holder.aceptar.setVisibility(View.INVISIBLE);
                            holder.rechazar.setVisibility(View.INVISIBLE);

                            holder.estado.setText("Rechazada");
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, titulo, descripcion, inicio, fin, estado;
        Button aceptar, rechazar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            titulo = itemView.findViewById(R.id.titulo);
            descripcion = itemView.findViewById(R.id.descripcion);
            inicio = itemView.findViewById(R.id.inicio);
            fin = itemView.findViewById(R.id.fin);
            estado = itemView.findViewById(R.id.estado);

            aceptar = itemView.findViewById(R.id.aceptar);
            rechazar = itemView.findViewById(R.id.rechazar);
        }
    }

}
