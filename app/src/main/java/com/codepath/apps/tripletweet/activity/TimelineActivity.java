package com.codepath.apps.tripletweet.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.tripletweet.R;
import com.codepath.apps.tripletweet.fragment.HomeTimelineFragment;
import com.codepath.apps.tripletweet.fragment.MentionsTimelineFragment;


import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.tripletweet.R.color.tweet;

public class TimelineActivity extends BaseClass {
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ButterKnife.bind(this);

        setupViewPager(viewPager);
        tabLayout.setViewPager(viewPager);

        /* set pager transformation
         (RotateUpTransformer, AccordionTransformer, CubeInTransformer, FlipHorizontalTransformer, ScaleInOutTransformer, ZoomInTransformer)*/
        viewPager.setPageTransformer(true, new RotateUpTransformer());

        // Check Connectivity
        if ( ! isNetworkAvailable()  || ! isOnline()){
            Toast.makeText(this,"Working Offline...", Toast.LENGTH_LONG).show();
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        TweetsPagerAdapter adapter = new TweetsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    /*
     *  TweetsPagerAdapter
     */
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"HOME", "MENTIONS"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
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


    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }
}
