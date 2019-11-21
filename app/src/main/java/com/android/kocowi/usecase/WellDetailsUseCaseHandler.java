package com.android.kocowi.usecase;

import com.android.kocowi.backend.users.UsersRepository;
import com.android.kocowi.backend.wells.WellsRepository;

public class WellDetailsUseCaseHandler {

    private final WellsRepository wellsRepository;
    private final UsersRepository usersRepository;

    public WellDetailsUseCaseHandler(WellsRepository wellsRepository, UsersRepository usersRepository) {
        this.wellsRepository = wellsRepository;
        this.usersRepository = usersRepository;
    }

    public void getCurrentOperator(UsersRepository.UsersRetrievingCallback callback) {
        usersRepository.getCurrentUserData(callback);
    }

    public void getWellDetails(String wellCode, WellsRepository.WellsRetrievingCallback callback) {
        wellsRepository.retrieveWellByCode(wellCode, callback);
    }
}
