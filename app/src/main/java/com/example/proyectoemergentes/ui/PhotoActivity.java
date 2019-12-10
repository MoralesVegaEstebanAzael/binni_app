package com.example.proyectoemergentes.ui;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.pojos.Photo;

public class PhotoActivity extends AppCompatActivity {
    public static final String EXTRA_SPACE_PHOTO = "PhotoActivity.SPACE_PHOTO";
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_photo);

        mImageView = findViewById(R.id.image);
        Photo photo = getIntent().getParcelableExtra(EXTRA_SPACE_PHOTO);
        Glide.with(this)
                .load(photo.getUrl())
                .error(R.drawable.ic_cloud_off_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(mImageView);
    }

}
