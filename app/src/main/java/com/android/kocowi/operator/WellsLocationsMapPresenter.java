package com.android.kocowi.operator;

import com.android.kocowi.backend.wells.WellsRepository;

public class WellsLocationsMapPresenter {

    private final WellsRepository wellsRepository;

    public WellsLocationsMapPresenter(WellsRepository wellsRepository) {
        this.wellsRepository = wellsRepository;
    }

    public void retrieveAllWells(WellsRepository.WellsRetrievingCallback callback) {
        wellsRepository.retrieveAllWells(callback);
    }

    public void retrieveWellsByGcCode(String gcCode, WellsRepository.WellsRetrievingCallback callback) {
        wellsRepository.retrieveWells(gcCode, callback);
    }
}
