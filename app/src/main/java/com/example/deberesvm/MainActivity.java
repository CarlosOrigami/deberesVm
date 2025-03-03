package com.example.deberesvm;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewTareas;
    private Adapter tareaAdapter;
    private TareasViewModel tareasViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewTareas = findViewById(R.id.recyclerViewTareas);
        recyclerViewTareas.setLayoutManager(new LinearLayoutManager(this));

        tareasViewModel = new ViewModelProvider(this).get(TareasViewModel.class);

        tareaAdapter = new Adapter(new ArrayList<>());
        recyclerViewTareas.setAdapter(tareaAdapter);

        tareasViewModel.getTareasLiveData().observe(this, tareaAdapter::actualizarLista);

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
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        tareasViewModel.eliminarTarea(position);
                        Toast.makeText(this, "Tarea eliminada.", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        completarOpcion.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();

            // Cambiar el estado de la tarea
            tarea.toggleEstado();

            // Actualizar en el ViewModel con la nueva lista
            tareasViewModel.actualizarTarea(position, tarea);

            // Mensaje de confirmación
            Toast.makeText(this, "Tarea ahora está: " + tarea.getEstado(), Toast.LENGTH_SHORT).show();
        });


        bottomSheetDialog.show();
    }
}

