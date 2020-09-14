package com.example.phonelocation;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyLocationListener implements LocationListener {
    Context context;

    MyLocationListener(Context context){
        this.context = context;
    }

    @Override
    public void onLocationChanged(Location location) {

        Toast.makeText(context, (location.getLongitude()) + " ||| "
                + (location.getLatitude()), Toast.LENGTH_SHORT).show();

        String myUrl = "http://api.openweathermap.org/data/2.5/weather?lat=" + location.getLatitude()
                + "&lon=" + location.getLongitude() + "&appid=af48b01e6fafefdeb1df4b83b2218877";
        new CallWebService(context).execute(myUrl);


        String myUrl2 = "http://10.0.2.2/findphonelocation/location.php?log=" + location.getLongitude()
                + "&lat=" + location.getLatitude();
        new MyAsynTaskInsertData().execute(myUrl2);

        String myUrl3 = "https://api.openweathermap.org/data/2.5/onecall?lat=" + location.getLatitude()
                + "&lon=" + location.getLongitude() + "&appid=af48b01e6fafefdeb1df4b83b2218877";
        new GetForecast(context).execute(myUrl3);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
//        Toast.makeText(context, "Provider status changed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
//        Toast.makeText(context, "Gps turned off . you cannot follow your location", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
//        Toast.makeText(context, "Gps turned on . you can follow ur location", Toast.LENGTH_LONG).show();
    }

    String result = "";
    public class MyAsynTaskInsertData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            result = "";
        }

        @Override
        protected String doInBackground(String... strings) {
            publishProgress("open connection");
            // this is how to connect to the server !
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                publishProgress("start read buffer");
                stream2string(inputStream);
                inputStream.close();

            }
            catch (Exception e){
                e.printStackTrace();
                publishProgress("cannot connect to server");
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
//            textViewResult.setText(values[0]);
//            textViewResult.setText(result);
        }

        @Override
        protected void onPostExecute(String se) {
            super.onPostExecute(se);
        }
    }

    public class MyAsynTaskForecast extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            result = "";
        }

        @Override
        protected String doInBackground(String... strings) {
            publishProgress("open connection");
            // this is how to connect to the server !
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                publishProgress("start read buffer");
                stream2string(inputStream);
                inputStream.close();

            }
            catch (Exception e){
                e.printStackTrace();
                publishProgress("cannot connect to server");
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
//            textViewResult.setText(values[0]);
//            textViewResult.setText(result);
        }

        @Override
        protected void onPostExecute(String se) {
            super.onPostExecute(se);
        }
    }

    // to convert the data stream to a string ( to can read it)
    private void stream2string(InputStream isr) {
        try{

            BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            isr.close();
            result=sb.toString();
        }
        catch(Exception e){
            Log.e("log_tag", "Error  converting result " + e.toString());
        }
    }
}