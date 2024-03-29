package com.android.kocowi.register;


import com.android.kocowi.BasePresenter;
import com.android.kocowi.BaseView;
import com.android.kocowi.backend.authentication.AuthenticationRepository;
import com.android.kocowi.model.User;

public interface RegisterContract {

    interface View extends BaseView<Presenter> {

        void setNameInputTextErrorMessage();

        void setUserNameInputTextErrorMessage();

        void setPasswordInputTextErrorMessage(String errorMessage);

        void setEmailInputTextErrorMessage();

        void setPhoneInputTextErrorMessage();

        void showProgressBar();

        void hideProgressBar();

    }

    interface Presenter extends BasePresenter {

        void registerNewUser(User po, AuthenticationRepository.RegistrationCallback callback);

        boolean validateUserData(User po);

    }

}
