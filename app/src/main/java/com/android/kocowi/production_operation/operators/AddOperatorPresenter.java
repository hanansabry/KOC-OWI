package com.android.kocowi.production_operation.operators;

import com.android.kocowi.backend.authentication.AuthenticationRepository;
import com.android.kocowi.model.User;
import com.android.kocowi.usecase.AuthenticationUseCaseHandler;

public class AddOperatorPresenter implements AddOperatorContract.Presenter {

    private final AddOperatorContract.View mView;
    private final AuthenticationUseCaseHandler mUseCaseHandler;

    public AddOperatorPresenter(AddOperatorContract.View view, AuthenticationUseCaseHandler useCaseHandler) {
        mView = view;
        mUseCaseHandler = useCaseHandler;

        mView.setPresenter(this);
    }

    @Override
    public void registerNewUser(User user, AuthenticationRepository.RegistrationCallback callback) {
        mUseCaseHandler.registerNewUser(user, callback);
    }

    @Override
    public boolean validateUserData(User po) {
        boolean validate = true;
        if (po.getName() == null || po.getName().isEmpty()) {
            validate = false;
            mView.setNameInputTextErrorMessage();
        }

        if (po.getPassword() == null || po.getPassword().isEmpty()) {
            validate = false;
            mView.setPasswordInputTextErrorMessage("Password can't be empty");
        }

        if (po.getPassword().length() < 6) {
            validate = false;
            mView.setPasswordInputTextErrorMessage("Password must be at least 6 characters");
        }

        if (po.getEmail() == null || po.getEmail().isEmpty()) {
            validate = false;
            mView.setEmailInputTextErrorMessage();
        }

        if (po.getPhone() == null || po.getPhone().isEmpty()) {
            validate = false;
            mView.setPhoneInputTextErrorMessage();
        }
        return validate;
    }

    @Override
    public void start() {

    }
}
