package com.example.proyectoemergentes.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.pojos.Photo;
import com.example.proyectoemergentes.ui.FullScreenGaleriaActivity;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>  {
    @Override
    public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.item_view_gallery, parent, false);
        ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {
        Photo photo = mPhotos[position];
        ImageView imageView = holder.mPhotoImageView;
        Glide.with(mContext)
                .load(photo.getUrl())
                .error(R.drawable.ic_cloud_off_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return (mPhotos.length);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mPhotoImageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            mPhotoImageView = itemView.findViewById(R.id.iv_photo);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                   /* Photo spacePhoto = mPhotos[position];
                    Intent intent = new Intent(mContext, PhotoActivity.class);
                    intent.putExtra(PhotoActivity.EXTRA_SPACE_PHOTO, spacePhoto);
                    mContext.startActivity(intent);
*/
                Bundle b=new Bundle();
                b.putStringArray("strings",urls);
                b.putInt("posicion",position);
                Intent intent=new Intent(mContext, FullScreenGaleriaActivity.class);
                intent.putExtras(b);
                mContext.startActivity(intent);
            }
        }
    }
    String[] urls;
    private void getURL(){
        urls = new String[mPhotos.length];
        for(int i = 0; i< mPhotos.length; i++){
            urls[i] = mPhotos[i].getUrl();
        }
    }
    private Photo[] mPhotos;
    private Context mContext;

    public ImageGalleryAdapter(Context context, Photo[] photos) {
        mContext = context;
        mPhotos = photos;
        getURL();
    }

}
