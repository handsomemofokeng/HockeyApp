package com.example.android.hockeyapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.hockeyapp.ApplicationClass.getUserString;
import static com.example.android.hockeyapp.ApplicationClass.isValidSpinner;
import static com.example.android.hockeyapp.ApplicationClass.loadSpinnerValues;
import static com.example.android.hockeyapp.ApplicationClass.playerList;
import static com.example.android.hockeyapp.ApplicationClass.progressDialog;
import static com.example.android.hockeyapp.ApplicationClass.selectAllQuery;
import static com.example.android.hockeyapp.ApplicationClass.sessionUser;
import static com.example.android.hockeyapp.ApplicationClass.setupActionBar;
import static com.example.android.hockeyapp.ApplicationClass.showCustomToast;
import static com.example.android.hockeyapp.ApplicationClass.showProgressDialog;

public class MatchLineUpActivity extends AppCompatActivity {

    View toastView;

    Spinner spnMatchTeam, spnMatchOpponent, spnPosition1, spnPosition2, spnPosition3, spnPosition4,
            spnPosition5, spnPosition6, spnPosition7, spnPosition8, spnPosition9, spnPosition10,
            spnPosition11, spnPosition12, spnPosition13, spnPosition14;

    Spinner spnGoals1, spnGoals2, spnGoals3, spnGoals4, spnGoals5, spnGoals6, spnGoals7, spnGoals8,
            spnGoals9, spnGoals10, spnGoals11, spnGoals12, spnGoals13, spnGoals14;

    RatingBar rbPlayerRating1, rbPlayerRating2, rbPlayerRating3, rbPlayerRating4, rbPlayerRating5,
            rbPlayerRating6, rbPlayerRating7, rbPlayerRating8, rbPlayerRating9, rbPlayerRating10,
            rbPlayerRating11, rbPlayerRating12, rbPlayerRating13, rbPlayerRating14;

