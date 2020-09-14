package com.example.phonelocation;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.phonelocation.Model.Main;
import com.example.phonelocation.Model.Root;
import com.example.phonelocation.Model.Sys;
import com.example.phonelocation.Model.Weather;
import com.example.phonelocation.Model.Wind;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CallWebService extends AsyncTask<String, String, String> {

    Context context;
    CallWebService(Context context){
        this.context = context;
    }


    String result = "";
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
            result = Stream2String(inputStream);
            inputStream.close();

        } catch (Exception e) {
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
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Root root = new Root();
        Wind wind = new Wind();
        Main main = new Main();
        Sys sys = new Sys();
        Weather weather = new Weather();

        UnitUtility unitUtility = new UnitUtility();
        try {
            Log.i("tagconvertstr", "["+result+"]");
            JSONObject Jsonobject = new JSONObject(result);

            /* get the day and time from the current date */
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("EEEE, H:mm", Locale.getDefault());
            String formattedDate = df.format(c);
            TextView textViewDate = (TextView) ((Activity)context).findViewById(R.id.textViewDate) ;
            textViewDate.setText(formattedDate.toUpperCase());


            /* get name of city from the jsonObject */
            root.setName(Jsonobject.getString("name"));
            Toast.makeText(context, "name : " + root.getName(), Toast.LENGTH_SHORT).show();
            TextView textViewName = (TextView) ((Activity)context).findViewById(R.id.textViewName);
            textViewName.setText(root.getName());


            /* get current time of location from the jsonObject (dt) */
//            Date date = new Date(Jsonobject.getInt("timezone") * 1000L);
//            SimpleDateFormat df = new SimpleDateFormat("EEEE, H:mm", Locale.getDefault());
//            String formattedDate = df.format(date);
//            root.setDt(formattedDate);
//            Toast.makeText(context, "date of location : " + root.getDt(), Toast.LENGTH_SHORT).show();
//            TextView textViewDate = (TextView) ((Activity)context).findViewById(R.id.textViewDate);
//            textViewDate.setText(root.getDt());

            /* get sunSet and Sunrise from sys jsonObject */
            JSONObject JsonSys = Jsonobject.getJSONObject("sys");
            Date dateSunSet = new Date(JsonSys.getInt("sunset") * 1000L);
            Date dateSunRise = new Date(JsonSys.getInt("sunrise") * 1000L);
            sys.setSunrise(unitUtility.formatTime(dateSunRise));
            sys.setSunset(unitUtility.formatTime(dateSunSet));
            Toast.makeText(context, "sunrise " + dateSunRise + " | sunset " + dateSunSet, Toast.LENGTH_LONG).show();
            TextView textViewSunRise = (TextView) ((Activity)context).findViewById(R.id.tvSunRiseValue) ;
            textViewSunRise.setText( sys.getSunrise());
            TextView textViewSunSet = (TextView) ((Activity)context).findViewById(R.id.tvSunSetValue) ;
            textViewSunSet.setText( sys.getSunset());



            /* get speed from wind jsonObject */
            JSONObject JsonWind = Jsonobject.getJSONObject("wind");
            wind.setSpeed(unitUtility.toKMH((float) JsonWind.getDouble("speed")));
            Toast.makeText(context, "wind getter km/h: " + (wind.getSpeed()) + " | " + JsonWind.getDouble("speed"), Toast.LENGTH_SHORT).show();
            TextView textViewWind = (TextView) ((Activity)context).findViewById(R.id.tvWindValue) ;
            textViewWind.setText((int) wind.getSpeed() + "km/h");


            /* get temp from main jsonObject & call a method to convert the value to c° */
            JSONObject JsonMain =Jsonobject.getJSONObject("main");
            main.setHumidity(JsonMain.getInt("humidity"));
            Toast.makeText(context, "humidity: " + main.getHumidity() +"%" , Toast.LENGTH_LONG).show();
            TextView textViewHumidity = (TextView) ((Activity)context).findViewById(R.id.tvHumidityValue) ;
            textViewHumidity.setText( (int) main.getHumidity() + "%");

            int celsius = unitUtility.toCelsius(unitUtility.toFar((float) JsonMain.getDouble("temp")));
            main.setTemp(celsius);
            Toast.makeText(context, "getter temp :  "  + main.getTemp() , Toast.LENGTH_SHORT).show();
            TextView textViewTemp = (TextView) ((Activity)context).findViewById(R.id.textViewTemp) ;
            textViewTemp.setText( (int) main.getTemp() + "°");


            // to get icons --------------------------------------------------------------------
            JSONArray JsonWeather =Jsonobject.getJSONArray("weather");
            for(int i = 0; i < JsonWeather.length(); i++) {
                JSONObject dailyForecast = JsonWeather.getJSONObject(i);
                weather.setIcon(dailyForecast.getString("icon"));
                Toast.makeText(context, "code of icon from getter: " + weather.getIcon(), Toast.LENGTH_SHORT).show();
                String iconUrl = "http://openweathermap.org/img/w/" + weather.getIcon() + ".png";
                ImageView imageViewWeather = (ImageView) ((Activity)context).findViewById(R.id.imageViewWeather);
                Picasso.with(context).load(iconUrl).into(imageViewWeather);


                weather.setDescription(dailyForecast.getString("description"));
                Toast.makeText(context, "description from getter: " + weather.getDescription(), Toast.LENGTH_SHORT).show();
                TextView textViewDescription = (TextView) ((Activity)context).findViewById(R.id.textViewDescription) ;
                textViewDescription.setText(weather.getDescription());




//                iconCode = dailyForecast.getString("icon");
//                Toast.makeText(context, "code of icon : " + iconCode, Toast.LENGTH_SHORT).show();

//                    add these minTemp and maxTemp to array or the
//                    way you want to
            }
//
//                String iconUrl = "http://openweathermap.org/img/w/" + iconCode + ".png";
//                Picasso.with(MainActivity.this).load(iconUrl).into(imageView);


        } catch (Exception e) {
            Log.e("log_tag", "Error Parsing Data "+e.toString());
        }
    }

    // to convert the data stream to a string ( to can read it)
    public String Stream2String(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String text = "";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                text += line;
            }
        } catch (Exception e) {}
        return text;
    }
}