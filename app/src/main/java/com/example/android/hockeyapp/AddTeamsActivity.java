package com.example.android.hockeyapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.List;
import static com.example.android.hockeyapp.ApplicationClass.clearFields;
import static com.example.android.hockeyapp.ApplicationClass.clearSpinners;
import static com.example.android.hockeyapp.ApplicationClass.getSpinnerValue;
import static com.example.android.hockeyapp.ApplicationClass.getUserString;
import static com.example.android.hockeyapp.ApplicationClass.hideViews;
import static com.example.android.hockeyapp.ApplicationClass.isValidFields;
import static com.example.android.hockeyapp.ApplicationClass.isValidSpinner;
import static com.example.android.hockeyapp.ApplicationClass.loadSpinnerValues;
import static com.example.android.hockeyapp.ApplicationClass.progressDialog;
import static com.example.android.hockeyapp.ApplicationClass.selectAllQuery;
import static com.example.android.hockeyapp.ApplicationClass.sessionUser;
import static com.example.android.hockeyapp.ApplicationClass.setupActionBar;
import static com.example.android.hockeyapp.ApplicationClass.showCustomToast;
import static com.example.android.hockeyapp.ApplicationClass.showProgressDialog;
import static com.example.android.hockeyapp.ApplicationClass.showViews;

public class AddTeamsActivity extends AppCompatActivity {

    View toastView;
    LinearLayout frmAddTeam, frmAddOpponent;
    Spinner spnTeamOrOpponent, spnTeamCoach, spnAgeGroup;
    EditText etTeamName, etOpponentName;
    List<BackendlessUser> coachList;
    private List<String> coachNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teams);

        initViews();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setupActionBar(actionBar, " Add Team or Opponent", getUserString(sessionUser));

        coachList = new ArrayList<>();
        coachNames = new ArrayList<>();
        getCoachList();

        spnTeamOrOpponent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupAddForm(spnTeamOrOpponent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showCustomToast(getApplicationContext(), toastView, "Nothing was selected");
            }

        });

        spnAgeGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spnAgeGroup.getSelectedItemPosition() > 0)
                    getSpinnerValue(spnAgeGroup, etTeamName);
                else
                    etTeamName.setText(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showCustomToast(getApplicationContext(), toastView, "Nothing was selected");
            }

        });

    }

    /**
     * This method initializes Views
     */
    private void initViews() {

        toastView = getLayoutInflater().inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout));
        frmAddTeam = findViewById(R.id.add_team_form);
        frmAddOpponent = findViewById(R.id.add_opponent_form);
        spnAgeGroup = findViewById(R.id.spnAgeGroup);
        spnTeamCoach = findViewById(R.id.spnTeamCoach);
        spnTeamOrOpponent = findViewById(R.id.spnTeamOrOpponent);
        etTeamName = findViewById(R.id.etTeamName);
        etOpponentName = findViewById(R.id.etOpponentName);

        coachNames = new ArrayList<>();
        coachNames.add("-- select coach for team --");
    }

    /**
     * This method sets up the landing page depending on the role of a logged in user;
     *
     * @param teamOrOpponent is used to determine which form to show
     */
    private void setupAddForm(String teamOrOpponent) {

        switch (teamOrOpponent) {
            case "Team":
                hideViews(frmAddOpponent);
                showViews(frmAddTeam);
                break;
            case "Opponent":
                hideViews(frmAddTeam);
                showViews(frmAddOpponent);
                break;
            default:
                hideViews(frmAddOpponent, frmAddTeam);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_teams_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ab_team_lists:
                startActivity(new Intent(getApplicationContext(), TeamListsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickSubmitTeam(View view) {

        boolean isValidTeam = false;
        Team newTeam = new Team();
        //first validate team
        if (spnTeamOrOpponent.getSelectedItem().toString().equalsIgnoreCase("Team")) {

            //set team according to which option is selected, in this case, Team
            if (isValidSpinner(spnAgeGroup, spnTeamCoach, spnTeamOrOpponent)) {
                if (isValidFields(etTeamName)) {
                    newTeam.setTeamName(etTeamName.getText().toString().trim());
                    newTeam.setTeamType(spnTeamOrOpponent.getSelectedItem().toString().trim());
                    newTeam.setAgeGroup(spnAgeGroup.getSelectedItem().toString().trim());
                    newTeam.setTeamCoach(spnTeamCoach.getSelectedItem().toString().trim());
                    newTeam.setHasMatch(false);
                    isValidTeam = true;
                }
            }

        } else {

            //this executes when Opponent is selected
            if (isValidFields(etOpponentName)) {
                newTeam.setTeamName(etOpponentName.getText().toString().trim());
                newTeam.setTeamType("Opponent");
                newTeam.setHasMatch(false);
                isValidTeam = true;
            }
        }

        if (isValidTeam) {

            showProgressDialog(getApplicationContext(), "Adding Team",
                    "please wait while adding new team...", false);
            Backendless.Data.save(newTeam, new AsyncCallback<Team>() {
                @Override
                public void handleResponse(Team response) {
                    progressDialog.dismiss();
                    showCustomToast(getApplicationContext(), toastView,
                            spnTeamOrOpponent.getSelectedItem().toString() + " "
                                    + response.getTeamName() + " added successfully");
                    clearFields(etTeamName, etOpponentName);
                    clearSpinners(spnAgeGroup, spnTeamCoach, spnTeamOrOpponent);
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    progressDialog.dismiss();
                    showCustomToast(getApplicationContext(), toastView, "Error: "
                            + fault.getMessage());
                    if (spnTeamOrOpponent.getSelectedItem().toString()
                            .equalsIgnoreCase("Team")) {
                        etTeamName.setError(fault.getMessage());
                    } else {
                        etOpponentName.setError(fault.getMessage());
                    }
                }
            });
        } else {
            showCustomToast(getApplicationContext(), toastView,
                    "Please make sure all options are selected.");
        }
    }

    private void getCoachList() {
        coachList.clear();
        coachNames.clear();
        coachNames.add(0, "-- please select coach --");
        showProgressDialog(this, "Getting Coaches",
                "please wait while retrieving coaches...", false);
        Backendless.Data.of(BackendlessUser.class).find(selectAllQuery("name"),
                new AsyncCallback<List<BackendlessUser>>() {
                    @Override
                    public void handleResponse(List<BackendlessUser> response) {
                        try {
                            for (int i = 0; i < response.size(); i++) {
                                if (response.get(i) != null && response.get(i).getProperty("role")
                                        .toString().equalsIgnoreCase("Coach")) {
                                    coachList.add(response.get(i));
                                    coachNames.add(getUserString(response.get(i)));
                                }
                            }

                            loadSpinnerValues(getApplicationContext(), spnTeamCoach, coachNames);

                        } catch (Exception ex) {
                            showCustomToast(getApplicationContext(), toastView,
                                    "Error Retrieving coaches:\n" + ex.getMessage());
                            coachNames.add("No coach selected");
                            loadSpinnerValues(getApplicationContext(), spnTeamCoach, coachNames);
                        } finally {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        progressDialog.dismiss();
                        showCustomToast(getApplicationContext(), toastView,
                                "Error Retrieving users:\n" + fault.getMessage());
                        coachNames.add("No coach selected");
                        loadSpinnerValues(getApplicationContext(), spnTeamCoach, coachNames);
                    }
                });

    }

}