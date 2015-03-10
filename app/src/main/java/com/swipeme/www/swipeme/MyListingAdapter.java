package com.swipeme.www.swipeme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;

public class MyListingAdapter extends ParseQueryAdapter<ParseObject> {

    private Context context;

    public MyListingAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Offers");
                query.whereEqualTo("userID", ParseUser.getCurrentUser().getUsername());
                query.orderByAscending("createdAt");
                return query;
            }
        });
        this.context = context;
    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(final ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.my_listing_item, null);
        }

        super.getItemView(object, v, parent);

        // Add the create time view
        TextView startTimeView = (TextView) v.findViewById(R.id.time);
        startTimeView.setText(object.getString("timeStart") + " - " + object.getString("timeEnd"));

        // Add the quantity and price time view
        TextView endTimeView = (TextView) v.findViewById(R.id.price);
        endTimeView.setText(
                object.getString("price") );

        // Add the restaurant view
        /*TextView restaurantsView = (TextView) v.findViewById(R.id.restaurants);
        restaurantsView.setText(object.getList("restaurants").toString());*/

        // Add info button functionality

        // Info Button
        Button info_button = (Button) v.findViewById(R.id.info_button);

        // Add button listener
        info_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Create custom alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                // Get the layout inflater
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();

                // Inflate and set the layout for the dialog
                builder.setView(inflater.inflate(R.layout.mylisting_dialog, null))
                        // Add action buttons
                        .setPositiveButton("Delete Post", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // Perform action on click
                                // Delete object from database
                                object.deleteInBackground(new DeleteCallback() {
                                    // Reload the adapter if deletion succeeded
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Toast.makeText(getContext(),
                                                    "Listing Successfully Deleted!", Toast.LENGTH_SHORT).show();
                                            getMyListingAdapter().loadObjects();
                                        }
                                        else {
                                            Log.e("MyListingAdapter", "Offer deletion failed");
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                Dialog dialog = builder.create();
                dialog.show();


                // set the custom dialog components
                TextView price = (TextView) dialog.findViewById(R.id.price);

                price.setText(object.getString("price"));

                TextView time = (TextView) dialog.findViewById(R.id.time);
                time.setText(object.getString("timeStart") + " - " + object.getString("timeEnd"));

                TextView quantity = (TextView) dialog.findViewById(R.id.quantity);
                quantity.setText(object.getString("quantity") + " swipes available");


                ArrayList<Object> list = new ArrayList<>(object.getList("restaurants"));
                String [] restaurants = list.toArray(new String[list.size()]);
                TextView restaurantsView = (TextView) dialog.findViewById(R.id.restaurants);
                for (int i = 0; i < restaurants.length; i++) {
                    if (i == 0)
                        restaurantsView.setText(restaurants[i] + "\n");
                    else
                        restaurantsView.append(restaurants[i] + "\n");
                }
            }
        } );

        // Add button click function
        Button button = (Button) v.findViewById(R.id.trash_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                // Delete object from database
                object.deleteInBackground(new DeleteCallback() {
                    // Reload the adapter if deletion succeeded
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getContext(),
                                    "Listing Successfully Deleted!", Toast.LENGTH_SHORT).show();
                            getMyListingAdapter().loadObjects();
                        }
                        else {
                            Log.e("MyListingAdapter", "Offer deletion failed");
                        }
                    }
                });
            }
        });

        return v;
    }

    private MyListingAdapter getMyListingAdapter() {
        return this;
    }

}