package com.swipeme.www.swipeme;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.List;


public class MyListingsActivity extends FragmentActivity {

    private String user_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);

        // Retrieve userID
        user_ID = ParseUser.getCurrentUser().getUsername();

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
        // Pass the factory into the ParseQueryAdapter's constructor.
        final ParseQueryAdapter adapter = new MyListingAdapter(this);
        adapter.setTextKey("name");

        // Set a callback to be fired upon successful loading of a new set of ParseObjects.
        adapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<ParseObject>() {
            public void onLoading() {
                // Trigger any "loading" UI
                createProgressBar();
            }

            public void onLoaded(java.util.List<ParseObject> list, java.lang.Exception e) {
                // Execute any post-loading logic, hide "loading" UI
                Log.i("MyListingsActivity", "Retrieved " + list.size() + " listings");
                if (list.size() == 0) {
                    showNoListings();
                }
                adapter.notifyDataSetChanged();
            }
        });

        // Attach to listView
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }

    private void createProgressBar() {
        // Create a progress bar to display while the list loads
        RelativeLayout layout = new RelativeLayout(this);
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar,params);

        getListView().setEmptyView(progressBar);

        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(layout);
    }

    private void showNoListings() {
        // Remove progress bar
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.removeAllViews();

        // Display message
        RelativeLayout layout = new RelativeLayout(this);
        TextView textview = new TextView(this);
        textview.setVisibility(View.VISIBLE);
        textview.setText("You have no current listings.");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(textview, params);
        root.addView(layout);
    }

    private ListView getListView() {
        return (ListView) findViewById(R.id.listview);
    }

}
