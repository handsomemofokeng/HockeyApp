package com.example.android.hockeyapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import static com.example.android.hockeyapp.ApplicationClass.getUserString;
import static com.example.android.hockeyapp.ApplicationClass.progressDialog;
import static com.example.android.hockeyapp.ApplicationClass.sessionUser;
import static com.example.android.hockeyapp.ApplicationClass.showCustomToast;
import static com.example.android.hockeyapp.ApplicationClass.showProgressDialog;

public class AddPlayerActivity extends AppCompatActivity
        implements FragPlayerDetails.SaveLearnerDetailsListener,
        FragMedicalDetails.SaveMedicalDetailsListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    Player newPlayer;

    FragPlayerDetails tab1;
    FragMedicalDetails tab2;
    private View toastView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_player);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setLogo(R.mipmap.ic_launcher_round);
        toolbar.setTitle(" Add New Player");
        toolbar.setSubtitle(getUserString(sessionUser));

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        //For replacing Dynamic Fragments
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        initViews();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        toastView = getLayoutInflater().inflate(R.layout.custom_toast,

                (ViewGroup) findViewById(R.id.toast_layout));
    }

    @Override
    public void onSavePlayerDetails(Player player) {

        newPlayer = player;
        showProgressDialog(this, "Add New Player",
                "Please wait while we add your player...", false);
        Backendless.Data.save(newPlayer, new AsyncCallback<Player>() {
            @Override
            public void handleResponse(Player response) {

                progressDialog.dismiss();
                showCustomToast(getApplicationContext(), toastView,
                        response.toString() + " added successfully");
                mViewPager.setCurrentItem(1, true);
                //showCustomToast(getApplicationContext(), toastView, response.getObjectId());//TODO: 2018/09/11 Remove!!
                showCustomToast(getApplicationContext(), toastView,
                        "Add Medical details for " + response.toString());

            }

            @Override
            public void handleFault(BackendlessFault fault) {

                progressDialog.dismiss();
                showCustomToast(getApplicationContext(), toastView, "Error: "
                        + fault.getMessage());

            }
        });
    }

    @Override
    public void onSaveMedicalDetails(Medical medical) {

        if (newPlayer != null) {
            newPlayer.setPlayerMedicalInfo(medical);

            showProgressDialog(this, "Medical Details", "Adding medical details for "
                    + newPlayer.toString(), true);
            Backendless.Persistence.of(Player.class).save(newPlayer, new AsyncCallback<Player>() {
                @Override
                public void handleResponse(Player response) {
                    progressDialog.dismiss();
                    showCustomToast(getApplicationContext(), toastView, "Medical details saved for " + newPlayer);
                    mViewPager.setCurrentItem(0, true);
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    progressDialog.dismiss();
                    showCustomToast(getApplicationContext(), toastView, "Error: " + fault.getMessage());
                }
            });
        } else {
            //Return to Player Details Tab
            mViewPager.setCurrentItem(0, true);
            showCustomToast(this, toastView, "Please save Player details before saving medical details.");
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_register_player, container,
                    false);
            TextView textView = rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);

            getPageTitle(position);
            switch (position) {
                case 0:
                    tab1 = new FragPlayerDetails();
                    return tab1;
                case 1:
                    tab2 = new FragMedicalDetails();
                    return tab2;
                default:
                    return null;
            }//end switch
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Player Info";
                case 1:
                    return "Medical Info";

            }//end switch
            return null;
        }//end method
    }
}