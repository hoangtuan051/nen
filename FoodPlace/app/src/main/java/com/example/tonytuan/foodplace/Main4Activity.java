package com.example.tonytuan.foodplace;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Main4Activity extends AppCompatActivity {

    List<Integer> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        list.add(1);
        list.add(2);
        list.add(3);

        list.add(1, 4);

        for(Integer i : list){
            Toast.makeText(getApplicationContext(), i + " ", Toast.LENGTH_SHORT).show();
        }
    }
}
