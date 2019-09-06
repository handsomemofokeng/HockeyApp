package com.example.android.hockeyapp;

//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.annotation.TargetApi;
//import android.content.pm.PackageManager;
//import android.support.annotation.NonNull;
//import android.app.LoaderManager.LoaderCallbacks;
//import android.content.CursorLoader;
//import java.util.ArrayList;
//import static android.Manifest.permission.READ_CONTACTS;
//import android.content.Loader;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.view.Gravity;
//import android.provider.ContactsContract;
//import android.telecom.Connection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import static com.example.android.hockeyapp.ApplicationClass.MY_SHARED_PREFERENCES_NAME;
import static com.example.android.hockeyapp.ApplicationClass.currentUserPassword;
import static com.example.android.hockeyapp.ApplicationClass.currentUsername;
import static com.example.android.hockeyapp.ApplicationClass.isEmailValid;
import static com.example.android.hockeyapp.ApplicationClass.isPasswordValid;
import static com.example.android.hockeyapp.ApplicationClass.isPhoneConnected;
import static com.example.android.hockeyapp.ApplicationClass.myPrefs;
import static com.example.android.hockeyapp.ApplicationClass.progressDialog;
import static com.example.android.hockeyapp.ApplicationClass.rememberMe;
import static com.example.android.hockeyapp.ApplicationClass.sessionUser;
import static com.example.android.hockeyapp.ApplicationClass.showCustomToast;
import static com.example.android.hockeyapp.ApplicationClass.showProgressDialog;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     * <p>
     * private static final int REQUEST_READ_CONTACTS = 0;
     * <p>
     * <p>
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private EditText etEmailView;
    private EditText etPasswordView;
    private View toastView;
    private CheckBox chkRememberMe;
    public String errors = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        initViews();

        //Get credentials on background
        new GetDataInBackground().execute();

        myPrefs = getSharedPreferences(MY_SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        rememberMe = myPrefs.getBoolean("RememberMe", false);
        currentUsername = myPrefs.getString("Username", "");
        currentUserPassword = myPrefs.getString("Password", "");

        chkRememberMe.setChecked(rememberMe);
        etEmailView.setText(currentUsername);
        etPasswordView.setText(currentUserPassword);

        if (rememberMe) {
            if (!(currentUsername.isEmpty() || currentUserPassword.isEmpty())) {
                progressDialog.setMessage("Logging in, please wait...");
                attemptLogin(currentUsername, currentUserPassword);
            }
        }

        etPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin(etEmailView.getText().toString().trim(), etPasswordView.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.btn_sign_in);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isEmailValid(etEmailView) && etPasswordView.getText().toString().trim().isEmpty())
                    showRegistrationForm(etEmailView.getText().toString().trim());
                else {
                    if (isPhoneConnected(getApplicationContext())) {
                        attemptLogin(etEmailView.getText().toString().trim(), etPasswordView.getText().toString().trim());
                    } else {
                        showCustomToast(getApplicationContext(), toastView, "Please check your internet connection!");
                    }
                }
            }
        });
    }

    private void initViews() {
        toastView = getLayoutInflater().inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
        etEmailView = findViewById(R.id.email);
        etPasswordView = findViewById(R.id.password);
        chkRememberMe = findViewById(R.id.chkRememberMe);
    }

    public void showRegistrationForm(String email) {

        Intent regUser = new Intent(getApplicationContext(), RegisterUserActivity.class)
                .putExtra("email", email);
        startActivity(regUser);
    }

