package com.example.deberesvm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class TareasViewModel extends ViewModel {

    private final MutableLiveData<List<Tarea>> tareasLiveData;

    public TareasViewModel() {
        tareasLiveData = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<Tarea>> getTareasLiveData() {
        return tareasLiveData;
    }

    public void agregarTarea(Tarea tarea) {
        List<Tarea> listaActual = new ArrayList<>(tareasLiveData.getValue());
        listaActual.add(tarea);
        tareasLiveData.setValue(listaActual);
    }

    public void eliminarTarea(int position) {
        List<Tarea> listaActual = new ArrayList<>(tareasLiveData.getValue());
        if (position >= 0 && position < listaActual.size()) {
            listaActual.remove(position);
            tareasLiveData.setValue(listaActual);
        }
    }

    public void actualizarTarea(int position, Tarea tarea) {
        if (tareasLiveData.getValue() == null) return;

        List<Tarea> listaActual = new ArrayList<>(tareasLiveData.getValue()); // Crear una nueva lista

        if (position >= 0 && position < listaActual.size()) {
            // Cambiar el estado de la tarea en la lista
            listaActual.get(position).setEstado(tarea.getEstado());

            // Notificar a LiveData con una nueva lista (para forzar actualización)
            tareasLiveData.setValue(new ArrayList<>(listaActual));
        }
    }



}


