package com.example.deberesvm;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Tarea> tareas;
    private OnItemClickListener onItemClickListener;

    public Adapter(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    public interface OnItemClickListener {
        void onItemClick(Tarea tarea, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void actualizarLista(List<Tarea> nuevasTareas) {
        this.tareas = nuevasTareas;
        notifyDataSetChanged(); // Notificar al RecyclerView del cambio
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarea, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tarea tarea = tareas.get(position);

        holder.textViewAsignatura.setText(tarea.getAsignatura());
        holder.textViewDescripcion.setText(tarea.getDescripcion());
        holder.textViewFecha.setText(tarea.getFecha());
        holder.textViewEstado.setText(tarea.getEstado());


        // Listener para clics en el ítem
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(tarea, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tareas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAsignatura, textViewDescripcion, textViewFecha, textViewEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAsignatura = itemView.findViewById(R.id.textViewAsignatura);
            textViewDescripcion = itemView.findViewById(R.id.textViewDescripcion);
            textViewFecha = itemView.findViewById(R.id.textViewFecha);
            textViewEstado = itemView.findViewById(R.id.textViewEstado);
        }
    }
}

