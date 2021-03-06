package com.example.freeapp.alarmapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Parvesh on 12-10-2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "myAlarmApp";

    // Contacts table name
    public static final String TABLE_ALARMS = "alarms";

    // Contacts Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_DATE_TIME = "dataTime";
    public  static final  String KEY_Toggle="toggle";
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_ALARMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_MESSAGE + " TEXT,"
                + KEY_DATE_TIME + " TEXT," +KEY_Toggle+" INTEGER "+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);

        // Create tables again
        onCreate(db);
    }

    public void addContact(AlarmModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
       // Contact Name
        values.put(KEY_MESSAGE, model.getMessage()); // Contact Phone Number
        values.put(KEY_DATE_TIME, model.getDateTime());
        values.put(KEY_Toggle, model.getToggle());
        // Inserting Row
        db.insert(TABLE_ALARMS, null, values);
        db.close(); // Closing database connection
    }


    public AlarmModel getAlarm(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ALARMS, new String[] { KEY_ID,
                        KEY_MESSAGE, KEY_DATE_TIME,KEY_Toggle }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        AlarmModel alarmModel = new AlarmModel(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),Integer.parseInt(cursor.getString(3)));
        // return contact
        return alarmModel;
    }


    public AlarmModel getAlarmUsingRawQuery(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        AlarmModel alarmModel=new AlarmModel();

        Cursor  cursor = db.rawQuery(query,null);
        if (cursor != null)
            if(cursor.moveToFirst()) {

                 alarmModel = new AlarmModel(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)));
                // return contact
            }
        return alarmModel;
    }


     public ArrayList<AlarmModel> getListOfAllRecord(){
         SQLiteDatabase db = this.getReadableDatabase();
         Cursor  cursor = db.rawQuery("select * from "+ TABLE_ALARMS,null);

         ArrayList<AlarmModel>list=new ArrayList<>();

         if (cursor .moveToFirst()) {

             while (cursor.isAfterLast() == false) {
                 AlarmModel alarmModel = new AlarmModel(Integer.parseInt(cursor.getString(0)),
                         cursor.getString(1), cursor.getString(2),Integer.parseInt(cursor.getString(3)));

                 list.add(alarmModel);
                 cursor.moveToNext();
             }
         }
         return list;
     }

    public boolean deleteRecord(int id)
    {   SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ALARMS, KEY_ID + "=" + id, null) > 0;
    }


    public  void updateRecord(int id, int toggel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data=new ContentValues();
        data.put(KEY_Toggle,toggel);

        db.update(TABLE_ALARMS, data, KEY_ID+"=" + id, null);
    }




}