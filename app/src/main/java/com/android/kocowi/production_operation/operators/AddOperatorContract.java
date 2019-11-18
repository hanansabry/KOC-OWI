package com.android.kocowi.production_operation.operators;

import com.android.kocowi.BasePresenter;
import com.android.kocowi.BaseView;
import com.android.kocowi.backend.authentication.AuthenticationRepository;
import com.android.kocowi.model.User;

public class AddOperatorContract {
    interface View extends BaseView<Presenter> {

        void setNameInputTextErrorMessage();

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
