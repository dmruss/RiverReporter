package com.example.riverconditionsreporter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class ViewRiverDialog extends DialogFragment {
    ArrayList<LocalDate> dates;
    ArrayList<String> daysOfWeek;
    //declare variables for textviews
    private TextView titleTextView;
    private TextView flowrateTextView;
    private TextView day1TextView;
    private TextView day2TextView;
    private TextView day3TextView;
    private TextView day4TextView;
    private TextView day5TextView;
    private TextView condition1TextView;
    private TextView condition2TextView;
    private TextView condition3TextView;
    private TextView condition4TextView;
    private TextView condition5TextView;
    private TextView high1TextView;
    private TextView high2TextView;
    private TextView high3TextView;
    private TextView high4TextView;
    private TextView high5TextView;
    private TextView low1TextView;
    private TextView low2TextView;
    private TextView low3TextView;
    private TextView low4TextView;
    private TextView low5TextView;

    private ToggleButton favoriteButton;
    private Button backButton;

    private River river;
    private USGSRequest usgsRequest;
    private WeatherRequest weatherRequest;
    private WeatherRequest.Forecast forecast;

    public ViewRiverDialog(River river, USGSRequest usgsRequest, WeatherRequest weatherRequest) {
        daysOfWeek = new ArrayList<String>();
        dates = new ArrayList<LocalDate>();
        this.river = river;
        this.usgsRequest = usgsRequest;
        this.weatherRequest = weatherRequest;
        forecast = weatherRequest.getForecast();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MapsActivity callingActivity = (MapsActivity) getActivity();

        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.river_view, null);

        getDaysOfWeek();
        getDayStringNew(dates, Locale.US);

        //find references to views on dialogfragment
        titleTextView = dialogView.findViewById(R.id.textViewTitle);
        flowrateTextView = dialogView.findViewById(R.id.textViewFlowrate);
        day1TextView = dialogView.findViewById(R.id.textViewDay1);
        day2TextView = dialogView.findViewById(R.id.textViewDay2);
        day3TextView = dialogView.findViewById(R.id.textViewDay3);
        day4TextView = dialogView.findViewById(R.id.textViewDay4);
        day5TextView = dialogView.findViewById(R.id.textViewDay5);
        condition1TextView = dialogView.findViewById(R.id.textViewCondition1);
        condition2TextView = dialogView.findViewById(R.id.textViewCondition2);
        condition3TextView = dialogView.findViewById(R.id.textViewCondition3);
        condition4TextView = dialogView.findViewById(R.id.textViewCondition4);
        condition5TextView = dialogView.findViewById(R.id.textViewCondition5);
        high1TextView = dialogView.findViewById(R.id.textViewHigh1);
        high2TextView = dialogView.findViewById(R.id.textViewHigh2);
        high3TextView = dialogView.findViewById(R.id.textViewHigh3);
        high4TextView = dialogView.findViewById(R.id.textViewHigh4);
        high5TextView = dialogView.findViewById(R.id.textViewHigh5);
        low1TextView = dialogView.findViewById(R.id.textViewLow1);
        low2TextView = dialogView.findViewById(R.id.textViewLow2);
        low3TextView = dialogView.findViewById(R.id.textViewLow3);
        low4TextView = dialogView.findViewById(R.id.textViewLow4);
        low5TextView = dialogView.findViewById(R.id.textViewLow5);

        favoriteButton = dialogView.findViewById(R.id.buttonFavorite);
        backButton = dialogView.findViewById(R.id.buttonBack);

        final ArrayList<River> favorites = callingActivity.getFavorites();

        //set fields
        titleTextView.setText(river.getName());
        Log.i("Favorites Contain", " " + river.getFavorite());
        if (river.getFavorite()) {
            favoriteButton.setChecked(true);
            favoriteButton.setSelected(true);
        }
        flowrateTextView.setText(usgsRequest.getDischarge() + " cfs");
        //days
        day1TextView.setText(daysOfWeek.get(0));
        day2TextView.setText(daysOfWeek.get(1));
        day3TextView.setText(daysOfWeek.get(2));
        day4TextView.setText(daysOfWeek.get(3));
        day5TextView.setText(daysOfWeek.get(4));
        //phrases
        condition1TextView.setText(forecast.getPhrases().get(0));
        condition2TextView.setText(forecast.getPhrases().get(1));
        condition3TextView.setText(forecast.getPhrases().get(2));
        condition4TextView.setText(forecast.getPhrases().get(3));
        condition5TextView.setText(forecast.getPhrases().get(4));
        //highs
        high1TextView.setText(forecast.getMaxTemps().get(0).toString() + "° F");
        high2TextView.setText(forecast.getMaxTemps().get(1).toString() + "° F");
        high3TextView.setText(forecast.getMaxTemps().get(2).toString() + "° F");
        high4TextView.setText(forecast.getMaxTemps().get(3).toString() + "° F");
        high5TextView.setText(forecast.getMaxTemps().get(4).toString() + "° F");
        //lows
        low1TextView.setText(forecast.getMinTemps().get(0).toString() + "° F");
        low2TextView.setText(forecast.getMinTemps().get(1).toString() + "° F");
        low3TextView.setText(forecast.getMinTemps().get(2).toString() + "° F");
        low4TextView.setText(forecast.getMinTemps().get(3).toString() + "° F");
        low5TextView.setText(forecast.getMinTemps().get(4).toString() + "° F");
        //favoriteButton.setChecked(true);
        //favoriteButton.setSelected(true);
       /* if (callingActivity.containsFavorite(river)) {
            Log.i("Is a favorite", "River " + river.getName());
            favoriteButton.setChecked(true);
            favoriteButton.setSelected(true);
        }*/


        //assign button functions with onclicklisteners
        backButton.setOnClickListener(new View.OnClickListener() {
            MapsActivity callingActivity = (MapsActivity) getActivity();

            @Override
            public void onClick(View view) {
                if (favoriteButton.isChecked()){
                    for (int i = 0; i < favorites.size(); i++) {
                        if (favorites.get(i).getName().equals(river.getName())){
                            dismiss();
                            return;
                        }
                    }
                    callingActivity.addNewFavorite(river);
                    Log.i("New Favorite", " " + river.getName());
                }else {
                    callingActivity.deleteFavorite(river);
                }
                dismiss();
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        builder.setView(dialogView);  //.setMessage(" ");

        return builder.create();





    }

    public void sendSelectedRiver (River river) {this.river = river;}

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getDaysOfWeek() {
        for (int i = 0; i < 5; i++) {
            dates.add(LocalDate.now().plusDays(i));
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getDayStringNew(ArrayList<LocalDate> dates, Locale locale) {
        for (int i = 0; i < dates.size(); i++) {
            DayOfWeek day = dates.get(i).getDayOfWeek();
            daysOfWeek.add(day.getDisplayName(TextStyle.FULL, locale));
        }


    }
}
