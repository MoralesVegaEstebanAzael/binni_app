package com.example.proyectoemergentes.ui.perfil;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.ui.dashboard.DashboardViewModel;


public class PerfilFragment extends Fragment {
    private PerfilViewModel perfilViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        perfilViewModel =
                ViewModelProviders.of(this).get(PerfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);
        final TextView textView = root.findViewById(R.id.text_perfil);
        perfilViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
