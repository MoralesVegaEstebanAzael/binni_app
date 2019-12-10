package com.example.proyectoemergentes.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoemergentes.MainActivity;
import com.example.proyectoemergentes.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    List<AuthUI.IdpConfig> providers;
    private static final int MyRequestCode=7117;

    public static  final String STRING_PREFERENCES_USUARIO = "com.example.proyectoemergentes";
    public static  final String PREFERENCES_ESTADO_SESION_USUARIO = "estado.sesion.activa.usuario";
    public static final String PREFERENCES_ID_USUARIO="id.usuario.sesion.usuario";
    public static final String PREFERENCES_NOMBRE_USUARIO= "nombre_usuario";
    public static final String PREFERENCES_CORREO_USUARIO= "correo_usuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);

        if(getSesion()){
            // Toast.makeText(this,"true",Toast.LENGTH_SHORT);
            goMainScreen();
            finish();
        }else{
            // Toast.makeText(this,"false",Toast.LENGTH_SHORT);

            //inciciamos provider
            providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.FacebookBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build()
            );
            mostrarOpcionesSignIn();
        }
    }

    private void mostrarOpcionesSignIn() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false)
                        .setAvailableProviders(providers)
                        .setTheme(R.style.GreenTheme)
                        .setLogo (R.drawable.logo_b)
                        .build(),MyRequestCode
        );
    }

    private void guardarEstadoSesion(boolean b) {
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES_USUARIO, MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCES_ESTADO_SESION_USUARIO, b).apply();
    }

    private void guardarDatos(String correo,String nombre) {
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES_USUARIO, MODE_PRIVATE);
        preferences.edit().putString(PREFERENCES_CORREO_USUARIO, correo).apply();
        preferences.edit().putString(PREFERENCES_NOMBRE_USUARIO, nombre).apply();
    }

    private boolean getSesion(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES_USUARIO,MODE_PRIVATE);
        return preferences.getBoolean(PREFERENCES_ESTADO_SESION_USUARIO,false);
    }

    private void guardarIdUsuario(String id){
        SharedPreferences prefs =
                getSharedPreferences(STRING_PREFERENCES_USUARIO,MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFERENCES_ID_USUARIO, id);
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==MyRequestCode){
            IdpResponse response= IdpResponse.fromResultIntent(data);
            if(resultCode==RESULT_OK){
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                guardarEstadoSesion(true);
                guardarIdUsuario(user.getUid()); //guarda el id del usuario en la data
                guardarDatos(user.getEmail(),user.getDisplayName());
                goMainScreen();
            }else{
                // Toast.makeText(this,""+response.getError().getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
