package com.android.kocowi.register;


import com.android.kocowi.backend.authentication.AuthenticationRepository;
import com.android.kocowi.model.User;
import com.android.kocowi.usecase.AuthenticationUseCaseHandler;

public class RegisterPresenter implements RegisterContract.Presenter {

    private final RegisterContract.View mRegistrationView;
    private final AuthenticationUseCaseHandler mUseCaseHandler;

    public RegisterPresenter(RegisterContract.View registrationView, AuthenticationUseCaseHandler useCaseHandler) {
        mRegistrationView = registrationView;
        mUseCaseHandler = useCaseHandler;

        mRegistrationView.setPresenter(this);
    }

    @Override
    public void registerNewUser(User po, AuthenticationRepository.RegistrationCallback callback) {
        mUseCaseHandler.registerNewUser(po, callback);
    }

    @Override
    public boolean validateUserData(User po) {
        boolean validate = true;
        if (po.getName() == null || po.getName().isEmpty()) {
            validate = false;
            mRegistrationView.setNameInputTextErrorMessage();
        }

        if (po.getPassword() == null || po.getPassword().isEmpty()) {
            validate = false;
            mRegistrationView.setPasswordInputTextErrorMessage("Password can't be empty");
        }

        if (po.getPassword().length() < 6) {
            validate = false;
            mRegistrationView.setPasswordInputTextErrorMessage("Password must be at least 6 characters");
        }

        if (po.getEmail() == null || po.getEmail().isEmpty()) {
            validate = false;
            mRegistrationView.setEmailInputTextErrorMessage();
        }

        if (po.getPhone() == null || po.getPhone().isEmpty()) {
            validate = false;
            mRegistrationView.setPhoneInputTextErrorMessage();
        }
        return validate;
    }

    @Override
    public void start() {

    }
}
