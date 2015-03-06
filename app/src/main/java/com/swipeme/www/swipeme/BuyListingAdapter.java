package com.swipeme.www.swipeme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;


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

        // Add button listener
        info_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // custom dialog
                final Dialog dialog = new Dialog(context, R.style.Theme_CustomDialog);
                dialog.setContentView(R.layout.buylisting_dialog);

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
                });

                dialog.show();
            }
        } );

        return v;
    }

    private BuyListingAdapter getBuyListingAdapter() {
        return this;
    }

}
