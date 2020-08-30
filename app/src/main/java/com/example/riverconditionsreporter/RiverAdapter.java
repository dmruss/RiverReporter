package com.example.riverconditionsreporter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RiverAdapter extends RecyclerView.Adapter<RiverAdapter.ListItemHolder> {
    private Context context;
    private ArrayList<River> favorites;


    public RiverAdapter (Context context, ArrayList<River> favorites){
        Log.i("RiverAdapter", " created");
        Log.i("RiverAdapter", "Favorites size: " + favorites.size());
        this.context = context;
        this.favorites = favorites;



    }

    @NonNull
    @Override
    public RiverAdapter.ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("RiverAdapter", "onCreateViewHolder called");

        View listItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ListItemHolder(listItem);    }

    @Override
    public void onBindViewHolder(@NonNull RiverAdapter.ListItemHolder holder, int position) {
        River river = favorites.get(position);
        holder.textViewName.setText(river.getName());
        Log.i("RiverAdapter", "Assigning name");
    }

    @Override
    public int getItemCount() {return favorites.size();}



    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewName;


        public ListItemHolder (View view) {
            super(view);

            textViewName = view.findViewById(R.id.textViewName);

            view.setClickable(true);
            view.setOnClickListener(this);
        }



        public void onClick (View view) {

            ((MapsActivity)context).showRiver(getAdapterPosition());
        }

    }
}


