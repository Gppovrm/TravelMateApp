package com.example.registerwithfirebaseapp;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.Scanner;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeatherFetcher {

    private static final String API_KEY = "bc7e4735b58fbed9142139733049dd46"; // API ключ
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static int getTemperatureForCity(String city) throws IOException, InterruptedException {
        final int[] temp = new int[1];
        temp[0] = 1;


                try {
                    Log.i("Api request", "Start request");
                    String str_url = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric"; // url запрос
                    Log.i("Api request", str_url);
                    // Create connection to the API end point, by default used GET method
                    URL url = new URL(str_url);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    conn.connect();

                    int responsecode = conn.getResponseCode();

                    if (responsecode != 200) {
                        throw new RuntimeException("HttpResponseCode: " + responsecode);
                    } else {

                        String inline = "";
                        Scanner scanner = new Scanner(url.openStream());

                        //Write all the JSON data into a string using a scanner
                        while (scanner.hasNext()) {
                            inline += scanner.nextLine();
                        }

                        scanner.close();

                        JSONObject jsonObject = new JSONObject(inline);

                        JSONObject obj = (JSONObject) jsonObject.get("main");

                        double d_temp = (double) obj.get("temp");
                        temp[0] = (int) d_temp;
                        int it = (int) temp[0];
                        Log.i("Api result", String.valueOf(it));
//                        temperature_number.setText(temp[0]);
                    }
                    Log.i("Api request", "End request");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

        Log.i("Api request", "Go out");
        return temp[0];








        //Using the JSON simple library parse the string into a json object
//            JSONParser parsee = new JSONParser();

//        try {
//
//
//            return
//        } catch (JSONException err) {
//            System.out.println("Exception : " + err.toString());
//        }
//        return 0;

    }
}