package com.swipeme.www.swipeme;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;


public class SellActivity extends FragmentActivity {

    private Spinner quantity_spinner, price_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        // Get checked values from HomeActivity
        ArrayList<String> getChecked = new ArrayList<String>();
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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                getChecked
        );
        lv.setAdapter(arrayAdapter);

        addListenerOnSpinnerItemSelection();
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

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void addListenerOnSpinnerItemSelection() {
        quantity_spinner = (Spinner) findViewById(R.id.quantity_spinner);
        price_spinner = (Spinner) findViewById(R.id.price_spinner);
        //quantity_spinner.setOnItemClickListener(new CustomOnItemSelectedListener);
    }
}
