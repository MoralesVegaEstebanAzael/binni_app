package com.example.proyectoemergentes.pojos;

import com.google.gson.annotations.SerializedName;

public class Anuncio {
    @SerializedName("idlugar")
    private String id;
    @SerializedName("idsocio")
    private String idSocio;
    @SerializedName("idlugar")
    private String idLugar;
    @SerializedName("fecha_inicio")
    private String fechaInicio;
    private String duracion;
    @SerializedName("idcategoria")
    private String idCategoria;
    @SerializedName("nombre_lugar")
    private String nombreLugar;
    private String url;

    private byte[] imagen;

    public Anuncio(String id, String idSocio, String idLugar, String fechaInicio, String duracion, String idCategoria, String nombreLugar, String url) {
        this.id = id;
        this.idSocio = idSocio;
        this.idLugar = idLugar;
        this.fechaInicio = fechaInicio;
        this.duracion = duracion;
        this.idCategoria = idCategoria;
        this.nombreLugar = nombreLugar;
        this.url = url;
    }

    public Anuncio(String id, String nombreLugar, byte[] imagen) {
        this.id = id;
        this.nombreLugar = nombreLugar;
        this.imagen = imagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(String idSocio) {
        this.idSocio = idSocio;
    }

    public String getIdLugar() {
        return idLugar;
    }

    public void setIdLugar(String idLugar) {
        this.idLugar = idLugar;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreLugar() {
        return nombreLugar;
    }

    public void setNombreLugar(String nombreLugar) {
        this.nombreLugar = nombreLugar;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}
