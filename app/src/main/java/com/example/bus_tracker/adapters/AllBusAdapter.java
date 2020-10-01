package com.example.bus_tracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bus_tracker.R;
import com.example.bus_tracker.utils.Bus;

import java.util.ArrayList;

public class AllBusAdapter extends RecyclerView.Adapter<AllBusAdapter.BusViewHolder> {

    private Context context;
    private ArrayList<Bus> busArrayList;

    public AllBusAdapter(Context context, ArrayList<Bus> busArrayList) {
        this.context = context;
        this.busArrayList = busArrayList;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_bus_view, parent, false);
        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return busArrayList.size();
    }

    public class BusViewHolder extends RecyclerView.ViewHolder {

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
