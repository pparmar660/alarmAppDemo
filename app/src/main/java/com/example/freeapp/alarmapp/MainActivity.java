package com.example.freeapp.alarmapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    Context context;
    my_parser parse_obj;
    TextView resultView;
    EditText editText;
    Button button;
    ImageView mikeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final my_parser parse_obj=  new my_parser(context);

        resultView=(TextView)findViewById(R.id.restultText);
        editText=(EditText) findViewById(R.id.editText);
        button=(Button)findViewById(R.id.getData);
        mikeImage=(ImageView) findViewById(R.id.mikeImage);


        mikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resultView.setText(parse_obj.parse_date(editText.getText().toString()).get("Start_Time"));

            }
        });


    }

}
