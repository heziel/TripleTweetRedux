package com.codepath.apps.tripletweet.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.tripletweet.R;
import com.codepath.apps.tripletweet.adapter.TweetsArrayAdapter;
import com.codepath.apps.tripletweet.models.Tweet;
import com.codepath.apps.tripletweet.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class TweetListFragment extends Fragment implements ComposeFragment.newTweetListener {
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainerLayout;

    private ArrayList<Tweet> tweetArrayList;
    private DialogFragment composeFragment;
    private TweetsArrayAdapter tweetsArrayAdapter;
    private RecyclerView rvTripleTweet;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;

    //getters
    public TweetsArrayAdapter getTweetsArrayAdapter() {
        return tweetsArrayAdapter;
    }

    public RecyclerView getRvTripleTweet() {
        return rvTripleTweet;
    }

    public ArrayList<Tweet> getTweetArrayList() {
        return tweetArrayList;
    }

    public SwipeRefreshLayout getSwipeContainer() {
        return swipeContainer;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, parent, false);

        tweetArrayList = new ArrayList<Tweet>();

        ButterKnife.bind(this, view);

        rvTripleTweet = (RecyclerView) view.findViewById(R.id.rvTripleTweet);
        rvTripleTweet.setHasFixedSize(true);

        //  tweetArrayList = new ArrayList<>();
        tweetsArrayAdapter = new TweetsArrayAdapter(getActivity(), tweetArrayList);

        // layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        // Attach the adapter to the recyclerview to populate items
        rvTripleTweet.setAdapter(tweetsArrayAdapter);

        // Set layout manager to position the items
        rvTripleTweet.setLayoutManager(linearLayoutManager);


        // Pagination
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadNextDataFromApi();
            }
        };

        rvTripleTweet.addOnScrollListener(scrollListener);

        pullToRefresh();

        rvTripleTweet.smoothScrollToPosition(0);

        return view;
    }

    public void addAll(ArrayList<Tweet> tweets) {
        if (tweets.size() != 0) {
            tweetArrayList.addAll(tweets);
        }
    }

    public abstract void loadNextDataFromApi();

    public abstract void populateTimeline(final String maxId);

    /*
    *   Pull To Refresh
    */
    private void pullToRefresh() {
        // Lookup the swipe container view
        swipeContainer = swipeContainerLayout;
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // clear the array of data
                // tweetArrayList.clear();
                // Notify the adapter of the update
                //  tweetsArrayAdapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
                getTweetsArrayAdapter().clear();
                populateTimeline(null);
                // tweetsArrayAdapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        rvTripleTweet.smoothScrollBy(0, 0);
    }




    @Override
    public void onFinishComposeTweet(Tweet tweet) {
        tweetArrayList.clear();
        loadNextDataFromApi();
    }
}
