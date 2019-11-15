package com.android.kocowi.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.kocowi.Injection;
import com.android.kocowi.R;
import com.android.kocowi.backend.authentication.AuthenticationRepository;
import com.android.kocowi.login.LoginScreen;
import com.android.kocowi.model.ProductionOperation;
import com.android.kocowi.production_operation.GatheringCenterActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity implements RegisterContract.View, AuthenticationRepository.RegistrationCallback {

    private RegisterContract.Presenter mPresenter;
    private TextInputLayout nameTextInput, userNameTextInput, passwordTextInput, emailTextInput, phoneTextInput;
    private EditText nameEditText, passwordEditText, emailEditText, phoneEditText;
    private ProgressBar progressBar;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mPresenter = new RegisterPresenter(this, Injection.provideAuthenticationUseCaseHandler());
        initializeView();
    }

    private void initializeView() {
        nameTextInput = findViewById(R.id.name_text_input_edittext);
        passwordTextInput = findViewById(R.id.password_text_input_edittext);
        emailTextInput = findViewById(R.id.email_text_input_edittext);
        phoneTextInput = findViewById(R.id.phone_text_input_edittext);

        nameEditText = findViewById(R.id.name_edit_text);
        nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                nameTextInput.setError(null);
                nameTextInput.setErrorEnabled(false);
            }
        });

        passwordEditText = findViewById(R.id.password_edit_text);
        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                passwordTextInput.setError(null);
                passwordTextInput.setErrorEnabled(false);
            }
        });
        emailEditText = findViewById(R.id.email_edit_text);
        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                emailTextInput.setError(null);
                emailTextInput.setErrorEnabled(false);
            }
        });
        phoneEditText = findViewById(R.id.phone_edit_text);
        phoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                phoneTextInput.setError(null);
                phoneTextInput.setErrorEnabled(false);
            }
        });

        progressBar = findViewById(R.id.progress_bar);
        registerButton = findViewById(R.id.register);
    }

    private ProductionOperation getPOInputData() {
        ProductionOperation po = new ProductionOperation();
        po.setName(nameEditText.getText().toString().trim());
        po.setPassword(passwordEditText.getText().toString().trim());
        po.setEmail(emailEditText.getText().toString().trim());
        po.setPhone(phoneEditText.getText().toString().trim());
        return po;
    }

    @Override
    public void setNameInputTextErrorMessage() {
        nameTextInput.setError(getString(R.string.name_empty_err_msg));
    }

    @Override
    public void setUserNameInputTextErrorMessage() {
        userNameTextInput.setError(getString(R.string.username_empty_err_msg));
    }

    @Override
    public void setPasswordInputTextErrorMessage(String errorMessage) {
        passwordTextInput.setError(errorMessage);
    }

    @Override
    public void setEmailInputTextErrorMessage() {
        emailTextInput.setError(getString(R.string.email_empty_err_msg));
    }

    @Override
    public void setPhoneInputTextErrorMessage() {
        phoneTextInput.setError(getString(R.string.phone_empty_err_msg));
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        registerButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void onLoginClicked(View view) {
        startActivity(new Intent(this, LoginScreen.class));
    }

    private void goToProductionOperationSection() {
        Intent homeIntent = new Intent(this, GatheringCenterActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }

    public void OnRegisterClicked(View view) {
        showProgressBar();
        ProductionOperation po = getPOInputData();
        if (mPresenter.validateUserData(po)) {
            mPresenter.registerNewUser(po, this);
        } else {
            hideProgressBar();
        }
    }

    @Override
    public void onSuccessfulRegistration(FirebaseUser firebaseUser) {
        hideProgressBar();
        Toast.makeText(this, "Registration is successfully completed\n Welcome " + firebaseUser.getEmail(), Toast.LENGTH_LONG).show();
        goToProductionOperationSection();
    }

    @Override
    public void onFailedRegistration(String errmsg) {
        hideProgressBar();
        Toast.makeText(this, "Registration Failed\n" + errmsg, Toast.LENGTH_LONG).show();
    }

    public void onBackClicked(View view) {
        onBackPressed();
    }

}
