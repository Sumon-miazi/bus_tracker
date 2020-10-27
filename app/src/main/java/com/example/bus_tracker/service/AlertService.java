package com.example.bus_tracker.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.bus_tracker.MainActivity;
import com.example.bus_tracker.R;
import com.example.bus_tracker.api.ApiCalls;
import com.example.bus_tracker.db.CustomSharedPref;
import com.example.bus_tracker.utils.CheckNetworkState;
import com.example.bus_tracker.utils.HaversineDistance;


public class AlertService extends Service {
    private static final String TAG = "AlertService";
    private static final int LOCATION_INTERVAL = 10000; // this is in milisec. after every this interval the user location will send to server
    private static final float LOCATION_DISTANCE = 0.0f; // this is in meter.
    LocationListener[] locationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    private LocationManager locationManager = null;
    private CheckNetworkState checkNetworkState;
    private HaversineDistance haversineDistance;

    private void checkBusCurrentPosition(Location mLastLocation) {
        if (mLastLocation == null || !checkNetworkState.haveNetworkConnection()) {
            return;
        }

        int bus_id = CustomSharedPref.getInstance(getApplicationContext()).getBusId();

        if (bus_id == 0) stopSelf();

        new ApiCalls().getBusCurrentPositionByBusId(bus_id, (latitude, longitude) -> {
            haversineDistance = new HaversineDistance(
                    mLastLocation.getLatitude(),
                    mLastLocation.getLongitude());

            double userAndBusDistance = haversineDistance.calculate(latitude, longitude);

            if (userAndBusDistance < 1) {
                sendNotification();
                stopSelf();
            }
        });

    }

    private void sendNotification() {
        String title = "Reminder alert";
        String messageBody = "Hello dear, the bus is near you about less then 1km";
        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        String channelName = getString(R.string.default_notification_channel_name);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        //   .setContent(notificationView)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        assert notificationManager != null;
        notificationManager.notify(101 /* ID of notification */, notificationBuilder.build());
    }


    @Override
    public IBinder onBind(Intent arg0) {
        checkNetworkState = new CheckNetworkState(getApplicationContext());
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        checkNetworkState = new CheckNetworkState(getApplicationContext());
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        // Toast.makeText(this, "onCreate clicked", Toast.LENGTH_SHORT).show();
        initializeLocationManager();

        try {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListeners[1]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListeners[0]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        CustomSharedPref.getInstance(getApplicationContext()).setBusId(0);

        if (locationManager != null) {
            for (int i = 0; i < locationListeners.length; i++) {
                try {
                    locationManager.removeUpdates(locationListeners[i]);
                } catch (Exception ex) {
                }
            }
        }
    }

    private void initializeLocationManager() {
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private class LocationListener implements android.location.LocationListener {
        Location userCurrentLocation;

        public LocationListener(String provider) {
            userCurrentLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            userCurrentLocation.set(location);
            checkBusCurrentPosition(this.userCurrentLocation);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }
}