package com.swipeme.www.swipeme;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;


public class BuyListingAdapter extends ParseQueryAdapter<ParseObject> {

private Context context;
    public BuyListingAdapter(Context context, final String timeStart, final String timeEnd, final ArrayList<String> getChecked) {
        // Use the QueryFactory to construct a PQA that will only show
        // Todos marked as high-pri
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Offers");
                query.addDescendingOrder("price");
                query.whereContainedIn("restaurants", getChecked);
                //TODO: Make checks for overlapping intervals
                /*
                Date filterTimeStart = null;
                Date filterTimeEnd = null;

                try {
                    filterTimeStart = new SimpleDateFormat("hh:mm a").parse(timeStart);
                    filterTimeEnd = new SimpleDateFormat("hh:mm a").parse(timeEnd);

                } catch (java.text.ParseException e){
                    //
                }
                query.whereGreaterThanOrEqualTo("timeStart", filterTimeEnd);
                query.whereLessThanOrEqualTo("timeEnd", filterTimeStart);*/
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

        message_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("message button", "click");
                String recipientUserID = object.getString("userID");



            }
        } );

        return v;
    }

    private BuyListingAdapter getBuyListingAdapter() {
        return this;
    }

}
