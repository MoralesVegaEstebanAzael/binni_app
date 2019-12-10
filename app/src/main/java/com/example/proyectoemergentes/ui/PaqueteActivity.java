package com.example.proyectoemergentes.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectoemergentes.R;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class PaqueteActivity extends AppCompatActivity {
    private ArrayList<String> lugares;
    private String idPaquete;
    private ImageView imageView;
    private AppBarLayout appBarLayout;
    private  Toolbar toolbar;

    RelativeLayout relativeLayout;
    Button viewmore;
    ValueAnimator mAnimator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paquete);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Intent intent=getIntent();
            Bundle b=intent.getExtras();
            lugares=b.getStringArrayList("LUGARES");
            idPaquete = b.getString("ID_PAQUETE");

        }
        init();
        setupCollapsinToolbar();
    }


    private void init(){

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
                    expand();
                } else {
                    collapse();
                }
            }
        });
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

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        relativeLayout.measure(widthSpec, heightSpec);

                        mAnimator = slideAnimator(0, relativeLayout.getMeasuredHeight());
                        return true;
                    }
                });
        //TabLayout tabLayout = findViewById(R.id.tabsPaquete);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_shopping_cart:
                break;
            case R.id.action_favorite:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void expand() {

        relativeLayout.setVisibility(View.VISIBLE);
        mAnimator.start();
    }

    private void collapse() {
        int finalHeight = relativeLayout.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                relativeLayout.setVisibility(View.GONE);
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

}


