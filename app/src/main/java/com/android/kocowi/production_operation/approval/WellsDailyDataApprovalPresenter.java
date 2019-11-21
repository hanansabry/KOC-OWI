package com.android.kocowi.production_operation.approval;

import com.android.kocowi.backend.wellsdata.WellsDailyDataRepository;
import com.android.kocowi.model.WellDailyData;

import java.util.ArrayList;

public class WellsDailyDataApprovalPresenter {

    private ArrayList<WellDailyData> wellDailyData = new ArrayList<>();
    private final WellsDailyDataRepository wellsDailyDataRepository;

    public WellsDailyDataApprovalPresenter(WellsDailyDataRepository wellsDailyDataRepository) {
        this.wellsDailyDataRepository = wellsDailyDataRepository;
    }

    public void bindWellsDailyData(ArrayList<WellDailyData> wellDailyData) {
        this.wellDailyData = wellDailyData;
    }

    public void onBindWellDailyDataItemAtPosition(WellsDailyDataAdapter.WellDailyDataViewHolder holder, int position) {
        WellDailyData wellData = wellDailyData.get(position);
        holder.setWellDate(wellData);
    }

    public int getWellsDailyDataListSize() {
        return wellDailyData.size();
    }

    public void setDataApproved(int position, boolean isChecked) {
        WellDailyData wellData = wellDailyData.get(position);
        wellsDailyDataRepository.approveWellData(wellData.getId(), isChecked);
    }

    public void retrieveWellsData(WellsDailyDataRepository.WellsDataRetrievingCallback wellsDataRetrievingCallback) {
        wellsDailyDataRepository.retrieveAllWellsData(wellsDataRetrievingCallback);
    }
}
