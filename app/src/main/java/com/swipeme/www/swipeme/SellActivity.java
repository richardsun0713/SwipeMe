package com.swipeme.www.swipeme;

import android.app.ActionBar;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.DialogFragment;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.widget.Toast;



public class SellActivity extends FragmentActivity {

    private Spinner quantity_spinner, price_spinner;
    private String user_ID;

    private Date timeStartDate, timeEndDate;

    ListView lv;
    public ArrayList<String> restaurants = new ArrayList<>();
    CustomListAdapter myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);


        // Set Action Bar font

        SpannableString s = new SpannableString("Sell");
        s.setSpan(new TypefaceSpan(this, "LobsterTwo-Bold.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(s);

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

        lv = (ListView) findViewById(R.id.list);
        restaurants = new ArrayList<>(getChecked);
        myadapter = new CustomListAdapter(this, restaurants);
        lv.setAdapter(myadapter);

        addListenerOnSpinnerItemSelection();

        // Retrieve userID
        user_ID = ParseUser.getCurrentUser().getUsername();
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
                    restaurants.remove(position);
                    myadapter.notifyDataSetChanged();
                    if (myadapter.isEmpty()) {
                        Log.i("SellActivity", "Checklist is empty");
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
        else if(id == R.id.action_display_menus)
        {
            startActivity(new Intent(this,DiningMenuActivity.class));
        }
        else if(id == R.id.action_display_times)
        {
            startActivity(new Intent(this,DiningTimesActivity.class));
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
                    timeStartDate = _24HourDt;
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
                    timeEndDate = _24HourDt;
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
    final int[][] allEnd={{20,21,20,20,2,0,0,0,0},{20,21,21,20,2,0,12,2,0},{20,21,21,20,2,0,12,2,0},{20,21,21,20,2,0,12,2,0},{20,21,21,20,2,0,12,2,0},{20,21,21,20,2,0,12,2,0},{20,21,20,20,2,0,0,0,0}};
    final int[][] allStart={{10,9,9,11,15,0,0,21,0},{7,11,7,11,7,11,7,21,7},{7,11,7,11,7,11,7,21,7},{7,11,7,11,7,11,7,21,7},{7,11,7,11,7,11,7,21,7},{7,11,7,11,7,11,7,21,7},{10,9,9,11,15,0,0,21,0}};
    final String[] m_restaurantNames = new String[] {"Bruin Plate", "Covel", "De Neve", "Feast",
            "Bruin Cafe", "Cafe 1919", "Rendezvous", "De Neve Late Night", "Hedrick Late Night"};
    public void postOffer(View view)
    {
        ParseObject userOffer=new ParseObject("Offers");
        Calendar c=Calendar.getInstance();
        int minutes=c.get(Calendar.MINUTE);
        int hours=c.get(Calendar.HOUR);
        int day=c.get(Calendar.DAY_OF_WEEK);
        int apm=c.get(Calendar.AM_PM);




        Button endButton = (Button) findViewById(R.id.end_time_button);
        Button startButton = (Button) findViewById(R.id.start_time_button);
        String start=startButton.getText().toString();
        String end=endButton.getText().toString();
        int startHour=Integer.parseInt(start.substring(0, 2));
        int startMinute= Integer.parseInt(start.substring(3,5));
        int endHour=Integer.parseInt(end.substring(0, 2));
        int endMinute= Integer.parseInt(end.substring(3,5));
        ArrayList<String> correct=new ArrayList<>();
        ArrayList<String> modifiedStart=new ArrayList<>();
        ArrayList<String> modifiedEnd=new ArrayList<>();
        ArrayList<String> deleted=new ArrayList<>();
        ArrayList<ArrayList<String>> finalOrder = new ArrayList<ArrayList<String>>();
        ArrayList<Integer> finalStart=new ArrayList<Integer>();
        ArrayList<Integer> finalEnd=new ArrayList<Integer>();
        ArrayList<Integer> modifiedStartTimes=new ArrayList<>();
        ArrayList<Integer> modifiedEndTimes=new ArrayList<>();
        ArrayList<Integer> correctStartTimes=new ArrayList<>();
        ArrayList<Integer> correctEndTimes=new ArrayList<>();
        if(start.charAt(start.length()-2)=='P')
        {
            startHour+=12;
        }
        if(end.charAt(start.length()-2)=='P')
        {
            endHour+=12;
        }
        if(apm==Calendar.PM)
        {
            hours+=12;
        }
        for(int i=0;i<restaurants.size();i++)
        {
            String hall=restaurants.get(i);
            int j=-1;
            if(hall.equals("Bruin Plate"))
            {
                j=0;
            }
            else if(hall.equals("Covel"))
            {
                j=1;
            }
            else if(hall.equals("De Neve"))
            {
                j=2;
            }
            else if(hall.equals("Feast"))
            {
                j=3;
            }
            else if(hall.equals("Bruin Cafe"))
            {
                j=4;
            }
            else if(hall.equals("Cafe 1919"))
            {
                j=5;
            }
            else if(hall.equals("Rendezvous"))
            {
                j=6;
            }
            else if(hall.equals("De Neve Late Night"))
            {
                j=7;
            }
            else if(hall.equals("Hedrick Late Night"))
            {
                j=8;
            }
            else
            {
                j=-1;
            }
            if(day==1||day==7)
            {
                if(j==6||j==8||j==5)
                {
                    deleted.add(hall);
                    continue;
                }
                else
                {
                    if(startHour>=allStart[day][j]&&endHour<=allEnd[day][j])
                    {
                        correct.add(hall);
                    }
                    else if(startHour<=allStart[day][j]&&endHour>=allEnd[day][j])
                    {
                        deleted.add(hall);
                    }
                    else if(startHour>=allStart[day][j]&&endHour>=allEnd[day][j])
                    {
                        modifiedEnd.add(restaurants.get(j));
                        modifiedEndTimes.add(endHour);
                    }
                    else if(startHour<=allStart[day][j]&&endHour<=allEnd[day][j])
                    {
                        modifiedStart.add((restaurants.get(j)));
                        modifiedStartTimes.add(startHour);
                    }
                }
            }
            else {
                if(startHour>=allStart[day][j]&&endHour<=allEnd[day][j])
                {
                    correct.add(hall);
                    correctEndTimes.add(endHour);
                    correctStartTimes.add(startHour);
                }
                else if(startHour<=allStart[day][j]&&endHour>=allEnd[day][j])
                {
                    deleted.add(hall);
                }
                else if(startHour>=allStart[day][j]&&endHour>=allEnd[day][j])
                {
                    modifiedEnd.add(restaurants.get(j));
                    modifiedEndTimes.add(endHour);
                }
                else if(startHour<=allStart[day][j]&&endHour<=allEnd[day][j])
                {
                    modifiedStart.add((restaurants.get(j)));
                    modifiedStartTimes.add(startHour);
                }
            }
        }
        int maxLength=modifiedEnd.size()>modifiedStart.size()?modifiedEnd.size():modifiedStart.size();
        maxLength=maxLength>correct.size()?maxLength:correct.size();
        for(int i=0;i<correct.size();i++)
        {
            for(int j=0;j<correctEndTimes.size();i++)
            {

            }

        }

        userOffer.put("price", price_spinner.getSelectedItem().toString());
        Log.i("LoginActivity", "price: " + price_spinner.getSelectedItem().toString());
        userOffer.put("quantity",quantity_spinner.getSelectedItem().toString());
        Log.i("LoginActivity", "quantity: " + quantity_spinner.getSelectedItem().toString());

        userOffer.put("timeStart",startButton.getText());
        Log.i("LoginActivity", "timeStart: " + startButton.getText());
        userOffer.put("timeEnd",endButton.getText());
        Log.i("LoginActivity", "timeEnd: " +endButton.getText());

        if (timeStartDate != null) {
            userOffer.put("timeStartDate", timeStartDate);
            Log.i("LoginActivity", "timeStart: " + timeStartDate);
        }
        if (timeEndDate != null) {
            userOffer.put("timeEndDate", timeEndDate);
            Log.i("LoginActivity", "timeEnd: " + timeEndDate);
        }


        userOffer.put("restaurants",restaurants);
        userOffer.put("userID", user_ID);
        Log.i("SellActivity", "userID: " + user_ID);
        Toast.makeText(getApplicationContext(),
                "Offer Successfully Posted!", Toast.LENGTH_LONG).show();
        userOffer.saveInBackground();

        // Start MyListingActivity
        Intent intent = new Intent(this, MyListingsActivity.class);
        finish();
        startActivity(intent);
    }
}
