package com.swipeme.www.swipeme;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

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

        displayListings();
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

    private void displayListings() {
        // Instantiate a QueryFactory to define the ParseQuery to be used for fetching items in this
        // Adapter.
        ParseQueryAdapter.QueryFactory<ParseObject> factory =
                new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery create() {
                        ParseQuery query = new ParseQuery("Offers");
                        query.whereEqualTo("userID", user_ID);
                        query.orderByAscending("createdAt");
                        return query;
                    }
                };

        // Pass the factory into the ParseQueryAdapter's constructor.
        final ParseQueryAdapter adapter = new MyListingAdapter(this, user_ID);
        adapter.setTextKey("name");

        // Set a callback to be fired upon successful loading of a new set of ParseObjects.
        adapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<ParseObject>() {
            public void onLoading() {
                // Trigger any "loading" UI
            }

            public void onLoaded(java.util.List<ParseObject> list, java.lang.Exception e) {
                // Execute any post-loading logic, hide "loading" UI
                Log.i("MyListingsActivity", "Retrieved " + list.size() + " listings");
            }
        });

        // Attach to listView
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }

}
