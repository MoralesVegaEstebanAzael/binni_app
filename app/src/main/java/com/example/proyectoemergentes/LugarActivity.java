package com.example.proyectoemergentes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import xyz.hanks.library.bang.SmallBangView;

public class LugarActivity extends AppCompatActivity {
    private String idLugar;
    private ImageView imageView;
    private TextView textViewDescripcion;

    private ImageView fav_image;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugar);

        AsyncTaskLoadDB asyncTaskLoadDB = new AsyncTaskLoadDB();
        asyncTaskLoadDB.execute();
    }

    private void init(){
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idLugar = extras.getString("ID_LUGAR");
        }
        final Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textViewDescripcion = findViewById(R.id.textVDescripcion);
        imageView = findViewById(R.id.img_lugar);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);

        fav_image = findViewById(R.id.favorito);

        //collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final SmallBangView like = findViewById(R.id.like_h);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (like.isSelected()) {
                    like.setSelected(false);
                } else {
                    like.setSelected(true);
                    like.likeAnimation(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if(!MainActivity.dataBaseHandler.isFavorito(idLugar)){
                                MainActivity.dataBaseHandler.addFavoritos(idLugar);
                                fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                            }else{
                                MainActivity.dataBaseHandler.removeFavoritos(idLugar);
                                fav_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            }
                        }
                    });
                }
            }
        });

    }

    private void cargarDatosLocalDB(){
       Cursor cursor = MainActivity.dataBaseHandler.getLugares("SELECT * FROM lugar WHERE id ="+idLugar);
        /* "(id STRING PRIMARY KEY, nombre STRING, lat STRING,lng STRING,categoria STRING" +
            ",descripcion STRING,imagen BLOB)";
        * */
        while(cursor.moveToNext()){
            String nombre = cursor.getString(1);
            String descripcion = cursor.getString(5);
            byte[] imagen = cursor.getBlob(6);
            textViewDescripcion.setText(descripcion);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagen, 0, imagen.length);
            imageView.setImageBitmap(bitmap);
            collapsingToolbarLayout.setTitle(nombre);
        }

        if(MainActivity.dataBaseHandler.isFavorito(idLugar)){ //si es favorito
            fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
    }

    private class AsyncTaskLoadDB extends AsyncTask<Void,Integer,Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            init();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {//acceso a la BD local en segundo plano
            if(idLugar!=""){
                cargarDatosLocalDB();
                publishProgress(1);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
