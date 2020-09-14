package com.example.phonelocation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 13;

    private ConstraintLayout mConstraintLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mConstraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayoutId);

        // check the time to change the bg between day and night mode
        if(checkCurrentTime()) {
            mConstraintLayout.setBackground(getDrawable(R.drawable.bg_day));
        }else{
            mConstraintLayout.setBackground(getDrawable(R.drawable.bg_night));
        }

        requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_CODE_ASK_PERMISSIONS);

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                1,
                new MyLocationListener(this));

    }

    // to change between day and night mode background
    private boolean checkCurrentTime(){
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY); //Current hour
        return currentHour < 18; //False if after 6pm
    }
}