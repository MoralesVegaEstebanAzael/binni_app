package com.example.proyectoemergentes.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectoemergentes.LugarActivity;
import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.dataBase.DataBaseHandler;
import com.example.proyectoemergentes.pojos.GaleriaImagen;
import com.example.proyectoemergentes.pojos.Lugar;
import com.example.proyectoemergentes.ui.FullScreenGaleriaActivity;

import java.util.ArrayList;

public class AdapterGaleria extends RecyclerView.Adapter<AdapterGaleria.MyViewHolder> {
    private Context context;
    private DataBaseHandler localDB;
    private String[] imagenes;


    public AdapterGaleria(Context context, String[] imagenArray) {
        this.context = context;
        this.imagenes = imagenArray;
        localDB = new DataBaseHandler(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_galeria, parent, false);
        return new MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(final AdapterGaleria.MyViewHolder holder, final int position) {
        String url = imagenes[position];
        Glide.with(context)
                .load(url)
                .centerCrop()
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Bundle b=new Bundle();
            b.putStringArray("strings",imagenes);
            b.putInt("posicion",position);
            Intent intent=new Intent(context,FullScreenGaleriaActivity.class);
            intent.putExtras(b);
            context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return imagenes.length;
    }
    public  class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = itemView.findViewById(R.id.lugar_imagen);
        }
    }
}
