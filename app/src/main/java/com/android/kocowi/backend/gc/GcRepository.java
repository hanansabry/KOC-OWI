package com.android.kocowi.backend.gc;

import com.android.kocowi.model.GC;

import java.util.ArrayList;

public interface GcRepository {

    interface GcRetrievingCallback {
        void onRetrievingGcSuccessfully(ArrayList<GC> gcList);

        void onRetrievingGcFailed(String err);
    }

    interface GcInsertionCallback {
        void onSuccessfullyAddingNewGc(GC gc);

        void onAddingNewGcFailed(String err);
    }

    void retrieveGcs(GcRetrievingCallback callback);

    void addNewGc(GC gc, GcInsertionCallback callback);
}
