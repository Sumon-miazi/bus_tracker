package com.example.bus_tracker.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlertService extends Service {
    public AlertService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
