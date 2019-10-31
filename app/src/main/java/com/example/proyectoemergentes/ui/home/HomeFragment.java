package com.example.proyectoemergentes.ui.home;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ByteArrayPool;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.ByteBufferFileLoader;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.example.proyectoemergentes.MainActivity;
import com.example.proyectoemergentes.adapter.AdapterLugar;
import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.adapter.SliderAdapter;
import com.example.proyectoemergentes.dataBase.DataBaseHandler;
import com.example.proyectoemergentes.pojos.Anuncio;
import com.example.proyectoemergentes.pojos.Lugar;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

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
        View  root  = inflater.inflate(R.layout.fragment_home, container, false);

        init(root);
        //initSlider(root);

        //asyn task cargar desde base de datos local
        AsyncTaskLoadDB asyncTaskLoadDB = new AsyncTaskLoadDB();
        asyncTaskLoadDB.execute();

        return root;
    }

    public void initSlider(View view){
        listAnuncios = new ArrayList<Anuncio>();
        viewPager = view.findViewById(R.id.viewPager);
        indicator = view.findViewById(R.id.indicador);
        adaptSlider = new SliderAdapter(getContext(),listAnuncios);
        viewPager.setAdapter(adaptSlider);
        indicator.setupWithViewPager(viewPager,true);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 6000, 4000);
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

                apiLugares(getString(R.string.url_api_lugares_categoria));

                AsyncTaskLoadDB asyncTaskLoadDB = new AsyncTaskLoadDB();
                asyncTaskLoadDB.execute();

                Fragment frg = null;
                frg = getActivity().getSupportFragmentManager().findFragmentByTag("HOMETAG");
                               final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });
    }

    private void cargarDatosLocalDB(){
        lugaresFromLocalDB("SELECT id,nombre,imagen FROM lugar where categoria = '1'",listZonasArq,adapterLugarZonasArq);
        lugaresFromLocalDB("SELECT id,nombre,imagen FROM lugar where categoria = '2'",listTemplos,adapterLugarTemplos);
        lugaresFromLocalDB("SELECT id,nombre,imagen FROM lugar where categoria = '3'",listMuseos,adapterLugarMuseos);
      //  anunciosFromLocalDB("SELECT idlugar,nombre_lugar,imagen FROM anuncio",listAnuncios,adaptSlider);
    }

    private void notificarAdaptadores(){
        adapterLugarMuseos.notifyDataSetChanged();
        adapterLugarTemplos.notifyDataSetChanged();
        adapterLugarZonasArq.notifyDataSetChanged();
        //adaptSlider.notifyDataSetChanged();
    }

    private void apiLugares(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.i("RESPUESTA",""+jsonArray);
                    addLugar(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
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
    }

    JSONArray json;
    private void addLugar(JSONArray jsonArray) throws JSONException, ExecutionException, InterruptedException {
        json = jsonArray;
        new Thread(new Runnable() {
            byte[] bytes ;
            public void run() {
                Lugar lugar;
                listPlaces.clear();
                for (int i=0; i<json.length(); i++) {
                    JSONObject jsonObject= null;
                    try {
                        jsonObject = json.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    lugar = new Lugar(
                            jsonObject.optString("idlugar"),
                            jsonObject.optString("nombre"),
                            jsonObject.optString("latitud"),
                            jsonObject.optString("longitud"),
                            jsonObject.optString("idcategoria"),
                            jsonObject.optString("descripcion"),
                            jsonObject.optString("url"));
                    if(isAdded()){
                        try {
                            bytes = Glide.with(getContext())
                                    .as(byte[].class)
                                    .load(lugar.getImage())
                                    .submit()
                                    .get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        MainActivity.dataBaseHandler.addLugar(lugar.getId(),
                                lugar.getNombre(),lugar.getLat(),
                                lugar.getLng(),lugar.getIdCategoria(),
                                lugar.getDescripcion(),
                                bytes);
                    }
                }
            }
        }).start();


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
       // adapterAnuncio.notifyDataSetChanged();
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


    //Proceso para obtener datos locales
    private class AsyncTaskLoadDB extends AsyncTask<Void,Integer,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Boolean doInBackground(Void... voids) {//acceso a la BD local en segundo plano
            cargarDatosLocalDB();
            publishProgress(1);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            notificarAdaptadores();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }



}