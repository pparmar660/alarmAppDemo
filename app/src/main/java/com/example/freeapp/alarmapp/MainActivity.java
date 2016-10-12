package com.example.freeapp.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    Context context;
    my_parser parse_obj;
    TextView resultView;
    EditText editText;
    Button button;
    ImageView mikeImage;
    private PendingIntent pendingIntent;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    DatabaseHandler databaseHandler;

    AlarmManager alarmManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         parse_obj=  new my_parser(context);

        resultView=(TextView)findViewById(R.id.restultText);
        editText=(EditText) findViewById(R.id.editText);
        button=(Button)findViewById(R.id.getData);
        mikeImage=(ImageView) findViewById(R.id.mikeImage);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        databaseHandler=new DatabaseHandler(getApplicationContext());

        mikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resultView.setText(parse_obj.parse_date(editText.getText().toString()).get("Start_Time"));

            }
        });


    }


    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String dateAndTime=parse_obj.parse_date(result.get(0)).get("Start_Time");


                    resultView.setText(dateAndTime);
                    SetAlarm(dateAndTime);
                    //long millis = System.currentTimeMillis();

                    databaseHandler.addContact(new AlarmModel(Integer.parseInt(System.currentTimeMillis()+""),
                            "Wake Up",dateAndTime,1));

                    //txtSpeechInput.setText();
                }
                break;
            }

        }
    }


    public void SetAlarm(String dateStr){

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(
                "EEE MMM dd HH:mm:ss z yyyy");

        try {
            cal.setTime(sdf.parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);
        alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);

    }

}
