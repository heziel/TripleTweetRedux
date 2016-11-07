package com.codepath.apps.tripletweet.fragment;


import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.tripletweet.models.Tweet;
import com.codepath.apps.tripletweet.network.TwitterApplication;
import com.codepath.apps.tripletweet.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class HomeTimelineFragment extends TweetListFragment {
    public TwitterClient twitterClient;

    public HomeTimelineFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getting singleton client
        twitterClient = TwitterApplication.getRestClient();

        populateTimeline(null);
    }

    // send an api request
    // fill the list view
    public void populateTimeline(final String maxId) {
        Log.d("HomeTimeline", "populateTimeline");

        twitterClient.getHomeTimeLine(maxId, new JsonHttpResponseHandler() {
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
}
