package com.swipeme.www.swipeme;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class BuyListingsActivity extends FragmentActivity {

    ArrayList<String> getChecked;
    String timeStart;
    String timeEnd;
    ArrayList<String> matched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_listings);

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
            timeStart = extras.getString("timeStart");
            timeEnd = extras.getString("timeEnd");
        }

        //Retrieve Parse objects with a Parse query
        ParseQuery<ParseObject> query = new ParseQuery("Offers");
        query.addDescendingOrder("price");
        query.whereContainedIn("restaurants", getChecked);
        //query.whereGreaterThanOrEqualTo("timeStart", timeEnd);
        //query.whereLessThanOrEqualTo("timeEnd", timeStart);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> matchedList, com.parse.ParseException e) {

                for (ParseObject object : matchedList){
                    matched.add((String) object.get("userID") + ". " + (String) object.get("price") + ". " + (String) object.get("quantity"));
                }

                if (e == null) {
                    Log.i("BuyListings", "Retrieved " + matchedList.size() + " matches");
                    updateList();
                } else {
                    Log.i("BuyListings", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void updateList(){
        ListView lv = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                matched
        );
        lv.setAdapter(arrayAdapter);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
