package com.example.proyectoemergentes.ui.lugares;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.proyectoemergentes.CodigoQR;
import com.example.proyectoemergentes.MainActivity;
import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.adapter.AdapterLugar;
import com.example.proyectoemergentes.pojos.Lugar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class LugaresFragment extends Fragment implements OnMapReadyCallback{


    private GoogleMap map;
    private ArrayList<Lugar> lugares;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_lugares, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        lugares = new ArrayList<>();
        cargarDatosLocalDB();

        return root;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        try {
            boolean success = googleMap.setMapStyle( MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.stylemap));

        } catch (Resources.NotFoundException e) {
            Log.e("k", "Can't find style. Error: ", e);
        }


        double lat=Double.parseDouble(lugares.get(0).getLat());
        double lng=Double.parseDouble(lugares.get(0).getLng());

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat,lng))
                .zoom(17)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        for(Lugar lug : lugares){
            lat = Double.parseDouble(lug.getLat());
            lng = Double.parseDouble(lug.getLng());
            LatLng latlng = new LatLng(lat,lng);
            map.addMarker(new MarkerOptions().position(latlng).title(lug.getNombre()));
        }

    }



    private void lugaresFromLocalDB(String sql, ArrayList arrayList){
        Cursor cursor = MainActivity.dataBaseHandler.getLugares(sql);

        arrayList.clear();
        Lugar lugar;
        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            String nombre = cursor.getString(1);
            String lat = cursor.getString(2);
            String lng = cursor.getString(3);
            lugar = new Lugar(id,nombre, lat, lng);
            arrayList.add(lugar);
        }
    }


    private void cargarDatosLocalDB(){
        lugaresFromLocalDB("SELECT id,nombre,lat,lng FROM lugar", lugares);
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
            Intent intent= new Intent(getActivity(), CodigoQR.class);
            startActivityForResult(intent,0);
        }
        return super.onOptionsItemSelected(item);
    }


}