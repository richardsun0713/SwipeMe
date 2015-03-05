package com.swipeme.www.swipeme;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class BuyListingAdapter extends ParseQueryAdapter<ParseObject> {

    public BuyListingAdapter(Context context, final ArrayList<String> getChecked, final String currentUserId, final Date timeStartDate, final Date timeEndDate) {
        // Use the QueryFactory to construct a PQA that will only show


        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {

                ParseQuery query = new ParseQuery("Offers");
                query.addDescendingOrder("price");
                query.whereContainedIn("restaurants", getChecked);

                //query.whereNotEqualTo("userID", currentUserId);

                return query;
            }
        });
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
        startTimeView.setText(object.getString("timeStart") + " ~ " + object.getString("timeEnd"));

        // Add the quantity and price time view
        TextView endTimeView = (TextView) v.findViewById(R.id.quantity_price);
        endTimeView.setText(
                object.getString("quantity") + " for " + object.getString("price") + " each");

        // Add the restaurant view
        TextView restaurantsView = (TextView) v.findViewById(R.id.restaurants);
        restaurantsView.setText(object.getList("restaurants").toString());

        return v;
    }

    private BuyListingAdapter getBuyListingAdapter() {
        return this;
    }

}
