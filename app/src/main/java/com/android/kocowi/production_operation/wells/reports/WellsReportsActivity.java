package com.android.kocowi.production_operation.wells.reports;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.kocowi.EmptyRecyclerView;
import com.android.kocowi.Injection;
import com.android.kocowi.R;
import com.android.kocowi.backend.gc.GcRepository;
import com.android.kocowi.backend.wells.WellsRepository;
import com.android.kocowi.backend.wellsdata.WellsDailyDataRepository;
import com.android.kocowi.model.GC;
import com.android.kocowi.model.Well;
import com.android.kocowi.model.WellDailyData;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

public class WellsReportsActivity extends AppCompatActivity implements WellsRepository.WellsRetrievingCallback, GcRepository.GcRetrievingCallback, WellsDailyDataRepository.WellsDataRetrievingCallback {

    private WellsReportsPresenter presenter;
    private String selectedGcCode;
    private String selectedWell;
    private CustomSpinnerAdapter gcAdapter;
    private CustomSpinnerAdapter wellsAdapter;
    private Spinner gcSpinner;
    private Spinner wellsSpinner;
    private EmptyRecyclerView recyclerView;
    private WellsReportsAdapter wellsReportsAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wells_reports);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new WellsReportsPresenter(Injection.provideWellsDataRepository(),
                Injection.provideWellsRepository(),
                Injection.provideGcRepository());
        presenter.retrieveAllGcs(this);
        initializeSpinners();
        initializeReportRecyclerView();
        progressBar = findViewById(R.id.progress_bar);
    }

    private void initializeSpinners() {
        gcAdapter = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item);
        gcAdapter.add("Select GC");
        gcSpinner = findViewById(R.id.gc_spinner);
        gcSpinner.setEnabled(false);
        gcSpinner.setClickable(false);
        gcSpinner.setAdapter(gcAdapter);

        wellsAdapter = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item);
        wellsAdapter.add("Select Well");
        wellsSpinner = findViewById(R.id.wells_spinner);
        wellsSpinner.setEnabled(false);
        wellsSpinner.setClickable(false);
        wellsSpinner.setAdapter(wellsAdapter);
    }

    private void populateGcSpinner(final ArrayList<GC> gcList) {
        gcSpinner.setEnabled(true);
        gcSpinner.setClickable(true);
        for (GC gc : gcList) {
            gcAdapter.add(gc.getCode());
        }
        gcAdapter.notifyDataSetChanged();
        gcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedGcCode = gcList.get(position - 1).getCode();
                    reinitializeWellsSpinner();
                    presenter.retrieveWellsByGc(selectedGcCode, WellsReportsActivity.this);
                    Toast.makeText(WellsReportsActivity.this, selectedGcCode, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void reinitializeWellsSpinner() {
        selectedWell = null;
        wellsAdapter.clear();
        wellsAdapter.add("Select Well");
        wellsAdapter.notifyDataSetChanged();
        wellsSpinner.setEnabled(false);
        wellsSpinner.setClickable(false);
    }

    private void populateWellsSpinner(final ArrayList<Well> wellsList) {
        if (wellsList.size() > 0) {
            wellsSpinner.setEnabled(true);
            wellsSpinner.setClickable(true);
            for (Well well : wellsList) {
                wellsAdapter.add(well.getName());
            }
            wellsAdapter.notifyDataSetChanged();
            wellsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        selectedWell = wellsList.get(position - 1).getName();
                        Toast.makeText(WellsReportsActivity.this, selectedWell, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void initializeReportRecyclerView() {
        recyclerView = findViewById(R.id.report_recyclerview);
        wellsReportsAdapter = new WellsReportsAdapter(presenter);
        recyclerView.setAdapter(wellsReportsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onRetrievingWellsSuccessfully(ArrayList<Well> wellsList) {
        populateWellsSpinner(wellsList);
    }

    @Override
    public void onRetrievingWellsFailed(String err) {
        Toast.makeText(this, err, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRetrievingGcSuccessfully(ArrayList<GC> gcList) {
        populateGcSpinner(gcList);
    }

    @Override
    public void onRetrievingGcFailed(String err) {
        Toast.makeText(this, err, Toast.LENGTH_LONG).show();
    }

    public void generateReport(View view) {
        progressBar.setVisibility(View.VISIBLE);
        if (selectedGcCode == null && selectedWell == null) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Please Select GC at least", Toast.LENGTH_LONG).show();
        } else if (selectedGcCode != null && selectedWell == null) {
            presenter.getWellsReportByGc(selectedGcCode, this);
        } else if (selectedGcCode != null && selectedWell != null) {
            presenter.getWellsReportByWell(selectedGcCode, selectedWell, this);
        }
    }

    @Override
    public void onRetrievingWellsDataSuccessfully(ArrayList<WellDailyData> wellsList) {
        if (wellsList.size() == 0) {
            wellsSpinner.setSelection(0);
            selectedWell = null;
            Toast.makeText(this, "No data available", Toast.LENGTH_LONG).show();
        }
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setEmptyView(findViewById(R.id.empty_view));
        wellsReportsAdapter.bindWellsData(wellsList);
    }

    @Override
    public void onRetrievingWellsDataFailed(String err) {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setEmptyView(findViewById(R.id.empty_view));
        Toast.makeText(this, err, Toast.LENGTH_LONG).show();
    }
}
