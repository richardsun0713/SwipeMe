package com.swipeme.www.swipeme;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.Parse;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends Activity {

    private static final String TAG = "SwipeMe";
    private String user_ID;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // Check if there is a currently logged in user
        // and it's linked to a Facebook account.
        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            // Go to the user info activity
            showHomeActivity();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    public void onLoginClick(View v) {
        progressDialog = ProgressDialog.show(LoginActivity.this, "", "Logging in...", true);

        List<String> permissions = Arrays.asList("public_profile", "email");
        // NOTE: for extended permissions, like "user_about_me", your app must be reviewed by the Facebook team
        // (https://developers.facebook.com/docs/facebook-login/permissions/)

        ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                progressDialog.dismiss();
                if (user == null) {
                    Log.d(TAG, "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d(TAG, "User signed up and logged in through Facebook!");
                    showHomeActivity();
                } else {
                    Log.d(TAG, "User logged in through Facebook!");
                    showHomeActivity();
                }
            }
        });
    }

    private void showHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        // Get user id
        Log.i("LoginActivity", "Attempt to get user id");
        user_ID = ParseUser.getCurrentUser().getUsername();
        Log.i("LoginActivity", "user_ID: " + user_ID);
        intent.putExtra("userID", user_ID);
        startActivity(intent);
    }

//    private FacebookLoginFragment FacebookLoginFragment;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (savedInstanceState == null) {
//            // Add the fragment on initial activity setup
//            FacebookLoginFragment = new FacebookLoginFragment();
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .add(android.R.id.content, FacebookLoginFragment)
//                    .commit();
//        } else {
//            // Or set the fragment from restored state info
//            FacebookLoginFragment = (FacebookLoginFragment) getSupportFragmentManager()
//                    .findFragmentById(android.R.id.content);
//        }
//
//        // Register parse models
//        ParseObject.registerSubclass(Message.class);
//        // Enable Local Datastore.
//        Parse.enableLocalDatastore(this);
//
//        Parse.initialize(this, "vhQM6PPW6eQOImqAi1ixKE12fcnM5QSzotSUI9Dh", "DdOmhTSFB5HALDBFHdmPvjeBV6I7umDPgDEvxrTX");
//
//        // Parse Testing
//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        // Logs 'install' and 'app activate' App Events.
//        AppEventsLogger.activateApp(this);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        // Logs 'app deactivate' App Event.
//        AppEventsLogger.deactivateApp(this);
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}