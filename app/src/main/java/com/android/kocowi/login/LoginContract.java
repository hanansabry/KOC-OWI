package com.android.kocowi.login;


import com.android.kocowi.BasePresenter;
import com.android.kocowi.BaseView;
import com.android.kocowi.backend.authentication.AuthenticationRepository;
import com.android.kocowi.model.User;

public interface LoginContract {

    interface View extends BaseView<Presenter>{

        void setEmailInputTextErrorMessage();

        void setPasswordInputTextErrorMessage();

        void showProgressBar();

        void hideProgressBar();

    }

    interface Presenter extends BasePresenter {
        void login(String email, String password, User.UserRole userRole, AuthenticationRepository.LoginCallback callback);

        boolean validateLoginData(String email, String password);
    }
}
