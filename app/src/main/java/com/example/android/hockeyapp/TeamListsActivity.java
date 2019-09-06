package com.example.android.hockeyapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.List;

import static com.example.android.hockeyapp.ApplicationClass.getUserString;
import static com.example.android.hockeyapp.ApplicationClass.progressDialog;
import static com.example.android.hockeyapp.ApplicationClass.selectAllQuery;
import static com.example.android.hockeyapp.ApplicationClass.sessionUser;
import static com.example.android.hockeyapp.ApplicationClass.setupActionBar;
import static com.example.android.hockeyapp.ApplicationClass.showCustomToast;
import static com.example.android.hockeyapp.ApplicationClass.showProgressDialog;

public class TeamListsActivity extends AppCompatActivity {

    private View toastView;
    TextView tvTeams, tvOpponents;
//    String teams = "", opponents = "";
//    List<String> teamList, opponentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_lists);

        initViews();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setupActionBar(actionBar, "Team/Opponent Lists", getUserString(sessionUser));

        showProgressDialog(this, "Getting Lists",
                "Please wait while loading...", false);
        Backendless.Data.of(Team.class).find(selectAllQuery("teamName"),
                new AsyncCallback<List<Team>>() {
                    @Override
                    public void handleResponse(List<Team> response) {
                        progressDialog.dismiss();
                        for (int i = 0; i < response.size(); i++) {
                            if (response.get(i).getTeamType().trim().contains("Team")) {
                                if (tvTeams.getText().toString().isEmpty()) {
                                    tvTeams.append(response.get(i).getTeamName());
                                } else {
                                    tvTeams.append("\n" + response.get(i).getTeamName());
                                }
                            } else {
                                if (response.get(i).getTeamType().trim().contains("Opponent")) {
                                    if (tvOpponents.getText().toString().isEmpty()) {
                                        tvOpponents.append(response.get(i).getTeamName());
                                    } else {
                                        tvOpponents.append("\n" + response.get(i).getTeamName());
                                    }
                                }
                            }
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

    private void initViews() {
        toastView = getLayoutInflater().inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout));
        tvTeams = findViewById(R.id.tvTeamNames);
        tvOpponents = findViewById(R.id.tvOpponentNames);
    }

}