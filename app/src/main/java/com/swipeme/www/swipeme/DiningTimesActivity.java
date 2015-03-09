package com.swipeme.www.swipeme;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;


public class DiningTimesActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WebView webView = (WebView)findViewById(R.id.webView);
        //you can load an html code
        //webView.loadData("yourCode Html to load on the webView " , "text/html" , "utf-8");
        // you can load an URL
        webView.loadUrl("https://secure5.ha.ucla.edu/restauranthours/dining-hall-hours-by-day.cfm");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining_times);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dining_times, menu);
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
