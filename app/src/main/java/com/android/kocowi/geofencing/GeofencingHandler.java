package com.android.kocowi.geofencing;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;

import java.util.ArrayList;

public class GeofencingHandler {

    private static final float GEOFENCE_RADIUS_IN_METERS = 100;

    private GeofencingClient geofencingClient;
    private Context context;
    private ArrayList<Geofence> geofenceList = new ArrayList<>();
    private PendingIntent geofencePendingIntent;

    public GeofencingHandler(Context context) {
        this.context = context;
        geofencingClient = getGeofencingClientInstance();
    }

    public GeofencingClient getGeofencingClientInstance() {
        return LocationServices.getGeofencingClient(context);
    }

    public void createGeofence(String requestId, double latitude, double longitude) {
        geofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(requestId)
                .setCircularRegion(
                        latitude,
                        longitude,
                        GEOFENCE_RADIUS_IN_METERS
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());
    }

    public GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    public PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(context, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    @SuppressLint("MissingPermission")
    public void addGeofences(OnCompleteListener<Void> listener) {
        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent()).addOnCompleteListener(listener);
    }

    public void removeGeofences(OnCompleteListener<Void> listener) {
        geofencingClient.removeGeofences(geofencePendingIntent).addOnCompleteListener(listener);
    }

}
