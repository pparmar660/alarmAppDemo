package com.example.freeapp.alarmapp;

/**
 * Created by Parvesh on 11-10-2016.
 */


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        String mess="Wake Up! Wake Up!";
        DatabaseHandler databaseHandler=new DatabaseHandler(getApplicationContext());
        Calendar calendar = Calendar.getInstance();
        ArrayList<AlarmModel> alarmList;
        alarmList=databaseHandler.getListOfAllRecord();
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
                if(model.getToggle()>0) mess=model.getMessage();
            }
        }








        sendNotification(mess);
    }

    private void sendNotification(String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Alarm").setSmallIcon(R.drawable.alarm)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);


        alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");
    }
}