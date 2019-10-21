package com.example.proyectoemergentes.dataBase;

public class Constantes {

    public static final String DATA_BASE_NAME="emergentes.db";
    public static final int DATA_BASE_VERSION=1;


    //tabla usuario
    public static final String TABLE_USUARIO="usuario";
    public static final String USUARIO_ID= "id";
    public static final String USUARIO_NOMBRE = "nombre";
    public static final String CREATE_TABLE_USUARIO = "CREATE TABLE "+TABLE_USUARIO
            +" ("+USUARIO_ID+" TEXT,"+USUARIO_NOMBRE+" TEXT)";

    //tabla lugar
    public static final String CREATE_TABLE_LUGAR="CREATE TABLE IF NOT EXISTS lugar " +
            "(id STRING PRIMARY KEY, nombre STRING, lat STRING,lng STRING,categoria STRING" +
            ",descripcion STRING,imagen BLOB)";
    //tabla categoria
    public static  final String CREATE_TABLE_CATEGORIA ="CREATE TABLE IF NOT EXISTS categoria " +
            "(id STRING PRIMARY KEY, nombre STRING)";
    //tabla favoritos
    public static final String TABLE_FAVORITOS="favoritos";
    public static final String FAVORITOS_LUGAR= "lugar";
    public static final String CREATE_TABLE_FAVORITOS = "CREATE TABLE " + TABLE_FAVORITOS
            + " ("+FAVORITOS_LUGAR+ " TEXT)";
    //tabla imagen
    public static final String CREATE_TABLE_IMAGEN = "CREATE TABLE  IF NOT EXISTS imagen " +
            "(imagen BLOB)";
}
