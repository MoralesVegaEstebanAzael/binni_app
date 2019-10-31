package com.example.proyectoemergentes.adapter;

import android.animation.Animator;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.example.proyectoemergentes.MainActivity;
import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.dataBase.DataBaseHandler;
import com.example.proyectoemergentes.pojos.Lugar;
import com.example.proyectoemergentes.ui.favoritos.FavoritosFragment;

import java.util.ArrayList;

public class AdapterLugar extends RecyclerView.Adapter<AdapterLugar.MyViewHolder> {
    private ArrayList<Lugar> placesArray;
    private Context context;
    private DataBaseHandler localDB;
    private View view;
    public AdapterLugar(Context context, ArrayList<Lugar> placesArray) {
        this.context = context;
        this.placesArray = placesArray;
        localDB = new DataBaseHandler(context);
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
        final Lugar lugar = placesArray.get(position);
        byte[] lugarImagen = lugar.getImagen();
        Glide.with(context)
                .load(lugarImagen)
                .thumbnail()
                .into(holder.imageView);
        holder.textViewNombre.setText(lugar.getNombre());

        if(localDB.isFavorito(lugar.getId())){ //si es favorito
            holder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
        holder.fav_image.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fragment frg = null;
            frg = ((FragmentActivity)context).getSupportFragmentManager().findFragmentByTag("FAVTAG");

            if(!localDB.isFavorito(lugar.getId())){
                localDB.addFavoritos(lugar.getId());
                holder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                Toast.makeText(context,R.string.accion_agregar_favorito,Toast.LENGTH_SHORT).show();
            }else{
                localDB.removeFavoritos(lugar.getId());
                holder.fav_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                Toast.makeText(context,R.string.accion_eliminar_favorito,Toast.LENGTH_SHORT).show();
            }

            if(frg!=null){
                final FragmentTransaction ft = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Lanzar activity",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return placesArray.size();
    }
    /*
    @Override
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
    }

    Glide.with(context)
                .load(lugar.getImage())
                .into(holder.imageView);
*/
    public  class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textViewNombre;
        private ImageView fav_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = itemView.findViewById(R.id.place_img);
            textViewNombre = itemView.findViewById(R.id.textVLugarNombre);
            fav_image = itemView.findViewById(R.id.fav);
        }
    }



}
