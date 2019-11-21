package com.android.kocowi.production_operation.wells;

import com.android.kocowi.backend.wells.WellsRepository;
import com.android.kocowi.model.Well;

import java.util.ArrayList;

public class WellsPresenter implements WellsContract.Presenter {

    private ArrayList<Well> wellsList = new ArrayList<>();
    private final WellsRepository wellsRepository;

    public WellsPresenter(WellsRepository wellsRepository) {
        this.wellsRepository = wellsRepository;
    }

    @Override
    public void bindWellsList(ArrayList<Well> wellsList) {
        this.wellsList = wellsList;
    }

    @Override
    public void onBindWellItemAtPosition(WellsAdapter.WellViewHolder holder, int position) {
        Well well = wellsList.get(position);
        holder.setWellName(well.getName());
    }

    @Override
    public int getWellsListSize() {
        return wellsList.size();
    }

    @Override
    public void retrieveWells(String gdCode, WellsRepository.WellsRetrievingCallback callback) {
        wellsRepository.retrieveWellsByGcCode(gdCode, callback);
    }

    @Override
    public void start() {

    }
}
