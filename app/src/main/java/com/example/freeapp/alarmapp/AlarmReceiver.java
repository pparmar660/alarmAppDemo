package com.example.freeapp.alarmapp;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        //this will update the UI with message
//        MainActivity inst = MainActivity.instance();
//        inst.setAlarmText("Alarm! Wake up! Wake up!");

        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)

        DatabaseHandler databaseHandler=new DatabaseHandler(context);


        Calendar calendar = Calendar.getInstance();


        ArrayList<AlarmModel> alarmList;
        alarmList=databaseHandler.getListOfAllRecord();
        boolean showAlarm=false;
        for(int i=0;i<alarmList.size();i++)
        {  AlarmModel model=alarmList.get(i);

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(MainActivity.dateFormat);
            try {
                cal.setTime(sdf.parse(model.getDateTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String time1=sdf.format(calendar.getTime());
            String time2=model.getDateTime();


            if(time1.equalsIgnoreCase(time2)){
                if(model.getToggle()>0) showAlarm=true;
            }
        }

         if(!showAlarm)
             return;

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}