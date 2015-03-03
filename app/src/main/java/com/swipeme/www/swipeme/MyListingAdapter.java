package com.swipeme.www.swipeme;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class MyListingAdapter extends ParseQueryAdapter<ParseObject> {

    public MyListingAdapter(Context context, final String user_ID) {
        // Use the QueryFactory to construct a PQA that will only show
        // Todos marked as high-pri
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Offers");
                query.whereEqualTo("userID", user_ID);
                query.orderByAscending("createdAt");
                return query;
            }
        });
    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.my_listing_item, null);
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

}