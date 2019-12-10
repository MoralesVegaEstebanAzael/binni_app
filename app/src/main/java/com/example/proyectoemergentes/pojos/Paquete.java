package com.example.proyectoemergentes.pojos;

import java.io.Serializable;
import java.util.ArrayList;

public class Paquete implements Serializable {
    private String id;
    private String nombre;
    private String precio;
    private ArrayList<String> lugares;
    private ArrayList<String> urlLugares;

    public ArrayList<String> getUrlLugares() {
        return urlLugares;
    }

    public void setUrlLugares(ArrayList<String> urlLugares) {
        this.urlLugares = urlLugares;
    }

    private String urlImagen;
    public Paquete(String id, String nombre, String precio, ArrayList<String> lugares, String urlImagen) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.lugares = lugares;
        this.urlImagen = urlImagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public ArrayList<String> getLugares() {
        return lugares;
    }

    public void setLugares(ArrayList<String> lugares) {
        this.lugares = lugares;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
}
