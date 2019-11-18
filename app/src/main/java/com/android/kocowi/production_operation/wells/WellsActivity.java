package com.android.kocowi.production_operation.wells;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.kocowi.EmptyRecyclerView;
import com.android.kocowi.Injection;
import com.android.kocowi.R;
import com.android.kocowi.backend.wells.WellsRepository;
import com.android.kocowi.model.GC;
import com.android.kocowi.model.Well;
import com.android.kocowi.production_operation.wells.add_well.AddWellBottomFragment;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

public class WellsActivity extends AppCompatActivity implements WellsRepository.WellsRetrievingCallback {

    private GC currentGC;
    private WellsAdapter wellsListAdapter;
    private WellsContract.Presenter presenter;
    private ProgressBar progressBar;
    private EmptyRecyclerView wellsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wells);

        presenter = new WellsPresenter(Injection.provideWellsRepository());
        currentGC = getCurrentGc();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(currentGC.getCode());

        presenter.retrieveWells(currentGC.getCode(), this);
        progressBar = findViewById(R.id.progress_bar);
        initializeWellsListRecyclerView();
    }

    public GC getCurrentGc() {
        return getIntent().getExtras().getParcelable(GC.class.getName());
    }

    private void initializeWellsListRecyclerView() {
        wellsRecyclerView = findViewById(R.id.well_recycler_view);
        wellsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        wellsListAdapter = new WellsAdapter(presenter);
        wellsRecyclerView.setAdapter(wellsListAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    public void onAddWellFabClicked(View view) {
        AddWellBottomFragment bottomSheetFragment = new AddWellBottomFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public void onRetrievingWellsSuccessfully(ArrayList<Well> wellsList) {
        wellsListAdapter.bindWells(wellsList);
        wellsRecyclerView.setEmptyView(findViewById(R.id.empty_view));
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRetrievingWellsFailed(String err) {
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
        wellsRecyclerView.setEmptyView(findViewById(R.id.empty_view));
        progressBar.setVisibility(View.INVISIBLE);
    }
}
