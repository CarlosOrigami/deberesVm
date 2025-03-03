package com.example.deberesvm;

public class Tarea {
    private String asignatura;
    private String descripcion;
    private String fecha;
    private String estado;

    public Tarea(String asignatura, String descripcion, String fecha) {
        this.asignatura = asignatura;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.estado = "Pendiente";
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void toggleEstado() {
        if (this.estado.equals("Pendiente")) {
            this.estado = "Completada";
        } else {
            this.estado = "Pendiente";
        }
    }
}
