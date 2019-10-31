package com.example.proyectoemergentes.ui.favoritos;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoemergentes.MainActivity;
import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.adapter.AdapterLugar;
import com.example.proyectoemergentes.pojos.Lugar;
import com.example.proyectoemergentes.ui.home.HomeFragment;

import java.util.ArrayList;

public class FavoritosFragment extends Fragment {
    private ArrayList<Lugar> arrayRecentPlaces;
    private ArrayList<Lugar> arrayFavorites;
    private RecyclerView recyclerViewFavorites;
    private RecyclerView recyclerViewRecent;
    private AdapterLugar adapterFavorites;
    private AdapterLugar adapterRecent;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favoritos, container, false);

        init(root);
        AsyncTaskLoadDB asyncTaskLoadDB = new AsyncTaskLoadDB();
        asyncTaskLoadDB.execute();
        return root;
    }
    public void init(View view){
        arrayRecentPlaces = new ArrayList<Lugar>();
        arrayFavorites = new ArrayList<Lugar>();

        recyclerViewRecent = view.findViewById(R.id.recycler_view_recent_places);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewRecent.setLayoutManager(layoutManager);
        adapterRecent = new AdapterLugar(getContext(),arrayRecentPlaces);
        recyclerViewRecent.setAdapter(adapterRecent);

        recyclerViewFavorites = view.findViewById(R.id.recycler_view_favorites);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewFavorites.setLayoutManager(mLayoutManager);
        adapterFavorites = new AdapterLugar(getContext(),arrayFavorites);
        recyclerViewFavorites.setAdapter(adapterFavorites);
    }
    private void favoritesFromLocalDB(){
        Cursor cursor = MainActivity.dataBaseHandler
                .getLugares("SELECT id,nombre,imagen FROM lugar INNER JOIN favoritos ON lugar.id = lugar");
        arrayFavorites.clear();
        Lugar lugar;
        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            String nombre = cursor.getString(1);
            byte[] image = cursor.getBlob(2);
            lugar = new Lugar(id,nombre,image);
            arrayFavorites.add(lugar);
        }
    }
    private void notificarAdaptadores(){
        adapterFavorites.notifyDataSetChanged();
        adapterRecent.notifyDataSetChanged();
    }
    private void recentFavFromLocalDB(){
        Cursor cursor = MainActivity.dataBaseHandler
                .getLugares("SELECT id,nombre,imagen " +
                        "FROM lugar INNER JOIN favoritos ON lugar.id = lugar " +
                        "ORDER BY lugar.id DESC LIMIT 5");
        arrayRecentPlaces.clear();
        Lugar lugar;
        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            String nombre = cursor.getString(1);
            byte[] image = cursor.getBlob(2);
            lugar = new Lugar(id,nombre,image);
            arrayRecentPlaces.add(lugar);
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
            Toast.makeText(getContext(),"CÓDIGO QR",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }



    private class AsyncTaskLoadDB extends AsyncTask<Void,Integer,Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Boolean doInBackground(Void... voids) {//acceso a la BD local en segundo plano
            favoritesFromLocalDB();
            recentFavFromLocalDB();
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