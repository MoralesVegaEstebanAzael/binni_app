package com.example.proyectoemergentes.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.pojos.Anuncio;


import java.util.ArrayList;


public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {
    private Context context;
    //array imagenes test
//    private int[] imagenes = new int[]{
//            R.drawable.beach,R.drawable.beer2
//    };
    private ArrayList<Anuncio> imagenArrayList;

    public SliderAdapter(Context context,ArrayList<Anuncio> imageneArrayList){
        this.context = context;
        this.imagenArrayList = imageneArrayList;
    }
    @Override
    public int getCount() {
        return imagenArrayList.size();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_slider, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Elemento en posicion " + position, Toast.LENGTH_SHORT).show();
            }
        });

        //aza code
        final Anuncio imagen = imagenArrayList.get(position);
        byte[] anuncioImagen = imagen.getImagen();
        Glide.with(context)
                .asDrawable()
                .load(anuncioImagen)
//                .thumbnail()
                .error(R.drawable.icon404)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("IMAGE LOAD::: ","Error cargando la el anuncio",e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(viewHolder.imageViewBackground);
        viewHolder.imageViewBackground.setScaleType(ImageView.ScaleType.FIT_XY);

    }


    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView imageViewBackground;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.imagen_view);
            this.itemView = itemView;
        }
    }

}



