package com.android.kocowi.production_operation.gc;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.kocowi.EmptyRecyclerView;
import com.android.kocowi.Injection;
import com.android.kocowi.R;
import com.android.kocowi.backend.gc.GcRepository;
import com.android.kocowi.login.LoginScreen;
import com.android.kocowi.model.GC;
import com.android.kocowi.production_operation.approval.WellsDailyDataForApprove;
import com.android.kocowi.production_operation.gc.add_gc.AddGcBottomFragment;
import com.android.kocowi.production_operation.operators.AddOperator;
import com.android.kocowi.production_operation.wells.WellsActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        GCContract.View, GcRepository.GcRetrievingCallback {

    private GCContract.Presenter presenter;
    private GCListAdapter gcListAdapter;
    private ProgressBar progressBar;
    private EmptyRecyclerView gcRecyclerView;

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

        progressBar = findViewById(R.id.progress_bar);

        initializeGcListRecyclerView();
        presenter.retrieveGcs(this);
        initializeGcListRecyclerView();
        initializeNavigationHeaderViews();
    }

    private void initializeNavigationHeaderViews() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        TextView userMailTextView = hView.findViewById(R.id.user_mail_textview);
        userMailTextView.setText(presenter.getUserEmail());
    }

    private void initializeGcListRecyclerView() {
        gcRecyclerView = findViewById(R.id.gc_recycler_views);
        gcRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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

        if (id == R.id.nav_approval) {
            startActivity(new Intent(this, WellsDailyDataForApprove.class));
        } else if (id == R.id.nav_logout) {
            presenter.logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onAddGCFabClicked(View view) {
        AddGcBottomFragment bottomSheetFragment = new AddGcBottomFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public void showGcActionsPopupMenu(View v, final int position) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.gc_actions);
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                GC selectedGc = presenter.getSelectedGc(position);
                switch (item.getItemId()) {
                    case R.id.add_operator_action:
                        Intent operatorIntent = new Intent(MainActivity.this, AddOperator.class);
                        operatorIntent.putExtra(GC.class.getName(), selectedGc);
                        startActivity(operatorIntent);
                        return true;
                    case R.id.add_well_action:
                        Intent wellIntent = new Intent(MainActivity.this, WellsActivity.class);
                        wellIntent.putExtra(GC.class.getName(), selectedGc);
                        startActivity(wellIntent);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }


    @Override
    public void setPresenter(GCContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void onRetrievingGcSuccessfully(ArrayList<GC> gcList) {
        gcListAdapter.bindGcList(gcList);
        progressBar.setVisibility(View.INVISIBLE);
        gcRecyclerView.setEmptyView(findViewById(R.id.empty_view));
    }

    @Override
    public void onRetrievingGcFailed(String err) {
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
        gcRecyclerView.setEmptyView(findViewById(R.id.empty_view));
    }

    @Override
    public void goToLoginScreen() {
        Intent intent = new Intent(this, LoginScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
