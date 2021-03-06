package com.swipeme.www.swipeme;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class BuyListingsActivity extends FragmentActivity {

    ArrayList<String> getChecked;
    Date timeStartDate;
    Date timeEndDate;
    ArrayList<String> matched;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_listings);
        // Set Action Bar font

        SpannableString s = new SpannableString("Buy Listings");
        s.setSpan(new TypefaceSpan(this, "LobsterTwo-Bold.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(s);
        matched = new ArrayList<>();

        //make dropdown show even if phone has menu option
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

        // Get checked values from HomeActivity
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            getChecked = extras.getStringArrayList("checked_restaurants");
            timeStartDate = new Date(extras.getLong("timeStart"));
            timeEndDate = new Date(extras.getLong("timeEnd"));
        }

        if (ParseUser.getCurrentUser().getObjectId() != null){
            currentUserID = ParseUser.getCurrentUser().getUsername();
        }


        displayList();
    }

    private void displayList() {
        // Pass the factory into the ParseQueryAdapter's constructor.
        final ParseQueryAdapter adapter = new BuyListingAdapter(this, getChecked,currentUserID, timeStartDate, timeEndDate);
        adapter.setTextKey("name");

        // Set a callback to be fired upon successful loading of a new set of ParseObjects.
        adapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<ParseObject>() {
            public void onLoading() {
                // Trigger any "loading" UI
                createProgressBar();
            }

            public void onLoaded(java.util.List<ParseObject> list, java.lang.Exception e) {
                // Execute any post-loading logic, hide "loading" UI

                if (list != null) {
                    Log.i("BuyListingsActivity", "Retrieved " + list.size() + " listings");
                    if (list.size() == 0) {
                        showNoListings();
                    }
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
        textview.setText("There are no current listings that match your query.");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(textview, params);
        root.addView(layout);
    }

    private ListView getListView() {
        return (ListView) findViewById(R.id.listview);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buy, menu);
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
        }*/
        return super.onOptionsItemSelected(item);
    }
}
