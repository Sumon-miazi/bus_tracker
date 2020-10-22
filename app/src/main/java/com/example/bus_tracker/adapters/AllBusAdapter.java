package com.example.bus_tracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bus_tracker.MapsActivity;
import com.example.bus_tracker.R;
import com.example.bus_tracker.utils.Bus;
import com.example.bus_tracker.utils.GetBusObj;

import java.util.ArrayList;

public class AllBusAdapter extends RecyclerView.Adapter<AllBusAdapter.BusViewHolder> {

    private Context context;
    private ArrayList<Bus> busArrayList;
    private GetBusObj getBusObj;

    public AllBusAdapter(Context context, ArrayList<Bus> busArrayList, GetBusObj getBusObj) {
        this.context = context;
        this.busArrayList = busArrayList;
        this.getBusObj = getBusObj;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_bus_view, parent, false);
        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        holder.busName.setText(busArrayList.get(position).name);
        holder.busLicense.setText(busArrayList.get(position).license);
        holder.busRoute.setText(busArrayList.get(position).routes);
        holder.busView.setOnClickListener(view -> {
            Intent intent = new Intent(context, MapsActivity.class);
            intent.putExtra("bus", busArrayList.get(position));
            context.startActivity(intent);
        });

        holder.busView.setOnLongClickListener(view -> {
            getBusObj.busObj(busArrayList.get(position));
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return busArrayList.size();
    }


    public class BusViewHolder extends RecyclerView.ViewHolder {
        TextView busName;
        ConstraintLayout busView;
        TextView busLicense;
        TextView busRoute;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);

            busName = itemView.findViewById(R.id.busName_Id);
            busView = itemView.findViewById(R.id.busViewId);
            busLicense = itemView.findViewById(R.id.busLicence_id);
            busRoute = itemView.findViewById(R.id.busRoutes_id);
        }
    }
}
