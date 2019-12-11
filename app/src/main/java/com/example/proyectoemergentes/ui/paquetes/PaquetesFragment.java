package com.example.proyectoemergentes.ui.paquetes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.adapter.AdapterPaquete;
import com.example.proyectoemergentes.pojos.Paquete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PaquetesFragment extends Fragment {
    private RecyclerView recyclerPaquetes;
    private ArrayList<Paquete> listPaquetes;
    private AdapterPaquete adapterPaquete;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paquetes, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerPaquetes = view.findViewById(R.id.recycler_paquetes);
        recyclerPaquetes.setHasFixedSize(true);
        listPaquetes = new ArrayList<Paquete>();
        recyclerPaquetes.setLayoutManager(layoutManager);
        adapterPaquete = new AdapterPaquete(getContext(),listPaquetes);
        recyclerPaquetes.setAdapter(adapterPaquete);

        apiPaquetes(getString(R.string.url_api_paquetes));
    }

    private void apiPaquetes(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.i("RESPUESTA",response);
                    addPaquete(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("ERRORAPI",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERRORAPI",error.getMessage());
            }
        });
        requestQueue.add(stringRequest);
    }

    JSONArray json;
    private void addPaquete(JSONArray jsonArray) throws JSONException{
        json = jsonArray;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Paquete paquete;
                ArrayList<String> lugares=new ArrayList<>();
                ArrayList<String> urlImagen=new ArrayList<>();
                String idPaquete="";
                String idAnterior="";

                for (int i=0;i<json.length();i++){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = json.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    lugares.add(jsonObject.optString("nombre_lugar"));
                    urlImagen.add(jsonObject.optString("url"));
                    idPaquete =  jsonObject.optString("idpaquete");

                    if(!idPaquete.equals(idAnterior)){
                        idPaquete =  jsonObject.optString("idpaquete");
                        paquete = new Paquete(
                                jsonObject.optString("idpaquete"),
                                jsonObject.optString("nombre_paquete"),
                                jsonObject.optString("precio"),
                                lugares,
                                urlImagen,
                                "http://s3.amazonaws.com/campeche/wp-content/uploads/2019/09/04100139/Screenshot_2.png");
                        listPaquetes.add(paquete);
                        lugares.clear();
                       // urlImagen.clear();
                    }

                    idAnterior = idPaquete;
                }
            }
        }).start();

        adapterPaquete.notifyDataSetChanged();

    }

}
