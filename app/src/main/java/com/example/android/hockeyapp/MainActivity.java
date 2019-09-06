package com.example.android.hockeyapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.text.MessageFormat;

import static com.example.android.hockeyapp.ApplicationClass.MY_SHARED_PREFERENCES_NAME;
import static com.example.android.hockeyapp.ApplicationClass.currentUserPassword;
import static com.example.android.hockeyapp.ApplicationClass.getUserString;
import static com.example.android.hockeyapp.ApplicationClass.hideViews;
import static com.example.android.hockeyapp.ApplicationClass.myPrefs;
import static com.example.android.hockeyapp.ApplicationClass.progressDialog;
import static com.example.android.hockeyapp.ApplicationClass.rememberMe;
import static com.example.android.hockeyapp.ApplicationClass.sessionUser;
import static com.example.android.hockeyapp.ApplicationClass.setupActionBar;
import static com.example.android.hockeyapp.ApplicationClass.showCustomToast;
import static com.example.android.hockeyapp.ApplicationClass.showProgressDialog;
import static com.example.android.hockeyapp.ApplicationClass.showViews;

public class MainActivity extends AppCompatActivity {

    View toastView;
    LinearLayout frmAdminMenu, frmCoachMenu;
    Button btnCoachMenu;
    TextView tvSessionUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        String role = sessionUser.getProperty("role").toString();
        tvSessionUser.setText(MessageFormat.format("{0}\n({1})", sessionUser.getEmail(), role));

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setupActionBar(actionBar, role + " Menu", getUserString(sessionUser));

        setupRoleMenu(role);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ab_log_out:
                logOutUser();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method initializes Views
     */
    private void initViews() {
        toastView = getLayoutInflater().inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout));
        frmAdminMenu = findViewById(R.id.frmAdminMenu);
        frmCoachMenu = findViewById(R.id.frmCoachMenu);
        btnCoachMenu = findViewById(R.id.btnCoachMenu);
        tvSessionUser = findViewById(R.id.tvSessionUser);
    }

    /**
     * This method sets up the landing page depending on the role of a logged in user;
     *
     * @param role is used to determine which menu to show
     */
    private void setupRoleMenu(String role) {
        switch (role) {
            case "Admin":
                hideViews(frmCoachMenu);
                break;
            case "Coach":

                hideViews(frmAdminMenu, btnCoachMenu);

                break;
            default:
                //Hide All buttons if role is null or empty
                hideViews(frmAdminMenu, frmCoachMenu, btnCoachMenu);
                break;
        }
    }

    public void onClickActionButton(View view) {

        switch (view.getId()) {

            case R.id.btnAddTeamOpponent:
                startActivity(new Intent(getApplicationContext(), AddTeamsActivity.class));
                break;

            case R.id.btnAddPlayers:
                startActivity(new Intent(getApplicationContext(), AddPlayerActivity.class));
                break;

            case R.id.btnCoachMenu:
                if (btnCoachMenu.getText().toString().contains("Coach")) {

                    btnCoachMenu.setText(R.string.action_admin_menu);
                    //show coach menu buttons
                    hideViews(frmAdminMenu);
                    showViews(frmCoachMenu);

                } else {

                    btnCoachMenu.setText(getString(R.string.action_coach_menu));
                    //hide coach menu buttons
                    showViews(frmAdminMenu);
                    hideViews(frmCoachMenu);
                }
                break;

            case R.id.btnMatches:

                startActivity(new Intent(getApplicationContext(), MatchesMenuActivity.class));
                break;

            case R.id.btnMovePlayers:

                startActivity(new Intent(getApplicationContext(), MovePlayersActivity.class));
                break;

            case R.id.btnMyPlayers:

                startActivity(new Intent(getApplicationContext(), MyPlayersActivity.class));
                break;
            case R.id.btnSetRoles:

                showCustomToast(this, toastView, "Assign Roles");
                startActivity(new Intent(getApplicationContext(), SetRolesActivity.class));
                break;
        }
    }

    public void logOutUser() {

        try {
            showProgressDialog(getApplicationContext(), "Logging out",
                    "Please wait...", false);
            Backendless.UserService.logout(new AsyncCallback<Void>() {
                @Override
                public void handleResponse(Void aVoid) {
                    progressDialog.dismiss();
                    showCustomToast(getApplicationContext(), toastView,
                            Backendless.UserService.loggedInUser()
                                    + " logged out successfully.");
                    /**
                     *Create and retrieve Shared Preferences
                     */
                    myPrefs = getSharedPreferences(MY_SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                    rememberMe = myPrefs.getBoolean("RememberMe", false);
                    currentUserPassword = myPrefs.getString("Password", "");

                    SharedPreferences.Editor editor = myPrefs.edit();
                    editor.putBoolean("RememberMe", false);
                    editor.putString("Password", "");
                    editor.commit();

//                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }

                @Override
                public void handleFault(BackendlessFault backendlessFault) {
                    progressDialog.dismiss();
                    showCustomToast(getApplicationContext(), toastView, "Error: "
                            + backendlessFault.getMessage());
                }
            });
        } catch (Exception ex) {
            showCustomToast(getApplicationContext(), toastView, "Error: " + ex.getMessage());
        }

        //return feedBack;
    }//end method
}