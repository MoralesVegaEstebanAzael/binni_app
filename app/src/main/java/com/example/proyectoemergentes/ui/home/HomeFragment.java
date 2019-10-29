package com.example.proyectoemergentes.ui.home;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.proyectoemergentes.MainActivity;
import com.example.proyectoemergentes.adapter.AdapterImagen;
import com.example.proyectoemergentes.adapter.AdapterLugar;
import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.adapter.SliderAdapter;
import com.example.proyectoemergentes.dataBase.DataBaseHandler;
import com.example.proyectoemergentes.pojos.Anuncio;
import com.example.proyectoemergentes.pojos.Imagen;
import com.example.proyectoemergentes.pojos.Lugar;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment{
    private RecyclerView recyclerViewZonasArq;
    private RecyclerView recyclerViewTemplos;
    private RecyclerView recyclerViewMuseos;
    private AdapterLugar adapterLugarZonasArq;
    private AdapterLugar adapterLugarTemplos;
    private AdapterLugar adapterLugarMuseos;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Lugar> listZonasArq;
    private ArrayList<Lugar> listPlaces;
    private ArrayList<Lugar> listTemplos;
    private ArrayList<Lugar> listMuseos;

    //slider utils
    private ViewPager viewPager;
    private TabLayout indicator;
    private SliderAdapter adaptSlider;
    ArrayList<Anuncio> listAnuncios;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initSlider(root);//slider task
        init(root);
        loadRecycerView();
        return root;
    }

    public void initSlider(View view){
        listAnuncios = new ArrayList<Anuncio>();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 6000, 4000);

        viewPager = view.findViewById(R.id.viewPager);
        indicator = view.findViewById(R.id.indicador);
        adaptSlider = new SliderAdapter(getContext(),listAnuncios);
        viewPager.setAdapter(adaptSlider);
        indicator.setupWithViewPager(viewPager,true);
    }

    public void init(View view){
        listZonasArq = new ArrayList<Lugar>();
        listPlaces = new ArrayList<Lugar>();
        listTemplos = new ArrayList<Lugar>();
        listMuseos = new ArrayList<Lugar>();

        recyclerViewZonasArq = view.findViewById(R.id.recycler_view_places);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewZonasArq.setLayoutManager(layoutManager);
        adapterLugarZonasArq = new AdapterLugar(getContext(),listZonasArq);
        recyclerViewZonasArq.setAdapter(adapterLugarZonasArq);

        recyclerViewTemplos = view.findViewById(R.id.reyclerViewTemplos);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewTemplos.setLayoutManager(layoutManager2);
        adapterLugarTemplos = new AdapterLugar(getContext(),listTemplos);
        recyclerViewTemplos.setAdapter(adapterLugarTemplos);


        recyclerViewMuseos = view.findViewById(R.id.reyclerViewMuseos);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext());
        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewMuseos.setLayoutManager(layoutManager3);
        adapterLugarMuseos = new AdapterLugar(getContext(),listMuseos);
        recyclerViewMuseos.setAdapter(adapterLugarMuseos);

        swipeRefreshListener(view);

    }

    private void swipeRefreshListener(View view){
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshHome);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //url1
                String url=getString(R.string.url_api_lugares_categoria);
                String urlAnuncios = getString(R.string.url_api_anuncios);
                apiLugares(url); //solicitud a API
                apiAnuncios(urlAnuncios);
                 //actualizar el recyclerView
                loadRecycerView();
                loadAnuncio();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }


        });
    }
    private void loadRecycerView(){
        lugaresFromLocalDB("SELECT id,nombre,imagen FROM lugar where categoria = '1'",listZonasArq,adapterLugarZonasArq);
        lugaresFromLocalDB("SELECT id,nombre,imagen FROM lugar where categoria = '2'",listTemplos,adapterLugarTemplos);
        lugaresFromLocalDB("SELECT id,nombre,imagen FROM lugar where categoria = '3'",listMuseos,adapterLugarMuseos);

    }

    private void loadAnuncio(){
        anunciosFromLocalDB("SELECT idlugar,nombre_lugar,imagen FROM anuncio",listAnuncios,adaptSlider);
    }

    public void apiLugares(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    addLugar(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Error al cargar los datos" ,Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void apiAnuncios(String urlAnuncios) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAnuncios, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    addAnuncios(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Error al cargar los datos" ,Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void addAnuncios(JSONArray jsonArray) throws JSONException{
        Anuncio anuncio;
        listAnuncios.clear();
        for (int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            anuncio = new Anuncio(
                    jsonObject.optString("idanuncio"),
                    jsonObject.optString("idsocio"),
                    jsonObject.optString("idlugar"),
                    jsonObject.optString("fecha_inicio"),
                    jsonObject.optString("duracion"),
                    jsonObject.optString("idcategoria"),
                    jsonObject.optString("nombre_lugar"),
                    jsonObject.optString("url"));
            listAnuncios.add(anuncio);

        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try{
                    for(Anuncio anuncioItem:listAnuncios){
                        byte[] bytes = Glide.with(getContext())
                                .as(byte[].class)
                                .load(anuncioItem.getUrl())
                                .submit()
                                .get();
                        MainActivity.dataBaseHandler.addAnuncio(anuncioItem.getId(),
                                anuncioItem.getIdSocio(),anuncioItem.getIdLugar(),
                                anuncioItem.getFechaInicio(),anuncioItem.getDuracion(),
                                anuncioItem.getIdCategoria(),anuncioItem.getNombreLugar(),
                                bytes);
                    }
                }catch (Exception e){
                    Log.i("ERRORSQLITE",e.getMessage());
                }
            }
        };
        new Thread(runnable).start();

    }

    private void addLugar(JSONArray jsonArray)throws JSONException{
        Lugar lugar;
        listPlaces.clear();
        for (int i=0; i<jsonArray.length(); i++) {
            JSONObject jsonObject= jsonArray.getJSONObject(i);
            lugar = new Lugar(
                    jsonObject.optString("idlugar"),
                    jsonObject.optString("nombre"),
                    jsonObject.optString("latitud"),
                    jsonObject.optString("longitud"),
                    jsonObject.optString("idcategoria"),
                    jsonObject.optString("descripcion"),
                    jsonObject.optString("url"));
            listPlaces.add(lugar);
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try{
                    for(Lugar lugar:listPlaces){
                        byte[] bytes = Glide.with(getContext())
                                .as(byte[].class)
                                .load(lugar.getImage())
                                .submit()
                                .get();
                        MainActivity.dataBaseHandler.addLugar(lugar.getId(),
                                lugar.getNombre(),lugar.getLat(),
                                lugar.getLng(),lugar.getIdCategoria(),
                                lugar.getDescripcion(),
                                bytes);
                    }
                }catch (Exception e){
                    Log.i("ERRORSQLITE",e.getMessage());
                }
            }
        };
        new Thread(runnable).start();
    }

    private void lugaresFromLocalDB(String sql,ArrayList arrayList,AdapterLugar adapterLugar){
        Cursor cursor = MainActivity.dataBaseHandler.getLugares(sql);
        arrayList.clear();
        Lugar lugar;
        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            String nombre = cursor.getString(1);
            byte[] image = cursor.getBlob(2);
            lugar = new Lugar(id,nombre,image);
            arrayList.add(lugar);
        }
        adapterLugar.notifyDataSetChanged();
    }

    private void anunciosFromLocalDB(String sql,ArrayList arrayList,SliderAdapter adapterAnuncio){
        Cursor cursor = MainActivity.dataBaseHandler.getAnuncios(sql);
        arrayList.clear();
        Anuncio anuncio;
        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            String nombreLugar = cursor.getString(1);
            byte[] image = cursor.getBlob(2);
            anuncio = new Anuncio(id,nombreLugar,image);
            arrayList.add(anuncio);
        }
        adapterAnuncio.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
       // ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    private class SliderTimer extends TimerTask {
        private Handler mHandler = new Handler(Looper.getMainLooper());

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    if(adaptSlider.getCount() > 0) {
                        if (viewPager.getCurrentItem() < adaptSlider.getCount() - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                        } else {
                            viewPager.setCurrentItem(0, true);
                        }
                    }
                }
            });

        }
    }
}