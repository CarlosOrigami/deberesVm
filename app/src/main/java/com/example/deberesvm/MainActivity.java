package com.example.deberesvm;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewTareas;
    private Adapter tareaAdapter;
    private MutableLiveData<List<Tarea>> tareasLiveData = new MutableLiveData<>();
    private List<Tarea> tareas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewTareas = findViewById(R.id.recyclerViewTareas);
        recyclerViewTareas.setLayoutManager(new LinearLayoutManager(this));

        tareaAdapter = new Adapter(new ArrayList<>());
        recyclerViewTareas.setAdapter(tareaAdapter);

        // Observar cambios en LiveData
        tareasLiveData.observe(this, tareaAdapter::actualizarLista);

        tareaAdapter.setOnItemClickListener(this::mostrarBottomSheet);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            AgregarTrabajo agregarTrabajoDialog = new AgregarTrabajo();
            agregarTrabajoDialog.show(getSupportFragmentManager(), "AgregarTrabajo");
        });
    }

    private void mostrarBottomSheet(Tarea tarea, int position) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        TextView editarOpcion = bottomSheetView.findViewById(R.id.opcionEditar);
        TextView eliminarOpcion = bottomSheetView.findViewById(R.id.opcionEliminar);
        TextView completarOpcion = bottomSheetView.findViewById(R.id.opcionCompletar);

        editarOpcion.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            AgregarTrabajo agregarTrabajoDialog = new AgregarTrabajo();
            Bundle args = new Bundle();
            args.putInt("posicion", position);
            args.putString("asignatura", tarea.getAsignatura());
            args.putString("descripcion", tarea.getDescripcion());
            args.putString("fecha", tarea.getFecha());
            agregarTrabajoDialog.setArguments(args);
            agregarTrabajoDialog.show(getSupportFragmentManager(), "EditarTrabajo");
        });

        eliminarOpcion.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            new AlertDialog.Builder(this)
                    .setMessage("¿Seguro que quieres eliminar esta tarea?")
                    .setPositiveButton("Eliminar", (dialog, which) -> eliminarTarea(position))
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        completarOpcion.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            completarTarea(position);
        });

        bottomSheetDialog.show();
    }

    public void agregarTarea(String asignatura, String descripcion, String fecha) {
        if (asignatura.isEmpty() || descripcion.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        tareas.add(new Tarea(asignatura, descripcion, fecha));
        tareasLiveData.setValue(new ArrayList<>(tareas)); // Notificar cambios
    }

    public void actualizarTarea(int position, String asignatura, String descripcion, String fecha) {
        if (position >= 0 && position < tareas.size()) {
            Tarea tarea = tareas.get(position);
            tarea.setAsignatura(asignatura);
            tarea.setDescripcion(descripcion);
            tarea.setFecha(fecha);
            tareasLiveData.setValue(new ArrayList<>(tareas)); // Notificar cambios
        }
    }

    private void eliminarTarea(int position) {
        if (position >= 0 && position < tareas.size()) {
            tareas.remove(position);
            tareasLiveData.setValue(new ArrayList<>(tareas)); // Notificar cambios
        }
    }

    private void completarTarea(int position) {
        if (position >= 0 && position < tareas.size()) {
            tareas.get(position).toggleEstado();
            tareasLiveData.setValue(new ArrayList<>(tareas)); // Notificar cambios
            Toast.makeText(this, "Tarea ahora está: " + tareas.get(position).getEstado(), Toast.LENGTH_SHORT).show();
        }
    }
}
