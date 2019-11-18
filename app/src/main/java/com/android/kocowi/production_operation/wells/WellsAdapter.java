package com.android.kocowi.production_operation.wells;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.kocowi.R;
import com.android.kocowi.model.Well;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WellsAdapter extends RecyclerView.Adapter<WellsAdapter.WellViewHolder> {

    private final WellsContract.Presenter presenter;

    public WellsAdapter(WellsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void bindWells(ArrayList<Well> wellsList) {
        presenter.bindWellsList(wellsList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.well_item_layout, parent, false);
        return new WellViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WellViewHolder holder, int position) {
        presenter.onBindWellItemAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getWellsListSize();
    }

    class WellViewHolder extends RecyclerView.ViewHolder {
        private TextView wellNameTextView;

        public WellViewHolder(@NonNull View itemView) {
            super(itemView);

            wellNameTextView = itemView.findViewById(R.id.well_name_textview);
        }

        public void setWellName(String name) {
            wellNameTextView.setText(String.format(Locale.US, "Name: %s", name));
        }
    }
}
