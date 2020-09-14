package com.example.phonelocation;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phonelocation.R;
import com.example.phonelocation.util.UnitUtility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GetForecast extends AsyncTask<String, String, String> {

    String data = "";
    Context context;

    GetForecast(Context context){
        this.context = context;
    }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            data = "";
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

        }

        @Override
        protected void onPostExecute(String se) {
            super.onPostExecute(se);
            UnitUtility unitUtility = new UnitUtility();
            try{

                JSONObject Jasonobject = new JSONObject(data);

                JSONArray JasonobjectDaily =Jasonobject.getJSONArray("daily");
                for(int i = 0; i < JasonobjectDaily.length(); i++) {
                    JSONObject dailyForecast = JasonobjectDaily.getJSONObject(i);
                    /* gte the temp of the whole week */
                    JSONObject getTemp = dailyForecast.getJSONObject("temp");

                    /* gte the days of the week */
                    Date getDay= new Date(dailyForecast.getInt("dt") * 1000L);
                    SimpleDateFormat df = new SimpleDateFormat("EEE", Locale.getDefault());
                    String formattedDate = df.format(getDay);

                    /* gte the icons of the whole week */
                    JSONArray getWeather = dailyForecast.getJSONArray("weather");
                    JSONObject daily = getWeather.getJSONObject(0);
                    String iconUrl = "http://openweathermap.org/img/w/" + daily .getString("icon") + ".png";

                    Toast.makeText(context, Double.toString(getTemp.getDouble("day")), Toast.LENGTH_SHORT).show();
                    TextView textViewTemp1 = (TextView) ((Activity)context).findViewById(R.id.textViewTemp1) ;
                    TextView textViewTemp2 = (TextView) ((Activity)context).findViewById(R.id.textViewTemp2) ;
                    TextView textViewTemp3 = (TextView) ((Activity)context).findViewById(R.id.textViewTemp3) ;
                    TextView textViewTemp4 = (TextView) ((Activity)context).findViewById(R.id.textViewTemp4) ;
                    TextView textViewTemp5 = (TextView) ((Activity)context).findViewById(R.id.textViewTemp5) ;
                    TextView textViewTemp6 = (TextView) ((Activity)context).findViewById(R.id.textViewTemp6) ;
                    TextView textViewTemp7 = (TextView) ((Activity)context).findViewById(R.id.textViewTemp7) ;

                    TextView textViewDay1 = (TextView) ((Activity)context).findViewById(R.id.textViewDay1) ;
                    TextView textViewDay2 = (TextView) ((Activity)context).findViewById(R.id.textViewDay2) ;
                    TextView textViewDay3 = (TextView) ((Activity)context).findViewById(R.id.textViewDay3) ;
                    TextView textViewDay4 = (TextView) ((Activity)context).findViewById(R.id.textViewDay4) ;
                    TextView textViewDay5 = (TextView) ((Activity)context).findViewById(R.id.textViewDay5) ;
                    TextView textViewDay6 = (TextView) ((Activity)context).findViewById(R.id.textViewDay6) ;
                    TextView textViewDay7 = (TextView) ((Activity)context).findViewById(R.id.textViewDay7) ;

                    ImageView imageView1 = (ImageView) ((Activity)context).findViewById(R.id.imageView1) ;
                    ImageView imageView2 = (ImageView) ((Activity)context).findViewById(R.id.imageView2) ;
                    ImageView imageView3 = (ImageView) ((Activity)context).findViewById(R.id.imageView3) ;
                    ImageView imageView4 = (ImageView) ((Activity)context).findViewById(R.id.imageView4) ;
                    ImageView imageView5 = (ImageView) ((Activity)context).findViewById(R.id.imageView5) ;
                    ImageView imageView6 = (ImageView) ((Activity)context).findViewById(R.id.imageView6) ;
                    ImageView imageView7 = (ImageView) ((Activity)context).findViewById(R.id.imageView7) ;

                    int celsius = unitUtility.toCelsius(unitUtility.toFar((float) getTemp.getDouble("day")));
                    if(i == 0) {
                        textViewTemp1.setText(celsius + "°");
                        textViewDay1.setText(formattedDate.toUpperCase());
                        Picasso.with(context).load(iconUrl).into(imageView1);
                    }
                    if(i == 1) {
                        textViewTemp2.setText(celsius + "°");
                        textViewDay2.setText(formattedDate.toUpperCase());
                        Picasso.with(context).load(iconUrl).into(imageView2);
                    }
                    if(i == 2) {
                        textViewTemp3.setText(celsius + "°");
                        textViewDay3.setText(formattedDate.toUpperCase());
                        Picasso.with(context).load(iconUrl).into(imageView3);
                    }
                    if(i == 3) {
                        textViewTemp4.setText(celsius + "°");
                        textViewDay4.setText(formattedDate.toUpperCase());
                        Picasso.with(context).load(iconUrl).into(imageView4);
                    }
                    if(i == 4) {
                        textViewTemp5.setText(celsius + "°");
                        textViewDay5.setText(formattedDate.toUpperCase());
                        Picasso.with(context).load(iconUrl).into(imageView5);
                    }
                    if(i == 5) {
                        textViewTemp6.setText(celsius + "°");
                        textViewDay6.setText(formattedDate.toUpperCase());
                        Picasso.with(context).load(iconUrl).into(imageView6);
                    }
                    if(i == 6) {
                        textViewTemp7.setText(celsius + "°");
                        textViewDay7.setText(formattedDate.toUpperCase());
                        Picasso.with(context).load(iconUrl).into(imageView7);
                    }

                }
            }catch (Exception e){

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
            data = sb.toString();
        }
        catch(Exception e){
            Log.e("log_tag", "Error  converting result " + e.toString());
        }
    }
}
