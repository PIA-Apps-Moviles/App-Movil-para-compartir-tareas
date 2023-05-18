package com.jahir.pia_appsmov.Model.datosTareaGlobal;

public class Datos_PDF {

    String Nombre, Materia, Descripcion, Carrera, Url;

    public Datos_PDF(){

    }

    public Datos_PDF(String nombre, String materia, String descripcion, String carrera, String url) {
        Nombre = nombre;
        Materia = materia;
        Descripcion = descripcion;
        Carrera = carrera;
        Url = url;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getMateria() {
        return Materia;
    }

    public void setMateria(String materia) {
        Materia = materia;
    }
    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getCarrera() {
        return Carrera;
    }

    public void setCarrera(String carrera) {
        Carrera = carrera;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

}
