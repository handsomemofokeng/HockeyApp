package com.example.android.hockeyapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.hockeyapp.ApplicationClass.clearFields;
import static com.example.android.hockeyapp.ApplicationClass.clearSpinners;
import static com.example.android.hockeyapp.ApplicationClass.isValidFields;
import static com.example.android.hockeyapp.ApplicationClass.isValidSpinner;
import static com.example.android.hockeyapp.ApplicationClass.loadSpinnerValues;
import static com.example.android.hockeyapp.ApplicationClass.progressDialog;
import static com.example.android.hockeyapp.ApplicationClass.selectQuery;
import static com.example.android.hockeyapp.ApplicationClass.sessionUser;
import static com.example.android.hockeyapp.ApplicationClass.showProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragPlayerDetails extends Fragment {


    View toastView;
    EditText etPlayerName, etPlayerSurname;
    Spinner spnPlayerTeam;
    Button btnSavePlayer;
    List<String> teamList;

    private SaveLearnerDetailsListener savePlayerDetailsListener;

    public FragPlayerDetails() {
        // Required empty public constructor
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View playerDetailsView = inflater.inflate(R.layout.fragment_player_details, container,
                false);

        //Initialise Fragment UI Reference
        etPlayerName = playerDetailsView.findViewById(R.id.etPlayerName);
        etPlayerSurname = playerDetailsView.findViewById(R.id.etPlayerSurname);
        spnPlayerTeam = playerDetailsView.findViewById(R.id.spnPlayerTeam);
        btnSavePlayer = playerDetailsView.findViewById(R.id.btnSavePlayer);

        teamList = new ArrayList<>();
        showProgressDialog(getContext(), "Loading Teams",
                "please wait while loading team list...", true);
        Backendless.Data.of(Team.class).find(selectQuery("teamType",
                "Team", "teamName"), new AsyncCallback<List<Team>>() {
            @Override
            public void handleResponse(List<Team> response) {
                progressDialog.dismiss();
                teamList.clear();
                teamList.add(0, "-- select team for player --");

                if (sessionUser.getProperty("role").toString().contains("Admin")){
                    //add all team names
                    for (Team team : response) {
                        teamList.add(team.getTeamName());
                    }
                }else{
                    //add only teams associated with coach
                    for (Team team : response) {
                        if (team.getTeamCoach().trim().contains(sessionUser.getEmail())) {
                            teamList.add(team.getTeamName());
                        }
                    }
                }

                if (teamList.isEmpty()) {
//                    coachList.clear();
                    teamList.add(0, "-- no teams found --");
                }
                loadSpinnerValues(getContext(), spnPlayerTeam, teamList);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error: " + fault.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

//        loadSpinnerValues(getContext(), spnPlayerTeam, teamList);

        btnSavePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validate fields
                if (isValidFields(etPlayerName, etPlayerSurname) && isValidSpinner(spnPlayerTeam)) {

                    //create new player object
                    Player newPlayer = new Player();
                    newPlayer.setName(etPlayerName.getText().toString().trim());
                    newPlayer.setSurname(etPlayerSurname.getText().toString().trim());
                    newPlayer.setTeamName(spnPlayerTeam.getSelectedItem().toString());

                    //sets player medical details to its default values (No data)
                    newPlayer.setPlayerMedicalInfo(new Medical());

                    //call interface to return the new player object
                    savePlayerDetailsListener.onSavePlayerDetails(newPlayer);
                    clearFields(etPlayerName, etPlayerSurname);
                    clearSpinners(spnPlayerTeam);
                } else {
                    Toast.makeText(getContext(), "Please make sure a Team is selected.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return playerDetailsView;
    }

    public interface SaveLearnerDetailsListener {
        void onSavePlayerDetails(Player player);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            savePlayerDetailsListener = (SaveLearnerDetailsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement interface"
                    + " onSavePlayerDetails");
        }
    }

}