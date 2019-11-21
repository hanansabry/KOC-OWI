package com.android.kocowi.backend.wellsdata;

import com.android.kocowi.model.WellDailyData;

import java.util.ArrayList;

public interface WellsDailyDataRepository {

    interface WellsDataRetrievingCallback {
        void onRetrievingWellsDaraSuccessfully(ArrayList<WellDailyData> wellsList);

        void onRetrievingWellsDataFailed(String err);
    }

    interface WellDailyDataInsertionCallback {
        void onSuccessfullyAddingNewWellData(WellDailyData well);

        void onAddingNewWellDataFailed(String err);
    }

    void retrieveAllWellsData(WellsDailyDataRepository.WellsDataRetrievingCallback callback);

    void addNewWellDailyData(WellDailyData well, WellsDailyDataRepository.WellDailyDataInsertionCallback callback);
}
