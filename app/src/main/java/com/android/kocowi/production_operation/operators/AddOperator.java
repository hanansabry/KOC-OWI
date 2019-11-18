package com.android.kocowi.production_operation.operators;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.kocowi.Injection;
import com.android.kocowi.R;
import com.android.kocowi.backend.authentication.AuthenticationRepository;
import com.android.kocowi.model.GC;
import com.android.kocowi.model.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddOperator extends AppCompatActivity implements AddOperatorContract.View, AuthenticationRepository.RegistrationCallback {

    private GC currentGC;
    private AddOperatorContract.Presenter mPresenter;
    private TextInputLayout nameTextInput, passwordTextInput, emailTextInput, phoneTextInput;
    private EditText nameEditText, passwordEditText, emailEditText, phoneEditText;
    private ProgressBar progressBar;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_operator);

        mPresenter = new AddOperatorPresenter(this, Injection.provideAuthenticationUseCaseHandler());
        currentGC = getCurrentGc();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(String.format(Locale.US, "%s - Add Operator", currentGC.getCode()));

        initializeView();
    }

    public GC getCurrentGc() {
        return getIntent().getExtras().getParcelable(GC.class.getName());
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

    private User getUserInputData() {
        User user = new User();
        user.setName(nameEditText.getText().toString().trim());
        user.setPassword(passwordEditText.getText().toString().trim());
        user.setEmail(emailEditText.getText().toString().trim());
        user.setPhone(phoneEditText.getText().toString().trim());
        user.setRole(User.UserRole.OPERATOR);
        user.setGcCode(currentGC.getCode());
        return user;
    }

    @Override
    public void setNameInputTextErrorMessage() {
        nameTextInput.setError(getString(R.string.name_empty_err_msg));
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
    public void setPresenter(AddOperatorContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void OnRegisterClicked(View view) {
        showProgressBar();
        User user = getUserInputData();
        if (mPresenter.validateUserData(user)) {
            mPresenter.registerNewUser(user, this);
        } else {
            hideProgressBar();
        }
    }

    @Override
    public void onSuccessfulRegistration(FirebaseUser firebaseUser) {
        hideProgressBar();
        Toast.makeText(this, "New Operator is added successfully", Toast.LENGTH_LONG).show();
        onBackPressed();
    }

    @Override
    public void onFailedRegistration(String errmsg) {
        hideProgressBar();
        Toast.makeText(this, "Registration Failed\n" + errmsg, Toast.LENGTH_LONG).show();
    }
}
