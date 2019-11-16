package com.android.kocowi.production_operation.gc;

import com.android.kocowi.BasePresenter;
import com.android.kocowi.BaseView;
import com.android.kocowi.backend.gc.GcRepository;
import com.android.kocowi.model.GC;

import java.util.ArrayList;

public class GCContract {

    interface View extends BaseView<Presenter> {

        void showGcActionsPopupMenu(android.view.View v);
    }

    interface Presenter extends BasePresenter {

        void bindGcList(ArrayList<GC> gcList);

        void onBindGcItemAtPosition(GCListAdapter.GCViewHolder holder, int position);

        int getGcListSize();

        void showGcActionsPopupMenu(android.view.View v);

        void retrieveGcs(GcRepository.GcRetrievingCallback callback);
    }
}
