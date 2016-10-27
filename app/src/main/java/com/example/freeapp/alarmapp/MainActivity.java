package com.example.freeapp.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    Context context;
    my_parser parse_obj;
 //   TextView resultView;
    EditText editText;
   Button button;
    ImageView mikeImage;
    AlarmAdapter alarmAdapter;
    private PendingIntent pendingIntent;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    DatabaseHandler databaseHandler;
    RecyclerView recyclerView;
    AlarmManager alarmManager;
        ArrayList<AlarmModel> alarmList;
    public static final  String dateFormat="EEE MMM d yyyy 'at' h:mm a";//"EEE MMM dd HH:mm:ss z yyyy"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         parse_obj=  new my_parser(context);

   //     resultView=(TextView)findViewById(R.id.restultText);
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
                if(editText.getText().toString().length()>0) {
                    Hashtable<String, String> outPut = parse_obj.parse_date(editText.getText().toString());
                    SetAlarm(outPut);
                    editText.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                }

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        alarmList=new ArrayList<>();

        alarmList=databaseHandler.getListOfAllRecord();

        Collections.reverse(alarmList);

        alarmAdapter = new AlarmAdapter(alarmList,databaseHandler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(alarmAdapter);



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

                    Hashtable<String, String> outPut = new Hashtable<String, String>();
                   outPut=parse_obj.parse_date(result.get(0));
                    SetAlarm(outPut);

                }
                break;
            }

        }
    }


    public void SetAlarm( Hashtable<String, String> outPut){

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(MainActivity.dateFormat);

        try {
            cal.setTime(sdf.parse(outPut.get("Start_Time")));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);
        alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);

        databaseHandler.addContact(new AlarmModel(
                outPut.get("Event"),outPut.get("Start_Time"),1));
     //   alarmList=databaseHandler.getListOfAllRecord();
       int lastItemId=1;
        if(alarmList.size()>0) {
            alarmList.get(alarmList.size() - 1).getId();
            lastItemId++;
        }
        alarmList.add(0,new AlarmModel(lastItemId,
                outPut.get("Event"),outPut.get("Start_Time"),1));
        alarmAdapter.notifyDataSetChanged();

    }

        }
