package com.codepath.apps.tripletweet.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.tripletweet.R;
import com.codepath.apps.tripletweet.models.Tweet;
import com.codepath.apps.tripletweet.utils.PatternEditableBuilder;
import com.codepath.apps.tripletweet.viewHolder.TweetViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.zip.Inflater;


public class TweetsArrayAdapter extends RecyclerView.Adapter<TweetViewHolder>{

    private List<Tweet> tweetList;
    private Context context;


    public TweetsArrayAdapter(Context context, List<Tweet> tweetList){
        this.context = context;
        this.tweetList = tweetList;
    }

    public List<Tweet> getTweetList() {
        return tweetList;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        TweetViewHolder viewHolder ;
        LayoutInflater inflater = LayoutInflater.from(context);

        View  tweetView =  inflater.inflate(R.layout.timeline_tripletweet_feed, parent,false);

        viewHolder = new TweetViewHolder(context,tweetView,tweetList);

        return  viewHolder;
    }


    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        Tweet tweet = tweetList.get(position);

        holder.getTvTweetFeed().setText(tweet.getBody());

        // Style clickable spans based on pattern
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.BLUE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Toast.makeText(getContext() , "Clicked username: " + text,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).into(holder.getTvTweetFeed());

        holder.getTvName().setText(tweet.getUser().getName());
        holder.getTvTimeStamp().setText(getRelativeTimeAgo(tweet.getCreatedAt()));
        holder.getTvUserName().setText(tweet.getUser().getScreenName());

        // populate the picture.
        Glide.with(context).load(tweet.getUser().getProfileImageUrl())
                .placeholder(R.drawable.twitter_bird_logo)
                .into(holder.getIvProfilePic());

        String media_url = tweet.getEntities().getMedia().getMediaUrl();

        Glide.with(context).load(media_url)
                .placeholder(null)
                .into(holder.getIvFeedImage());
    }

    @Override
    public int getItemCount() {
        if (tweetList != null)
            return tweetList.size();
        return 0;
    }

    /*
    * getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    */
    public String getRelativeTimeAgo(String rawJsonDate) {

        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    /* 2 methods For Pull to Refresh */

    // Clean all elements of the recycler
    public void clear() {
        tweetList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Tweet> tweets) {
        tweetList.addAll(tweets);
        notifyDataSetChanged();
    }

    public void add(Tweet tweet) {
        tweetList.add(tweet);
        notifyDataSetChanged();
    }
}