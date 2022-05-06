package com.example.workwide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workwide.modelo.Trabajo;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<Trabajo> dataSet;
    private int tipo;

    public Adapter(ArrayList<Trabajo> data, int tipo){
        this.dataSet = data;
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trabajos_temp, null, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nombre.setText(tipo == 1 ? dataSet.get(position).getNombreEpleado() : dataSet.get(position).getNombreEpleador());
        holder.titulo.setText(dataSet.get(position).getTitulo());
        holder.descripcion.setText(dataSet.get(position).getDescripcion());
        holder.fin.setText(dataSet.get(position).getFechaFin().toString());
        holder.inicio.setText(dataSet.get(position).getFechaInicio().toString());


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, titulo, descripcion, inicio, fin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.nombre);
            titulo = itemView.findViewById(R.id.titulo);
            descripcion = itemView.findViewById(R.id.descripcion);
            inicio = itemView.findViewById(R.id.inicio);
            fin = itemView.findViewById(R.id.fin);
        }
    }
}
