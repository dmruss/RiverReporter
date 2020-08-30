package com.example.riverconditionsreporter;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherRequest {
    private String apiKey = "iR0duWVAmlgvCNo5z06Ul1Trurw3XYNy";
    private String locationKey;
    private RequestQueue mQueue;
    private String longitude;
    private String latitude;

    private Forecast forecast;

    public WeatherRequest() {}

    public WeatherRequest(Context context, River river){
        mQueue = Volley.newRequestQueue(context.getApplicationContext());
        longitude = river.getLongitude().toString();
        latitude = river.getLatitude().toString();
        jsonParseLocationKey(context);


       // forecast = new Forecast(context);


    }


    public String jsonParseLocationKey(final Context context) {
        String locationUrl = "http://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=";
        String aUrl = locationUrl + apiKey + "&q=" + latitude + "%2C%20" + longitude;
        Log.i("jsonParseLocationKey", "Location api url: " + aUrl);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, aUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            setLocationKey(response.getString("Key"));
                            Log.i("WeatherRequest", "Location Key: " + locationKey);
                            forecast = new Forecast(context);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
        Log.i("Volley", "Making location key request now");
        return locationKey;
    }



    public class Forecast {
        private ArrayList<Double> minTemps;
        private ArrayList<Double> maxTemps;
        private ArrayList<Integer> icons;
        private ArrayList<String> phrases;

        public Forecast(Context context) {
            //mQueue = Volley.newRequestQueue(context.getApplicationContext());
            minTemps = new ArrayList<Double>();
            maxTemps = new ArrayList<Double>();
            icons = new ArrayList<Integer>();
            phrases = new ArrayList<String>();
            jsonParseForecast(minTemps, maxTemps, icons, phrases);


        }

        public ArrayList<Integer> getIcons() {
            return icons;
        }

        public ArrayList<Double> getMaxTemps() {
            return maxTemps;
        }

        public ArrayList<Double> getMinTemps() {
            return minTemps;
        }

        public ArrayList<String> getPhrases() {
            return phrases;
        }

        public void setIcons(ArrayList<Integer> icons) {
            this.icons = icons;
        }

        public void setMaxTemps(ArrayList<Double> maxTemps) {
            this.maxTemps = maxTemps;
        }

        public void setMinTemps(ArrayList<Double> minTemps) {
            this.minTemps = minTemps;
        }

        public void setPhrases(ArrayList<String> phrases) {
            this.phrases = phrases;
        }

        public void jsonParseForecast(final ArrayList<Double> minTemps, final ArrayList<Double> maxTemps, final ArrayList<Integer> icons,
                                      final ArrayList<String> phrases) {
            Log.i("jsonParseForecast", "locationKey: " + locationKey);
            String forcastUrl = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/";
            String url = forcastUrl + locationKey + "?apikey=" + apiKey;
            Log.i("jsonParseForecast", "" + url);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray forecasts = response.getJSONArray("DailyForecasts");

                                for (int i = 0; i < forecasts.length(); i++) {
                                    JSONObject jsonObject = forecasts.getJSONObject(i);
                                    //get temps
                                    JSONObject temps = jsonObject.getJSONObject("Temperature");
                                    JSONObject min = temps.getJSONObject("Minimum");
                                    JSONObject max = temps.getJSONObject("Maximum");
                                    minTemps.add(min.getDouble("Value"));
                                    maxTemps.add(max.getDouble("Value"));
                                    //get icon and phrase
                                    JSONObject day = jsonObject.getJSONObject("Day");
                                    icons.add(day.getInt("Icon"));
                                    phrases.add(day.getString("IconPhrase"));


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mQueue.add(request);
            Log.i("Volley", "Making forcast request now");
        }
    }


    public Forecast getForecast() {
        return forecast;
    }

    public void setLocationKey(String locationKey) {
        this.locationKey = locationKey;
    }
}
