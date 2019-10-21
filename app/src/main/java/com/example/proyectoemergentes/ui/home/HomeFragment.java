package com.example.proyectoemergentes.ui.home;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoemergentes.MainActivity;
import com.example.proyectoemergentes.adapter.AdapterImagen;
import com.example.proyectoemergentes.adapter.AdapterLugar;
import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.dataBase.DataBaseHandler;
import com.example.proyectoemergentes.pojos.Imagen;
import com.example.proyectoemergentes.pojos.Lugar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private HomeViewModel homeViewModel;
    private ArrayList<Lugar> placesArray;
    private ArrayList<Imagen> imagenesList;
    private Button btnCargarDatos;
    private Button btnCargarDBLocal;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewImagenes;
    private AdapterImagen adapterImagen;
    private AdapterLugar adapterLugar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        init(root);
        return root;
    }

    public void init(View view){
        btnCargarDatos = view.findViewById(R.id.btnCargarDatos);
        btnCargarDBLocal = view.findViewById(R.id.btnCargarDBLocal);

        placesArray = new ArrayList<Lugar>();
        imagenesList = new ArrayList<Imagen>();

        recyclerView = view.findViewById(R.id.recycler_view_places);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        adapterLugar = new AdapterLugar(getContext(),placesArray);
        recyclerView.setAdapter(adapterLugar);

        recyclerViewImagenes = view.findViewById(R.id.reycler_view_imagenes);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewImagenes.setLayoutManager(layoutManager2);
        adapterImagen = new AdapterImagen(getContext(),imagenesList);
        recyclerViewImagenes.setAdapter(adapterImagen);


        btnCargarDBLocal.setOnClickListener(this);
        btnCargarDatos.setOnClickListener(this);
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
            Toast.makeText(getContext(),"CÓDIGO QR",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCargarDatos:
                places();
                break;
            case R.id.btnCargarDBLocal:
                lugaresFromLocalDB();
                break;
        }
    }

    class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

    public void places(){
        String url=getString(R.string.url_server)+"mostrarPlaces.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    llenarListaLugares(jsonArray);
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

    private void llenarListaLugares(JSONArray jsonArray) throws JSONException {
        Lugar lugar;
        for (int i=0; i<jsonArray.length(); i++) {
            JSONObject jsonObject= jsonArray.getJSONObject(i);
            lugar = new Lugar(
                    jsonObject.optString("id_place"),
                    jsonObject.optString("ruta_imagen"),
                    jsonObject.optString("descripcion"));
            placesArray.add(lugar);
        }
        adapterLugar.notifyDataSetChanged();
    }

    private void lugaresFromLocalDB(){

        Cursor cursor = MainActivity.dataBaseHandler.getLugares("SELECT * FROM imagen");
        placesArray.clear();

        while(cursor.moveToNext()){
            byte[] image = cursor.getBlob(0);
            imagenesList.add(new Imagen(image));
        }
        adapterImagen.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
       // mRecyclerView.setAdapter(mAdapter);
    }
}