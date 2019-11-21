package com.android.kocowi.operator.welldetails;

import com.android.kocowi.BasePresenter;
import com.android.kocowi.backend.users.UsersRepository;
import com.android.kocowi.backend.wells.WellsRepository;
import com.android.kocowi.usecase.WellDetailsUseCaseHandler;

public class WellDetailsPresenter implements BasePresenter {

    private final WellDetailsUseCaseHandler useCaseHandler;

    public WellDetailsPresenter(WellDetailsUseCaseHandler useCaseHandler) {
        this.useCaseHandler = useCaseHandler;
    }

    public void getCurrentOperator(UsersRepository.UsersRetrievingCallback callback) {
        useCaseHandler.getCurrentOperator(callback);
    }

    public void getWellDetails(String wellCode, WellsRepository.WellsRetrievingCallback callback) {
        useCaseHandler.getWellDetails(wellCode, callback);
    }

    @Override
    public void start() {

    }
}
