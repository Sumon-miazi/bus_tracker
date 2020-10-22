package com.example.bus_tracker;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bus_tracker.adapters.AllBusAdapter;
import com.example.bus_tracker.api.ApiCalls;
import com.example.bus_tracker.utils.Bus;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private long time;
    private Bus bus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView allBuses = findViewById(R.id.allBusView);
        allBuses.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        new ApiCalls().getAllBuses(this, (busArrayList, message) -> {
            if (busArrayList != null) {
                allBuses.setLayoutManager(new LinearLayoutManager(this));
                allBuses.setAdapter(new AllBusAdapter(this, busArrayList, bus -> {
                    this.bus = bus;
                    showNotificationAlert();
                }));
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

    private void showNotificationAlert() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.show_noti_settings);

        Button enableNotiBtn = dialog.findViewById(R.id.enableNotiBtnId);
        Button disableNotiBtn = dialog.findViewById(R.id.disableNotiBtnId);

        enableNotiBtn.setOnClickListener(v -> showWeeklyDays(true));
        disableNotiBtn.setOnClickListener(v -> showWeeklyDays(false));

        dialog.show();
    }

    private void showWeeklyDays(boolean enableClicked) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.weekly_days);

        TextView title = dialog.findViewById(R.id.textView);
        if (enableClicked) title.setText(getResources().getString(R.string.enableNoti));
        else title.setText(getResources().getString(R.string.disableNoti));

        TextView saturday = dialog.findViewById(R.id.saturdayId);
        TextView sunday = dialog.findViewById(R.id.sundayId);
        TextView monday = dialog.findViewById(R.id.mondayId);
        TextView tuesday = dialog.findViewById(R.id.tuesdayId);
        TextView wednesday = dialog.findViewById(R.id.wednesdayId);
        TextView thursday = dialog.findViewById(R.id.thursdayId);

        saturday.setOnClickListener(v -> subscribeAndUnsubscribeNotification(enableClicked, "saturday"));
        sunday.setOnClickListener(v -> subscribeAndUnsubscribeNotification(enableClicked, "sunday"));
        monday.setOnClickListener(v -> subscribeAndUnsubscribeNotification(enableClicked, "monday"));
        tuesday.setOnClickListener(v -> subscribeAndUnsubscribeNotification(enableClicked, "tuesday"));
        wednesday.setOnClickListener(v -> subscribeAndUnsubscribeNotification(enableClicked, "wednesday"));
        thursday.setOnClickListener(v -> subscribeAndUnsubscribeNotification(enableClicked, "thursday"));

        Button btn_dialog = dialog.findViewById(R.id.btn_dialog);
        btn_dialog.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void subscribeAndUnsubscribeNotification(boolean flag, String dayName) {
        String topicName = bus.name + "_" + dayName;

        System.out.println(">>>>>>>>>>>> " + flag + " " + topicName);
        if (flag) FirebaseMessaging.getInstance().subscribeToTopic(topicName);
        else FirebaseMessaging.getInstance().unsubscribeFromTopic(topicName);
    }
}