//    private boolean mayRequestContacts() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            Snackbar.make(etEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        @TargetApi(Build.VERSION_CODES.M)
//                        public void onClick(View v) {
//                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//                        }
//                    });
//        } else {
//            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//        }
//        return false;
//    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     *
     * @param email    user's log in username
     * @param password user's log in password
     */
    private void attemptLogin(String email, String password) {

        // Reset errors.
        etEmailView.setError(null); //removes the error on view
        etPasswordView.setError(null);

        // Store values at the time of the login attempt.
//        String email = etEmailView.getText().toString().trim();
//        final String password = etPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = etPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            etEmailView.setError(getString(R.string.error_field_required));
            focusView = etEmailView;
            cancel = true;
        } else if (!isEmailValid(etEmailView)) {
            //etEmailView.setError(getString(R.string.error_invalid_email));
            focusView = etEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            // TODO: 2018/09/06 Get ser online, remove hardcoded user, this is for offline testing purposes
//            sessionUser = new BackendlessUser();
//            sessionUser.setEmail("apptester@cut.com");
//            sessionUser.setProperty("name", "App");
//            sessionUser.setProperty("surname", "Tester");
//            sessionUser.setProperty("role", "Admin");
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//
            // TODO: 2018/09/14  uncomment for online testing

            showProgressDialog(this, "Logging In", "Please wait while we log you in...", true);
            Backendless.UserService.login(etEmailView.getText().toString().trim(),
                    etPasswordView.getText().toString().trim(), new AsyncCallback<BackendlessUser>() {

                        @Override
                        public void handleResponse(BackendlessUser response) {

                            sessionUser = response;
                            if (chkRememberMe.isChecked()) {

                                /*
                                 *Create and retrieve Shared Preferences
                                 */
                                myPrefs = getSharedPreferences(MY_SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                                rememberMe = myPrefs.getBoolean("RememberMe", false);
                                currentUsername = myPrefs.getString("Username", "");
                                currentUserPassword = myPrefs.getString("Password", "");
//                                cUserRole = myPrefs.getString("UserRole", "");

                                SharedPreferences.Editor editor = myPrefs.edit();
                                editor.putBoolean("RememberMe", chkRememberMe.isChecked());
                                editor.putString("Username", response.getEmail());
                                editor.putString("Password", etPasswordView.getText().toString().trim());
//                                editor.putString("UserRole", response.getProperty("role").toString());
                                editor.commit();
                            }
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            progressDialog.dismiss();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            progressDialog.dismiss();
                            showCustomToast(getApplicationContext(), toastView, "Error: "
                                    + fault.getMessage());
                        }
                    });

        }
    }

    /**
     * This method creates a customised Reset Dialog for password
     *
     * @param dialogView customised dialogView
     * @return inflated dialogView
     */
    private AlertDialog createDialog(View dialogView) {

        AlertDialog.Builder resetBuilder = new AlertDialog.Builder(this);
        resetBuilder.setView(dialogView);
        return resetBuilder.create();

    }

    public void onClickResetPassword(View view) {

        showResetDialog(this);

    }

    /**w
     * @param context in which the Dialog is gonna be shown
     */
    public void showResetDialog(final Context context) {

        try {
            View resetDialogView = getLayoutInflater().inflate(R.layout.dialog_reset_password, null);
            Button btnReset = resetDialogView.findViewById(R.id.btnReset);
            Button btnCancel = resetDialogView.findViewById(R.id.btnCancelReset);
            final EditText etEmailReset = resetDialogView.findViewById(R.id.etEmailReset);

            final AlertDialog resetDialog = createDialog(resetDialogView);
            resetDialog.show();

            /*
            Set onClick Listener fot Reset Button
             */
            btnReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEmailValid(etEmailReset)) {

                        showProgressDialog(context, "Reset Password",
                                "Sending reset password link...", true);
                        Backendless.UserService.restorePassword(etEmailReset.getText().toString().trim(),

                                new AsyncCallback<Void>() {
                                    @Override
                                    public void handleResponse(Void aVoid) {

                                        progressDialog.dismiss();
                                        showCustomToast(context, toastView,
                                                "Reset link sent to " +
                                                        etEmailReset.getText().toString().trim());
                                        resetDialog.dismiss();
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault backendlessFault) {

                                        showCustomToast(context, toastView, "Error: "
                                                + backendlessFault.getMessage());
                                        progressDialog.dismiss();
                                    }
                                });

                    } else if (TextUtils.isEmpty(etEmailReset.getText().toString().trim())) {
                        etEmailReset.setError(getString(R.string.error_field_required));
                    } else if (!isEmailValid(etEmailReset)) {
                        etEmailReset.setError(getString(R.string.error_invalid_email));
                    }//end if-else
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetDialog.dismiss();
                }
            });
        } catch (Exception ex) {
            showCustomToast(context, toastView, "Error: " + ex.getMessage());
        }
    }

    private class GetDataInBackground extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {

                showProgressDialog(getApplicationContext(), "Initializing",
                        "Getting your settings, please wait...", false);

            } catch (Exception ex) {

                showCustomToast(getApplicationContext(), toastView,
                        "Error: " + ex.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            progressDialog.setMessage("Getting things ready...");
            Backendless.Data.of(BackendlessUser.class).getObjectCount(new AsyncCallback<Integer>() {

                @Override
                public void handleResponse(Integer integer) {

                    myPrefs = getSharedPreferences(MY_SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                    rememberMe = myPrefs.getBoolean("RememberMe", false);
                    currentUsername = myPrefs.getString("Username", "");
                    currentUserPassword = myPrefs.getString("Password", "");
//                    progressDialog.dismiss();
                }

                @Override
                public void handleFault(BackendlessFault backendlessFault) {
                    errors += "Settings Error: " + backendlessFault.getMessage() + "\n";
                    showCustomToast(getApplicationContext(), toastView, errors);
//                    progressDialog.dismiss();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            progressDialog.dismiss();
        }
    }
}