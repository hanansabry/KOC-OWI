package com.android.kocowi.production_operation.wells.reports;

import com.android.kocowi.backend.gc.GcRepository;
import com.android.kocowi.backend.wells.WellsRepository;
import com.android.kocowi.backend.wellsdata.WellsDailyDataRepository;
import com.android.kocowi.model.WellDailyData;

import java.util.ArrayList;

public class WellsReportsPresenter {

    private ArrayList<WellDailyData> wellsData = new ArrayList<>();
    private final WellsDailyDataRepository wellsDailyDataRepository;
    private final WellsRepository wellsRepository;
    private final GcRepository gcRepository;

    public WellsReportsPresenter(WellsDailyDataRepository wellsDailyDataRepository, WellsRepository wellsRepository, GcRepository gcRepository) {
        this.wellsDailyDataRepository = wellsDailyDataRepository;
        this.wellsRepository = wellsRepository;
        this.gcRepository = gcRepository;
    }

    public void getWellsReportByGc(String gcCode, WellsDailyDataRepository.WellsDataRetrievingCallback callback) {
        wellsDailyDataRepository.retrieveWellsDataByGC(gcCode, callback);
    }

    public void getWellsReportByWell(String gcCode, String wellCode, WellsDailyDataRepository.WellsDataRetrievingCallback callback) {
        wellsDailyDataRepository.retrieveWellsDataByWell(gcCode, wellCode, callback);
    }

    public void retrieveAllGcs(GcRepository.GcRetrievingCallback callback) {
        gcRepository.retrieveGcs(callback);
    }

    public void retrieveWellsByGc(String gcCode, WellsRepository.WellsRetrievingCallback callback) {
        wellsRepository.retrieveWellsByGcCode(gcCode, callback);
    }

    public int getWellsDataSize() {
        return wellsData.size();
    }

    public void onBindWellDataAtPosition(WellsReportsAdapter.WellReportViewHolder holder, int position) {
        WellDailyData wellData = wellsData.get(position);
        holder.setReportData(wellData);
    }

    public void bindWellsData(ArrayList<WellDailyData> wellsData) {
        this.wellsData = wellsData;
    }
}
