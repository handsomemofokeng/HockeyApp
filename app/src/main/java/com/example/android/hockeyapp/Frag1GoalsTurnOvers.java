package com.example.android.hockeyapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import static com.example.android.hockeyapp.ApplicationClass.decrementPoints;
import static com.example.android.hockeyapp.ApplicationClass.incrementPoints;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag1GoalsTurnOvers extends Fragment {

    View toastView;

    int teamTurnOvers1 = 0, teamTurnOvers2 = 0, opponentTurnOvers1 = 0, opponentTurnOvers2 = 0,
            teamGoals1 = 0, teamGoals2 = 0, opponentGoals1 = 0, opponentGoals2 = 0;

    RadioButton rdbFirstHalfTOV, rdbSecondHalfTOV;

    Button btnTeamMinusTurnOvers, btnTeamAddTurnOvers, btnOpponentMinusTurnOvers, btnTeamMinusGoals,
            btnOpponentAddTurnOvers, btnTeamAddGoals, btnOpponentMinusGoals, btnOpponentAddGoals;

    TextView tvHalf1TeamTurnOvers, tvHalf2TeamTurnOvers, tvHalf1OpponentTurnOvers, tvHalf1TeamGoals,
            tvHalf2OpponentTurnOvers, tvHalf2TeamGoals, tvHalf1OpponentGoals, tvHalf2OpponentGoals;
    private SetScoreListener setScoreListener;

    public Frag1GoalsTurnOvers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragGoalsTurnOvers = inflater.inflate(R.layout.frag1_goals_turn_overs,
                container, false);

        //Initialise Fragment UI Reference
        rdbFirstHalfTOV = fragGoalsTurnOvers.findViewById(R.id.rdbFirstHalfTOV);
        rdbSecondHalfTOV = fragGoalsTurnOvers.findViewById(R.id.rdbSecondHalfTOV);

        rdbFirstHalfTOV.toggle();

        btnTeamMinusTurnOvers = fragGoalsTurnOvers.findViewById(R.id.btnTeamMinusTurnOvers);
        btnTeamAddTurnOvers = fragGoalsTurnOvers.findViewById(R.id.btnTeamAddTurnOvers);
        btnOpponentMinusTurnOvers = fragGoalsTurnOvers.findViewById(R.id.btnOpponentMinusTurnOvers);
        btnTeamMinusGoals = fragGoalsTurnOvers.findViewById(R.id.btnTeamMinusGoals);
        btnOpponentAddTurnOvers = fragGoalsTurnOvers.findViewById(R.id.btnOpponentAddTurnOvers);
        btnTeamAddGoals = fragGoalsTurnOvers.findViewById(R.id.btnTeamAddGoals);
        btnOpponentMinusGoals = fragGoalsTurnOvers.findViewById(R.id.btnOpponentMinusGoals);
        btnOpponentAddGoals = fragGoalsTurnOvers.findViewById(R.id.btnOpponentAddGoals);

        tvHalf1TeamTurnOvers = fragGoalsTurnOvers.findViewById(R.id.tvHalf1TeamTurnOvers);
        tvHalf2TeamTurnOvers = fragGoalsTurnOvers.findViewById(R.id.tvHalf2TeamTurnOvers);
        tvHalf1OpponentTurnOvers = fragGoalsTurnOvers.findViewById(R.id.tvHalf1OpponentTurnOvers);
        tvHalf1TeamGoals = fragGoalsTurnOvers.findViewById(R.id.tvHalf1TeamGoals);
        tvHalf2OpponentTurnOvers = fragGoalsTurnOvers.findViewById(R.id.tvHalf2OpponentTurnOvers);
        tvHalf2TeamGoals = fragGoalsTurnOvers.findViewById(R.id.tvHalf2TeamGoals);
        tvHalf1OpponentGoals = fragGoalsTurnOvers.findViewById(R.id.tvHalf1OpponentGoals);
        tvHalf2OpponentGoals = fragGoalsTurnOvers.findViewById(R.id.tvHalf2OpponentGoals);

        return fragGoalsTurnOvers;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnTeamMinusTurnOvers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdbFirstHalfTOV.isChecked()) {
                    decrementPoints(tvHalf1TeamTurnOvers);
                } else if (rdbSecondHalfTOV.isChecked()) {
                    decrementPoints(tvHalf2TeamTurnOvers);
                }
            }
        });

        btnTeamAddTurnOvers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdbFirstHalfTOV.isChecked()) {
                    incrementPoints(tvHalf1TeamTurnOvers);
                } else if (rdbSecondHalfTOV.isChecked()) {
                    incrementPoints(tvHalf2TeamTurnOvers);
                }
            }
        });

        btnOpponentMinusTurnOvers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdbFirstHalfTOV.isChecked()) {
                    decrementPoints(tvHalf1OpponentTurnOvers);
                } else if (rdbSecondHalfTOV.isChecked()) {
                    decrementPoints(tvHalf2OpponentTurnOvers);
                }
            }
        });

        btnTeamMinusGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdbFirstHalfTOV.isChecked()) {
                    decrementPoints(tvHalf1TeamGoals);
                } else if (rdbSecondHalfTOV.isChecked()) {
                    decrementPoints(tvHalf2TeamGoals);
                }
                setScoreListener.onSetScore();
            }
        });

        btnOpponentAddTurnOvers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdbFirstHalfTOV.isChecked()) {
                    incrementPoints(tvHalf1OpponentTurnOvers);
                } else if (rdbSecondHalfTOV.isChecked()) {
                    incrementPoints(tvHalf2OpponentTurnOvers);
                }
            }
        });

        btnTeamAddGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdbFirstHalfTOV.isChecked()) {
                    incrementPoints(tvHalf1TeamGoals);
                } else if (rdbSecondHalfTOV.isChecked()) {
                    incrementPoints(tvHalf2TeamGoals);
                }

                setScoreListener.onSetScore();
            }
        });

        btnOpponentMinusGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdbFirstHalfTOV.isChecked()) {
                    decrementPoints(tvHalf1OpponentGoals);
                } else if (rdbSecondHalfTOV.isChecked()) {
                    decrementPoints(tvHalf2OpponentGoals);
                }

                setScoreListener.onSetScore();
            }
        });

        btnOpponentAddGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdbFirstHalfTOV.isChecked()) {
                    incrementPoints(tvHalf1OpponentGoals);
                } else if (rdbSecondHalfTOV.isChecked()) {
                    incrementPoints(tvHalf2OpponentGoals);
                }

                setScoreListener.onSetScore();
            }
        });
    }

    public interface SetScoreListener {
        void onSetScore();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            setScoreListener = (Frag1GoalsTurnOvers.SetScoreListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement interface"
                    + " onSavePlayerDetails");
        }
    }
}
