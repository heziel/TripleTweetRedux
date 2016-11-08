package com.codepath.apps.tripletweet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.codepath.apps.tripletweet.R;
import com.codepath.apps.tripletweet.fragment.FollowersFragment;
import com.codepath.apps.tripletweet.fragment.FollowingFragment;
import com.codepath.apps.tripletweet.fragment.HomeTimelineFragment;
import com.codepath.apps.tripletweet.fragment.MentionsTimelineFragment;
import com.codepath.apps.tripletweet.fragment.UserTimelineFragment;
import com.codepath.apps.tripletweet.models.Tweet;
import com.codepath.apps.tripletweet.models.User;
import com.codepath.apps.tripletweet.network.TwitterApplication;
import com.codepath.apps.tripletweet.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends BaseClass {
    @BindView(R.id.ivUserBackgroundImage)
    ImageView ivUserBackgroundImage;

    @BindView(R.id.ivUserPhoto)
    ImageView  ivUserPhoto;

    @BindView(R.id.tvUserName)
    TextView tvUserName;

    @BindView(R.id.tvUserScreenName)
    TextView tvUserScreenName;

    @BindView(R.id.tvUserDescription)
    TextView tvUserDescription;

    @BindView(R.id.tvFollowersCount)
    TextView tvFollowersCount;

    @BindView(R.id.tvFollowingCount)
    TextView tvFollowingCount;

    @BindView(R.id.vpProfile)
    ViewPager viewPager;

    @BindView(R.id.tabsProfile)
    PagerSlidingTabStrip tabLayout;

    public TwitterClient twitterClient;
    public User user;
    public ProfilePagerAdapter adapter;
    public Long userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        // getting singleton client
        twitterClient = TwitterApplication.getRestClient();

        Intent intent = getIntent();
        userId = intent.getLongExtra("userId",0);


        if (userId != 0) {
            populateUserProfile(userId);
        }
        else {
            populateUserProfile();
        }

        adapter = new ProfilePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);

        viewPager.setPageTransformer(true, new CubeInTransformer());
    }


    private void fillUserProfile() {
        tvUserName.setText(user.getName());
        tvUserScreenName.setText(user.getScreenName());
        tvUserDescription.setText(user.getDescription());
        tvFollowersCount.setText(String.valueOf(user.getFollowersCount()));
        tvFollowingCount.setText(String.valueOf(user.getFollowingCount()));

        Glide.with(this).load(user.getProfileBackgroundImageUrl()).error(R.drawable.shape_banner_placeholder).
                placeholder(R.drawable.shape_banner_placeholder).into(ivUserBackgroundImage);

        Glide.with(this).load(user.getProfileImageUrl()).error(R.drawable.photo_placeholder).
                placeholder(R.drawable.photo_placeholder).dontAnimate().into(ivUserPhoto);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.twitter_logo_white_48);
    }

    // Populate User Profile
    public void populateUserProfile() {
        Log.d("Profile Activity", "populateUserProfile");

        twitterClient.getUserProfile(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
               user = User.fromJSON(json);
                fillUserProfile();
                Log.d("Profile Fragment", json.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Profile Fragment", responseString);
            }
        });
    }


    // Populate User Profile
    public void populateUserProfile(long userId) {
        Log.d("Profile Activity", "populateUserProfile");

        twitterClient.getUserProfile(userId,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                user = User.fromJSON(json);
                fillUserProfile();
                Log.d("Profile Fragment", json.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Profile Fragment", responseString);
            }
        });
    }


    /*********************************************************************************/

    /*
     *  TweetsPagerAdapter
     */
    public class ProfilePagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"TWEETS", "FOLLOWING", "FOLLOWERS"};
      //  private long userId;

        public ProfilePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return UserTimelineFragment.newInstance(userId);
            }else if (position == 1) {
                return FollowingFragment.newInstance(userId);
            } else if ( position == 2){
                return FollowersFragment.newInstance(userId);
            } else
                return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }





}
