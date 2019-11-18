package com.android.kocowi.production_operation.wells.add_well;

import com.android.kocowi.BasePresenter;
import com.android.kocowi.backend.wells.WellsRepository;
import com.android.kocowi.model.Well;

public class AddWellPresenter implements BasePresenter {

    private final AddWellBottomFragment addWellBottomFragment;
    private final WellsRepository wellsRepository;

    public AddWellPresenter(AddWellBottomFragment view, WellsRepository wellsRepository) {
        this.wellsRepository = wellsRepository;
        addWellBottomFragment = view;
        addWellBottomFragment.setPresenter(this);
    }

    public boolean validateUserData(Well well) {
        boolean validate = true;
        if (well.getName() == null || well.getName().isEmpty()) {
            validate = false;
            addWellBottomFragment.setNameInputTextErrorMessage();
        }

        if (well.getLatitude() == 0) {
            validate = false;
            addWellBottomFragment.setLatitudeInputTextErrorMessage();
        }

        if (well.getLongitude() == 0) {
            validate = false;
            addWellBottomFragment.setLongitudeInputTextErrorMessage();
        }

        return validate;
    }

    public void addNewWell(Well well, WellsRepository.WellInsertionCallback callback) {
        wellsRepository.addNewWell(well, callback);
    }

    @Override
    public void start() {

    }
}
