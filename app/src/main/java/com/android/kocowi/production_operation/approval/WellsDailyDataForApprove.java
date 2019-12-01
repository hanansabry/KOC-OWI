package com.android.kocowi.production_operation.approval;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.kocowi.EmptyRecyclerView;
import com.android.kocowi.Injection;
import com.android.kocowi.R;
import com.android.kocowi.backend.wellsdata.WellsDailyDataRepository;
import com.android.kocowi.model.WellDailyData;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

public class WellsDailyDataForApprove extends AppCompatActivity implements WellsDailyDataRepository.WellsDataRetrievingCallback {

    private WellsDailyDataApprovalPresenter presenter;
    private WellsDailyDataAdapter adapter;
    private ProgressBar progressBar;
    private EmptyRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wells_daily_data_for_approve);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progress_bar);
        presenter = new WellsDailyDataApprovalPresenter(Injection.provideWellsDataRepository());
        presenter.retrieveWellsData(this);
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.wells_data_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new WellsDailyDataAdapter(presenter);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRetrievingWellsDataSuccessfully(ArrayList<WellDailyData> wellsList) {
        adapter.bindWellsDailyData(wellsList);
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setEmptyView(findViewById(R.id.empty_view));
    }

    @Override
    public void onRetrievingWellsDataFailed(String err) {
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setEmptyView(findViewById(R.id.empty_view));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
