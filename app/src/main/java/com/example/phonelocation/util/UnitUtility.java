package com.example.phonelocation.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UnitUtility {


    public static int toCelsius(float temp) {
        return (int) Math.round((temp - 32) / 1.8);
    }



    public static int toFar(float temp) {
        return (int) ((temp - 273.15) * 1.8 + 32);
    }

    public static int toKMH(float val) {
        return (int) Math.round(val * 0.2778);
    }

    public static String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }


}