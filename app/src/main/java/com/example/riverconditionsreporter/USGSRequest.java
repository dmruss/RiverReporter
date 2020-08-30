package com.example.riverconditionsreporter;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class USGSRequest {
    private String iD;
    private String discharge;
    private String url;
    private RequestQueue mQueue;

    private River river;

    public USGSRequest() {}

    public USGSRequest(Context context, River river) {
        this.river = river;
        iD = river.getId();
        Log.i("USGSRequest", "Getting id " + iD);
        mQueue = Volley.newRequestQueue(context.getApplicationContext());
        jsonParse();
    }


    public void jsonParse() {
        url = "https://waterservices.usgs.gov/nwis/iv/?format=json&sites=";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url + iD, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("value");
                            JSONArray timeSeries = jsonObject.getJSONArray("timeSeries");
                            //for loop to find the time series object containing discharge
                            int obj = 0;
                            for (int i = 0; i < timeSeries.length(); i++) {
                                JSONObject values = timeSeries.getJSONObject(i);
                                JSONObject variable = values.getJSONObject("variable");
                                JSONArray variableCode = variable.getJSONArray("variableCode");
                                JSONObject aCode = variableCode.getJSONObject(0);
                                String code = aCode.getString("value");

                                Log.i("USGSRequest", "variable code: " + code);
                                if (code.equals("00060")) {
                                    obj = i;
                                    break;
                                }
                            }


                            JSONObject values = timeSeries.getJSONObject(obj);
                            JSONArray values2 = values.getJSONArray("values");
                            JSONObject values3 = values2.getJSONObject(0);
                            JSONArray values4 = values3.getJSONArray("value");
                            JSONObject values5 = values4.getJSONObject(0);
                            discharge = values5.getString("value");


                            Log.i("USGSRequest", "Discharge value: " + getDischarge());
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
        Log.i("Volley", "Making USGS api request now");
        Log.i("USGS request", " " + url + iD);
    }

    public void setDischarge(String discharge) {
        this.discharge = discharge;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public void setmQueue(RequestQueue mQueue) {
        this.mQueue = mQueue;
    }

    public void setRiver(River river) {
        this.river = river;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getiD() {
        return iD;
    }

    public River getRiver() {
        return river;
    }

    public String getDischarge() {
        return discharge;
    }

    public String getUrl() {
        return url;
    }

    public RequestQueue getmQueue() {
        return mQueue;
    }
}
