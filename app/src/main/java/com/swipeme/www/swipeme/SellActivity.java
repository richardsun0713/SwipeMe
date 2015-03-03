package com.swipeme.www.swipeme;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.EditText;
import com.parse.ParseObject;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.widget.Toast;


public class SellActivity extends FragmentActivity {

    private Spinner quantity_spinner, price_spinner;
    private String user_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

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

        // Get checked values from HomeActivity
        ArrayList<String> getChecked = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            getChecked = extras.getStringArrayList("checked_restaurants");
        }

        // Display checked values in ListView
        /* TODO: make display better looking, display appropriate information dependent on which
         * boxes were selected
         */
        ListView lv = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                getChecked
        );
        lv.setAdapter(arrayAdapter);

        addListenerOnSpinnerItemSelection();

        // Retrieve userID
        if(extras != null)
        {
            user_ID = extras.getString("userID");
        }
        Log.d("HomeActivity", "UserID: " + user_ID);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sell, menu);
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

    public void showStartTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
               Button startButton = (Button) findViewById(R.id.start_time_button);
                try {
                    String _24HourTime = "" + hourOfDay + ":" + minute;
                    SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                    Date _24HourDt = _24HourSDF.parse(_24HourTime);
                    startButton.setText(_12HourSDF.format(_24HourDt));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    public void showEndTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Button endButton = (Button) findViewById(R.id.end_time_button);
                try {
                    String _24HourTime = "" + hourOfDay + ":" + minute;
                    SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                    Date _24HourDt = _24HourSDF.parse(_24HourTime);
                    endButton.setText(_12HourSDF.format(_24HourDt));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void addListenerOnSpinnerItemSelection() {
        quantity_spinner = (Spinner) findViewById(R.id.quantity_spinner);
        price_spinner = (Spinner) findViewById(R.id.price_spinner);
        //quantity_spinner.setOnItemClickListener(new CustomOnItemSelectedListener);
    }
    public void postOffer(View view)
    {
        ParseObject userOffer=new ParseObject("Offers");

        userOffer.put("price",price_spinner.getSelectedItem().toString());
        Log.i("LoginActivity", "price: " + price_spinner.getSelectedItem().toString());
        ArrayList<String> getChecked = new ArrayList<String>();
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            getChecked = extras.getStringArrayList("checked_restaurants");
        }
        Button endButton = (Button) findViewById(R.id.end_time_button);
        Button startButton = (Button) findViewById(R.id.start_time_button);
        userOffer.put("quantity",quantity_spinner.getSelectedItem().toString());
        Log.i("LoginActivity", "quantity: " + quantity_spinner.getSelectedItem().toString());
        userOffer.put("timeStart",startButton.getText());
        Log.i("LoginActivity", "timeStart: " + startButton.getText());
        userOffer.put("timeEnd",endButton.getText());
        Log.i("LoginActivity", "timeEnd: " +endButton.getText());
        userOffer.put("restaurants",getChecked);
        userOffer.put("userID", user_ID);
        Log.i("LoginActivity", "userID: " + user_ID);
        Toast.makeText(getApplicationContext(),
                "Offer Successfully Posted!", Toast.LENGTH_LONG).show();
        userOffer.saveInBackground();

        // Start MyListingActivity
        Intent intent = new Intent(this, MyListingsActivity.class);
        intent.putExtra("userID", user_ID);
        finish();
        startActivity(intent);
    }
}
