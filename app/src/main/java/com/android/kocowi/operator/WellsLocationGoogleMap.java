package com.android.kocowi.operator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.kocowi.BuildConfig;
import com.android.kocowi.Injection;
import com.android.kocowi.R;
import com.android.kocowi.backend.wells.WellsRepository;
import com.android.kocowi.geofencing.GeofenceErrorMessages;
import com.android.kocowi.geofencing.GeofencingHandler;
import com.android.kocowi.login.LoginScreen;
import com.android.kocowi.model.Well;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

public class WellsLocationGoogleMap extends AppCompatActivity implements OnMapReadyCallback, WellsRepository.WellsRetrievingCallback, OnCompleteListener<Void> {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private static final String TAG = WellsLocationGoogleMap.class.getSimpleName();

    private GoogleMap mMap;
    private WellsLocationsMapPresenter presenter;
    private GeofencingHandler geofencingHandler;
    private boolean retrieved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wells_location_google_map);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geofencingHandler = new GeofencingHandler(this);
        presenter = new WellsLocationsMapPresenter(Injection.provideWellsRepository());
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            retrieved = true;
            presenter.retrieveAllWells(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!retrieved) {
            if (!checkPermissions()) {
                requestPermissions();
            } else {
                retrieved = true;
                presenter.retrieveAllWells(this);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Move camera to kuwait
        LatLng Kuwait = new LatLng(29.380813, 48.012446);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Kuwait, 11));
//        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onRetrievingWellsSuccessfully(ArrayList<Well> wellsList) {
        if (wellsList != null && wellsList.size() > 0) {
            LatLng latLng = null;
            for (Well well : wellsList) {
                latLng = new LatLng(well.getLatitude(), well.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(well.getName()));
                createGeofence(well);
            }
            geofencingHandler.addGeofences(this);
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        } else {
            Toast.makeText(this, "No available wells", Toast.LENGTH_SHORT).show();
        }

    }

    private void createGeofence(Well well) {
        if (!checkPermissions()) {
            showSnackbar(getString(R.string.insufficient_permissions));
            return;
        }
        geofencingHandler.createGeofence(
                String.format(Locale.US, "%1s - %2s", well.getName(), well.getGcCode()), well.getLatitude(), well.getLongitude());
    }

    @Override
    public void onRetrievingWellsFailed(String err) {
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isSuccessful()) {
            Toast.makeText(this, "Geofence is added or removed", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Geofence is added or removed");
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this, task.getException());
            Log.w(TAG, errorMessage);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.retrieveAllWells(this);
                Log.i(TAG, "Permission granted.");
            } else {
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        geofencingHandler.removeGeofences(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_logout) {
            presenter.signout();
            Intent homeIntent = new Intent(this, LoginScreen.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homeIntent);
        }
        return true;
    }

    private void showSnackbar(final String text) {
        View container = findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(WellsLocationGoogleMap.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(WellsLocationGoogleMap.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

}
