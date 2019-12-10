package com.example.proyectoemergentes.ui.codigoQR;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.ui.PlaceActivity;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class CodigoQR extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMARA=1;
    private ZXingScannerView scannerView;
    private Button buttonCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_qr);
        buttonCancelar = findViewById(R.id.btn_cancelar);
        scannerView= findViewById(R.id.scanner);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkPermission()){
            }else {
                Toast.makeText(CodigoQR.this, "Permisos concedidos", Toast.LENGTH_LONG).show();
                requestPermision();
            }
        }

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private boolean checkPermission(){
        return (ContextCompat.checkSelfPermission( getApplicationContext(), CAMERA)==PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermision(){
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},REQUEST_CAMARA);
    }

    public void onRequestPermissiosResult(int requestCode, String permission[], int grantResults[]){
        switch (requestCode){
            case REQUEST_CAMARA :
                if(grantResults.length>0){
                    boolean cameraAccepted= grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        Toast.makeText(CodigoQR.this, "Permisos concedidos", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(CodigoQR.this, "Permiso denegado", Toast.LENGTH_LONG).show();
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                            if(shouldShowRequestPermissionRationale(CAMERA)){
                                displayAlertMessage("Necesitas das permisos a la aplicacion",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                                                    requestPermissions(new String[]{CAMERA}, REQUEST_CAMARA);
                                                }
                                            }
                                        }
                                );
                                return;
                            }
                        }

                    }
                }
            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                if(scannerView==null){
                    scannerView= new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }else{
                requestPermision();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(getApplicationContext())
                .setMessage(message)
                .setPositiveButton("Aceptar", listener)
                .setNegativeButton("Salir", null)
                .create()
                .show();
    }
    @Override
    public void handleResult(final Result result) {
        final String scanResult= result.getText();
        /*Log.d("QRCodeScanner", result.getText());
        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());

       AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Resultado del scan");
        builder.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                scannerView.resumeCameraPreview(CodigoQR.this);
            }
        });
        builder.setNeutralButton("Visitar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(scanResult));
                //startActivity(intent);

            }
        });
        builder.setMessage(result.getText());
        AlertDialog alert= builder.create();
        alert.show();*/

        Intent intent = new Intent (this, PlaceActivity.class);
        intent.putExtra("ID_LUGAR",scanResult);
        startActivity(intent);
        this.finish();
    }
}
