package com.example.proyectoemergentes.ui.lugares;

import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectoemergentes.MainActivity;
import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.pojos.Lugar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

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
                .target(new LatLng(17.060504, -96.725241))
                .zoom(17)
                .build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        for(Lugar lug : lugares){
            lat = Double.parseDouble(lug.getLat());
            lng = Double.parseDouble(lug.getLng());
            LatLng latlng = new LatLng(lat,lng);
/*
            if(lug.getIdCategoria().equals("1")){
                map.addMarker(new MarkerOptions()
                        .position(latlng)
                        .title("Sitio Arqueologico")
                        .snippet(lug.getNombre())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.zona2)));
            }
            else if(lug.getIdCategoria().equals("2")){
                map.addMarker(new MarkerOptions()
                        .position(latlng)
                        .title("Templos y Excnventos")
                        .snippet(lug.getNombre())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.templo)));
            }
            else if(lug.getIdCategoria().equals("3")){
                map.addMarker(new MarkerOptions()
                        .position(latlng)
                        .title("Museo")
                        .snippet(lug.getNombre())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.museo)));
            }
            else if(lug.getIdCategoria().equals("4")){
                map.addMarker(new MarkerOptions()
                        .position(latlng)
                        .title("Mercados")
                        .snippet(lug.getNombre())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mercado)));
            }
*/

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
            String idcategoria = cursor.getString(4);
            lugar = new Lugar(id,nombre, lat, lng, idcategoria);
            arrayList.add(lugar);
        }
    }


    private void cargarDatosLocalDB(){
        lugaresFromLocalDB("SELECT id,nombre,lat,lng,categoria FROM lugar ", lugares);
    }





}