package com.example.android.hockeyapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.MessageFormat;

import static com.example.android.hockeyapp.ApplicationClass.getUserString;
import static com.example.android.hockeyapp.ApplicationClass.sessionUser;
import static com.example.android.hockeyapp.ApplicationClass.setupActionBar;

public class MatchesMenuActivity extends AppCompatActivity {

    private View toastView;
    TextView tvSessionUserStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_menu);

        initViews();

        String role = sessionUser.getProperty("role").toString();
        tvSessionUserStats.setText(MessageFormat.format("{0}\n({1})",
                sessionUser.getEmail(), role));

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setupActionBar(actionBar, "Matches Menu", getUserString(sessionUser));
    }

    /**
     * This method initializes Views
     */
    private void initViews() {
        toastView = getLayoutInflater().inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout));
        tvSessionUserStats = findViewById(R.id.tvSessionUserStats);

    }

    public void onClickMatchButton(View view) {

        switch (view.getId()){
            case R.id.btnMatchLineUp:
                startActivity(new Intent(getApplicationContext(), MatchLineUpActivity.class));
            break;

            case R.id.btnMatchStats:
                startActivity(new Intent(getApplicationContext(), MatchStatsActivity.class)); // TODO: 2018/10/28 Remove!!
                // TODO: 2018/10/28 Uncomment! 
//                startActivity(new Intent(getApplicationContext(), MatchListActivity.class));
            break;
        }
    }
}
