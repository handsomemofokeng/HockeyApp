package com.example.android.hockeyapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import static com.example.android.hockeyapp.ApplicationClass.getUserString;
import static com.example.android.hockeyapp.ApplicationClass.isEmailValid;
import static com.example.android.hockeyapp.ApplicationClass.isPasswordsMatching;
import static com.example.android.hockeyapp.ApplicationClass.isValidFields;
import static com.example.android.hockeyapp.ApplicationClass.progressDialog;
import static com.example.android.hockeyapp.ApplicationClass.sessionUser;
import static com.example.android.hockeyapp.ApplicationClass.setupActionBar;
import static com.example.android.hockeyapp.ApplicationClass.showCustomToast;
import static com.example.android.hockeyapp.ApplicationClass.showProgressDialog;

public class RegisterUserActivity extends AppCompatActivity {

    EditText etEmail, etName, etSurname, etPassword, etConfirmPassword;
    private View toastView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        initViews();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setupActionBar(actionBar, "Register New User", getUserString(sessionUser));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            etEmail.setText(extras.getString("email"));
            etName.requestFocus();
        }
    }

    private void initViews() {

        toastView = getLayoutInflater().inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout));
        etEmail = findViewById(R.id.etRegEmail);
        etName = findViewById(R.id.etRegName);
        etSurname = findViewById(R.id.etRegSurname);
        etPassword = findViewById(R.id.etRegPassword);
        etConfirmPassword = findViewById(R.id.etRegPasswordConfirm);
    }

    public void onClickRegisterUser(View view) {
        if (isValidFields(etEmail, etName, etSurname, etPassword, etConfirmPassword)) {

            if (isEmailValid(etEmail)) {
                if (isPasswordsMatching(etPassword, etConfirmPassword)) {

                    final BackendlessUser newUser = new BackendlessUser();
                    newUser.setEmail(etEmail.getText().toString().trim());
                    newUser.setProperty("name", etName.getText().toString().trim());
                    newUser.setProperty("surname", etSurname.getText().toString().trim());
                    // TODO: 2018/09/06 Set Role to None
                    newUser.setProperty("role", "None");
                    newUser.setPassword(etPassword.getText().toString().trim());

                    showProgressDialog(this, "Register User",
                            "Please wait while we register you...", false);
                    Backendless.UserService.register(newUser, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {

                            progressDialog.dismiss();
                            showCustomToast(getApplicationContext(), toastView,
                                    "Email confirmation sent to " + response.getEmail());
                            finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            progressDialog.dismiss();
                            showCustomToast(getApplicationContext(), toastView, "Error: " +
                                    fault.getMessage());
                        }
                    });
                }
            } else {
                showCustomToast(this, toastView, getString(R.string.error_invalid_email));
            }
        }
    }
}