    List<Team> teamList;
    List<String> myPlayerList, myTeamList, myOpponentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_line_up);

        initViews();

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setupActionBar(actionBar, "Match Line-Up", getUserString(sessionUser));

        myPlayerList = new ArrayList<>();
        myTeamList = new ArrayList<>();
        myOpponentList = new ArrayList<>();
        teamList = new ArrayList<>();

        getPlayers();
        progressDialog.dismiss();
        getTeamsAndOpponents();

        spnMatchTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isValidSpinner(spnMatchTeam)) {
                    getPlayersByTeam(spnMatchTeam.getSelectedItem().toString().trim());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getPlayers() {

        playerList = new ArrayList<>();

//        DataQueryBuilder selectQuery;
//        if (sessionUser.getProperty("role").toString().trim().contains("Coach")) {
//            selectQuery = selectQuery("teamName", getUserString(sessionUser), "name");
//        } else {
//            selectQuery = selectAllQuery("teamName");
//        }

        showProgressDialog(this, "Getting Lists", "Please wait while loading...",
                false);
        Backendless.Data.of(Player.class).find(selectAllQuery("name"),
                new AsyncCallback<List<Player>>() {
                    @Override
                    public void handleResponse(List<Player> response) {
                        progressDialog.dismiss();
                        playerList.addAll(response);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        progressDialog.dismiss();
                        showCustomToast(getApplicationContext(), toastView, "Error:\n"
                                + fault.getMessage());
                    }
                });

    }

    private void getPlayersByTeam(String teamName) {

        myPlayerList.clear();
        myPlayerList.add(0, "-- select player --");

        for (Player player : playerList) {
            if (player.getTeamName().contains(teamName))
                myPlayerList.add(player.toString());
        }

        if (myPlayerList.size() <= 1) {
            myPlayerList.clear();
            myPlayerList.add(0, "-- no players for team --");
            showCustomToast(getApplicationContext(), toastView, "No players for " + teamName);
        }

        populatePlayerSpinners(myPlayerList, spnPosition1, spnPosition2, spnPosition3, spnPosition4,
                spnPosition5, spnPosition6, spnPosition7, spnPosition8, spnPosition9, spnPosition10,
                spnPosition11, spnPosition12, spnPosition13, spnPosition14);
    }

    private void getTeamsAndOpponents() {
        showProgressDialog(this, "Getting Lists", "Please wait while loading...",
                false);

//        DataQueryBuilder queryBuilder;
//        if (sessionUser.getProperty("role").toString().contains("Admin"))
//            queryBuilder = selectAllQuery("teamName");
//        else
//            queryBuilder = selectQuery("teamCoach", getUserString(sessionUser), "teamName");

        Backendless.Data.of(Team.class).find(selectAllQuery("teamName"),
                new AsyncCallback<List<Team>>() {
                    @Override
                    public void handleResponse(List<Team> response) {

                        progressDialog.dismiss();
                        myOpponentList.clear();
                        myTeamList.clear();

                        teamList.addAll(response);

                        myTeamList.add(0, "-- select team --");
                        myOpponentList.add(0, "-- select opponent --");

                        for (int i = 0; i < response.size(); i++) {
                            if (response.get(i).getTeamType().trim().contains("Team")) {
                                myTeamList.add(response.get(i).getTeamName());
                            } else {
                                if (response.get(i).getTeamType().trim().contains("Opponent")) {
                                    myOpponentList.add(response.get(i).getTeamName());
                                }
                            }
                        }
                        if (sessionUser.getProperty("role").toString().contains("Admin")) {
                            loadSpinnerValues(getApplicationContext(), spnMatchTeam, myTeamList);
                        } else {
                            //we have established that the User is a Coach
                            myTeamList.clear();
                            myTeamList.add(0, "-- select team --");
                            for (Team team : response) {
                                if (team.getTeamCoach().contains(sessionUser.getEmail())) {
                                    myTeamList.add(team.getTeamName());
                                }
                            }
                            if (!myTeamList.isEmpty())
                                loadSpinnerValues(getApplicationContext(), spnMatchTeam, myTeamList);
                            else {
                                myTeamList.add(0, "-- no teams assigned --");
                            }
                        }
                        loadSpinnerValues(getApplicationContext(), spnMatchOpponent, myOpponentList);

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        progressDialog.dismiss();
                        showCustomToast(getApplicationContext(), toastView, "Error:\n"
                                + fault.getMessage());
                    }

                });
    }

    private void populatePlayerSpinners(List<String> list, Spinner... spinners) {
        for (Spinner spn : spinners) {
            loadSpinnerValues(getApplicationContext(), spn, list);
        }
    }

    private void initViews() {

        toastView = getLayoutInflater().inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout));

        spnMatchTeam = findViewById(R.id.spnMatchTeam);
        spnMatchOpponent = findViewById(R.id.spnMatchOpponent);
        spnPosition1 = findViewById(R.id.spnPosition1);
        spnPosition2 = findViewById(R.id.spnPosition2);
        spnPosition3 = findViewById(R.id.spnPosition3);
        spnPosition4 = findViewById(R.id.spnPosition4);
        spnPosition5 = findViewById(R.id.spnPosition5);
        spnPosition6 = findViewById(R.id.spnPosition6);
        spnPosition7 = findViewById(R.id.spnPosition7);
        spnPosition8 = findViewById(R.id.spnPosition8);
        spnPosition9 = findViewById(R.id.spnPosition9);
        spnPosition10 = findViewById(R.id.spnPosition10);
        spnPosition11 = findViewById(R.id.spnPosition11);
        spnPosition12 = findViewById(R.id.spnPosition12);
        spnPosition13 = findViewById(R.id.spnPosition13);
        spnPosition14 = findViewById(R.id.spnPosition14);

        spnGoals1 = findViewById(R.id.spnGoals1);
        spnGoals2 = findViewById(R.id.spnGoals2);
        spnGoals3 = findViewById(R.id.spnGoals3);
        spnGoals4 = findViewById(R.id.spnGoals4);
        spnGoals5 = findViewById(R.id.spnGoals5);
        spnGoals6 = findViewById(R.id.spnGoals6);
        spnGoals7 = findViewById(R.id.spnGoals7);
        spnGoals8 = findViewById(R.id.spnGoals8);
        spnGoals9 = findViewById(R.id.spnGoals9);
        spnGoals10 = findViewById(R.id.spnGoals10);
        spnGoals11 = findViewById(R.id.spnGoals11);
        spnGoals12 = findViewById(R.id.spnGoals12);
        spnGoals13 = findViewById(R.id.spnGoals13);
        spnGoals14 = findViewById(R.id.spnGoals14);

        rbPlayerRating1 = findViewById(R.id.rbPlayerRating1);
        rbPlayerRating2 = findViewById(R.id.rbPlayerRating2);
        rbPlayerRating3 = findViewById(R.id.rbPlayerRating3);
        rbPlayerRating4 = findViewById(R.id.rbPlayerRating4);
        rbPlayerRating5 = findViewById(R.id.rbPlayerRating5);
        rbPlayerRating6 = findViewById(R.id.rbPlayerRating6);
        rbPlayerRating7 = findViewById(R.id.rbPlayerRating7);
        rbPlayerRating8 = findViewById(R.id.rbPlayerRating8);
        rbPlayerRating9 = findViewById(R.id.rbPlayerRating9);
        rbPlayerRating10 = findViewById(R.id.rbPlayerRating10);
        rbPlayerRating11 = findViewById(R.id.rbPlayerRating11);
        rbPlayerRating12 = findViewById(R.id.rbPlayerRating12);
        rbPlayerRating13 = findViewById(R.id.rbPlayerRating13);
        rbPlayerRating14 = findViewById(R.id.rbPlayerRating14);

    }

}