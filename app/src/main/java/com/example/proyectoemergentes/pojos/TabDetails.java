package com.example.proyectoemergentes.pojos;

import android.os.Bundle;

import com.example.proyectoemergentes.ui.PlaceActivity;

public class TabDetails {
    private String tabName;
    private PlaceActivity.PlaceholderFragment fragment;
    public TabDetails(String tabName, PlaceActivity.PlaceholderFragment fragment) {
        this.tabName = tabName;
        this.fragment = fragment;
    }
    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public PlaceActivity.PlaceholderFragment getFragment() {
        return fragment;
    }

    public void setFragment(PlaceActivity.PlaceholderFragment fragment) {
        this.fragment = fragment;
    }

    public void putExtras(Bundle bundle){
        fragment.setArguments(bundle);
    }

}