package com.example.android.hockeyapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;

import java.util.Objects;

import static com.example.android.hockeyapp.ApplicationClass.getUserString;
import static com.example.android.hockeyapp.ApplicationClass.sessionUser;
import static com.example.android.hockeyapp.ApplicationClass.setPieData;
import static com.example.android.hockeyapp.ApplicationClass.setupActionBar;

public class MatchStatsActivity extends AppCompatActivity implements
        Frag1GoalsTurnOvers.SetScoreListener, Frag2CirclePenetrations.SetCPScoreListener,
Frag3PenaltyCorners.SetPCScoreListener, Frag4GoalShots.SetGCScoreListener{

    private View toastView;

    FragmentManager manager;

    LinearLayout frmFragmentsHolder;

    PieChart pieChart1, pieChart2, pieChart3;

    TextView tvScore;

    Frag1GoalsTurnOvers tab1;
    Frag2CirclePenetrations tab2;
    Frag3PenaltyCorners tab3;
    Frag4GoalShots tab4;

    TextView tvHalf1TeamTurnOvers, tvHalf2TeamTurnOvers, tvHalf1OpponentTurnOvers, tvHalf1TeamGoals,
            tvHalf2OpponentTurnOvers, tvHalf2TeamGoals, tvHalf1OpponentGoals, tvHalf2OpponentGoals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_stats);
        initViews();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setupActionBar(actionBar, "Team VS Opponent", getUserString(sessionUser));// TODO: 2018/10/29 Change Title!!

        tvScore.setText(updateMatchScore(tvHalf1TeamGoals, tvHalf2TeamGoals, tvHalf1OpponentGoals,
                tvHalf2OpponentGoals));


    }

    private void initViews() {


        toastView = getLayoutInflater().inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout));
        BottomNavigationView navigation = findViewById(R.id.stats_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        tvScore = findViewById(R.id.tvScore);

        tvHalf1TeamTurnOvers = findViewById(R.id.tvHalf1TeamTurnOvers);
        tvHalf2TeamTurnOvers = findViewById(R.id.tvHalf2TeamTurnOvers);
        tvHalf1OpponentTurnOvers = findViewById(R.id.tvHalf1OpponentTurnOvers);
        tvHalf1TeamGoals = findViewById(R.id.tvHalf1TeamGoals);
        tvHalf2OpponentTurnOvers = findViewById(R.id.tvHalf2OpponentTurnOvers);
        tvHalf2TeamGoals = findViewById(R.id.tvHalf2TeamGoals);
        tvHalf1OpponentGoals = findViewById(R.id.tvHalf1OpponentGoals);
        tvHalf2OpponentGoals = findViewById(R.id.tvHalf2OpponentGoals);

        frmFragmentsHolder = findViewById(R.id.frmFragmentsHolder);

        pieChart1 = findViewById(R.id.pieChartCirclePenetrations);
        pieChart2 = findViewById(R.id.pieChartPenaltyCorners);
        pieChart3 = findViewById(R.id.pieChartGoalShots);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_goals_turn_overs:

                    getSelectedTab(R.id.tab1);

                    return true;
                case R.id.navigation_circle_presentations:

                    getSelectedTab(R.id.tab2);
                    animateChart(pieChart1);

                    return true;
                case R.id.navigation_penalty_corners:

                    getSelectedTab(R.id.tab3);
                    animateChart(pieChart2);

                    return true;
                case R.id.navigation_goal_shots:

                    getSelectedTab(R.id.tab4);
                    animateChart(pieChart3);
//
                    return true;
            }
            return false;
        }
    };

    private void animateChart(PieChart pieChart) {
        pieChart.setData(setPieData("Penalty Corners"));
        pieChart.setRotationEnabled(false);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutBounce);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getSelectedTab(int tabId) {
        manager = getSupportFragmentManager();
        manager.beginTransaction()
                .hide(Objects.requireNonNull(manager.findFragmentById(R.id.tab1)))
                .hide(Objects.requireNonNull(manager.findFragmentById(R.id.tab2)))
                .hide(Objects.requireNonNull(manager.findFragmentById(R.id.tab3)))
                .hide(Objects.requireNonNull(manager.findFragmentById(R.id.tab4)))
                .show(Objects.requireNonNull(manager.findFragmentById(tabId)))
                .commit();
    }

    private String updateMatchScore(TextView tvHalf1TeamGoals, TextView tvHalf2TeamGoals,
                                    TextView tvHalf1OpponentGoals, TextView tvHalf2OpponentGoals) {

        int teamScore = (Integer.parseInt(tvHalf1TeamGoals.getText().toString())) +
                (Integer.parseInt(tvHalf2TeamGoals.getText().toString()));

        int opponentScore = (Integer.parseInt(tvHalf1OpponentGoals.getText().toString())) +
                (Integer.parseInt(tvHalf2OpponentGoals.getText().toString()));

        return "Score ( " + teamScore + " - " + opponentScore + " )";
    }

    @Override
    public void onSetScore() {
        tvScore.setText(updateMatchScore(tvHalf1TeamGoals, tvHalf2TeamGoals, tvHalf1OpponentGoals,
                tvHalf2OpponentGoals));
    }

    @Override
    public void onSetCPScore() {

    }

    @Override
    public void onSetPCScore() {

    }

    @Override
    public void onSetGCScore() {

    }
}