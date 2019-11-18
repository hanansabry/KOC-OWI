package com.android.kocowi.operator;

import android.os.Bundle;
import android.widget.Toast;

import com.android.kocowi.Injection;
import com.android.kocowi.R;
import com.android.kocowi.backend.wells.WellsRepository;
import com.android.kocowi.model.Well;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import androidx.fragment.app.FragmentActivity;

public class WellsLocationGoogleMap extends FragmentActivity implements OnMapReadyCallback, WellsRepository.WellsRetrievingCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private WellsLocationsMapPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wells_location_google_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        presenter = new WellsLocationsMapPresenter(Injection.provideWellsRepository());
        presenter.retrieveAllWells(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Move camera to kuwait
        LatLng Kuwait = new LatLng(29.380813, 48.012446);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Kuwait, 10));
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onRetrievingWellsSuccessfully(ArrayList<Well> wellsList) {
        if (wellsList != null && wellsList.size() > 0) {
            LatLng latLng = null;
            for (Well well : wellsList) {
                latLng = new LatLng(well.getLatitude(), well.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(well.getName()));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        } else {
            Toast.makeText(this, "No available wells", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRetrievingWellsFailed(String err) {
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
