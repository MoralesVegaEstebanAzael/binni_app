package com.example.proyectoemergentes.ui.perfil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoemergentes.MainActivity;
import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.ui.dashboard.DashboardViewModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;


public class PerfilFragment extends Fragment {
    private ImageView imageViewAvatar;
    private ImageView imageViewEdit;
    private TextView textViewUser;
    private TextView textViewEmail;
    final int REQUEST_CODE_GALLERY =999;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);
        init(root);
        imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
                cargarImagen();
            }
        });
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE_GALLERY){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent.createChooser(intent,"Seleccione la app"),10);
            }else{
                Toast.makeText(getContext(),R.string.perfil_permiso_galeria,Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
   private void cargarImagen(){
       Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
       intent.setType("image/");
       startActivityForResult(intent.createChooser(intent,"Seleccione la app"),10);
   }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK ){
            try {
                Uri uri = data.getData();
                InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewAvatar.setImageBitmap(bitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();

                MainActivity.dataBaseHandler.updateImagenUsuario("1",bitmapdata);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init(View view){
        imageViewAvatar = view.findViewById(R.id.imageViewAvatar);
        imageViewEdit = view.findViewById(R.id.userEditAvatar);
        textViewUser = view.findViewById(R.id.textViewUserName);
        textViewEmail = view.findViewById(R.id.textViewUserEmail);
        Cursor cursor = MainActivity.dataBaseHandler
                .select("SELECT * FROM usuario WHERE id = 1");

        while(cursor.moveToNext()){
            String nombre = cursor.getString(1);
            byte[] image = cursor.getBlob(3);
            if(image!=null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageViewAvatar.setImageBitmap(bitmap);
            }

            textViewUser.setText(nombre);

        }
    }


}
