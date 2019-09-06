package com.example.android.hockeyapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.hockeyapp.ApplicationClass.SELECTED_PLAYER;
import static com.example.android.hockeyapp.ApplicationClass.getUserString;
import static com.example.android.hockeyapp.ApplicationClass.isValidFields;
import static com.example.android.hockeyapp.ApplicationClass.loadSpinnerValues;
import static com.example.android.hockeyapp.ApplicationClass.playerList;
import static com.example.android.hockeyapp.ApplicationClass.progressDialog;
import static com.example.android.hockeyapp.ApplicationClass.selectAllQuery;
import static com.example.android.hockeyapp.ApplicationClass.selectQuery;
import static com.example.android.hockeyapp.ApplicationClass.sessionUser;
import static com.example.android.hockeyapp.ApplicationClass.setupActionBar;
import static com.example.android.hockeyapp.ApplicationClass.showCustomToast;
import static com.example.android.hockeyapp.ApplicationClass.showProgressDialog;

public class MyPlayersActivity extends AppCompatActivity implements PlayerListFragment.OnListFragmentInteractionListener {

    View toastView;
    Spinner spnTeamsPL;
    List<Player> filteredPlayerList;
    public static List<Team> myTeams;
    List<String> allTeams, filteredTeams;
    EditText etSearchPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);

        initViews();

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setupActionBar(actionBar, "My Players", getUserString(sessionUser));

        filteredPlayerList = new ArrayList<>();
        playerList = new ArrayList<>();
        filteredPlayerList = new ArrayList<>();

        allTeams = new ArrayList<>();
        filteredTeams = new ArrayList<>();
        myTeams = new ArrayList<>();

        getPlayers();
        progressDialog.dismiss();
        getTeams();

        spnTeamsPL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    filteredPlayerList.clear();
                    for (Player player : playerList) {
                        if (player.getTeamName().equalsIgnoreCase(spnTeamsPL.getSelectedItem().toString())) {
                            filteredPlayerList.add(player);
                        }
                    }
                    if (filteredPlayerList.size() < 1) {
                        showCustomToast(getApplicationContext(), toastView,
                                "No players found for " + spnTeamsPL.getSelectedItem().toString());
                    } else {
                        showCustomToast(getApplicationContext(), toastView,
                                "Displaying players for " + spnTeamsPL.getSelectedItem().toString());
                    }
                    PlayerListFragment.recyclerView.setAdapter(new PlayerAdapter(filteredPlayerList,
                            MyPlayersActivity.this));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getTeams() {
        //get Teams

        showProgressDialog(this, "Getting Lists", "Please wait while loading...",
                true);
        Backendless.Data.of(Team.class).find(selectQuery("teamType", "Team",
                "teamName"), new AsyncCallback<List<Team>>() {
            @Override
            public void handleResponse(List<Team> response) {

                progressDialog.dismiss();
                myTeams.addAll(response);

                filteredTeams.clear();
                filteredTeams.add(0, "-- filter by team --");
                allTeams.add(0, "-- filter by team --");

                if (!response.isEmpty()) {
                    if (String.valueOf(sessionUser.getProperty("role"))
                            .contains("Coach")) {

                        for (Team team : myTeams) {
                            if (team.getTeamCoach().contains(sessionUser.getEmail())) {
                                filteredTeams.add(team.getTeamName());
                            }
                        }
                        loadSpinnerValues(getApplicationContext(), spnTeamsPL, filteredTeams);
                    } else {

                        for (Team team : response) {
                            allTeams.add(team.getTeamName());
                        }
                        loadSpinnerValues(getApplicationContext(), spnTeamsPL, allTeams);
                    }
                } else {
                    showCustomToast(getApplicationContext(), toastView, "No teams found!");
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                progressDialog.dismiss();
                showCustomToast(getApplicationContext(), toastView, "Error:\n"
                        + fault.getMessage());
            }
        });
    }

    private void getPlayers() {
        //get Players
        showProgressDialog(this, "Getting Players",
                "please wait while loading players...", true);
        Backendless.Data.of(Player.class).find(selectAllQuery("name"), new AsyncCallback<List<Player>>() {
            @Override
            public void handleResponse(List<Player> response) {

                progressDialog.dismiss();
                playerList.clear();
                playerList.addAll(response);

                PlayerListFragment.recyclerView.setAdapter(new PlayerAdapter(playerList,
                        MyPlayersActivity.this));

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                progressDialog.dismiss();
                showCustomToast(getApplicationContext(), toastView,
                        "Error: " + fault.getMessage());
            }
        });
    }

    private void initViews() {

        toastView = getLayoutInflater().inflate(R.layout.custom_toast, (
                ViewGroup) findViewById(R.id.toast_layout));

        spnTeamsPL = findViewById(R.id.spnTeamsPL);
        etSearchPlayer = findViewById(R.id.etSearchPlayer);

    }

    public void onClick_SearchPlayer(View view) {

        List<Player> fullList;
        if (sessionUser.getProperty("role").toString().equalsIgnoreCase("Admin")) {
            fullList = playerList;
        } else {
            fullList = filteredPlayerList;
        }

        if (isValidFields(etSearchPlayer)) {

            ArrayList<Player> searchList = new ArrayList<>();
            for (Player player : fullList) {
                if (player.toString().toLowerCase()
                        .contains(etSearchPlayer.getText().toString().trim().toLowerCase())) {
                    searchList.add(player);
                }
            }
            String message;
            if (searchList.isEmpty()) {
                message = "No records found for " + etSearchPlayer.getText().toString();
            } else {
                message = "Displaying records found for " + etSearchPlayer.getText().toString();
            }
            showCustomToast(getApplicationContext(), toastView, message);
            PlayerListFragment.recyclerView.setAdapter(new PlayerAdapter(searchList,
                    MyPlayersActivity.this));
        } else {
            etSearchPlayer.setError("Please enter learner name to search");
        }
    }

    @Override
    public void onListFragmentInteraction(Player player) {

        startActivity(new Intent(getApplicationContext(), PlayerDetailsActivity.class)
                .putExtra(SELECTED_PLAYER, player));

    }

}