package com.swipeme.www.swipeme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.facebook.Session;

import java.util.ArrayList;


public class HomeActivity extends FragmentActivity {

    String[] array = new String[] {"Bruin Plate", "Covel", "De Neve", "Feast", "Bruin Cafe",
            "Cafe 1919", "Rendevous", "De Neve Late Night", "Hedrick Late Night"};

    private static final String TAG = "HomeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
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
            callFacebookLogout(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * Logout From Facebook
     */
    public void callFacebookLogout(Context context) {
        Session session = Session.getActiveSession();
        if (session != null) {

            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
                //clear your preferences if saved
            }
        } else {

            session = new Session(context);
            Session.setActiveSession(session);

            session.closeAndClearTokenInformation();
            //clear your preferences if saved

        }
        // Finish current activity
        finish();
    }

    /** Called when the user clicks the Buy button */
    public void startBuyActivity(View view) {

        // Get checkbox data
        ArrayList<String> checked = new ArrayList<String>();
        ListView mListView = (ListView) findViewById(R.id.checkboxList);
        SparseBooleanArray pos = mListView.getCheckedItemPositions();
        for (int i = 0; i < array.length; i++) {
            if (pos.get(i) == true) {
                checked.add(array[i]);
            }
        }
        Intent intent = new Intent(this, BuyActivity.class);
        intent.putStringArrayListExtra("list", checked);
        startActivity(intent);
    }

    /** Called when the user clicks the Sell button */
    public void startSellActivity(View view) {
        Intent intent = new Intent(this, SellActivity.class);
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
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }

    }
}
