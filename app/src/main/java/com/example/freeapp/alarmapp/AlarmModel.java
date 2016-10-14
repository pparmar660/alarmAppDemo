package com.example.freeapp.alarmapp;

import android.content.Intent;

/**
 * Created by Parvesh on 12-10-2016.
 */

public class AlarmModel {

    String message,dateTime;
    int id,toggle=0;
     AlarmModel(){}

     AlarmModel(int id, String message,String dateTime,int toggle){

        this.id=id;
        this.message=message;
        this.dateTime=dateTime;
        this.toggle=toggle;
    }


    AlarmModel(String message,String dateTime,int toggle){

        this.message=message;
        this.dateTime=dateTime;
        this.toggle=toggle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getToggle() {
        return toggle;
    }

    public void setToggle(int toggle) {
        this.toggle = toggle;
    }
}
