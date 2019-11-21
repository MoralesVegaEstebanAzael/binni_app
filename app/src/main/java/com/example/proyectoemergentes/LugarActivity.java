package com.example.proyectoemergentes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoemergentes.adapter.AdapterGaleria;
import com.example.proyectoemergentes.pojos.GaleriaImagen;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import xyz.hanks.library.bang.SmallBangView;

public class LugarActivity extends AppCompatActivity  implements
        OnMapReadyCallback{
    private String idLugar;
    private ImageView imageView;
    private TextView textViewDescripcion;
    private ImageView fav_image;
    private RecyclerView recyclerViewGaleria;
    private AdapterGaleria adapterGaleria;
    private ArrayList<String> listGaleria;
    private String[] imagenes;
    private GoogleMap map;
    private double lat,lng;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugar);

        AsyncTaskLoadDB asyncTaskLoadDB = new AsyncTaskLoadDB();
        asyncTaskLoadDB.execute();

    }

    private void init(){
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idLugar = extras.getString("ID_LUGAR");

        }
        final Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textViewDescripcion = findViewById(R.id.textVDescripcion);
        imageView = findViewById(R.id.img_lugar);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);

        fav_image = findViewById(R.id.favorito);

        //collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final SmallBangView like = findViewById(R.id.like_h);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (like.isSelected()) {
                    like.setSelected(false);
                } else {
                    like.setSelected(true);
                    like.likeAnimation(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if(!MainActivity.dataBaseHandler.isFavorito(idLugar)){
                                MainActivity.dataBaseHandler.addFavoritos(idLugar);
                                fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                            }else{
                                MainActivity.dataBaseHandler.removeFavoritos(idLugar);
                                fav_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            }
                        }
                    });
                }
            }
        });


        //imagenes
        listGaleria = new ArrayList<String>();
        recyclerViewGaleria = findViewById(R.id.recycler_view_galeria);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewGaleria.setLayoutManager(layoutManager);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_ubicacion);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

    }

    private void cargarDatosLocalDB(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = MainActivity.dataBaseHandler.getLugares("SELECT * FROM lugar WHERE id ="+idLugar);
        /* "(id STRING PRIMARY KEY, nombre STRING, lat STRING,lng STRING,categoria STRING" +
            ",descripcion STRING,imagen BLOB)";
        * */
                while(cursor.moveToNext()){
                    String nombre = cursor.getString(1);
                    String descripcion = cursor.getString(5);
                    byte[] imagen = cursor.getBlob(6);
                    lat = Double.parseDouble(cursor.getString(2));
                    lng = Double.parseDouble(cursor.getString(3));


                    textViewDescripcion.setText(descripcion);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imagen, 0, imagen.length);
                    imageView.setImageBitmap(bitmap);
                    collapsingToolbarLayout.setTitle(nombre);
                }

                if(MainActivity.dataBaseHandler.isFavorito(idLugar)){ //si es favorito
                    fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                }
            }
        });
    }

    private class AsyncTaskLoadDB extends AsyncTask<Void,Integer,Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            init();
            cargarDatosLocalDB();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {//acceso a la BD local en segundo plano
            if(idLugar!=""){
                apiGaleria(getString(R.string.url_api_galeria)+idLugar);
                publishProgress(1);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
//            adapterGaleria.notifyDataSetChanged();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private void apiGaleria(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.i("RESPUESTA",""+jsonArray);
                    addImagen(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(this,"Error al cargar los datos" ,Toast.LENGTH_LONG).show();
                Log.i("ERROR: ",error.getMessage());
            }
        });
        requestQueue.add(stringRequest);
    }
    private JSONArray json;

    private void addImagen(JSONArray jsonArray) {
        json = jsonArray;
        imagenes = new String[jsonArray.length()];
        new Thread(new Runnable() {
            public void run() {
                GaleriaImagen imagen;
                listGaleria.clear();
                JSONObject jsonObject= null;
                for (int i=0; i<json.length(); i++) {
                    try {
                        jsonObject = json.getJSONObject(i);
                        //listGaleria.add(jsonObject.optString("url"));
                        imagenes[i] = jsonObject.optString("url");
                    } catch (JSONException e) {
                        Log.i("ERROR:",e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        adapterGaleria = new AdapterGaleria(this,imagenes);
        recyclerViewGaleria.setAdapter(adapterGaleria);
        adapterGaleria.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
       // cargarDatosLocalDB();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        try {
            boolean success = googleMap.setMapStyle( MapStyleOptions.loadRawResourceStyle(this, R.raw.stylemap));

        } catch (Resources.NotFoundException e) {
            Log.e("k", "Can't find style. Error: ", e);
        }


        double lat=this.lat;
        double lng=this.lng;
        Log.i("INFO",lat + " " +lng);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat,lng))
                .zoom(17)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        LatLng latlng = new LatLng(lat,lng);
        map.addMarker(new MarkerOptions().position(latlng));

    }
}
