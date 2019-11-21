package com.android.kocowi.backend.wells;

import com.android.kocowi.model.Well;

import java.util.ArrayList;

public interface WellsRepository {

    interface WellsRetrievingCallback {
        void onRetrievingWellsSuccessfully(ArrayList<Well> wellsList);

        void onRetrievingWellsFailed(String err);
    }

    interface WellInsertionCallback {
        void onSuccessfullyAddingNewWell(Well well);

        void onAddingNewWellFailed(String err);
    }

    void retrieveWellsByGcCode(String gcCode, WellsRetrievingCallback callback);

    void retrieveAllWells(WellsRetrievingCallback callback);

    void retrieveWellByCode(String wellCode, WellsRetrievingCallback callback);

    void addNewWell(Well well, WellInsertionCallback callback);
}
