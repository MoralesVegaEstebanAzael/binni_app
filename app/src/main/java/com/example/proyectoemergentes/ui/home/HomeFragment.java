package com.example.proyectoemergentes.ui.home;

<<<<<<< HEAD
import android.content.Intent;
=======
>>>>>>> a76c2af7f41c4930e39ae33b164f2cc7b3ad8483
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
<<<<<<< HEAD
import androidx.annotation.Nullable;
=======
>>>>>>> a76c2af7f41c4930e39ae33b164f2cc7b3ad8483
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
<<<<<<< HEAD
import com.example.proyectoemergentes.CodigoQR;
=======
>>>>>>> a76c2af7f41c4930e39ae33b164f2cc7b3ad8483
import com.example.proyectoemergentes.MainActivity;
import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.adapter.AdapterLugar;
import com.example.proyectoemergentes.adapter.SliderAdapter;
<<<<<<< HEAD
=======
import com.example.proyectoemergentes.pager.AutoScrollViewPager;
>>>>>>> a76c2af7f41c4930e39ae33b164f2cc7b3ad8483
import com.example.proyectoemergentes.pojos.Anuncio;
import com.example.proyectoemergentes.pojos.Lugar;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    private AutoScrollViewPager viewPager;
    private TabLayout indicator;
    private SliderAdapter adaptSlider;
    ArrayList<Anuncio> listAnuncios;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View  root  = inflater.inflate(R.layout.fragment_home, container, false);

        init(root);
        initSlider(root);

        //asyn task cargar desde base de datos local
        AsyncTaskLoadDB asyncTaskLoadDB = new AsyncTaskLoadDB();
        asyncTaskLoadDB.execute();

        AsyntaskLoadAnuncios asyntaskLoadAnuncios = new AsyntaskLoadAnuncios();
        asyntaskLoadAnuncios.execute();
        return root;
    }

    public void initSlider(View view){
        listAnuncios = new ArrayList<Anuncio>();
        viewPager = view.findViewById(R.id.viewPager);
        indicator = view.findViewById(R.id.indicador);
        adaptSlider = new SliderAdapter(getContext(),listAnuncios);
//        viewPager.setAdapter(adaptSlider); //this is right but i do not care it works now
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

                apiLugares(getString(R.string.url_api_lugares_categoria));
                apiAnuncios(getString(R.string.url_api_anuncios));
                AsyncTaskLoadDB asyncTaskLoadDB = new AsyncTaskLoadDB();
                asyncTaskLoadDB.execute();

                AsyntaskLoadAnuncios asyntaskLoadAnuncios = new AsyntaskLoadAnuncios();
                asyntaskLoadAnuncios.execute();



               /* Fragment frg = null;
                frg = getActivity().getSupportFragmentManager().findFragmentByTag("HOMETAG");
                               final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
*/
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

    }

    public void cargarDatosLocalDBAnuncios(){
        anunciosFromLocalDB("SELECT idlugar,nombre_lugar,imagen FROM anuncio",listAnuncios,adaptSlider);
    }

    private void notificarAdaptadores(){
        adapterLugarMuseos.notifyDataSetChanged();
        adapterLugarTemplos.notifyDataSetChanged();
        adapterLugarZonasArq.notifyDataSetChanged();
    }

    private void notificarAdaptSlider(){
        adaptSlider.notifyDataSetChanged();
    }
    private void apiLugares(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    //Log.i("RESPUESTA",""+jsonArray);
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
    JSONArray jsonA;
    private void addAnuncios(JSONArray jsonArray) throws JSONException{
        jsonA = jsonArray;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Anuncio anuncio;
                byte[] bytes = null;
                listAnuncios.clear();
                for (int i=0;i<jsonA.length();i++){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = jsonA.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

                    if(isAdded()){
                        try {
                            bytes = Glide.with(getContext())
                                    .as(byte[].class)
                                    .load(anuncio.getUrl())
                                    .submit()
                                    .get();
                        }catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        MainActivity.dataBaseHandler.addAnuncio(anuncio.getId(),anuncio.getIdSocio(),anuncio.getIdLugar(),
                                anuncio.getFechaInicio(),anuncio.getDuracion(),anuncio.getIdCategoria(),anuncio.getNombreLugar(),bytes);
                    }

                }
            }
        }).start();

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
                            jsonObject.optString("url"),
                            jsonObject.optString("precio"));
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
                                bytes,lugar.getPrecio());
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

    private class AsyntaskLoadAnuncios extends AsyncTask<Void,Integer,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {

            cargarDatosLocalDBAnuncios();

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            viewPager.setAdapter(adaptSlider);// jeje it works!! XD que mal programador!!

            notificarAdaptSlider();
                viewPager.startAutoScroll();
                viewPager.setInterval(7000);
                viewPager.setCycle(true);
                viewPager.setStopScrollWhenTouch(true);
                viewPager.setBorderAnimation(false);

        }

        @Override
        protected void onCancelled() {


        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu,menu);
        menu.findItem(R.id.action_qr_code).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_qr_code){
            Intent intent= new Intent(getActivity(), CodigoQR.class);
            startActivityForResult(intent,0);
        }
        return super.onOptionsItemSelected(item);
    }

}