package com.swipeme.www.swipeme;

/*
<<<<<<< HEAD
//public class FacebookLoginFragment extends Fragment {
//    private static final String TAG = "FacebookLoginFragment";
//    private UiLifecycleHelper uiHelper;
//    private Session.StatusCallback callback = new Session.StatusCallback() {
//        @Override
//        public void call(Session session, SessionState state, Exception exception) {
//            onSessionStateChange(session, state, exception);
//        }
//    };
//    public FacebookLoginFragment() {
//    // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        uiHelper = new UiLifecycleHelper(getActivity(), callback);
//        uiHelper.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater,
//                             ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_main, container, false);
//        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
//        final TextView username = (TextView) view.findViewById(R.id.username);
//
//        authButton.setUserInfoChangedCallback(new UserInfoChangedCallback() {
//            @Override
//            public void onUserInfoFetched(GraphUser graphUser) {
//                if (graphUser != null) {
//                    username.setText("Welcome " + graphUser.getFirstName() + "!");
//                } else {
//                    username.setText("You are currently not logged in.");
//                }
//            }
//        });
//
//        authButton.setFragment(this);
//        return view;
//    }
//
//    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
//        if (state.isOpened()) {
//            Log.i(TAG, "Logged in...");
//            //We are logged in so start a new activity here
//            Intent intent = new Intent(getActivity(), HomeActivity.class);
//            startActivity(intent);
//        } else if (state.isClosed()) {
//            Log.i(TAG, "Logged out...");
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    // For scenarios where the main activity is launched and user
//    // session is not null, the session state change notification
//    // may not be triggered. Trigger it if it's open/closed.
//        Session session = Session.getActiveSession();
//        if (session != null &&
//                (session.isOpened() || session.isClosed()) ) {
//            onSessionStateChange(session, session.getState(), null);
//        }
//        uiHelper.onResume();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        uiHelper.onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        uiHelper.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        uiHelper.onDestroy();
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        uiHelper.onSaveInstanceState(outState);
//    }
//}
/*
=======
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;



public class FacebookLoginFragment extends Fragment {
    private static final String TAG = "FacebookLoginFragment";
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    String user_ID;
    public FacebookLoginFragment() {
    // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        final TextView username = (TextView) view.findViewById(R.id.username);

        authButton.setUserInfoChangedCallback(new UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser graphUser) {
                if (graphUser != null) {
                    username.setText("Welcome " + graphUser.getFirstName() + "!");
                } else {
                    username.setText("You are currently not logged in.");
                }
            }
        });

        authButton.setFragment(this);
        return view;
    }

    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
            //We are logged in so start a new activity here
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            // Get user id
            if (session != null && session.isOpened()) {
                // If the session is open, make an API call to get user data
                // and define a new callback to handle the response
                Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        // If the response is successful
                        if (session == Session.getActiveSession()) {
                            if (user != null) {
                                user_ID = user.getId(); //user id
                                Log.i(TAG, user_ID);
                            }
                        }
                    }
                });
                Request.executeBatchAsync(request);
            }
            intent.putExtra("userID", user_ID);
            startActivity(intent);
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    // For scenarios where the main activity is launched and user
    // session is not null, the session state change notification
    // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
}
>>>>>>> richard*/
