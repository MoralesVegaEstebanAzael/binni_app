package com.example.proyectoemergentes.pojos;

public class ItemShoppingCart {
    /*
     "CREATE TABLE IF NOT EXISTS shopping_cart(idpaquete STRING,nombrepaquete STRING," +
            "fecha STRING,npersonas STRING,preciopaquete STRING)"
    * */
    private String id;
    private String nombre;
    private String fecha;
    private String personas;
    private String precio;

    public ItemShoppingCart(String id, String nombre, String fecha, String personas, String precio) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.personas = personas;
        this.precio = precio;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPersonas() {
        return personas;
    }

    public void setPersonas(String personas) {
        this.personas = personas;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
