package com.example.riverconditionsreporter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewFavoritesDialog extends DialogFragment {
    private ArrayList<River> favorites;
    private RiverAdapter riverAdapter;
    private RecyclerView recyclerView;
    //private MapsActivity mapsActivity;
    private Context context;
    private Button backButton;

    public ViewFavoritesDialog(Context context, ArrayList<River> favorites) {
        this.context = context;
        //this.mapsActivity = mapsActivity;
        this.favorites = favorites;
        //this.recyclerView = recyclerView;

    }

   /* @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        riverAdapter = new RiverAdapter(context, favorites);
        recyclerView = new RecyclerView(context);


        recyclerView.findViewById(R.id.list_item);

        View dialogView = inflater.inflate(R.layout.list_item, container, false);

        recyclerView = dialogView.findViewById(R.id.recyclerView);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //recyclerView = new RecyclerView(context);
                //(RecyclerView) v.findViewById(R.id.list_item);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        //riverAdapter = new RiverAdapter(mapsActivity, favorites);
        recyclerView.setAdapter(riverAdapter);


        builder.setView(dialogView).setMessage(" ");
        return dialogView;
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.i("ViewFavoritesDialog", "onCreateDialog now");

        super.onCreateDialog(savedInstanceState);

        //dialogfragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.favorite_list, null);

        recyclerView = dialogView.findViewById(R.id.recyclerView);
        backButton = dialogView.findViewById(R.id.buttonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        //recycler view
        riverAdapter = new RiverAdapter(getActivity(), favorites);

        //recyclerView = new RecyclerView(getActivity());

        //recyclerView.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));


        //bug, need to initialize river adapter here


        recyclerView.setAdapter(riverAdapter);

        //set title
        View title = inflater.inflate(R.layout.fav_title, null);
        builder.setView(dialogView).setCustomTitle(title);

        Log.i("ViewFavoritesDialog", "Finished onCreateDialog");
        return builder.create();
    }

}
