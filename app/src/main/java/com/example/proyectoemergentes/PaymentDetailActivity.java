package com.example.proyectoemergentes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.paypal.android.sdk.payments.PayPalService;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetailActivity extends AppCompatActivity {
    TextView txtEstatus, txtMonto;
    private String totalCompra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);
        init();
    }


    private void init(){
        txtEstatus= findViewById(R.id.txtEstatus);
        txtMonto= findViewById(R.id.txtMonto);
        Intent intent= getIntent();
        try {
            JSONObject jsonObject= new JSONObject(intent.getStringExtra("PaymentDetails"));
            totalCompra = intent.getStringExtra("PaymentAmount");
            detalles(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void detalles(JSONObject response, String monto) {
        //seteo de parametros
        try{
            txtEstatus.setText(response.getString("state"));
            txtMonto.setText("TOTAL: $" + monto);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, PayPalService.class));
    }

}
