package com.example.android.hockeyapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static com.example.android.hockeyapp.ApplicationClass.getUserString;
import static com.example.android.hockeyapp.ApplicationClass.sessionUser;
import static com.example.android.hockeyapp.ApplicationClass.setupActionBar;

public class MatchListActivity extends AppCompatActivity implements MatchListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);


        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setupActionBar(actionBar, "Match Lists", getUserString(sessionUser));
    }

    @Override
    public void onListFragmentInteraction(MatchStats item) {

    }
}