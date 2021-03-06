package com.swipeme.www.swipeme;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListUsersActivity extends Activity {

    private String currentUserId;
    private ArrayAdapter<String> namesArrayAdapter;
    private ArrayList<String> names;
    private ListView usersListView;
    private ProgressDialog progressDialog;
    private BroadcastReceiver receiver = null;
    private ArrayList<String> fbName;


    //TODO: set relations so that users are related if they have active messages with each other.
    //Add yourself into other users' activelyMessaging relation
    //TODO: only show users that the current user is related to
    //get a list of all users where you appear in the their activelyMessaging relation


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

//        showSpinner();

    }

    //display clickable a list of all users
    private void setConversationsList() {
        currentUserId = ParseUser.getCurrentUser().getObjectId();
        names = new ArrayList<String>();
        fbName = new ArrayList<String>();

        final List<String> validUsers = new ArrayList<String>();

        ParseQuery<ParseObject> senderQuery = new ParseQuery<ParseObject>("ParseMessage");
        senderQuery.whereEqualTo("senderId", currentUserId);

        senderQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null){

                    for (int i=0; i < parseObjects.size(); i++){
                        Log.i("List Users Activity", "Found senderID:" + parseObjects.get(i).getString("senderId"));

                        if (!validUsers.contains(parseObjects.get(i).getString("recipientId"))){
                            validUsers.add(parseObjects.get(i).getString("recipientId"));
                        }
                    }

                    ParseQuery<ParseObject> recipientQuery = new ParseQuery<ParseObject>("ParseMessage");
                    recipientQuery.whereEqualTo("recipientId", currentUserId);
                    recipientQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e) {
                            if (e == null){

                                for (int i=0; i < parseObjects.size(); i++){
                                    Log.i("List Users Activity", "Found recipientID:" + parseObjects.get(i).getString("recipientId"));

                                    if (!validUsers.contains(parseObjects.get(i).getString("senderId"))){
                                        validUsers.add(parseObjects.get(i).getString("senderId"));
                                    }
                                }

                                ParseQuery<ParseUser> query = ParseUser.getQuery();
                                query.whereNotEqualTo("objectId", currentUserId);
                                query.findInBackground(new FindCallback<ParseUser>() {
                                    public void done(List<ParseUser> userList, ParseException e) {
                                        if (e == null) {

                                            for (int i=0; i<userList.size(); i++) {
                                                if (validUsers.contains(userList.get(i).getObjectId().toString())){
                                                    names.add(userList.get(i).getUsername().toString());
                                                    fbName.add(userList.get(i).getString("fbName"));
                                                }
                                            }

                                            usersListView = (ListView)findViewById(R.id.usersListView);
                                            namesArrayAdapter =
                                                    new ArrayAdapter<String>(getApplicationContext(),
                                                            R.layout.user_list_item, fbName);
                                            usersListView.setAdapter(namesArrayAdapter);

                                            usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                                                    openConversation(names, i);
                                                }
                                            });

                                        } else {
                                            Toast.makeText(getApplicationContext(),
                                                    "Error loading user list",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Error loading user list",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error loading user list",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //open a conversation with one person
    public void openConversation(ArrayList<String> names, int pos) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", names.get(pos));
        query.findInBackground(new FindCallback<ParseUser>() {
           public void done(List<ParseUser> user, ParseException e) {
               if (e == null) {
                   Intent intent = new Intent(getApplicationContext(), MessagingActivity.class);
                   intent.putExtra("RECIPIENT_ID", user.get(0).getObjectId());
                   intent.putExtra("fbName", user.get(0).getString("fbName"));
                   startActivity(intent);
               } else {
                   Toast.makeText(getApplicationContext(),
                       "Error finding that user",
                           Toast.LENGTH_SHORT).show();
               }
           }
        });
    }

    //show a loading spinner while the sinch client starts
    private void showSpinner() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Boolean success = intent.getBooleanExtra("success", false);
                progressDialog.dismiss();
                if (!success) {
                    Toast.makeText(getApplicationContext(), "Messaging service failed to start", Toast.LENGTH_LONG).show();
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("com.swipeme.www.swipeme.ListUsersActivity"));
    }


    @Override
    public void onResume() {
        setConversationsList();
        super.onResume();
    }
}


