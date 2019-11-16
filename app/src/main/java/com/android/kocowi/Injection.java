package com.android.kocowi;


import com.android.kocowi.backend.authentication.AuthenticationRepository;
import com.android.kocowi.backend.authentication.AuthenticationRepositoryImpl;
import com.android.kocowi.backend.fieldheaders.FieldHeaderRepository;
import com.android.kocowi.backend.fieldheaders.FieldHeaderRepositoryImpl;
import com.android.kocowi.backend.gc.GcRepository;
import com.android.kocowi.backend.gc.GcRepositoryImpl;
import com.android.kocowi.usecase.AuthenticationUseCaseHandler;
import com.android.kocowi.usecase.GcUseCaseHandler;

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
}
