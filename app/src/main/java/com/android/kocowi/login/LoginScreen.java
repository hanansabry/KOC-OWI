package com.android.kocowi.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.kocowi.Injection;
import com.android.kocowi.R;
import com.android.kocowi.backend.authentication.AuthenticationRepository;
import com.android.kocowi.model.User;
import com.android.kocowi.operator.WellsLocationGoogleMap;
import com.android.kocowi.production_operation.gc.MainActivity;
import com.android.kocowi.register.RegisterActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class LoginScreen extends AppCompatActivity implements LoginContract.View, AuthenticationRepository.LoginCallback {

    private LoginContract.Presenter mPresenter;
    private TextInputLayout emailTextInput, passwordTextInput;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;
    private User.UserRole selectedRole = User.UserRole.PRODUCTION_OPERATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPresenter = new LoginPresenter(this, Injection.provideAuthenticationUseCaseHandler());
        initializeView();
    }

    private void initializeView() {
        emailTextInput = findViewById(R.id.email_text_input_edittext);
        passwordTextInput = findViewById(R.id.password_text_input_edittext);

        emailEditText = findViewById(R.id.email_edit_text);
        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                emailTextInput.setError(null);
                emailTextInput.setErrorEnabled(false);
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

        loginButton = findViewById(R.id.login);
        progressBar = findViewById(R.id.progress_bar);
        final TextView registerButton = findViewById(R.id.signup_btn);

        RadioGroup roleRadioGroup = findViewById(R.id.role_radio_group);
        roleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.po_role) {
                    selectedRole = User.UserRole.PRODUCTION_OPERATION;
                    registerButton.setEnabled(true);
                } else if (checkedId == R.id.operator_role) {
                    selectedRole = User.UserRole.OPERATOR;
                    registerButton.setEnabled(false);
                }
            }
        });
    }

    public void onLoginClicked(View view) {
        showProgressBar();

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (mPresenter.validateLoginData(email, password)) {
            mPresenter.login(email, password, selectedRole, this);
        } else {
            hideProgressBar();
        }
    }

    public void OnRegisterClicked(View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        registerIntent.putExtra(User.class.getName(), selectedRole.name());
        startActivity(registerIntent);
    }

    @Override
    public void setEmailInputTextErrorMessage() {
        emailTextInput.setError(getString(R.string.email_empty_err_msg));
    }

    @Override
    public void setPasswordInputTextErrorMessage() {
        passwordTextInput.setError(getString(R.string.password_empty_err_msg));
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onSuccessLogin(FirebaseUser firebaseUser) {
        hideProgressBar();
        Toast.makeText(this, "Welcome, " + firebaseUser.getEmail(), Toast.LENGTH_LONG).show();
        if (selectedRole == User.UserRole.PRODUCTION_OPERATION) {
            goToProductionOperationSection();
        } else if (selectedRole == User.UserRole.OPERATOR) {
            goToOperatorSection();
        }
    }

    private void goToOperatorSection() {
        Intent homeIntent = new Intent(this, WellsLocationGoogleMap.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }

    private void goToProductionOperationSection() {
        Intent homeIntent = new Intent(this, MainActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }

    @Override
    public void onFailedLogin(String errmsg) {
        hideProgressBar();
        Toast.makeText(this, errmsg, Toast.LENGTH_LONG).show();
    }
}
