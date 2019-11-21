package com.android.kocowi;


import com.android.kocowi.backend.authentication.AuthenticationRepository;
import com.android.kocowi.backend.authentication.AuthenticationRepositoryImpl;
import com.android.kocowi.backend.fieldheaders.FieldHeaderRepository;
import com.android.kocowi.backend.fieldheaders.FieldHeaderRepositoryImpl;
import com.android.kocowi.backend.gc.GcRepository;
import com.android.kocowi.backend.gc.GcRepositoryImpl;
import com.android.kocowi.backend.users.UsersRepository;
import com.android.kocowi.backend.users.UsersRepositoryImpl;
import com.android.kocowi.backend.wells.WellsRepository;
import com.android.kocowi.backend.wells.WellsRepositoryImpl;
import com.android.kocowi.backend.wellsdata.WellsDailyDataRepository;
import com.android.kocowi.backend.wellsdata.WellsDailyDataRepositoryImpl;
import com.android.kocowi.usecase.AuthenticationUseCaseHandler;
import com.android.kocowi.usecase.GcUseCaseHandler;
import com.android.kocowi.usecase.WellDetailsUseCaseHandler;

public class Injection {

    public static AuthenticationRepository provideAuthenticationRepository() {
        return new AuthenticationRepositoryImpl();
    }

    public static AuthenticationUseCaseHandler provideAuthenticationUseCaseHandler() {
        return new AuthenticationUseCaseHandler(provideAuthenticationRepository());
    }


    public static GcUseCaseHandler provideGcUseCaseHandler() {
        return new GcUseCaseHandler(provideFieldHeaderRepository(), provideGcRepository());
    }

    public static FieldHeaderRepository provideFieldHeaderRepository() {
        return new FieldHeaderRepositoryImpl();
    }

    public static GcRepository provideGcRepository() {
        return new GcRepositoryImpl();
    }

    public static WellsRepository provideWellsRepository() {
        return new WellsRepositoryImpl();
    }

    public static WellDetailsUseCaseHandler provideWellDetailsUseCaseHandler() {
        return new WellDetailsUseCaseHandler(provideWellsRepository(), provideUsersRepository());
    }

    private static UsersRepository provideUsersRepository() {
        return new UsersRepositoryImpl();
    }

    public static WellsDailyDataRepository provideWellsDataRepository() {
        return new WellsDailyDataRepositoryImpl();
    }
}
