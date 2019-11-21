package com.example.proyectoemergentes.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.adapter.FullScreenGaleryAdapter;
import com.example.proyectoemergentes.pojos.GaleriaImagen;

import java.util.ArrayList;

public class FullScreenGaleriaActivity extends Activity {
    private ViewPager viewPager;
    private String[] imagenes;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_galeria);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Intent intent=getIntent();
            Bundle b=intent.getExtras();
            imagenes=b.getStringArray("strings");
            position = b.getInt("posicion");
            viewPager = findViewById(R.id.viewPagerGallery);
            FullScreenGaleryAdapter fullScreenAdapter = new FullScreenGaleryAdapter(this,imagenes);
            viewPager.setAdapter(fullScreenAdapter);
            viewPager.setCurrentItem(position,true);
        }
    }
}
