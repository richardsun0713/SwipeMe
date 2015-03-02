package com.swipeme.www.swipeme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.parse.ParseUser;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class HomeActivity extends FragmentActivity {

    private static final String[] m_restaurantNames = new String[] {"Bruin Plate", "Covel", "De Neve", "Feast",
            "Bruin Cafe", "Cafe 1919", "Rendevous", "De Neve Late Night", "Hedrick Late Night"};
    private static final String[] m_listIds = new String [] {"check_bplate", "check_covel", "check_deneve",
            "check_feast", "check_bcafe", "check_1919", "check_rendez", "check_latenight",
            "check_hedrick" };
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
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            user_ID = extras.getString("userID");
        }
        Log.d("HomeActivity", "UserID: " + user_ID);
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
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_logout){
//            callFacebookLogout(this);
            logout();
            return true;
        }
        else if (id == R.id.action_messages) {
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.action_listings) {
            Intent intent = new Intent(this, MyListingsActivity.class);
            intent.putExtra("userID", user_ID);
            startActivity(intent);
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
            CheckBox cb = (CheckBox) findViewById(getResources().getIdentifier(
                    m_listIds[i], "id", getPackageName()));
            if (cb.isChecked()) {
                checked.add(m_restaurantNames[i]);
            }
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
            CheckBox cb = (CheckBox) findViewById(getResources().getIdentifier(
                    m_listIds[i], "id", getPackageName()));
            if (cb.isChecked()) {
                checked.add(m_restaurantNames[i]);
            }
        }

        // Start new Sell Activity
        Intent intent = new Intent(this, SellActivity.class);
        intent.putStringArrayListExtra("checked_restaurants", checked);
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
