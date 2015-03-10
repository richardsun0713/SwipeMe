package com.swipeme.www.swipeme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BuyListingAdapter extends ParseQueryAdapter<ParseObject> {



private Context context;

        // Use the QueryFactory to construct a PQA that will only show

    public BuyListingAdapter(Context context, final ArrayList<String> getChecked, final String currentUserId, final Date startDateConstraint, final Date endDateConstraint) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {

                ParseQuery query = new ParseQuery("Offers");
                query.addDescendingOrder("price");
                query.whereContainedIn("restaurants", getChecked);
                query.whereGreaterThanOrEqualTo("timeEndDate", startDateConstraint);
                query.whereLessThanOrEqualTo("timeStartDate", endDateConstraint);
                query.whereNotEqualTo("userID", currentUserId);

                return query;
            }
        });
        this.context = context;
    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(final ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.buy_listing_item, null);
        }

        super.getItemView(object, v, parent);

        // Add the create time view
        TextView startTimeView = (TextView) v.findViewById(R.id.time);
        startTimeView.setText(object.getString("timeStart") + " - " + object.getString("timeEnd"));

        // Add the quantity and price time view
        TextView endTimeView = (TextView) v.findViewById(R.id.price);
        endTimeView.setText(
                object.getString("price"));

        // Info Button
        Button info_button = (Button) v.findViewById(R.id.info_button);
        // Message Button
        Button message_button = (Button)v.findViewById(R.id.message_button);

        // Add button listener
                info_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Get the layout inflater
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.buylisting_dialog, null))
                        // Add action buttons
                        .setPositiveButton("Message", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // sign in the user ...
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                Dialog dialog = builder.create();
                dialog.show();
                
                // custom dialog
                /*final Dialog dialog = new Dialog(context, R.style.Theme_CustomDialog);
                dialog.setContentView(R.layout.buylisting_dialog);

               //custom alert dialog


                //AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                //dialog.setView(R.layout.buylisting_dialog);
                //dialog.setContentView(R.layout.buylisting_dialog);

                // set the custom dialog components
                TextView price = (TextView) dialog.findViewById(R.id.price);

                price.setText(object.getString("price"));

                TextView time = (TextView) dialog.findViewById(R.id.time);
                time.setText(object.getString("timeStart") + " - " + object.getString("timeEnd"));

                TextView quantity = (TextView) dialog.findViewById(R.id.quantity);
                quantity.setText(object.getString("quantity") + " swipes available");

                // TODO: implement GraphUser information for name
                TextView facebookUserName = (TextView) dialog.findViewById(R.id.posted_by);
                facebookUserName.setText("Posted by: Dummy User");

                ArrayList<Object> list = new ArrayList<>(object.getList("restaurants"));
                String [] restaurants = list.toArray(new String[list.size()]);
                TextView restaurantsView = (TextView) dialog.findViewById(R.id.restaurants);
                for (int i = 0; i < restaurants.length; i++)
                {
                    if (i == 0)
                        restaurantsView.setText(restaurants[i] + "\n");
                    else
                        restaurantsView.append(restaurants[i] + "\n");
                }

                // set x button
                Button dialogButton = (Button) dialog.findViewById(R.id.exit_button);
                // if button is clicked, close the dialog
                dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }

                }); */

                //dialog.show();
            }
        } );

        message_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("message button", "click");
                String recipientUserID = object.getString("userID");

                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.include("activeMessages");
                query.whereEqualTo("username", recipientUserID);
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> userList, ParseException e) {
                        if (e == null) {
                            String recipientObjectID = userList.get(0).getObjectId();

//                            String currentUserObjectID = ParseUser.getCurrentUser().getObjectId();
//                            ParseUser recipient = userList.get(0);
//                            recipient.addUnique("activeMessages", currentUserObjectID);
//                            Log.i("Messaging", currentUserObjectID);
//                            recipient.saveInBackground();
                            ParseUser.getCurrentUser().addUnique("activeMessages", userList.get(0));
                            ParseUser.getCurrentUser().saveInBackground();

                            Intent intent = new Intent(context, MessagingActivity.class);
                            intent.putExtra("RECIPIENT_ID", recipientObjectID);
                            intent.putExtra("fbName", userList.get(0).getString("fbName"));
                            context.startActivity(intent);
                        } else {
                                Toast.makeText(context, "Unable to start chat",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } );

        return v;
    }

    private BuyListingAdapter getBuyListingAdapter() {
        return this;
    }

}
