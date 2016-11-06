package com.codepath.apps.tripletweet.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.tripletweet.R;
import com.codepath.apps.tripletweet.adapter.TweetsArrayAdapter;
import com.codepath.apps.tripletweet.fragment.ComposeFragment;
import com.codepath.apps.tripletweet.fragment.HomeTimelineFragment;
import com.codepath.apps.tripletweet.fragment.MentionsTimelineFragment;
import com.codepath.apps.tripletweet.models.Tweet;
import com.codepath.apps.tripletweet.network.TwitterApplication;
import com.codepath.apps.tripletweet.network.TwitterClient;
import com.codepath.apps.tripletweet.utils.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.tripletweet.R.color.tweet;

public class TimelineActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.fab)
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ButterKnife.bind(this);

        floatingActionButton();

        // Action bar support
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewPager(viewPager);

        //tabLayout.setupWithViewPager(viewPager);
        tabLayout.setViewPager(viewPager);

        /* set pager transformation
         (RotateUpTransformer, AccordionTransformer, CubeInTransformer, FlipHorizontalTransformer, ScaleInOutTransformer, ZoomInTransformer)*/
        viewPager.setPageTransformer(true, new RotateUpTransformer());

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


    /*
*   Floating Action Button
*/
    private void floatingActionButton() {

        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(getString(R.string.tweet_color))));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComposeDialog();
            }
        });
    }

    /*
    *   Show Compose Dialog
    */
    public void showComposeDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ComposeFragment composeDialogFragment = ComposeFragment.newInstance(getString(R.string.new_tweet));
        composeDialogFragment.show(fragmentManager, getString(R.string.compose_fragment));
        composeDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Dialog_NoActionBar);
    }
}
