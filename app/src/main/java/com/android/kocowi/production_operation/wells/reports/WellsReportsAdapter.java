package com.android.kocowi.production_operation.wells.reports;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.kocowi.R;
import com.android.kocowi.model.WellDailyData;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WellsReportsAdapter extends RecyclerView.Adapter<WellsReportsAdapter.WellReportViewHolder> {

    private final WellsReportsPresenter presenter;

    public WellsReportsAdapter(WellsReportsPresenter presenter) {
        this.presenter = presenter;
    }

    public void bindWellsData(ArrayList<WellDailyData> wellsData) {
        presenter.bindWellsData(wellsData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WellReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.well_day_report_item, parent, false);
        return new WellReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WellReportViewHolder holder, int position) {
        if (position == 0) {
            holder.setHeader();
        } else {
            presenter.onBindWellDataAtPosition(holder, position - 1);
        }
    }

    @Override
    public int getItemCount() {
        return presenter.getWellsDataSize() + 1;
    }

    class WellReportViewHolder extends RecyclerView.ViewHolder {

        private TextView wellName, whp, flp, temp, status, time, approving;

        public WellReportViewHolder(@NonNull View itemView) {
            super(itemView);

            wellName = itemView.findViewById(R.id.well_name_textview);
            whp = itemView.findViewById(R.id.whp_textview);
            flp = itemView.findViewById(R.id.flp_textview);
            temp = itemView.findViewById(R.id.temp_textview);
            status = itemView.findViewById(R.id.status_textview);
            time = itemView.findViewById(R.id.time_textview);
            approving = itemView.findViewById(R.id.approving_textview);
        }

        public void setReportData(WellDailyData data) {
            wellName.setText(data.getWell().getName());
            whp.setText(String.valueOf(data.getWhp()));
            flp.setText(String.valueOf(data.getFlp()));
            temp.setText(String.valueOf(data.getTemp()));
            status.setText(data.getStatus());
            time.setText(String.format("%1s:%2s", data.getDay(), data.getTime()));
            approving.setText(String.valueOf(data.isApproved()));
        }

        public void setHeader() {
            wellName.setText("Well Name");
            whp.setText("WHP");
            flp.setText("FLP");
            temp.setText("TEMP");
            status.setText("STATUS");
            time.setText("TIME");
            approving.setText("APPROVED");
        }
    }
}
