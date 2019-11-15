package com.android.kocowi;


import com.android.kocowi.backend.authentication.AuthenticationRepository;
import com.android.kocowi.backend.authentication.AuthenticationRepositoryImpl;
import com.android.kocowi.usecase.AuthenticationUseCaseHandler;

public class Injection {

    public static AuthenticationRepository provideAuthenticationRepository() {
        return new AuthenticationRepositoryImpl();
    }

    public static AuthenticationUseCaseHandler provideAuthenticationUseCaseHandler() {
        return new AuthenticationUseCaseHandler(provideAuthenticationRepository());
    }


}
