package com.example.freeapp.alarmapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    private void parsedata(String data_str2) {
        // TODO Auto-generated method stub
        my_parser parse_obj = new my_parser(context);


    }
}
