package com.example.android.hockeyapp;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.persistence.DataQueryBuilder;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ApplicationClass extends Application {
    public static ProgressDialog progressDialog;
    //public static View toastView;

    public static final String APPLICATION_ID = "2C939002-45FF-A107-FF75-1B9283447400";
    public static final String API_KEY = "4B43B005-7016-2AEF-FFB7-043E21BF7700";
    public static final String SERVER_URL = "https://api.backendless.com",
            MY_SHARED_PREFERENCES_NAME = "com.example.android.hockeyapp.HockeyApp",
            SELECTED_PLAYER = "selectedPlayer";
    public static final int REQUEST_PHONE = 123;

    //SharedPreferences used to save User data
    public static SharedPreferences myPrefs;
    public static boolean rememberMe;
    public static String currentUsername, currentUserPassword;
    public static List<Player> playerList;
    public static List<MatchStats> matchStatsList;

    //this is to keep track of who logged in
    public static BackendlessUser sessionUser;

    //this will be used for changing roles
    public static List<BackendlessUser> userList;

    // TODO: 2018/09/13 Review later 
    public Context context;


    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl(SERVER_URL);
        Backendless.initApp(getApplicationContext(),
                APPLICATION_ID,
                API_KEY);
        context = getApplicationContext();
        playerList = new ArrayList<>();
        matchStatsList = new ArrayList<>();
    }

    /**
     * @param context in which the Progress Dialog will be shown
     * @param title   of the Progress Dialog
     * @param message to be displayed on the Dialog
     */
    public static void showProgressDialog(Context context, String title, String message,
                                          boolean isCancelable) {

        try {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
            progressDialog.setIcon(R.mipmap.ic_launcher);
            progressDialog.setCancelable(isCancelable);
            progressDialog.show();
        } catch (Exception ex) {
            Log.d("Error", "Error: " + ex.getMessage());
        }

    }//end method

    /**
     * This method checks if the email provided is valid
     *
     * @param etEmail to be checked for validity
     * @return true if email is valid
     */
    public static boolean isEmailValid(EditText etEmail) {
        String email = etEmail.getText().toString().trim();

        boolean isValid = email.contains("@") && (email.endsWith(".com") || email.endsWith(".za"));
        if (!isValid) {
            etEmail.setError("Invalid email format");
        }

        return isValid;
    }

    /**
     * This method checks if the password is valid
     *
     * @param password to be checked
     * @return true if password matches the criteria
     */
    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }

    /**
     * This method checks if all field provided in parameters have values
     *
     * @param fields to be checked for validity
     * @return True if all fields have values, False if not.
     */
    public static boolean isValidFields(EditText... fields) {
        boolean isValid = true;
        for (EditText field : fields) {
            if (field.getText().toString().trim().isEmpty()) {
                field.setError("This field is required!");
                isValid = false;
            }
        }
        return isValid;
    }

    /**
     * This method checks if both passwords provided by user match and sets error message if not
     *
     * @param etPassword field to be compared with
     * @param etConfirm  another field to be compared with
     * @return True if passwords match, False if not.
     */
    public static boolean isPasswordsMatching(EditText etPassword, EditText etConfirm) {
        boolean isMatching = etPassword.getText().toString().trim().equals(etConfirm.getText().toString().trim());
        if (!isMatching) {
            etPassword.setError("Passwords must match!");
            etConfirm.setError("Passwords must match!");
        } else {
            etPassword.setError(null);
            etConfirm.setError(null);
        }

        return isMatching;
    }

    /**
     * This method returns a detailed String of a Backendeless User object
     *
     * @param user Backendless User object
     * @return detailed string of a Backendless User object
     */
    public static String getUserString(BackendlessUser user) {

        String userStr = "Unidentified User";
        if (user != null)
            userStr = String.format("%s, %s (%s)", user.getProperty("name"), user.getProperty("surname"),
                    user.getEmail());
        return userStr;
    }

    /**
     * This method sets up an Action Bar for any given Activity
     *
     * @param actionBar to be shown on Activity
     * @param title     of the Activity
     * @param subtitle  shows additional details
     * @return inflated and customised action bar
     */
    public static ActionBar setupActionBar(ActionBar actionBar, String title, String subtitle) {

        /*
          Setup a new action bar
         */
        assert actionBar != null;
        actionBar.setLogo(R.mipmap.ic_launcher_round);
        actionBar.setTitle(" " + title);
        actionBar.setSubtitle(" " + subtitle);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        return actionBar;
    }

    /**
     * A brief message to be displayed as feedback to user
     *
     * @param context   in which the toast will appear
     * @param toastView customised view of a toast
     * @param message   message to be displayed
     */
    public static void showCustomToast(Context context, View toastView, String message) {

        TextView tvToast = toastView.findViewById(R.id.tv_toast);
        tvToast.setText(message);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastView);
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
        toast.show();
    }

    /**
     * @param context in which the Activity is at
     * @return True if phone has active internet connection, False if not
     */
    public static boolean isPhoneConnected(Context context) {

        boolean isConnected = false;
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                isConnected = true;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                isConnected = true;
            }
        }
        return isConnected;
    }

    /**
     * This method populates a spinner with items on a list parameter
     *
     * @param context in which the spinner is at
     * @param spinner to be populated with string items
     * @param list    that contains items to be added to the spinner
     */
    public static void loadSpinnerValues(Context context, Spinner spinner, List<String> list) {
        spinner.setAdapter((new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, list)));
    }

    /**
     * This method sets the text of an editText to a selected spinner value
     *
     * @param spinner  to extract a prefix text
     * @param editText to be prefixed with the spinner value
     */
    public static void getSpinnerValue(Spinner spinner, EditText editText) {
        editText.setText(spinner.getSelectedItem().toString());
    }

    public static boolean isValidSpinner(Spinner... spinners) {
        boolean isValid = true;
        for (Spinner spn : spinners) {
            if (spn.getSelectedItemPosition() <= 0) {
                isValid = false;
            }
        }
        return isValid;
    }

    public static void clearSpinners(Spinner... spinners) {
        for (Spinner spinner : spinners) {
            spinner.setSelection(0);
        }
    }

    /**
     * This method clears text fields
     *
     * @param fields to be cleared
     */
    public static void clearFields(EditText... fields) {
        for (EditText field : fields) field.setText("");
    }

    /**
     * @param views to be hidden on given context
     */
    public static void hideViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * @param views to be shown on given context
     */
    public static void showViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Method activates views
     *
     * @param views to be activated
     */
    public static void enableViews(View... views) {
        for (View view : views) {
            view.setEnabled(true);
        }
    }

    /**
     * Method deactivates views
     *
     * @param views to be deactivated
     */
    public static void disableViews(View... views) {
        for (View view : views) {
            view.setEnabled(false);
        }
    }

    /**
     * This method creates a Query to find items by specified parameters
     *
     * @param property which property to look for in a table
     * @param value    to be used to filter
     * @param sortBy   to be used to arrange
     * @return query that specifies which specific items to return.
     */
    public static DataQueryBuilder selectQuery(String property, String value, String sortBy) {
        String whereClause = property + " = '" + value + "'";
        DataQueryBuilder dataQueryBuilder = DataQueryBuilder.create().setWhereClause(whereClause);
        dataQueryBuilder.setPageSize(100).setOffset(0).setSortBy(sortBy);
        return dataQueryBuilder;
    }

    /**
     * This method selects all objects within any Table in Backendless, every @NonNull object has
     * a 'created' property hence the whereClause is "created != null"
     *
     * @param sortBy sorts the list according to the property within the specified table
     * @return a query to be passed on Backendless Async
     */
    public static DataQueryBuilder selectAllQuery(String sortBy) {
        String whereClause = "created != null";
        DataQueryBuilder dataQueryBuilder = DataQueryBuilder.create().setWhereClause(whereClause);
        dataQueryBuilder.setPageSize(100).setOffset(0).setSortBy(sortBy);
        return dataQueryBuilder;
    }

    public static void decrementPoints(TextView... textViews) {

        for (TextView textView : textViews) {
            if (Integer.parseInt(textView.getText().toString()) > 0) {
                textView.setText(String.valueOf(Integer.parseInt(textView.getText().toString()) - 1));
            }
        }
    }

    public static void incrementPoints(TextView... textViews) {

        for (TextView textView : textViews) {
            textView.setText(String.valueOf(Integer.parseInt(textView.getText().toString()) + 1));
        }
    }

    public static PieData setPieData(String label) {

        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            pieEntries.add(new PieEntry(20.0f));
        }
        PieDataSet pieDataSet = new PieDataSet(pieEntries, label);
        pieDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        return new PieData(pieDataSet);
    }

}