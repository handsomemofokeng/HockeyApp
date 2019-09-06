package com.example.android.hockeyapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.hockeyapp.ApplicationClass.clearSpinners;
import static com.example.android.hockeyapp.ApplicationClass.getUserString;
import static com.example.android.hockeyapp.ApplicationClass.isValidSpinner;
import static com.example.android.hockeyapp.ApplicationClass.loadSpinnerValues;
import static com.example.android.hockeyapp.ApplicationClass.progressDialog;
import static com.example.android.hockeyapp.ApplicationClass.selectAllQuery;
import static com.example.android.hockeyapp.ApplicationClass.sessionUser;
import static com.example.android.hockeyapp.ApplicationClass.setupActionBar;
import static com.example.android.hockeyapp.ApplicationClass.showCustomToast;
import static com.example.android.hockeyapp.ApplicationClass.showProgressDialog;

public class MovePlayersActivity extends AppCompatActivity {

    Spinner spnPlayers, spnTeams;
    private View toastView;
    TextView tvCurrentTeam;

    List<Player> playerList;
    List<String> playerNames;

    List<Team> teamList;
    List<String> teamNames;
    private Player selectedPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_players);

        initViews();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setupActionBar(actionBar, "Move Players", getUserString(sessionUser));

        playerList = new ArrayList<>();
        playerNames = new ArrayList<>();

        teamList = new ArrayList<>();
        teamNames = new ArrayList<>();

        getAllPlayers();

        spnPlayers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spnPlayers.getSelectedItemPosition() > 0) {
                    selectedPlayer = playerList.get(position - 1);
                    tvCurrentTeam.setText(String.format(getString(R.string.player_current_team),
                            selectedPlayer.getName(), selectedPlayer.getSurname(),
                            selectedPlayer.getTeamName()));
                    spnTeams.setSelection(0);
                } else {
                    tvCurrentTeam.setText(getString(R.string.no_player_selected));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void getAllPlayers() {

        playerNames.clear();
        playerNames.add(0, "-- select player to move --");
        showProgressDialog(this, "Getting Players", "Please wait while " +
                "loading payers...", true);
        Backendless.Data.of(Player.class).find(selectAllQuery("name"),
                new AsyncCallback<List<Player>>() {

                    @Override
                    public void handleResponse(List<Player> response) {

                        progressDialog.dismiss();
                        playerList.addAll(response);
                        for (Player p : playerList) {
                            playerNames.add(p.toString());
                        }

                        loadSpinnerValues(getApplicationContext(), spnPlayers, playerNames);
                        getAllTeams();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        progressDialog.dismiss();
                        showCustomToast(getApplicationContext(), toastView, "Error: "
                                + fault.getMessage());
                    }

                });
    }

    private void initViews() {
        toastView = getLayoutInflater().inflate(R.layout.custom_toast, (
                ViewGroup) findViewById(R.id.toast_layout));
        spnPlayers = findViewById(R.id.spnPlayerMove);
        spnTeams = findViewById(R.id.spnTeamMove);
        tvCurrentTeam = findViewById(R.id.tvCurrentTeam);
    }

    private void getAllTeams() {
        teamNames.clear();
        teamNames.add(0, "-- select team to move to --");
        showProgressDialog(this, "Getting Teams", "Please wait while loading "
                + "teams...", true);
        Backendless.Data.of(Team.class).find(selectAllQuery("teamName"),
                new AsyncCallback<List<Team>>() {
                    @Override
                    public void handleResponse(List<Team> response) {

                        progressDialog.dismiss();
                        teamList.addAll(response);
                        for (Team t : teamList) {
                            if (t.getTeamType().trim().contains("Team")) {
                                teamNames.add(t.getTeamName());
                            }
                        }
                        loadSpinnerValues(getApplicationContext(), spnTeams, teamNames);

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        progressDialog.dismiss();
                        showCustomToast(getApplicationContext(), toastView, "Error: "
                                + fault.getMessage());
                    }
                });
    }

    public void onClickMovePlayer(View view) {

        if (isValidSpinner(spnPlayers, spnTeams)) {

            if (!selectedPlayer.getTeamName().contains(spnTeams.getSelectedItem().toString())) {
                selectedPlayer.setTeamName(spnTeams.getSelectedItem().toString());
                showProgressDialog(getApplicationContext(), "Moving Player", "moving "
                        + selectedPlayer.getName() + " to " + spnTeams.getSelectedItem().toString()
                        + "...", true);
                Backendless.Persistence.save(selectedPlayer, new AsyncCallback<Player>() {
                    @Override
                    public void handleResponse(Player response) {

                        clearSpinners(spnPlayers, spnTeams);
                        progressDialog.dismiss();
                        showCustomToast(getApplicationContext(), toastView, "Successfully moved "
                                + selectedPlayer.toString() + " to " + selectedPlayer.getTeamName());
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        progressDialog.dismiss();
                        showCustomToast(getApplicationContext(), toastView,
                                "Error: " + fault.getMessage());

                    }
                });
            } else {
                showCustomToast(getApplicationContext(), toastView, "Cannot Move "
                        + selectedPlayer + "!\nPlayer already in " + spnTeams.getSelectedItem().toString());
            }
        } else {
            showCustomToast(getApplicationContext(), toastView,
                    "Please make sure a Player and Team are selected.");
        }
    }

}
