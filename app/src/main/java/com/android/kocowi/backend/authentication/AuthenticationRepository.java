package com.android.kocowi.backend.authentication;

import com.android.kocowi.model.User;
import com.google.firebase.auth.FirebaseUser;

public interface AuthenticationRepository {

    interface RegistrationCallback {
        void onSuccessfulRegistration(FirebaseUser firebaseUser);

        void onFailedRegistration(String errmsg);
    }

    interface LoginCallback {
        void onSuccessLogin(FirebaseUser firebaseUser);

        void onFailedLogin(String errmsg);
    }

    void registerNewUser(User po, RegistrationCallback callback);

    void login(String email, String password, User.UserRole userRole, LoginCallback callback);

}
