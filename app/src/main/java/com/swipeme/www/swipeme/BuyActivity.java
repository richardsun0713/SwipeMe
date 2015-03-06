package com.swipeme.www.swipeme;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class BuyActivity extends FragmentActivity {

    ArrayList<String> getChecked;
    CustomListAdapter myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        // Set Action Bar font

        SpannableString s = new SpannableString("Buy");
        s.setSpan(new TypefaceSpan(this, "LobsterTwo-Bold.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(s);

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
        }


        // Display checked values in ListView
        /* TODO: make display better looking, display appropriate information dependent on which
         * boxes were selected
         */
        ListView lv = (ListView) findViewById(R.id.list);
        myadapter = new CustomListAdapter(this, getChecked);
        lv.setAdapter(myadapter);

    }

    public class CustomListAdapter extends BaseAdapter {

        public String title[];
        public String description[];
        ArrayList<String> arr_calllog_name = new ArrayList<>();
        public Activity context;
        // ArrayList<Bitmap> imageId;

        public LayoutInflater inflater;

        public CustomListAdapter(Activity context, ArrayList<String> arr_calllog_name) {
            super();

            this.context = context;
            this.arr_calllog_name = arr_calllog_name;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return arr_calllog_name.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public class ViewHolder
        {
            // ImageView image;
            TextView txtName;
            Button btn;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null)
            {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_item, null);

                holder.txtName = (TextView) convertView.findViewById(R.id.textView);
                holder.btn = (Button) convertView.findViewById(R.id.deletebutton);
                convertView.setTag(holder);
            }
            else
                holder = (ViewHolder)convertView.getTag();

            holder.txtName.setText(arr_calllog_name.get(position));

            holder.btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getChecked.remove(position);
                    myadapter.notifyDataSetChanged();
                    if (myadapter.isEmpty()) {
                        Log.i("BuyActivity", "Checklist is empty");
                        finish();
                    }
                }
            });

            return convertView;
        }
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

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
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

    public void startBuyListings(View view) {
        //Check to see if we should enable the search listing button.
        Button startButton = (Button) findViewById(R.id.start_time_button);
        Button endButton = (Button) findViewById(R.id.end_time_button);
        if (startButton.getText().equals("Start Time") || endButton.getText().equals("End Time")){
            AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(BuyActivity.this);
            alertDialog1.setTitle("You must enter start and end times in order to continue.");
            alertDialog1.setCancelable(true);
            AlertDialog alert = alertDialog1.create();
            alert.setCanceledOnTouchOutside(true);
            alert.show();
        } else {
        // Start new Buy Activity
        Intent intent = new Intent(this, BuyListingsActivity.class);
        intent.putStringArrayListExtra("checked_restaurants", getChecked);

        intent.putExtra("timeStart",startButton.getText());
        Log.i("LoginActivity", "timeStart: " + startButton.getText());
        intent.putExtra("timeEnd",endButton.getText());
        Log.i("LoginActivity", "timeEnd: " +endButton.getText());

        startActivity(intent);
        }
    }
}
