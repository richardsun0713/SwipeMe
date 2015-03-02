package com.swipeme.www.swipeme;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class MyListingsActivity extends FragmentActivity {

    private String user_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);

        // Retrieve userID
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            user_ID = extras.getString("userID");
        }

        Log.d("MyListingActivity", "UserID: " + user_ID);

        // Retrieve listings from Parse
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Offers");
        query.whereEqualTo("userID", user_ID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> myListingsList, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + myListingsList.size() + " listings");
                    // TODO: add listings to listView
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_listings, menu);
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
}
