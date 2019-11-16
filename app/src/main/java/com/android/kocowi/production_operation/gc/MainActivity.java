package com.android.kocowi.production_operation.gc;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.kocowi.EmptyRecyclerView;
import com.android.kocowi.Injection;
import com.android.kocowi.R;
import com.android.kocowi.backend.gc.GcRepository;
import com.android.kocowi.model.GC;
import com.android.kocowi.production_operation.gc.add_gc.AddGcBottomFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GCContract.View, PopupMenu.OnMenuItemClickListener, GcRepository.GcRetrievingCallback {

    private GCContract.Presenter presenter;
    private GCListAdapter gcListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenter = new GCPresenter(this, Injection.provideGcRepository());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initializeGcListRecyclerView();
        presenter.retrieveGcs(this);
    }

    private void initializeGcListRecyclerView() {
        EmptyRecyclerView gcRecyclerView = findViewById(R.id.gc_recycler_views);
        gcRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        gcRecyclerView.setEmptyView(findViewById(R.id.empty_view));
        gcListAdapter = new GCListAdapter(presenter);
        gcRecyclerView.setAdapter(gcListAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onAddGCFabClicked(View view) {
//        Snackbar.make(view, "Add new GC", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
        AddGcBottomFragment bottomSheetFragment = new AddGcBottomFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public void showGcActionsPopupMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.gc_actions);
        popup.show();
    }



    @Override
    public void setPresenter(GCContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_operator_action:
                Toast.makeText(this, "Add Operator action", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.add_well_action:
                Toast.makeText(this, "Add Well action", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    //temp
    @Override
    protected void onResume() {
        super.onResume();
        initializeGcListRecyclerView();
    }

    @Override
    public void onRetrievingGcSuccessfully(ArrayList<GC> gcList) {
        gcListAdapter.bindGcList(gcList);
    }

    @Override
    public void onRetrievingGcFailed(String err) {
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
    }
}
