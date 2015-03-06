package com.swipeme.www.swipeme;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
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

                // custom dialog
                final Dialog dialog = new Dialog(context, R.style.Theme_CustomDialog);
                dialog.setContentView(R.layout.mylisting_dialog);

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
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
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