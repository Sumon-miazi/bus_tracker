package com.example.bus_tracker;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bus_tracker.adapters.AllBusAdapter;
import com.example.bus_tracker.api.ApiCalls;

public class MainActivity extends AppCompatActivity {
    private long time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView allBuses = findViewById(R.id.allBusView);

        new ApiCalls().getAllBuses(this, (busArrayList, message) -> {
            if (busArrayList != null) {
                allBuses.setLayoutManager(new LinearLayoutManager(this));
                allBuses.setAdapter(new AllBusAdapter(this, busArrayList));
            } else Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
        // startActivity(new Intent(this, MapsActivity.class));
        // FirebaseMessaging.getInstance().subscribeToTopic("notification");
    }

    @Override
    public void onBackPressed() {

        if (time + 2000 > System.currentTimeMillis()) {
            finish();
        } else {
            time = System.currentTimeMillis();
            Toast.makeText(this, "press again to exit", Toast.LENGTH_SHORT).show();
        }

    }
}