package com.example.proyectoemergentes;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.proyectoemergentes.dataBase.DataBaseHandler;
import com.example.proyectoemergentes.ui.lugares.LugaresFragment;
import com.example.proyectoemergentes.ui.home.HomeFragment;
import com.example.proyectoemergentes.ui.favoritos.FavoritosFragment;
import com.example.proyectoemergentes.ui.perfil.PerfilFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.transition.FragmentTransitionSupport;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

import static android.graphics.BitmapFactory.decodeResource;

public class MainActivity extends AppCompatActivity {
    public static DataBaseHandler dataBaseHandler;
    NavigationTabBar navigationTabBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //creacion de la base de datos local
         dataBaseHandler = new DataBaseHandler(this);


        // attaching bottom sheet behaviour - hide / show on scroll



        setContentView(R.layout.activity_main);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_lugares,
                R.id.navigation_favoritos,R.id.navigation_perfil)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, new HomeFragment());
        transaction.commit();

        /*BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);*/
        tabbar();
        add();
    }

    private void add(){
        Drawable d = getResources().getDrawable(R.drawable.avatar);
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        dataBaseHandler.addUsuario("1","Azael Morales",
                "azaelmorales@hotmail.com",bitmapdata);

    }

    public void tabbar(){
        final String[] colors= getResources().getStringArray(R.array.colorful);
        navigationTabBar = findViewById(R.id.ntb);
        ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_home_black_24dp),
                        Color.parseColor(colors[0])
                ).title(getString(R.string.title_home)).build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_marcador),
                        Color.parseColor(colors[1])
                ).title(getString(R.string.title_places)).build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_favorito),
                        Color.parseColor(colors[2])
                ).title(getString(R.string.title_favoritos)).build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_usuario),
                        Color.parseColor(colors[3])
                ).title(getString(R.string.title_usuario)).build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setBadgeSize(10);
        navigationTabBar.setIsTinted(true);
        navigationTabBar.setIsSwiped(true);
        navigationTabBar.onPageSelected(1);
        navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);
        navigationTabBar.setBadgePosition(NavigationTabBar.BadgePosition.CENTER);
        navigationTabBar.setIsBadged(true);
        navigationTabBar.setIsTitled(true);
        navigationTabBar.setIsBadgeUseTypeface(true);
        navigationTabBar.setBadgeTitleColor(Color.WHITE);
        navigationTabBar.setTitleSize(25);


        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

               /// FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                //ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
                //ft.add(R.id.primaryLayout, fragment, tag);

               // ft.commit();
                switch (index){
                    case 0:
                        transaction.replace(R.id.nav_host_fragment, new HomeFragment(),"HOMETAG");
                        transaction.addToBackStack("HOMETAG");
                        getSupportActionBar().setTitle(getString(R.string.title_home));
                        transaction.commit();
                        break;
                    case 1:
                        transaction.replace(R.id.nav_host_fragment, new LugaresFragment(),"LUGARESTAG");
                        transaction.addToBackStack("LUGARESTAG");
                        getSupportActionBar().setTitle(getString(R.string.title_places));
                        transaction.commit();
                        break;
                    case 2:
                        transaction.replace(R.id.nav_host_fragment, new FavoritosFragment(),"FAVTAG");
                        getSupportActionBar().setTitle(getString(R.string.title_favoritos));
                        transaction.addToBackStack("FAVTAG");
                        transaction.commit();
                        break;
                    case 3:
                        transaction.replace(R.id.nav_host_fragment, new PerfilFragment(),"PERFILTAG");
                        getSupportActionBar().setTitle(getString(R.string.title_usuario));
                        transaction.addToBackStack("PERFILTAG");
                        transaction.commit();
                        break;

                }

            }

            @Override
            public void onEndTabSelected(NavigationTabBar.Model model, int index) {

            }
        });
    }


    /*
    *  <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/nav_view"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="0dp"
        android:layout_marginEnd="0dp"
        android:background="?android:attr/windowBackground"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:menu="@menu/bottom_nav_menu" />
    * */

}
