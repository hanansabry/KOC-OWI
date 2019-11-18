package com.android.kocowi.production_operation.wells;

import com.android.kocowi.BasePresenter;
import com.android.kocowi.BaseView;
import com.android.kocowi.backend.wells.WellsRepository;
import com.android.kocowi.model.Well;

import java.util.ArrayList;

public interface WellsContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        void bindWellsList(ArrayList<Well> wellsList);

        void onBindWellItemAtPosition(WellsAdapter.WellViewHolder holder, int position);

        int getWellsListSize();

        void retrieveWells(String gcCode, WellsRepository.WellsRetrievingCallback callback);

    }
}
