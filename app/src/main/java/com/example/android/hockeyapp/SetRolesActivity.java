package com.example.android.hockeyapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.hockeyapp.ApplicationClass.getUserString;
import static com.example.android.hockeyapp.ApplicationClass.loadSpinnerValues;
import static com.example.android.hockeyapp.ApplicationClass.progressDialog;
import static com.example.android.hockeyapp.ApplicationClass.selectAllQuery;
import static com.example.android.hockeyapp.ApplicationClass.sessionUser;
import static com.example.android.hockeyapp.ApplicationClass.setupActionBar;
import static com.example.android.hockeyapp.ApplicationClass.showCustomToast;
import static com.example.android.hockeyapp.ApplicationClass.showProgressDialog;
import static com.example.android.hockeyapp.ApplicationClass.userList;

public class SetRolesActivity extends AppCompatActivity {

    View toastView;
    Spinner spnUsers;
    RadioButton rdAdmin, rdCoach, rdNone;
    private List<String> userNames;
    BackendlessUser selectedUser;
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_roles);

        initViews();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setupActionBar(actionBar, "Set Roles", getUserString(sessionUser));

        userList = new ArrayList<>();
        userNames = new ArrayList<>();

        updateUserList();

        spnUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                getCurrentUserRole(position, rdAdmin, rdCoach, rdNone);
                selectedUser = userList.get(position);
                pos = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showCustomToast(getApplicationContext(), toastView, "No User selected!");
            }
        });

    }

    private void updateUserList() {
        userList.clear();
        userNames.clear();

        showProgressDialog(this, "Getting Users", "please wait while retrieving users...", false);
        Backendless.Data.of(BackendlessUser.class).find(selectAllQuery("name"), new AsyncCallback<List<BackendlessUser>>() {
            @Override
            public void handleResponse(List<BackendlessUser> response) {
                try {
                    for (int i = 0; i < response.size(); i++) {
                        if (response.get(i) != null) {
                            userList.add(response.get(i));
                            userNames.add(getUserString(response.get(i)));
                        }
                    }

                    loadSpinnerValues(getApplicationContext(), spnUsers, userNames);
                    spnUsers.setSelection(pos);

                } catch (Exception ex) {
                    showCustomToast(getApplicationContext(), toastView, "Error Retrieving users:\n" + ex.getMessage());
                } finally {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                progressDialog.dismiss();
                showCustomToast(getApplicationContext(), toastView, "Error Retrieving users:\n" + fault.getMessage());
            }
        });

    }

    private void initViews() {

        toastView = getLayoutInflater().inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
        spnUsers = findViewById(R.id.spnUsers);
        rdAdmin = findViewById(R.id.rdAdmin);
        rdCoach = findViewById(R.id.rdCoach);
        rdNone = findViewById(R.id.rdNone);

    }

    /**
     * This method automatically selects a selected user's role
     *
     * @param position     in which the selected user is located on the list
     * @param radioButtons that shows what role the user's current role
     */
    private void getCurrentUserRole(int position, RadioButton... radioButtons) {

        if (userList != null) {
            String role = userList.get(position).getProperty("role").toString();
            for (RadioButton rd : radioButtons) {
                if (rd.getText().toString().contains(role)) {
                    rd.setChecked(true);
                    break;
                }
            }
        } else {
            showCustomToast(getApplicationContext(), toastView, "User list empty.");
        }
    }

    public void onClickSaveRole(View view) {

        if (selectedUser != null) {
            selectedUser.setProperty("role", getSelectedRole(rdAdmin, rdCoach, rdNone));
            showProgressDialog(getApplicationContext(),
                    String.format("Assigning Role to %s (%s)", selectedUser.getProperty("name"), selectedUser.getEmail()),
                    "please wait while selected user is being updated...", false);
            Backendless.Persistence.save(selectedUser, new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser response) {

                    progressDialog.dismiss();
                    updateUserList();
                    showCustomToast(getApplicationContext(), toastView,
                            response.getProperty("name") + " " + response.getProperty("surname") +
                                    " successfully assigned role: " + response.getProperty("role"));

                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    progressDialog.dismiss();
                    showCustomToast(getApplicationContext(), toastView, "Error Assigning User Role:\n" + fault.getMessage());
                }
            });
        } else {
            showCustomToast(getApplicationContext(), toastView, "please select user from the dropdown button.");
        }
    }

    /**
     * @param radioButtons that will be used to change the role of the selected user
     * @return String role determined by the selected radiobutton
     */
    private String getSelectedRole(RadioButton... radioButtons) {
        String role = "None";
        for (RadioButton rd : radioButtons) {
            if (rd.isChecked()) {
                if (rd.getText().toString().contains("Admin")) {
                    role = "Admin";
                } else if (rd.getText().toString().contains("Coach")) {
                    role = "Coach";
                }
                break;
            }
        }
        return role;
    }

}