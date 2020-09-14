package com.example.phonelocation.util;

import java.util.Calendar;

public class BgColor {
    private boolean checkCurrentTime(){
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY); //Current hour
        return currentHour < 18; //False if after 6pm
    }
}
