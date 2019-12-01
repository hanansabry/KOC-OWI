package com.android.kocowi.backend.wellsdata;

import com.android.kocowi.model.WellDailyData;

import java.util.ArrayList;

public interface WellsDailyDataRepository {

    interface WellsDataRetrievingCallback {
        void onRetrievingWellsDataSuccessfully(ArrayList<WellDailyData> wellsList);

        void onRetrievingWellsDataFailed(String err);
    }

    interface WellDailyDataInsertionCallback {
        void onSuccessfullyAddingNewWellData(WellDailyData well);

        void onAddingNewWellDataFailed(String err);
    }

    void retrieveAllWellsData(WellsDataRetrievingCallback callback);

    void addNewWellDailyData(WellDailyData well, WellDailyDataInsertionCallback callback);

    void approveWellData(String wellDataId, boolean checked);

    void retrieveWellsDataByGC(String gcCode, WellsDataRetrievingCallback callback);

    void retrieveWellsDataByWell(String gcCode, String wellCode, WellsDataRetrievingCallback callback);
}
