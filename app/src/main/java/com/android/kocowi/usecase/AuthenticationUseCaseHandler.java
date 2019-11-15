package com.android.kocowi.usecase;


import com.android.kocowi.backend.authentication.AuthenticationRepository;
import com.android.kocowi.model.ProductionOperation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationUseCaseHandler {

    private AuthenticationRepository mRepository;

    public AuthenticationUseCaseHandler(AuthenticationRepository repository) {
        mRepository = repository;
    }

    public void registerNewUser(ProductionOperation po, AuthenticationRepository.RegistrationCallback callback){
        mRepository.registerNewUser(po, callback);
    }

    public void login(String email, String password, AuthenticationRepository.LoginCallback callback) {
        mRepository.login(email, password, callback);
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public String getUserEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getEmail();
        } else {
            return "";
        }
    }
}
