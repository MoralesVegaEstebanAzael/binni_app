package com.example.proyectoemergentes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyectoemergentes.R;

public class FullScreenGaleryAdapter extends PagerAdapter {
    private Context context;
    private String[] imagenes;
    private LayoutInflater layoutInflater;
    public FullScreenGaleryAdapter(Context context,String[] imagenes){
        this.context=context;
        this.imagenes = imagenes;
    }
    @Override
    public int getCount() {
        return imagenes.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
       layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View v = layoutInflater.inflate(R.layout.full_item,null);
        ImageView imageView = v.findViewById(R.id.itemImgFull);
       // Glide.with(context).load(imagenes[position]).apply(new RequestOptions().centerInside()).into(imageView);
        Glide.with(context)
                .load(imagenes[position])
                .centerCrop()
                .into(imageView);

        ViewPager vp = (ViewPager)container;
        vp.addView(v,0);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager)container;
        View v = (View)object;
        viewPager.removeView(v);
    }
}
