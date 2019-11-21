package com.android.kocowi.production_operation.approval;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.kocowi.R;
import com.android.kocowi.model.WellDailyData;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WellsDailyDataAdapter extends RecyclerView.Adapter<WellsDailyDataAdapter.WellDailyDataViewHolder> {

    private final WellsDailyDataApprovalPresenter presenter;

    public WellsDailyDataAdapter(WellsDailyDataApprovalPresenter presenter) {
        this.presenter = presenter;
    }

    public void bindWellsDailyData(ArrayList<WellDailyData> wellDailyData) {
        presenter.bindWellsDailyData(wellDailyData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WellDailyDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.well_daily_data_item_layout, parent, false);
        return new WellDailyDataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WellDailyDataViewHolder holder, int position) {
        presenter.onBindWellDailyDataItemAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getWellsDailyDataListSize();
    }

    class WellDailyDataViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        private TextView wellGcTextView, whpTextView, flpTextView, tempTextView, statusTextView, dateTextView;
        private CheckBox approveCheckbox;

        public WellDailyDataViewHolder(@NonNull View itemView) {
            super(itemView);

            wellGcTextView = itemView.findViewById(R.id.well_name_gccode_textview);
            whpTextView = itemView.findViewById(R.id.whp_textview);
            flpTextView = itemView.findViewById(R.id.flp_textview);
            tempTextView = itemView.findViewById(R.id.temp_textview);
            statusTextView = itemView.findViewById(R.id.status_textview);
            dateTextView = itemView.findViewById(R.id.date_textview);
            approveCheckbox = itemView.findViewById(R.id.approved_checkbox);
            approveCheckbox.setOnCheckedChangeListener(this);
        }

        public void setWellDate(WellDailyData data) {
            wellGcTextView.setText(String.format(Locale.US, "%1s - %2s", data.getWell().getName(), data.getWell().getGcCode()));
            whpTextView.setText(String.format(Locale.US, "whp: %s", data.getWhp()));
            flpTextView.setText(String.format(Locale.US, "flp: %s", data.getFlp()));
            tempTextView.setText(String.format(Locale.US, "temp: %s", data.getTemp()));
            statusTextView.setText(String.format(Locale.US, "status: %s", data.getStatus()));
            dateTextView.setText(String.format(Locale.US, "%1s, %2s", data.getDay(), data.getTime()));
            approveCheckbox.setChecked(data.isApproved());

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            presenter.setDataApproved(getAdapterPosition(), isChecked);
        }
    }
}
