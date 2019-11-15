package com.android.kocowi.login;


import com.android.kocowi.BasePresenter;
import com.android.kocowi.BaseView;
import com.android.kocowi.backend.authentication.AuthenticationRepository;

public interface LoginContract {

    interface View extends BaseView<Presenter>{

        void setEmailInputTextErrorMessage();

        void setPasswordInputTextErrorMessage();

        void showProgressBar();

        void hideProgressBar();

    }

    interface Presenter extends BasePresenter {
        void login(String email, String password, AuthenticationRepository.LoginCallback callback);

        boolean validateLoginData(String email, String password);
    }
}
