package com.example.android.hockeyapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.List;

import static com.example.android.hockeyapp.ApplicationClass.REQUEST_PHONE;
import static com.example.android.hockeyapp.ApplicationClass.SELECTED_PLAYER;
import static com.example.android.hockeyapp.ApplicationClass.disableViews;
import static com.example.android.hockeyapp.ApplicationClass.enableViews;
import static com.example.android.hockeyapp.ApplicationClass.hideViews;
import static com.example.android.hockeyapp.ApplicationClass.progressDialog;
import static com.example.android.hockeyapp.ApplicationClass.selectAllQuery;
import static com.example.android.hockeyapp.ApplicationClass.showCustomToast;
import static com.example.android.hockeyapp.ApplicationClass.showProgressDialog;
import static com.example.android.hockeyapp.ApplicationClass.showViews;

public class PlayerDetailsActivity extends AppCompatActivity implements FragMedicalDetails.SaveMedicalDetailsListener {

    View toastView;
    EditText etPlayerName, etPlayerSurname, etAidName, etAidPlan, etAidNumber, etAllergies,
            etPhone1, etPhone2;
    Button btnSaveMedicalInfo;
    ImageButton ibtnCallPar1, ibtnCallPar2;
    Player selectedPlayer;
    Team playerTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);

        initViews();

        disableForm();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            selectedPlayer = (Player) extras.getSerializable(SELECTED_PLAYER);

            populateFields(selectedPlayer);

            showProgressDialog(getApplicationContext(), "Getting Team",
                    "please wait while we get the team for " + selectedPlayer, false);
            Backendless.Data.of(Team.class).find(selectAllQuery("teamName"),
                    new AsyncCallback<List<Team>>() {
                        @Override
                        public void handleResponse(List<Team> response) {
                            progressDialog.dismiss();
                            if (!response.isEmpty()) {
                                playerTeam = getPlayerTeam(selectedPlayer.getTeamName(), response);
                            } else {
                                showCustomToast(getApplicationContext(), toastView,
                                        "Player team not found.");
                            }
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            progressDialog.dismiss();
                            showCustomToast(getApplicationContext(), toastView,
                                    "Error: " + fault.getMessage());

                        }
                    });

            ibtnCallPar1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPlayer.getPar1CellNo().isEmpty() ||
                            selectedPlayer.getPar1CellNo().contains("No Data")) {
                        showCustomToast(getApplicationContext(), toastView,
                                "No phone number provided for Parent 1");
                    } else {
                        callParent(selectedPlayer.getPar1CellNo());
                    }
                }
            });

            ibtnCallPar2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPlayer.getPar2CellNo().isEmpty() || selectedPlayer.getPar2CellNo().contains("No Data")) {
                        showCustomToast(getApplicationContext(), toastView, "No phone number provided for Parent 2");
                    } else {
                        callParent(selectedPlayer.getPar2CellNo());
                    }
                }
            });
        }
    }

    private void disableForm() {
        disableViews(etPlayerName, etPlayerSurname, etAidName, etAidPlan, etAidNumber, etAllergies,
                etPhone1, etPhone2);
        hideViews(btnSaveMedicalInfo);
        showViews(ibtnCallPar1, ibtnCallPar2);
    }

    private void enableForm() {
        enableViews(etPlayerName, etPlayerSurname, etAidName, etAidPlan, etAidNumber, etAllergies,
                etPhone1, etPhone2);
        showViews(btnSaveMedicalInfo);
        hideViews(ibtnCallPar1, ibtnCallPar2);
    }

    private void populateFields(Player selectedPlayer) {

        etPlayerName.setText(selectedPlayer.getName());
        etPlayerSurname.setText(selectedPlayer.getSurname());
        etAidName.setText(selectedPlayer.getAidName());
        etAidPlan.setText(selectedPlayer.getAidPlan());
        etAidNumber.setText(selectedPlayer.getAidNumber());
        etAllergies.setText(selectedPlayer.getAllergies());
        etPhone1.setText(selectedPlayer.getPar1CellNo());
        etPhone2.setText(selectedPlayer.getPar2CellNo());

    }

    private void initViews() {

        toastView = getLayoutInflater().inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout));
        etPlayerName = findViewById(R.id.etPlayerNamePD);
        etPlayerSurname = findViewById(R.id.etPlayerSurnamePD);
        etAidName = findViewById(R.id.etAidName);
        etAidPlan = findViewById(R.id.etAidPlan);
        etAidNumber = findViewById(R.id.etAidNumber);
        etAllergies = findViewById(R.id.etAllergies);
        etPhone1 = findViewById(R.id.etPhone1);
        etPhone2 = findViewById(R.id.etPhone2);
        btnSaveMedicalInfo = findViewById(R.id.btnSaveMedicalInfo);
        ibtnCallPar1 = findViewById(R.id.ibtnCallPar1);
        ibtnCallPar2 = findViewById(R.id.ibtnCallPar2);

        btnSaveMedicalInfo.setText("Update Player");

    }

    @Override
    public void onSaveMedicalDetails(Medical medical) {

        selectedPlayer.setName(etPlayerName.getText().toString().trim());
        selectedPlayer.setSurname(etPlayerSurname.getText().toString().trim());
        selectedPlayer.setPlayerMedicalInfo(medical);

        showProgressDialog(getApplicationContext(), "Updating Player",
                "please wait while updating " + selectedPlayer + "...", true);
        Backendless.Persistence.save(selectedPlayer, new AsyncCallback<Player>() {
            @Override
            public void handleResponse(Player response) {
                progressDialog.dismiss();
                disableForm();
                showCustomToast(getApplicationContext(), toastView, "Successfully updated "
                        + response);
                populateFields(response);

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                progressDialog.dismiss();
                populateFields(selectedPlayer);
                showCustomToast(getApplicationContext(), toastView, "Error:\n"
                        + fault.getMessage());
            }
        })
        ;
    }

    /**
     * This method starts an implicit intent that calls a parent (stackoverflow)
     *
     * @param parentPhone cellphone string of a parent
     */
    private void callParent(String parentPhone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + parentPhone));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE);
        } else {
            startActivity(callIntent);
        }
    }

    /**
     * This methods finds a team in an List of Teams by its name
     *
     * @param playerTeamName name of the team of a player
     * @param teamList       list of teams to search through for a team
     * @return the Team in which the player is playing for, or null if no team is found
     */
    private Team getPlayerTeam(String playerTeamName, List<Team> teamList) {

        Team playerTeam = null;

        for (Team t : teamList) {
            if (t.getTeamName().contains(playerTeamName)) {
                playerTeam = t;
                break;
            }
        }
        return playerTeam;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_player_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ab_edit_player:
                enableForm();
                break;
            case R.id.ab_delete_player:


                // TODO: 2018/10/13 check if team has match
                if (!playerTeam.hasMatch()) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setIcon(android.R.drawable.ic_menu_delete);
                    dialog.setTitle(getString(R.string.alert_delete_title) + ": "
                            + selectedPlayer.getName());
                    dialog.setMessage(String.format(getString(R.string.alert_delete_message),
                            selectedPlayer.toString()));
                    dialog.setCancelable(false);

                    dialog.setPositiveButton(getString(R.string.alert_positive),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        showProgressDialog(getApplicationContext(), "Deleting Player",
                                                "please wait while removing "
                                                        + selectedPlayer + "...", true);
                                        Backendless.Persistence.of(Player.class).remove(selectedPlayer,
                                                new AsyncCallback<Long>() {
                                                    @Override
                                                    public void handleResponse(Long response) {
                                                        progressDialog.dismiss();
                                                        showCustomToast(getApplicationContext(),
                                                                toastView, selectedPlayer
                                                                        + " successfully deleted!");
                                                        finish();
                                                    }

                                                    @Override
                                                    public void handleFault(BackendlessFault fault) {
                                                        progressDialog.dismiss();
                                                        showCustomToast(getApplicationContext(),
                                                                toastView, "Error Deleting: "
                                                                        + fault.getMessage());
                                                    }
                                                });
                                    } catch (Exception e) {
                                        showCustomToast(getApplicationContext(), toastView,
                                                "Delete Error: " + e.getMessage());
                                    }
                                }//end onClick
                            });

                    dialog.setNegativeButton(getString(R.string.alert_negative),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog d = dialog.create();
                    d.show();
                } else {
                    showCustomToast(getApplicationContext(), toastView, "Cannot delete!\n"
                            + selectedPlayer + " has a match pending.");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}