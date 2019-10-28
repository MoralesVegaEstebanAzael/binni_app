package com.example.proyectoemergentes.adapter;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.dataBase.DataBaseHandler;
import com.example.proyectoemergentes.pojos.Imagen;
import com.example.proyectoemergentes.pojos.Lugar;

import java.util.ArrayList;

public class AdapterImagen extends RecyclerView.Adapter<AdapterImagen.MyViewHolder> {
    private ArrayList<Imagen> imagenesList;
    private Context context;
    private int width;
    private int height;
    public AdapterImagen(Context context, ArrayList<Imagen> imagenesList) {
        this.context = context;
        this.imagenesList = imagenesList;

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        width = metrics.widthPixels; // ancho absoluto en pixels
        height = metrics.heightPixels; // alto absoluto en pixels
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_place, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Imagen imagen = imagenesList.get(position);
        byte[] lugarImagen = imagen.getImagen();
        Glide.with(context)
                .load(lugarImagen)
                .thumbnail()
                .into(holder.imageView);
        holder.textView.setText("from DB");
    }
    @Override
    public int getItemCount() {
        return imagenesList.size();
    }

  /*  @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        animateCircularReveal(holder.itemView);
    }

    public void animateCircularReveal(View view){
        int centerX = 0;
        int centerY = 0;
        int startRadius = 0;
        int endRadius = Math.max(view.getWidth(),view.getHeight());
        Animator animator = ViewAnimationUtils.createCircularReveal(view,centerX,centerY,startRadius,endRadius);
        view.setVisibility(View.VISIBLE);
        animator.start();
    }*/

    public  class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        private ImageView fav_image;
        private CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.place_img);
            textView = itemView.findViewById(R.id.textVLugarNombre);
            fav_image = itemView.findViewById(R.id.fav);

        }
    }
}
