package com.codepath.apps.tripletweet.fragment;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.tripletweet.models.Tweet;
import com.codepath.apps.tripletweet.network.TwitterApplication;
import com.codepath.apps.tripletweet.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MentionsTimelineFragment extends TweetListFragment{
    public TwitterClient twitterClient;

    public MentionsTimelineFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getting singleton client
        twitterClient = TwitterApplication.getRestClient();
        populateTimeline(null);
    }


    // send an api request
    // fill the list view
    public void populateTimeline(String maxId) {
        Log.d("Mentions Fragment", "populateTimeline");

        twitterClient.getMentionsTimeline(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {

                Log.d("Mentions Fragment", json.toString());
                ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
                addAll(tweets);

                getTweetsArrayAdapter().notifyDataSetChanged();

                // disable pull to refresh
                getSwipeContainer().setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Mentions Fragment", responseString);
            }

            @Override
            protected void handleMessage(Message message) {
                Log.d("Mentions Fragment", message.toString());
                super.handleMessage(message);
            }

            @Override
            public void onUserException(Throwable error) {
                Log.d("Mentions Fragment", error.getMessage());
            }
        });
    }

    @Override
    public void loadNextDataFromApi() {
        int arrayLength = getTweetArrayList().size() - 1;
        String maxID = String.valueOf(getTweetArrayList().get(arrayLength).getUnique_id());
        Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_SHORT).show();
        populateTimeline(maxID);
    }

    @Override
    public void onResume() {
        super.onResume();
        fab.setVisibility(View.GONE);
    }
}
