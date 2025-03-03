package com.example.deberesvm;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

public class AgregarTrabajo extends DialogFragment {

    private EditText descripcionEditText;
    private EditText fechaEditText;
    private Spinner asignaturaSpinner;
    private int posicionEdicion = -1;
    private MainActivity mainActivity; // Referencia a MainActivity

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.agregar_trabajo, null);

        descripcionEditText = view.findViewById(R.id.descripcionEditText);
        fechaEditText = view.findViewById(R.id.fechaEditText);
        asignaturaSpinner = view.findViewById(R.id.asignaturaSpinner);

        // Obtener referencia a MainActivity
        mainActivity = (MainActivity) getActivity();

        // Configurar Spinner de asignaturas
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.asignaturas,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        asignaturaSpinner.setAdapter(adapter);

        // Verificar si hay datos para edición
        if (getArguments() != null) {
            posicionEdicion = getArguments().getInt("posicion", -1);
            String asignatura = getArguments().getString("asignatura");
            String descripcion = getArguments().getString("descripcion");
            String fecha = getArguments().getString("fecha");

            asignaturaSpinner.setSelection(adapter.getPosition(asignatura));
            descripcionEditText.setText(descripcion);
            fechaEditText.setText(fecha);
        }

        // Configuración del DatePickerDialog
        fechaEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(getActivity(), (view1, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                fechaEditText.setText(selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear);
            }, year, month, day).show();
        });

        builder.setView(view)
                .setPositiveButton("Guardar", (dialog, id) -> guardarTarea())
                .setNegativeButton("Cancelar", (dialog, id) -> dismiss());

        return builder.create();
    }

    private void guardarTarea() {
        String asignatura = asignaturaSpinner.getSelectedItem().toString();
        String descripcion = descripcionEditText.getText().toString();
        String fecha = fechaEditText.getText().toString();

        if (asignatura.isEmpty() || descripcion.isEmpty() || fecha.isEmpty()) {
            return;
        }

        if (mainActivity != null) {
            if (posicionEdicion != -1) { // Se está editando una tarea existente
                mainActivity.actualizarTarea(posicionEdicion, asignatura, descripcion, fecha);
            } else { // Se está creando una nueva tarea
                mainActivity.agregarTarea(asignatura, descripcion, fecha);
            }
        }
    }
}
