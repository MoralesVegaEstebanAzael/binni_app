package com.example.proyectoemergentes;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoemergentes.adapter.AdapterItemShoppingCart;
import com.example.proyectoemergentes.dataBase.Constantes;
import com.example.proyectoemergentes.pojos.ItemShoppingCart;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ShoppingActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rvShopping;
    private ArrayList<ItemShoppingCart> listItems;
    private AdapterItemShoppingCart adapterItemShoppingCart;
    private TextView textViewTotal,textViewItems;
    private Button btnComprar;
    private int itemsCount;
    private double totalCompra;
    private static final int PAYPAL_REQUEST_CODE=7171;
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Constantes.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        init();
        AsyntaskLoadItems asyntaskLoadAnuncios = new AsyntaskLoadItems();
        asyntaskLoadAnuncios.execute();
    }

    private void init(){
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        rvShopping = findViewById(R.id.recyclerShopping);
        rvShopping.setHasFixedSize(true);
        listItems = new ArrayList<ItemShoppingCart>();
        rvShopping.setLayoutManager(layoutManager);
        adapterItemShoppingCart = new AdapterItemShoppingCart(this,listItems);
        rvShopping.setAdapter(adapterItemShoppingCart);

        textViewTotal = findViewById(R.id.compraTotal);
        textViewItems = findViewById(R.id.comprarItems);
        btnComprar = findViewById(R.id.btnComprar);
        btnComprar.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(getString(R.string.title_shopping_activity));
    }

    private void itemsFromLocalDB(){
        Cursor cursor = MainActivity.dataBaseHandler.select("SELECT * FROM shopping_cart");
        ItemShoppingCart itemShoppingCart;
        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            String nombre=cursor.getString(1);
            String fecha = cursor.getString(2);
            String personas = cursor.getString(3);
            String precio = cursor.getString(4);
            totalCompra +=Double.parseDouble(precio);
            itemShoppingCart= new ItemShoppingCart(id,nombre,fecha,personas,precio);
            listItems.add(itemShoppingCart);
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);
        procesarPago();
    }

    private void procesarPago(){
        //recibir el monto
        PayPalPayment payPalPayment = new PayPalPayment(
                new BigDecimal(String.valueOf(totalCompra)),
                "MXN","Pagador por",PayPalPayment.PAYMENT_INTENT_SALE);
        //enviamos los parametros
        Intent intent= new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(
                        PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetailActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                //.putExtra("ID_ENVIO",idEnvio)
                                .putExtra("PaymentAmount", totalCompra));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("EROR EN PAGO", e.getMessage());
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancelada", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalidada", Toast.LENGTH_SHORT).show();
    }

    private class AsyntaskLoadItems extends AsyncTask<Void,Integer,Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            itemsFromLocalDB();
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Resources res =getResources();
            String total = String.format(res.getString(R.string.compras_total), totalCompra+"");
            textViewTotal.setText(total);
            itemsCount = MainActivity.dataBaseHandler.getCountShoppingCart();
            String items = String.format(res.getString(R.string.compras_items), itemsCount+"");
            textViewItems.setText(items);
            adapterItemShoppingCart.notifyDataSetChanged();

        }

        @Override
        protected void onCancelled() {
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
