package com.example.freeapp.alarmapp;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import android.content.Context;

public class my_parser {
    Context ctx;
    List<DateGroup> groups;
    Parser parser;

    public my_parser(Context ctx) {

        this.ctx = ctx;

    }

    @SuppressWarnings("deprecation")
    public Hashtable<String, String> parse_date(String str) {

        // public void parse_date(String str) {

        //
        Hashtable<String, String> source = new Hashtable<String, String>();
        String date = "";
        String time_info = "";
        String all_day = "false";
        String time_val = "";

        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse(str);

        List<Date> dateList = new ArrayList<Date>();

        for (DateGroup group : groups) {

            List<Date> dates = group.getDates();
            time_info = group.getText();

            int line = group.getLine();
            int column = group.getPosition();

            String matchingValue = group.getText();
            String syntaxTree = group.getSyntaxTree().toStringTree();
            Map parseMap = group.getParseLocations();
            boolean isRecurreing = group.isRecurring();
            Date recursUntil = group.getRecursUntil();

			/*
			 * if any Dates are present in current group then add them to
			 * dateList
			 */
            if (group.getDates() != null) {
                dateList.addAll(group.getDates());
            }
        }

        if (dateList.isEmpty()) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat(
                    "EEE MMM dd HH:mm:ss z yyyy");
            date = df.format(c.getTime());
            all_day = "true";

        } else {

            Calendar c = Calendar.getInstance();
            Date my_dat = dateList.get(0);
            SimpleDateFormat df = new SimpleDateFormat(
                    "EEE MMM dd HH:mm:ss z yyyy");

            date = df.format(my_dat.getTime());

            if (my_dat.getHours() == c.get(Calendar.HOUR_OF_DAY)
                    && my_dat.getMinutes() == c.get(Calendar.MINUTE)) {
                all_day = "true";
            } else {

                all_day = "false";
                SimpleDateFormat df2 = new SimpleDateFormat("HH:mm");
                time_val = df2.format(my_dat.getTime());
            }

        }

//		 System.out.println("Look date list " + date);
//		 date = "Mon Apr 3 18:32:00 GMT 2014";
//		 all_day = "false";

        source.put("Event", str.replace(time_info, ""));
        source.put("Start_Time", date);
        source.put("time", time_val);
        source.put("All_day", all_day);

        System.out.println("Data  source " + source);
        return source;

    }

    public String getLocatio(String str) {

        String loc = "";

        if (str.contains("at")) {
            int j = 0;
            String word[] = str.split(" ");

            for (int i = 0; i < word.length; i++) {

                if (word[i].equalsIgnoreCase("at")) {
                    j = i;
                    break;

                }

            }

            for (int i = j; i < word.length; i++) {

                loc = loc + " " + word[i];
            }
        }
        return loc;

    }

}
