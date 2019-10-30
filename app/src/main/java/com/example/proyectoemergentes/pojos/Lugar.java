package com.example.proyectoemergentes.pojos;

public class Lugar {
    private String id;
    private String nombre;
    private String lat;
    private String lng;
    private String idCategoria;
    private String descripcion;
    private String image;
    private byte[] imagen;
    public Lugar(String id, String nombre, String lat, String lng,
                 String categoria, String descripcion, String image) {
        this.id = id;
        this.nombre = nombre;
        this.lat = lat;
        this.lng = lng;
        this.idCategoria = categoria;
        this.descripcion = descripcion;
        this.image = image;
    }
    public Lugar(String id,String nombre,byte[] imagen ){
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public Lugar(String id, String nombre, String  lat, String lng){
        this.id = id;
        this.nombre=nombre;
        this.lat=lat;
        this.lng=lng;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
