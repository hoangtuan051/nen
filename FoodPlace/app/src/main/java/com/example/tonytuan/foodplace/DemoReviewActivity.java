package com.example.tonytuan.foodplace;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.tonytuan.foodplace.Entity.FragmentFactory;

public class DemoReviewActivity extends AppCompatActivity {

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_review);
        fragmentManager = getSupportFragmentManager();
    }

    public void replaceFragment(int key){
        Fragment fragment = FragmentFactory.getFragmentByKey(key);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).show(fragment);
        ft.add(R.id.frMain, fragment);
        ft.commit();
    }
}
