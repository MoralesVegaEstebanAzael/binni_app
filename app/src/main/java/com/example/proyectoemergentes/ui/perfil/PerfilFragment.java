package com.example.proyectoemergentes.ui.perfil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoemergentes.LoginActivity;
import com.example.proyectoemergentes.MainActivity;
import com.example.proyectoemergentes.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;
import static com.example.proyectoemergentes.LoginActivity.PREFERENCES_ESTADO_SESION_USUARIO;
import static com.example.proyectoemergentes.LoginActivity.STRING_PREFERENCES_USUARIO;


public class PerfilFragment extends Fragment {
    private ImageView imageViewAvatar;
    private ImageView imageViewEdit;
    private TextView textViewUser;
    private TextView textViewEmail;
    final int REQUEST_CODE_GALLERY =999;

    private LinearLayout btnSignOut;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_perfil, container, false);
        init(root);

        btnSignOut= root.findViewById(R.id.singout);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LoginActivity.sesion =false;
                AuthUI.getInstance()
                        .signOut(getActivity())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                sesionActiva(false);
                                goMainScreen();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );*/
                cargarImagen();
            }
        });
        return root;
    }

    public void sesionActiva(boolean b){
        SharedPreferences preferences = getActivity().getSharedPreferences(
                STRING_PREFERENCES_USUARIO, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCES_ESTADO_SESION_USUARIO,b).apply();
    }

    private void goMainScreen() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE_GALLERY){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent.createChooser(intent,"Seleccione la app"),10);
            }else{
                Toast.makeText(getContext(),R.string.perfil_permiso_galeria, Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
   private void cargarImagen(){
       Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
       intent.setType("image/");
       startActivityForResult(intent.createChooser(intent,getString(R.string.perfil_permiso_abrir_galeria)),10);
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

                MainActivity.dataBaseHandler.updateImagenUsuario(bitmapdata);
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
       // new Thread(new Runnable() {
         //   public void run() {
                Cursor cursor = MainActivity.dataBaseHandler
                        .getImagen("SELECT * FROM imagen WHERE idimagen = 1");
                while(cursor.moveToNext()){
                    byte[] image = cursor.getBlob(1);
                    if(image!=null){
                        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                        imageViewAvatar.setImageBitmap(bitmap);
                    }
                }
           // }
        //}).start();
    }
}
