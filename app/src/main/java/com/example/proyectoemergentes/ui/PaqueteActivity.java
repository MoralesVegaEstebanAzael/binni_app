package com.example.proyectoemergentes.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.ShoppingActivity;
import com.example.proyectoemergentes.dataBase.DataBaseHandler;
import com.example.proyectoemergentes.pojos.Paquete;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class PaqueteActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<String> lugares;
    private String idPaquete;
    private ImageView imageView;
    private AppBarLayout appBarLayout;
    private  Toolbar toolbar;
    RelativeLayout relativeLayout;
    RelativeLayout relativeLayout2;
    ValueAnimator mAnimator;
    private Button btnDecrementar,btnAumentar,btnVerificar,btnAddShopping;
    private TextView textViewNombre,textViewPrecio,textViewContador;
    private TextView textViewNombre2,textViewPrecio2,textViewTotal;
    private int contador;
    private int height, height1;
    private Paquete paquete;
    private double precioUnitario;
    private TextView textCartItemCount;
    private TextView textViewDescripion;
    private DataBaseHandler localDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paquete);

        localDB = new DataBaseHandler(this);
        paquete = (Paquete) getIntent().getSerializableExtra("PAQUETECLASS");
        lugares=paquete.getLugares();
        idPaquete = paquete.getId();
        precioUnitario = Double.parseDouble(paquete.getPrecio());

        init();
        setupCollapsinToolbar();
    }


    private void init(){
        btnDecrementar = findViewById(R.id.btnDecrementar);
        btnAumentar = findViewById(R.id.btnAumentar);
        btnAddShopping = findViewById(R.id.btnAddShopping);
        textViewContador = findViewById(R.id.textVContador);
        textViewNombre = findViewById(R.id.paqueteNombre);
        textViewNombre2 = findViewById(R.id.paqueteNombre2);
        textViewPrecio = findViewById(R.id.paquetePrecio);
        textViewPrecio2 = findViewById(R.id.paquetePrecio2);
        textViewTotal = findViewById(R.id.totalPaquete);
        textViewDescripion = findViewById(R.id.textVDescripcionPaquete);


        Resources res = getResources();
        String precio = String.format(res.getString(R.string.paquete_precio_individual), paquete.getPrecio());


        textViewNombre.setText(""+paquete.getNombre());
        textViewNombre2.setText(""+paquete.getNombre());
        textViewPrecio.setText(precio);
        textViewPrecio2.setText(precio);

        btnVerificar = findViewById(R.id.btnReservar);
        contador=0;

        String descripcion ="";
        for (String lugar:lugares){
            ///Cursor cursor = MainActivity.dataBaseHandler.getLugar(lugar);
            //if(cursor!=null){
                descripcion+= lugar +"\n";
            //}
        }
        textViewDescripion.setText("Recorrido\n " +  descripcion);
        btnAumentar.setOnClickListener(this);
        btnDecrementar.setOnClickListener(this);
        btnVerificar.setOnClickListener(this);
        btnAddShopping.setOnClickListener(this);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                Log.i("DIAA",""+date.getTime().getDay());
                if (relativeLayout.getVisibility() == View.GONE) {
                    expand(relativeLayout, height);
                } else {
                    collapse(relativeLayout);
                }
            }
        });
        relativeLayout2 = findViewById(R.id.layoutComprar);
        relativeLayout = findViewById(R.id.expandable2);
        /*relativeLayout = findViewById(R.id.expandable2);
        viewmore = findViewById(R.id.viewmore2);
        viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (relativeLayout.getVisibility() == View.GONE) {
                    expand();
                } else {
                    collapse();
                }
            }
        });*/


        relativeLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        relativeLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        relativeLayout.setVisibility(View.GONE);
                        relativeLayout2.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        relativeLayout.measure(widthSpec, heightSpec);
                        height = relativeLayout.getMeasuredHeight();
                        height1 = relativeLayout.getMeasuredHeight();
                        relativeLayout2.measure(widthSpec, heightSpec);
                        return true;
                    }
                });
        imageView = findViewById(R.id.imgPaquete);
        //imageView.setImageResource(R.drawable.img_place1);
        appBarLayout = findViewById(R.id.appBarLayoutPaquete);

        Glide.with(this)
                .load("http://s3.amazonaws.com/campeche/wp-content/uploads/2019/09/04100139/Screenshot_2.png")
                .error(R.drawable.ic_cloud_off_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView);

        //
        toolbar = findViewById(R.id.toolbarPaquete);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.inflateMenu(R.menu.toolbar_menu_paquete);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void setupCollapsinToolbar(){
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) { //invisible
                    toolbar.setTitleTextColor(Color.BLACK);
                    toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));

                } else {
                    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
                    toolbar.setTitleTextColor(Color.WHITE);
                    toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.fui_transparent)));
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu_paquete, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_shopping_cart);

        View actionView= MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        int count = localDB.getCountShoppingCart();
        textCartItemCount.setText(count+"");

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_shopping_cart:
                Toast.makeText(this,"Carrito",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ShoppingActivity.class);
                startActivity(intent);
                break;
            case R.id.action_favorite:
                Toast.makeText(this,"Favorito",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void expand(RelativeLayout layout, int layoutHeight) {
        layout.setVisibility(View.VISIBLE);
        ValueAnimator animator = slideAnimator(layout, 0, layoutHeight);
        animator.start();
    }

    private void collapse(final RelativeLayout layout) {
        int finalHeight = layout.getHeight();
        ValueAnimator mAnimator = slideAnimator(layout, finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                layout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }


    private ValueAnimator slideAnimator(final RelativeLayout layout, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
                layoutParams.height = value;
                layout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }



    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = relativeLayout.getLayoutParams();
                layoutParams.height = value;
                relativeLayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btnAumentar:
                if(contador<9)
                    contador++;
                break;
            case R.id.btnDecrementar:
                if(contador>0)
                    contador--;
                break;
            case R.id.btnReservar:
                if (relativeLayout2.getVisibility() == View.GONE && contador!=0) {
                    double total = contador*precioUnitario;
                    Resources res = getResources();
                    String personas = String.format(res.getString(R.string.paquete_personas),contador+"", paquete.getPrecio());
                    textViewPrecio2.setText(personas);
                    textViewTotal.setText("$ "+total);
                    expand(relativeLayout2, height);
                } else {
                    collapse(relativeLayout2);
                }
                break;
            case R.id.btnAddShopping:
                localDB.addShoppingCart(paquete.getId());
                int count =localDB.getCountShoppingCart();
                textCartItemCount.setText(""+count);
                break;
        }
        textViewContador.setText(""+contador);
    }
}


