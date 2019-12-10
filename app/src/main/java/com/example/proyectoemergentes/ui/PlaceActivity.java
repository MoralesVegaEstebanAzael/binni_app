package com.example.proyectoemergentes.ui;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoemergentes.MainActivity;
import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.adapter.ImageGalleryAdapter;
import com.example.proyectoemergentes.pojos.Photo;
import com.example.proyectoemergentes.pojos.TabDetails;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlaceActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static String idLugar;
    private ImageView imageView;
    public static String descripcion;
    public static double lat,lng;
    public static GoogleMap map;
    public static String precio;
    public  CollapsingToolbarLayout collapsingToolbar;
    AppBarLayout appBarLayout;
    FrameLayout frameLayout;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        init();
        cargarDatosLocalDB();
    }

    private void setupCollapsingToolbar() {
      collapsingToolbar = findViewById(R.id.collapse_toolbar);
      collapsingToolbar.setTitleEnabled(false);
      collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
    }
    private void init(){
        imageView = findViewById(R.id.img_header);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idLugar = extras.getString("ID_LUGAR");
        }
        frameLayout = findViewById(R.id.fraToolFloat);
        appBarLayout = findViewById(R.id.appBar);
        floatingActionButton=findViewById(R.id.btn_favorito);
        setupToolbar();
        setupViewPager();
        setupCollapsingToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) { //invisible
                    mTabLayout.setBackgroundColor(Color.WHITE);
                    toolbar.setTitleTextColor(Color.BLACK);
                    mTabLayout.setTabTextColors(ColorStateList.valueOf(ContextCompat.getColor(PlaceActivity.this, R.color.colorAccent)));
                    frameLayout.setVisibility(View.GONE);
                    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
                } else {
                    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
                    frameLayout.setVisibility(View.VISIBLE);
                    toolbar.setTitleTextColor(Color.WHITE);
                    mTabLayout.setTabTextColors(ColorStateList.valueOf(ContextCompat.getColor(PlaceActivity.this, R.color.colorPrimary)));
                    mTabLayout.setBackground(ContextCompat.getDrawable(PlaceActivity.this, R.drawable.indicador_overlay));

                }

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.dataBaseHandler.isFavorito(idLugar)){
                    MainActivity.dataBaseHandler.addFavoritos(idLugar);
                    floatingActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                }else{
                    MainActivity.dataBaseHandler.removeFavoritos(idLugar);
                    floatingActionButton.setImageResource(R.drawable.ic_favorite_border_gray_24dp);
                }
            }
        });

    }
    private void setupViewPager() {
        mTabLayout = findViewById(R.id.tabs);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager =  findViewById(R.id.viewpagerLugar);
        populateViewPager();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lugar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void cargarDatosLocalDB(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = MainActivity.dataBaseHandler.getLugares("SELECT * FROM lugar WHERE id ="+idLugar);
                while(cursor.moveToNext()){
                    String nombre = cursor.getString(1);
                    descripcion = cursor.getString(5);
                    byte[] imagen = cursor.getBlob(6);
                    precio = cursor.getString(7);
                    lat = Double.parseDouble(cursor.getString(2));
                    lng = Double.parseDouble(cursor.getString(3));
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imagen, 0, imagen.length);
                    imageView.setImageBitmap(bitmap);
                    getSupportActionBar().setTitle(nombre);
                }

                if(MainActivity.dataBaseHandler.isFavorito(idLugar)){ //si es favorito
                    floatingActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                    //floatingActionButton.setBackgroundColor(Color.WHITE);
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void populateViewPager() {
        TabDetails tab;
        tab = new TabDetails("Informacion",
                PlaceholderFragment.newInstance(R.layout.fragment_lugar_informacion, 1));
        mSectionsPagerAdapter.addFragment(tab);
        tab = new TabDetails("Ubicación",
                PlaceholderFragment.newInstance(R.layout.fragment_lugar_mapa,2));
        mSectionsPagerAdapter.addFragment(tab);
        tab = new TabDetails("Galeria",
                PlaceholderFragment.newInstance(R.layout.fragment_lugar_galeria,3));
        mSectionsPagerAdapter.addFragment(tab);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
    public static class PlaceholderFragment extends Fragment implements OnMapReadyCallback{
        private static final String ARG_LAYOUT = "layout";
        private static final String ARG_COUNTER = "counter";

        ImageGalleryAdapter adapter;
        RecyclerView recyclerView;

        public PlaceholderFragment() {
        }
        public static PlaceholderFragment newInstance(int layout, Integer counter) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_LAYOUT, layout);
            args.putInt(ARG_COUNTER, counter);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(getArguments().getInt(ARG_LAYOUT), container, false);
            Integer counter = getArguments().getInt(ARG_COUNTER);
           if (counter != 0) {
               switch (counter){
                   case 1: //descripcion
                       TextView descrip,precioL;
                       descrip = rootView.findViewById(R.id.textVDescripcionL);
                       precioL = rootView.findViewById(R.id.precioLugar);
                       descrip.setText(descripcion);
                       precioL.setText(precio);
                       break;
                   case 2: //mapa
                       SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                               .findFragmentById(R.id.mapa_ubicacion);
                       if (mapFragment != null) {
                           mapFragment.getMapAsync(this);
                       }
                       break;
                   case 3://galeria
                       apiGaleria(getString(R.string.url_api_galeria)+idLugar);
                       RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                       recyclerView = rootView.findViewById(R.id.rv_images);
                       recyclerView.setHasFixedSize(true);
                       recyclerView.setLayoutManager(layoutManager);
                       break;
               }
           }
            return rootView;
        }

        private void apiGaleria(String url){
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        spacePhotos(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("ERROR",e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("ERROR",error.getMessage());
                }
            });
            requestQueue.add(stringRequest);
        }
        Photo[] photos;
        String [] urls;
        private void spacePhotos(JSONArray jsonArray){
            photos = new Photo[jsonArray.length()];
            urls = new String[jsonArray.length()];
            JSONObject jsonObject= null;
            for (int i=0; i<jsonArray.length(); i++) {
                try {
                    jsonObject = jsonArray.getJSONObject(i);
                    photos[i] =  new Photo( jsonObject.optString("url"), "");
                    urls[i] = jsonObject.optString("url");
                } catch (JSONException e) {
                    Log.i("ERROR:",e.getMessage());
                }
            }
            Photo.pothoVector = photos;
            adapter = new ImageGalleryAdapter(getContext(), photos);
            recyclerView.setAdapter(adapter);
        }
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
           try {
                boolean success = googleMap.setMapStyle( MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.stylemap));

            } catch (Resources.NotFoundException e) {
                Log.e("k", "Can't find style. Error: ", e);
            }
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
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<TabDetails> tabs = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabs.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        private void addFragment(TabDetails tab) {
            tabs.add(tab);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position).getTabName();
        }
    }



}
