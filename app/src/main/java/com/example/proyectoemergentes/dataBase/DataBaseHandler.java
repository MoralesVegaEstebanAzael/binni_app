package com.example.proyectoemergentes.dataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class DataBaseHandler extends SQLiteOpenHelper {
    SQLiteDatabase database;
    public DataBaseHandler(@Nullable Context context,
                           @Nullable String name,
                           @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name , factory, version);
    }

    public DataBaseHandler(Context context){
        super(context, Constantes.DATA_BASE_NAME , null, Constantes.DATA_BASE_VERSION);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Constantes.CREATE_TABLE_CATEGORIA);
        sqLiteDatabase.execSQL(Constantes.CREATE_TABLE_FAVORITOS);
        sqLiteDatabase.execSQL(Constantes.CREATE_TABLE_LUGAR);
        sqLiteDatabase.execSQL(Constantes.CREATE_TABLE_IMAGEN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS favoritos");
        onCreate(sqLiteDatabase);
    }


    public void addFavoritos(String lugarID){
       // SQLiteDatabase db = getReadableDatabase();
        database = getWritableDatabase();
        String query = String.format("INSERT INTO " + Constantes.TABLE_FAVORITOS +
                "("+Constantes.FAVORITOS_LUGAR+") " + " VALUES ('%s');",lugarID);
        database.execSQL(query);
    }
    public void removeFavoritos(String lugarID){
      //  SQLiteDatabase db = getReadableDatabase();
        database = getWritableDatabase();
        String query = String.format("DELETE FROM " + Constantes.TABLE_FAVORITOS +
                " WHERE "+Constantes.FAVORITOS_LUGAR+" ='%s';",lugarID);
        database.execSQL(query);
    }
    public boolean isFavorito(String lugarID){
        //SQLiteDatabase db = getReadableDatabase();
        database = getReadableDatabase();
        String query = String.format("SELECT *  FROM " + Constantes.TABLE_FAVORITOS +
                " WHERE "+Constantes.FAVORITOS_LUGAR+" ='%s';",lugarID);
        Cursor cursor = database.rawQuery(query,null);

        if(cursor.getCount()<=0){
            cursor.close();
            return false;
        }else{
            cursor.close();
            return true;
        }
    }

    public void addLugar(String id,String nombre,String lat,String lng,
                         String categoria,String descripcion,ImageView imageView){
        byte[] imagen = imageViewToByte(imageView);
        SQLiteDatabase database = getWritableDatabase();
        String query = "INSERT INTO lugar VALUES(?, ?, ?,? ,? ,? ,?,?)";
        SQLiteStatement statement = database.compileStatement(query);
        statement.clearBindings();
        statement.bindString(1,id);
        statement.bindString(2,nombre);
        statement.bindString(3,lat);
        statement.bindString(4,lng);
        statement.bindString(5,categoria);
        statement.bindString(5,descripcion);
        statement.bindBlob(7,imagen);
        statement.executeInsert();
    }

    public Cursor getLugares(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    public void addCategoria(String id,String nombre){
        SQLiteDatabase database = getWritableDatabase();
        String query = "INSERT INTO categoria VALUES(?, ?)";
        SQLiteStatement statement = database.compileStatement(query);
        statement.clearBindings();
        statement.bindString(1,id);
        statement.bindString(2,nombre);
        statement.executeInsert();
    }
    public void addImagen(byte[] imagen){

        SQLiteDatabase database = getWritableDatabase();
        String query = "INSERT INTO imagen VALUES(?)";
        SQLiteStatement statement = database.compileStatement(query);
        statement.clearBindings();
        statement.bindBlob(1,imagen);
        statement.executeInsert();
    }
    public boolean isEmptyTableImagen(){
        database = getReadableDatabase();
        String query = String.format("SELECT *  FROM imagen");
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.getCount()<=0){
            cursor.close();
            return true;//vacia
        }else{
            cursor.close();
            return false;
        }
    }
    public Cursor getCategoria(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }
    public byte[] imageViewToByte(ImageView image){
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
