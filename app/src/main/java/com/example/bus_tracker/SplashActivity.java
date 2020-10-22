package com.example.bus_tracker;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bus_tracker.utils.CheckNetworkState;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        CheckNetworkState checkNetworkState = new CheckNetworkState(SplashActivity.this);
        if (!checkNetworkState.haveNetworkConnection()) {
            showNoWifiInternet();
            //animationView.setAnimation("wifi_animation.json");
            return;
        }

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showNoWifiInternet() {
        final Dialog dialog = new Dialog(SplashActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.no_internet);

        Button dialogButton = dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}