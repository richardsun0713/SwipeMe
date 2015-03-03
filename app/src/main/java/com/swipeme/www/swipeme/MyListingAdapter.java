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
    public View getItemView(final ParseObject object, View v, ViewGroup parent) {
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

        // Add button click function
        Button button = (Button) v.findViewById(R.id.cancel_button);
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
                                    "Listing Successfully Deleted!", Toast.LENGTH_LONG).show();
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