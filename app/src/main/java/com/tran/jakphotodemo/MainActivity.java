package com.tran.jakphotodemo;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMainFragment(savedInstanceState);
    }

    // adding a layer of modularity rather than puting everything on the MainActivity
    private void setMainFragment(Bundle savedInstanceState) {

        // if savedInstanceState exists, we know that the fragment already exist
        if (savedInstanceState == null) {
            MainFragment mFrag = new MainFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.activity_main, mFrag, "main_frag").commit();
        }
    }
}
