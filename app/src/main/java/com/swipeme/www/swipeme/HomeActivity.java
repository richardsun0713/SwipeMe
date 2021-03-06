package com.swipeme.www.swipeme;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import com.swipeme.www.swipeme.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;


import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.ParseUser;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class HomeActivity extends FragmentActivity {

    private static final String[] m_restaurantNames = new String[] {"Bruin Plate", "Feast", "Covel", "De Neve",
            "Bruin Cafe", "Cafe 1919", "De Neve Late Night", "Rendezvous"};
    private static final String[] m_listIds = new String [] {"bplateButton", "feastButton", "covelButton",
            "deneveButton", "bruincafeButton", "cafe1919Button", "latenightButton", "rendezvousButton"};
    private String user_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        // Set Action Bar font

        SpannableString s = new SpannableString("SwipeMe");
        s.setSpan(new TypefaceSpan(this, "LobsterTwo-Bold.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(s);

        //make dropdown show even if phone has menu button
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }

        // Retrieve userID
        user_ID = ParseUser.getCurrentUser().getUsername();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }
        else*/ if (id == R.id.action_logout){
//            callFacebookLogout(this);
            logout();
            return true;
        }
        else if (id == R.id.action_messages) {
            Intent intent = new Intent(this, ListUsersActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.action_listings) {
            Intent intent = new Intent(this, MyListingsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_display_times)
        {
            Intent intent = new Intent(this, DiningTimesActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_display_menus)
        {
            startActivity(new Intent(this,DiningMenuActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

//    /**
//     * Logout From Facebook
//     */
//    public void callFacebookLogout(Context context) {
//        Session session = Session.getActiveSession();
//        if (session != null) {
//
//            if (!session.isClosed()) {
//                session.closeAndClearTokenInformation();
//                //clear your preferences if saved
//            }
//        } else {
//
//            session = new Session(context);
//            Session.setActiveSession(session);
//
//            session.closeAndClearTokenInformation();
//            //clear your preferences if saved
//
//        }
//        // Finish current activity
//        finish();
//    }

    /** Called when the user clicks the Buy button */
    public void startBuyActivity(View view) {

        // Get checkbox data
        ArrayList<String> checked = new ArrayList<>();
        for (int i = 0; i < m_listIds.length; i++) {
            ToggleButton cb = (ToggleButton) findViewById(getResources().getIdentifier(
                    m_listIds[i], "id", getPackageName()));
            if (cb.isChecked()) {
                checked.add(m_restaurantNames[i]);
            }
        }

        // If no restaurants are checked, show alert dialog
        if (checked.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You must select at least one restaurant to continue.");
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        // Start new Buy Activity
        Intent intent = new Intent(this, BuyActivity.class);
        intent.putStringArrayListExtra("checked_restaurants", checked);
        startActivity(intent);
    }

    /** Called when the user clicks the Sell button */
    public void startSellActivity(View view) {
        // Get checkbox data
        ArrayList<String> checked = new ArrayList<>();
        for (int i = 0; i < m_listIds.length; i++) {
            ToggleButton cb = (ToggleButton) findViewById(getResources().getIdentifier(
                    m_listIds[i], "id", getPackageName()));
            if (cb.isChecked()) {
                checked.add(m_restaurantNames[i]);
            }
        }

        // If no restaurants are checked, show alert dialog
        if (checked.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You must select at least one restaurant to continue.");
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        // Start new Sell Activity
        Intent intent = new Intent(this, SellActivity.class);
        intent.putStringArrayListExtra("checked_restaurants", checked);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            intent.putExtra("userID",extras.getString("userID"));
        }
        startActivity(intent);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_home, container, false);
        }
    }

    private void logout() {
        // Stop Messaging Service
        stopService(new Intent(this, MessageService.class));
        // Log the user out
        ParseUser.logOut();
        // Go to the login view
        startLoginActivity();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
