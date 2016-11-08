package com.codepath.apps.tripletweet.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.tripletweet.R;
import com.codepath.apps.tripletweet.models.Tweet;
import com.codepath.apps.tripletweet.network.TwitterApplication;
import com.codepath.apps.tripletweet.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class UserTimelineFragment extends TweetListFragment{
    private TwitterClient twitterClient;
    private Long userId;

    public static UserTimelineFragment newInstance(Long userId) {
        UserTimelineFragment fragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putLong("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twitterClient = TwitterApplication.getRestClient();
        userId = getArguments().getLong("userId");


        populateTimeline(null);
    }

    // send an api request
    // fill the list view
    public void populateTimeline(final String maxId) {
        Log.d("HomeTimeline", "populateTimeline");

        twitterClient.getHomeTimeLine(userId, maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {

                ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
                addAll(tweets);
                getTweetsArrayAdapter().notifyDataSetChanged();

                // disable pull to refresh
                getSwipeContainer().setRefreshing(false);

                Log.d("Home Fragment", json.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Home Fragment", responseString);
            }
        });
    }

    @Override
    public void loadNextDataFromApi() {
        int arrayLength = getTweetArrayList().size() - 1;
        if ( arrayLength > 0) {
            String maxID = String.valueOf(getTweetArrayList().get(arrayLength).getUnique_id());
            Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_SHORT).show();
            populateTimeline(maxID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fab.setVisibility(View.GONE);
    }
}
